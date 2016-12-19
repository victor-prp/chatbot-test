package com.victorp.chatbot

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.victorp.chatbot.dto.ChatMessage

/**
 * @author victorp
 */
class ChatManager(val consumerId:String) extends Actor{




  override def receive: Receive = {
    case chatMsg: ChatMessage  =>
  }


}
