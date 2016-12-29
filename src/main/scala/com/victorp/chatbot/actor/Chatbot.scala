package com.victorp.chatbot.actor

import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.victorp.chatbot.app.AppContext
import com.victorp.chatbot.dto.{UserTextMsgDTO, BotTextMsgDTO}
import com.victorp.chatbot.model.{ChatMsg, UserProfile}
import com.victorp.chatbot.util.TimeUtil

/**
 * @author victorp
 */
class Chatbot(val consumerId:String) extends BaseActor{

  def echo(userMsg: UserTextMsgDTO): BotTextMsgDTO = {
    BotTextMsgDTO(userMsg.msgPlatform,userMsg.platformUserId,None,s"Got you: ${userMsg.text}",TimeUtil.now())
  }

  override def receive: Receive = {
    case chatMsg: UserTextMsgDTO => {
      log.debug(s"ChatManager received msg: {}", chatMsg)

      val botMsg = echo(chatMsg)
      AppContext.fbConnector ! botMsg //TODO patch: use pub/sub instead of direct messaging
    }

  }



}
