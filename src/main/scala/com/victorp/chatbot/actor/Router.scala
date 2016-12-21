package com.victorp.chatbot.actor

import akka.pattern.ask
import akka.actor.{Props, ActorRef}
import com.victorp.chatbot.dto.ChatMessage


/**
 * @author victorp
 */
class Router extends BaseActor {

  var chatManagers: Map[String, ActorRef] = Map()

  def createChatbot(userId: String): ActorRef = {
    val cm = context.actorOf(Props(classOf[Chatbot], userId), name = s"ChatManager-$userId")
    chatManagers += (userId -> cm)
    cm
  }


  def chatbotActor(userId: String): ActorRef = {
    chatManagers.get(userId) match {
      case Some(cm) => cm
      case None => createChatbot(userId)
    }
  }


  override def receive: Receive = {
    case chatMsg: ChatMessage => {
      log.debug(s"Router received msg: {}", chatMsg)
      val chatManager = chatbotActor(chatMsg.userId)
      chatManager ! chatMsg
    }
  }
}


