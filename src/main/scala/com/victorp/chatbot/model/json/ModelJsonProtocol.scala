package com.victorp.chatbot.model.json

import java.net.URL
import java.time.ZoneId

import com.victorp.chatbot.model._
import spray.json.DefaultJsonProtocol

/**
 * @author victorp
 */
trait ModelJsonProtocol extends DefaultJsonProtocol {

  /**
   * Model objects
   */
    implicit val facebookProfile = jsonFormat6(FacebookProfile)
    implicit val chatProfile = jsonFormat1(ChatProfile)
    implicit val userProfile = jsonFormat3(UserProfile)
    implicit val chatMsg = jsonFormat8(ChatMsg)



  /**
   * Special objects that are not a part of the model
   * The are used for holding the data in a single json
   */


  /**
   * It holds the whole DB in order to represent it as a single json
   */
  implicit val jsonUserData = jsonFormat2(UserData)

  /**
   * It holds the whole DB in order to represent it as a single json
   */
  implicit val jsonDB = jsonFormat1(JsonDB)

}

case class UserData(userProfile:Option[UserProfile] = None,chatMsgs:List[ChatMsg] = List())
/**
 * Represents json that holds the whole DB
 */
case class JsonDB(usersData:Map[String,UserData] = Map())





