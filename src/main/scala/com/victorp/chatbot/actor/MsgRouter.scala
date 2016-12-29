package com.victorp.chatbot.actor

import akka.actor.{ActorRef, Props}
import com.victorp.chatbot.dto.{UserTextMsgDTO, BotTextMsgDTO}
import com.victorp.chatbot.model.ChatMsg


/**
 * @author victorp
 */
class MsgRouter extends BaseActor {

  var chatbots: Map[String, ActorRef] = Map()

  def createChatbot(userId: String): ActorRef = {
    val cm = context.actorOf(Props(classOf[Chatbot], userId), name = s"ChatManager-$userId")
    chatbots += (userId -> cm)
    cm
  }


  def chatbotActor(platformUserId: String): ActorRef = {
    chatbots.get(platformUserId) match {
      case Some(chatbot) => chatbot
      case None => createChatbot(platformUserId)
    }
  }


  override def receive: Receive = {
    case chatMsg: UserTextMsgDTO => {
      log.debug(s"Router received msg: {}", chatMsg)
      val chatbot = chatbotActor(chatMsg.platformUserId)
      chatbot ! chatMsg
    }
  }
}


