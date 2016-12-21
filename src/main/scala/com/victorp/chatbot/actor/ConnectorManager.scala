package com.victorp.chatbot.actor

import akka.actor.Actor.Receive
import com.victorp.chatbot.dto.ChatMessage

/**
 * @author victorp
 */
class ConnectorManager extends BaseActor {
  override def receive: Receive ={
    case chatMsg: ChatMessage => {
      log.debug(s"ConnectorManager received msg: {}", chatMsg)
      System.out.println(s"bot: ${chatMsg.text}")
  }
}

}
