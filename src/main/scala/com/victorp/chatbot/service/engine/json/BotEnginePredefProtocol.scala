package com.victorp.chatbot.service.engine.json

import com.victorp.chatbot.service.engine.PredefMsg
import spray.json.DefaultJsonProtocol._


/**
 * @author victorp
 */
trait BotEnginePredefProtocol {
  implicit val jsonProtocolPredefMsg = jsonFormat3(PredefMsg)
  implicit val jsonProtocolPredefDialog = jsonFormat3(PredefDialog)
  implicit val jsonProtocolPredef = jsonFormat1(PredefDialogs)



  case class PredefDialog(name:String,hints:List[String],msgs:List[PredefMsg])
  case class PredefDialogs(dialogs:List[PredefDialog])

}

