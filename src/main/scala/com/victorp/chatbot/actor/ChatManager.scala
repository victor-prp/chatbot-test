package com.victorp.chatbot.actor

import com.victorp.chatbot.dto.ChatMessage

/**
 * @author victorp
 */
class ChatManager(val consumerId:String) extends BaseActor{

  override def receive: Receive = {
    case chatMsg: ChatMessage => {
      log.debug(s"ChatManager received msg: {}", chatMsg)
    }
  }


}
