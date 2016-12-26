package com.victorp.chatbot


import akka.actor._
import com.victorp.chatbot.dto.FBUserTextMessage
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.victorp.chatbot.rest.Resources

/**
 * @author victorp
 */
object ChatbotApp extends App{

  System.out.println("Hello From Chatbot")

  AppContext.fbConnector ! FBUserTextMessage("user-1","Startup Msg!")

  implicit val system = AppContext.system
  implicit val materializer = ActorMaterializer()


  val bindingFuture = Http().bindAndHandle(Resources.routes, AppContext.chatServerHost, AppContext.chatServerPort)

}
