package com.victorp.chatbot


import akka.actor._
import com.typesafe.config.ConfigFactory
import com.victorp.chatbot.dto.FBUserTextMessage
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.victorp.chatbot.rest.Resources

/**
 * @author victorp
 */
object ChatbotApp extends App{

  System.out.println("Hello From Chatbot")

  AppContext.fbConnector ! FBUserTextMessage("user-1","Hello Bot!")

  implicit val system = AppContext.system
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  //implicit val executionContext = system.dispatcher

  val config = ConfigFactory.load()



  val bindingFuture = Http().bindAndHandle(Resources.routes, "localhost", 10000)

}
