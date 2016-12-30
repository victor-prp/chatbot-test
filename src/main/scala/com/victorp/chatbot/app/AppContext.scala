package com.victorp.chatbot.app

import java.nio.file.Paths

import akka.actor.{ActorSystem, Props}
import com.victorp.chatbot.actor.{Chatbot, MsgRouter, PlatformProxyConnector}
import com.victorp.chatbot.service.dao.{UserProfileDao, ChatMsgDao}

/**
 * @author victorp
 */
object AppContext extends AppConfig{

  // Create an Akka system
  val system = ActorSystem("ChatbotSystem")

  // create actors
  val router = system.actorOf(Props[MsgRouter])

  val fbConnector = system.actorOf(Props(new PlatformProxyConnector(router,s"$facebookProxyHost:$facebookProxyPort")))

  Paths.get(userDataFilePath)

  val chatMsgDao = new ChatMsgDao(Paths.get(userDataFilePath))
  val userProfileDao = new UserProfileDao(Paths.get(userDataFilePath))

  def newChatBot(msgPlatform:String,platformUserId:String):Chatbot = {
    new Chatbot(msgPlatform,platformUserId,chatMsgDao,userProfileDao)
  }

}
