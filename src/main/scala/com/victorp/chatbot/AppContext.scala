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

  val fbConnector = system.actorOf(Props(new FBConnector(router,"ec2-35-166-157-125.us-west-2.compute.amazonaws.com:10000")))

}
