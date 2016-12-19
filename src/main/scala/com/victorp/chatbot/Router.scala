package com.victorp.chatbot

import akka.actor.{ActorRef, Actor}
import com.victorp.chatbot.actor.BaseActor
import com.victorp.chatbot.dto.ChatMessage

/**
 * @author victorp
 */
class Router extends BaseActor{

  val chatManagers:Map[String,ActorRef] = Map()

  override def receive: Receive = {
    case chatMsg: ChatMessage  => {
      log.debug(s"Router received msg: {}",chatMsg)
    }
  }
}


