package com.victorp.chatbot.service.dao

import java.nio.file.Paths

import com.victorp.chatbot.com.victorp.chatbot.TestUtil._
import com.victorp.chatbot.model._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.{Await, ExecutionContext}
import scala.reflect.io.File
import scala.concurrent.duration._
/**
 * @author victorp
 */
@RunWith(classOf[JUnitRunner])
class ChatMsgDaoTest extends FunSuite with BeforeAndAfter {
  before {
    File(testFilePath().toString).delete()
  }

  after {
    File(testFilePath().toString).delete()
  }



  test("single msg (save and get)") {
    new ChatMsgDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val allChatMsgsFuture =
        for{
          _ <- save(chatMsg1)
          allChatMsgs <- getAll(chatMsg1.userId,chatMsg1.msgPlatform)
        }yield allChatMsgs

      val chatMsgs:Seq[ChatMsg] = Await.result(allChatMsgsFuture,10 seconds)
      assert(chatMsgs.size === 1)
      assert(chatMsgs.find(_.sequenceNumber ==  chatMsg1.sequenceNumber) === Some(chatMsg1))
    }
  }



  test("two msgs (save and get)") {
    new ChatMsgDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val allChatMsgsFuture =
        for{
          _ <- save(chatMsg1)
          _ <- save(chatMsg2)
          allChatMsgs <- getAll(chatMsg1.userId,chatMsg1.msgPlatform)
        }yield allChatMsgs

      val chatMsgs:Seq[ChatMsg] = Await.result(allChatMsgsFuture,10 seconds)
      assert(chatMsgs.size === 2)
      assert(chatMsgs.find(_.sequenceNumber ==  chatMsg1.sequenceNumber) === Some(chatMsg1))
      assert(chatMsgs.find(_.sequenceNumber ==  chatMsg2.sequenceNumber) === Some(chatMsg2))

    }
  }


  def testFilePath() = Paths.get(tempDir,s"${this.getClass.getSimpleName}.json")

  trait TestData{
    val chatMsg1 = ChatMsg("user-1",MsgPlatform.FACEBOOK,1,"Hello1",0,SentBy.BOT,None )
    val chatMsg2 = ChatMsg("user-1",MsgPlatform.FACEBOOK,2,"Hello2",0,SentBy.BOT,None )

  }
}

