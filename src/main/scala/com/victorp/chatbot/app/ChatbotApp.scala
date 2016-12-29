package com.victorp.chatbot.app

import akka.actor._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.victorp.chatbot.dto.UserTextMsgDTO
import com.victorp.chatbot.model.MsgPlatform
import com.victorp.chatbot.rest.Resources
import com.victorp.chatbot.util.TimeUtil

/**
 * @author victorp
 */
object ChatbotApp extends App{

  System.out.println("Hello From Chatbot")

  AppContext.fbConnector ! UserTextMsgDTO(MsgPlatform.FACEBOOK,"fb-1",None,s"App started",TimeUtil.now())

  implicit val system = AppContext.system
  implicit val materializer = ActorMaterializer()


  val bindingFuture = Http().bindAndHandle(Resources.routes, AppContext.chatServerHost, AppContext.chatServerPort)

}
