package com.victorp.chatbot.actor

import akka.pattern.ask
import akka.actor.{Props, ActorRef}
import com.victorp.chatbot.dto.FBUserTextMessage
import com.victorp.chatbot.model.ChatMsg


/**
 * @author victorp
 */
class Router extends BaseActor {

  var chatbots: Map[String, ActorRef] = Map()

  def createChatbot(userId: String): ActorRef = {
    val cm = context.actorOf(Props(classOf[Chatbot], userId), name = s"ChatManager-$userId")
    chatbots += (userId -> cm)
    cm
  }


  def chatbotActor(userId: String): ActorRef = {
    chatbots.get(userId) match {
      case Some(cm) => cm
      case None => createChatbot(userId)
    }
  }


  override def receive: Receive = {
    case chatMsg: ChatMsg => {
      log.debug(s"Router received msg: {}", chatMsg)
      val chatbot = chatbotActor(chatMsg.targetId)
      chatbot ! chatMsg
    }
  }
}


