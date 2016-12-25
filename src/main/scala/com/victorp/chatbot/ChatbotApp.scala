package com.victorp.chatbot


import java.net.URL

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

  AppContext.fbConnector ! FBUserTextMessage("user-1","Internal Message!")

  implicit val system = AppContext.system
  implicit val materializer = ActorMaterializer()


  val defaultConfig = ConfigFactory.load()
  val config = ConfigFactory.parseURL(new URL("file:/seemedics/conf/akka/application.conf")).withFallback(defaultConfig)


  val bindingFuture = Http().bindAndHandle(Resources.routes, config.getString("chat-server.host"), config.getInt("chat-server.port"))

}
