package com.victorp.chatbot

import java.text.SimpleDateFormat
import java.util.GregorianCalendar

import akka.actor.{Props, ActorSystem}
import com.victorp.chatbot.dto.ChatMessage
import akka.actor._

/**
 * @author victorp
 */
object ChatbotApp extends App{


  System.out.println("Hello From Chatbot")


  val set = Set()

  // Create an Akka system
  val system = ActorSystem("MergeSortSystem")

  // create actors
  val router = system.actorOf(Props[Router])

  router ! ChatMessage("user-1","Hello Bot!")

}
