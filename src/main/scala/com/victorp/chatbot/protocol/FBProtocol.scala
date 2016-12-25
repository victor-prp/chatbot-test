package com.victorp.chatbot.protocol

import com.victorp.chatbot.dto._

import spray.json.DefaultJsonProtocol

/**
 * @author victorp
 */
trait FBProtocol extends DefaultJsonProtocol {
  implicit val botTextMessage = jsonFormat2(FBBotTextMessage)
  implicit val userTextMessage = jsonFormat2(FBUserTextMessage)
}