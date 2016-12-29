package com.victorp.chatbot.service.dao

import java.nio.file.Paths

import com.victorp.chatbot.com.victorp.chatbot.TestUtil._
import com.victorp.chatbot.model._
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.{Await, ExecutionContext}
import scala.reflect.io.File
import scala.concurrent.duration._
/**
 * @author victorp
 */
class ChatMsgDaoTest extends FunSuite with BeforeAndAfter {
  before {
    File(testFilePath().toString).delete()
  }

//  after {
//    File(testFilePath().toString).delete()
//  }



  test("single msg (save and get)") {
    new ChatMsgDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val allChatMsgsFuture =
        for{
          v <- saveNew(chatMsg1)
          allChatMsgs <- getAll(chatMsg1.userId)
        }yield allChatMsgs

      val chatMsgs:Seq[ChatMsg] = Await.result(allChatMsgsFuture,10 seconds)
      assert(chatMsgs.size === 1)
      assert(chatMsgs.find(_.id ==  chatMsg1.id) === Some(chatMsg1))
    }
  }



  test("two msgs (save and get)") {
    new ChatMsgDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val allChatMsgsFuture =
        for{
          v1 <- saveNew(chatMsg1)
          v2 <- saveNew(chatMsg2)
          allChatMsgs <- getAll(chatMsg1.userId)
        }yield allChatMsgs

      val chatMsgs:Seq[ChatMsg] = Await.result(allChatMsgsFuture,10 seconds)
      assert(chatMsgs.size === 2)
      assert(chatMsgs.find(_.id ==  chatMsg1.id) === Some(chatMsg1))
      assert(chatMsgs.find(_.id ==  chatMsg2.id) === Some(chatMsg2))

    }
  }


  def testFilePath() = Paths.get(tempDir,s"${this.getClass.getName}.json")

  trait TestData{
    val chatMsg1 = ChatMsg("msg-1","user-1","Hello1",0,SentBy.BOT,MsgPlatform.FACEBOOK,"fb-1",None )
    val chatMsg2 = ChatMsg("msg-2","user-1","Hello2",0,SentBy.BOT,MsgPlatform.FACEBOOK,"fb-2",None )

  }
}

