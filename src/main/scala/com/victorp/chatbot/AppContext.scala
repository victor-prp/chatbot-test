package com.victorp.chatbot

import java.net.URL

import akka.actor.{Props, ActorSystem}
import com.victorp.chatbot.actor.{FBConnector, Router}

/**
 * @author victorp
 */
object AppContext extends AppConfig{

  // Create an Akka system
  val system = ActorSystem("ChatbotSystem")

  // create actors
  val router = system.actorOf(Props[Router])

  val fbConnector = system.actorOf(Props(new FBConnector(router,s"$facebookProxyHost:$facebookProxyPort")))

}
