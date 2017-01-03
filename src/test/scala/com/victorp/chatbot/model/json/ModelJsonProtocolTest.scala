package com.victorp.chatbot.model.json

import com.victorp.chatbot.model._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import spray.json._

/**
 * @author victorp
 */
@RunWith(classOf[JUnitRunner])
class ModelJsonProtocolTest extends FunSuite with ModelJsonProtocol {

  test("empty DB to json") {
      assert("""{"usersData":[]}""" === dbJsonProtocol.write(JsonDB()).toString())
    }

  test("empty DB from json") {
    assert(dbJsonProtocol.read("""{"usersData":[]}""".parseJson) === JsonDB())

  }

  test("single User to  json") {
    new TestDB {
      val serialized = dbJsonProtocol.write(dbUser1).toString()
      assert(jsonhUser1 === serialized)
    }
  }

  test("single User from json")  {
    new TestDB {
      assert(dbJsonProtocol.read(jsonhUser1.parseJson) === dbUser1)
    }
  }

  test("single User with single Msg to  json") {
    new TestDB {
      val json = dbJsonProtocol.write(dbUser1Msg1).toString()
      assert(jsonUser1Msg1 === json)
    }
  }


  trait TestDB{
    val userId = "fb-1"
    val msgPlatform = MsgPlatform.FACEBOOK
    val user1 = UserData(UserProfile(userId,msgPlatform,UserDetails(firstName = Some("Micha"))))
    val dbUser1 = JsonDB(List(user1))
    val jsonhUser1 = """{"usersData":[{"userProfile":{"userId":"fb-1","msgPlatform":"facebook","userDetails":{"firstName":"Micha"}},"chatMsgs":[]}]}"""

    val chatMsg1 = ChatMsg(userId,msgPlatform,1,"Hi Micha",0,SentBy.BOT,None)
    val user1Msg1 = user1.copy(chatMsgs = List(chatMsg1))
    val dbUser1Msg1 = JsonDB(List(user1Msg1))
    val jsonUser1Msg1 = """{"usersData":[{"userProfile":{"userId":"fb-1","msgPlatform":"facebook","userDetails":{"firstName":"Micha"}},"chatMsgs":[{"timestamp":0,"text":"Hi Micha","sentBy":"bot","msgPlatform":"facebook","userId":"fb-1","sequenceNumber":1}]}]}"""


  }
}
