package com.victorp.chatbot.model.json

import com.victorp.chatbot.model._
import spray.json.DefaultJsonProtocol

/**
 * @author victorp
 */
trait ModelJsonProtocol extends DefaultJsonProtocol {

  /**
   * Model objects
   */

  implicit val userDetailsJsonProtocol = jsonFormat6(UserDetails)
  implicit val chatMsgJsonProtocol = jsonFormat7(ChatMsg)
  implicit val userProfileJsonProtocol = jsonFormat3(UserProfile)



  /**
   * Special objects that are not a part of the model
   * The are used for holding the data in a single json
   */


  /**
   * It holds the whole DB in order to represent it as a single json
   */
  implicit val userDataJsonProtocol = jsonFormat2(UserData)

  /**
   * It holds the whole DB in order to represent it as a single json
   */
  implicit val dbJsonProtocol = jsonFormat1(JsonDB)

}

case class UserData(userProfile:UserProfile,chatMsgs:List[ChatMsg] = List())
/**
 * Represents json that holds the whole DB
 */
case class JsonDB(usersData:List[UserData] = List())





