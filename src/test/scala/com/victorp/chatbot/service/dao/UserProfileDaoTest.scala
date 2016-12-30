package com.victorp.chatbot.service.dao

import java.nio.file.Paths

import com.victorp.chatbot.com.victorp.chatbot.TestUtil.tempDir
import com.victorp.chatbot.model.{FacebookProfile, ChatProfile, UserProfile}
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._
import scala.reflect.io.File

/**
 * @author victorp
 */
class UserProfileDaoTest extends FunSuite with BeforeAndAfter {
  before{
    File(testFilePath().toString).delete()
  }

//  after{
//    File(testFilePath().toString).delete()
//  }
//
  test("single user profile (save and get)") {
    new UserProfileDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val allUsersFuture =
      for{
        v <- saveNew(userProfile1)
        allUsers <- getAll()
      }yield allUsers

      val usersProfiles:Seq[UserProfile] = Await.result(allUsersFuture,10 seconds)
      assert(usersProfiles.size === 1)
      assert(usersProfiles.find(_.id ==  userProfile1.id) === Some(userProfile1))
    }
  }

  test("two users profiles (save and get)") {
    new UserProfileDao(testFilePath()) with TestData {
      import ExecutionContext.Implicits.global
      val saveUser1Future = saveNew(userProfile1)
      val saveUser2Future = saveNew(userProfile2)

      val allUsersFuture =
        for{
          _ <- saveUser1Future
          _ <- saveUser2Future
          allUsers <- getAll()
        }yield allUsers

      val usersProfiles:Seq[UserProfile] = Await.result(allUsersFuture,10 seconds)
      assert(usersProfiles.size === 2)
      assert(usersProfiles.find(_.id ==  userProfile1.id) === Some(userProfile1))
      assert(usersProfiles.find(_.id ==  userProfile2.id) === Some(userProfile2))

    }
  }

  def testFilePath() = Paths.get(tempDir,s"${this.getClass.getName}.json")

  def withUserProfileDao() = new UserProfileDao(testFilePath())

  trait TestData{
    val userProfile1 = UserProfile("user-1",Some(ChatProfile(Some("Micha"))),Some(FacebookProfile("fb-1")))
    val userProfile2 = UserProfile("user-2",Some(ChatProfile(Some("Tom"))),None)
  }
}

