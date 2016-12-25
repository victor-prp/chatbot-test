package com.victorp.chatbot

import akka.actor.{Props, ActorSystem}
import com.victorp.chatbot.actor.{FBConnector, Router}

/**
 * @author victorp
 */
object AppContext {


  // Create an Akka system
  val system = ActorSystem("ChatbotSystem")

  // create actors
  val router = system.actorOf(Props[Router])

  val fbConnector = system.actorOf(Props(new FBConnector(router,"127.0.0.1:10000")))

}
