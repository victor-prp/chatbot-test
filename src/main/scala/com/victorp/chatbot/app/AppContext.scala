package com.victorp.chatbot.app

import akka.actor.{ActorSystem, Props}
import com.victorp.chatbot.actor.{MsgRouter, PlatformProxyConnector}

/**
 * @author victorp
 */
object AppContext extends AppConfig{

  // Create an Akka system
  val system = ActorSystem("ChatbotSystem")

  // create actors
  val router = system.actorOf(Props[MsgRouter])

  val fbConnector = system.actorOf(Props(new PlatformProxyConnector(router,s"$facebookProxyHost:$facebookProxyPort")))

}
