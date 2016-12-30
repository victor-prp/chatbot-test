package com.victorp.chatbot.actor

import akka.actor.{ActorRef, Props}
import com.victorp.chatbot.app.AppContext
import com.victorp.chatbot.dto.{UserTextMsgDTO, BotTextMsgDTO}
import com.victorp.chatbot.model.ChatMsg


/**
 * @author victorp
 */
class MsgRouter extends BaseActor {

  var chatbots: Map[String, ActorRef] = Map()
  val DOT = "."

  def createChatbot(msgPlatform:String,platformUserId: String): ActorRef = {
    val botId = chatbotId(msgPlatform,platformUserId)
    val cm = context.actorOf(Props(AppContext.newChatBot(msgPlatform,platformUserId)), name = s"chatbot-$botId")
    chatbots += (botId -> cm)
    cm
  }

  def chatbotId(msgPlatform:String,platformUserId: String) = msgPlatform+DOT+platformUserId


  def chatbotActor(msgPlatform:String,platformUserId: String): ActorRef = {
    chatbots.get(chatbotId(msgPlatform,platformUserId)) match {
      case Some(chatbot) => chatbot
      case None => createChatbot(msgPlatform,platformUserId)
    }
  }


  override def receive: Receive = {
    case chatMsg: UserTextMsgDTO => {
      log.debug(s"Router received msg: {}", chatMsg)
      val chatbot = chatbotActor(chatMsg.msgPlatform, chatMsg.platformUserId)
      chatbot ! chatMsg
    }
  }
}


