package com.victorp.chatbot.model.json

import com.victorp.chatbot.model.{ChatMsg, ChatProfile, UserProfile}
import org.scalatest.FunSuite
import spray.json._

/**
 * @author victorp
 */
class ModelJsonProtocolTest extends FunSuite with ModelJsonProtocol {

  test("empty DB to json") {
      assert("""{"usersData":{}}""" === jsonDB.write(JsonDB()).toString())
    }

  test("empty DB from json") {
    assert(jsonDB.read("""{"usersData":{}}""".parseJson) === JsonDB())

  }

  test("single User to  json") {
    new TestDB {
      assert(jsonhUser1 === jsonDB.write(dbUser1).toString())
    }
  }

  test("single User from json")  {
    new TestDB {
      assert(jsonDB.read(jsonhUser1.parseJson) === dbUser1)
    }
  }

  test("single User with single Msh to  json") {
    new TestDB {
      assert(jsonUser1Msg1 === jsonDB.write(dbUser1Msg1).toString())
    }
  }


  trait TestDB{
    val user1 = UserData(Some(UserProfile("user-1",Some(ChatProfile(Some("Micha"))),None)),List())
    val dbUser1 = JsonDB(Map("user-1"-> user1))
    val jsonhUser1 = """{"usersData":{"user-1":{"userProfile":{"id":"user-1","chatProfile":{"name":"Micha"}},"chatMsgs":[]}}}"""

    val user1Msg1 = user1.copy(chatMsgs = List(ChatMsg("msg1",user1.userProfile.get.id,"Hello",0,"USER","FACEBOOK","fb-1",None)))
    val dbUser1Msg1 = JsonDB(Map("user-1"-> user1Msg1))
    val jsonUser1Msg1 = """{"usersData":{"user-1":{"userProfile":{"id":"user-1","chatProfile":{"name":"Micha"}},"chatMsgs":[{"timestamp":0,"text":"Hello","id":"msg1","sentBy":"USER","userId":"user-1","platform":"FACEBOOK"}]}}}""" 
  }
}
