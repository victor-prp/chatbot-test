package com.victorp.chatbot.service.dao

import java.nio.file.Paths

import com.victorp.chatbot.com.victorp.chatbot.TestUtil
import com.victorp.chatbot.model._
import com.victorp.chatbot.service.dao.UserProfileDao
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner

import scala.concurrent.{Await, ExecutionContext}
import scala.reflect.io.File
import scala.concurrent.duration._

/**
 * @author victorp
 */
@RunWith(classOf[JUnitRunner])
class UserProfileAndMsgsTest extends FunSuite with BeforeAndAfter {
  before {
    File(testFilePath.toString).delete()
  }

//  after {
//    File(testFilePath.toString).delete()
//  }

  test("single user data (save and get)") {
    new TestData {
      import ExecutionContext.Implicits.global

      val saveMsg2Future = chatMsgDao.save(chatMsg2)
      val saveProfileFuture = userProfileDao.save(userProfile1)
      val saveMsg1Future = chatMsgDao.save(chatMsg1)


      val allDataFuture =
        for{
          _ <- saveProfileFuture
          _ <- saveMsg1Future
          _ <- saveMsg2Future
          allProfiles <- userProfileDao.getAll()
          allMsgs <- chatMsgDao.getAll(userProfile1.userId,userProfile1.msgPlatform)
        }yield (allProfiles,allMsgs)

      val (usersProfiles,chatMsgs) = Await.result(allDataFuture,10 seconds)
      assert(usersProfiles.size === 1)
      assert(usersProfiles.find(_.userId ==  userProfile1.userId) === Some(userProfile1))

      assert(chatMsgs.size === 2)
      assert(chatMsgs.find(_.sequenceNumber ==  chatMsg1.sequenceNumber) === Some(chatMsg1))
      assert(chatMsgs.find(_.sequenceNumber ==  chatMsg2.sequenceNumber) === Some(chatMsg2))
    }
  }

  val testFilePath = Paths.get(TestUtil.tempDir, s"${this.getClass.getSimpleName}.json")
  val userProfileDao = new UserProfileDao(testFilePath)
  val chatMsgDao = new ChatMsgDao(testFilePath)



  trait TestData{
    val userProfile1 = UserProfile("fb-1",MsgPlatform.FACEBOOK,UserDetails(firstName = Some("Kuku1")))
    val chatMsg1 = ChatMsg("fb-1",MsgPlatform.FACEBOOK,1,"Hello1",0,SentBy.BOT,None )
    val chatMsg2 = ChatMsg("fb-1",MsgPlatform.FACEBOOK,2,"Hello2",0,SentBy.BOT,None )

  }
}
