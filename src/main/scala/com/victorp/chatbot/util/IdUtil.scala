package com.victorp.chatbot.util

/**
 * @author victorp
 */
object IdUtil {
  
  val botId = "bot-0.0.0.1"

  def nextUserId():String = "user-"+uuid
  def nextMsgId():String = "msg-"+uuid


  def uuid() = java.util.UUID.randomUUID.toString

}
