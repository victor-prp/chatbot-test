package com.victorp.chatbot.actor

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorRef}
import com.victorp.chatbot.dto.ChatMessage


/**
 * @author victorp
 */
class Router extends BaseActor {

  var chatManagers: Map[String, ActorRef] = Map()

  def createChatManager(userId: String): ActorRef = {
    val cm = context.actorOf(Props(classOf[ChatManager], userId), name = s"ChatManager-$userId")
    chatManagers += (userId -> cm)
    cm
  }


  def chatManagerActor(userId: String): ActorRef = {
    chatManagers.get(userId) match {
      case Some(cm) => cm
      case None => createChatManager(userId)
    }
  }


  override def receive: Receive = {
    case chatMsg: ChatMessage => {
      log.debug(s"Router received msg: {}", chatMsg)
      val chatManager = chatManagerActor(chatMsg.userId)
      chatManager ! chatMsg
    }
  }
}


