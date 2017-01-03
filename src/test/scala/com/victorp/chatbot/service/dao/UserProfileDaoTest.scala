package com.victorp.chatbot.service.dao

import java.nio.file.Paths

import com.victorp.chatbot.com.victorp.chatbot.TestUtil
import com.victorp.chatbot.model.{UserDetails, MsgPlatform, UserProfile}
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
class UserProfileDaoTest extends FunSuite with BeforeAndAfter {
  before{
    File(testFilePath().toString).delete()
  }

  after{
    File(testFilePath().toString).delete()
  }

  test("single user profile (save and get)") {
    new UserProfileDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val allUsersFuture =
      for{
        _ <- save(userProfile1)
        allUsers <- getAll()
      }yield allUsers

      val usersProfiles:Seq[UserProfile] = Await.result(allUsersFuture,10 seconds)
      assert(usersProfiles.size === 1)
      assert(usersProfiles.find(_.userId ==  userProfile1.userId) === Some(userProfile1))
    }
  }

  test("two  user profiles (save and get)") {
    new UserProfileDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val allUsersFuture =
        for{
          _ <- save(userProfile1)
          _ <- save(userProfile2)
          allUsers <- getAll()
        }yield allUsers

      val usersProfiles:Seq[UserProfile] = Await.result(allUsersFuture,10 seconds)
      assert(usersProfiles.size === 2)
      assert(usersProfiles.find(_.userId ==  userProfile1.userId) === Some(userProfile1))
      assert(usersProfiles.find(_.userId ==  userProfile2.userId) === Some(userProfile2))

    }
  }

  def testFilePath() = Paths.get(TestUtil.tempDir,s"${this.getClass.getSimpleName}.json")

  def withUserProfileDao() = new UserProfileDao(testFilePath())

  trait TestData{
    val userProfile1 = UserProfile("fb-1",MsgPlatform.FACEBOOK,UserDetails(firstName = Some("Kuku")))
    val userProfile2 = UserProfile("fb-2",MsgPlatform.FACEBOOK,UserDetails(firstName = Some("Kuku2")))

  }
}

