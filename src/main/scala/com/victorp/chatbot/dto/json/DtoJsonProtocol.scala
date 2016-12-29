package com.victorp.chatbot.dto.json

import com.victorp.chatbot.dto._

import spray.json.DefaultJsonProtocol

/**
 * @author victorp
 */
trait DtoJsonProtocol extends DefaultJsonProtocol {
  implicit val botTextMessage = jsonFormat5(BotTextMsgDTO)
  implicit val userTextMessage = jsonFormat5(UserTextMsgDTO)
}