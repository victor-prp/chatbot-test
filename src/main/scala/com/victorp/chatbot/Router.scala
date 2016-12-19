package com.victorp.chatbot

import akka.actor.{ActorRef, Actor}
import com.victorp.chatbot.dto.ChatMessage

/**
 * @author victorp
 */
class Router extends Actor{

  val chatManagers:Map[String,ActorRef] = Map()

  override def receive: Receive = {
    case chatMsg: ChatMessage  => {
      System.out.println("Router recieved msg:" + chatMsg)
      System.exit(0)
    }
  }
}


