package com.victorp.chatbot.app

import java.nio.file.Paths

import akka.actor.{ActorSystem, Props}
import com.victorp.chatbot.actor.{FacebookChatbot, MsgRouter, PlatformProxyConnector}
import com.victorp.chatbot.service.dao.{UserProfileDao, ChatMsgDao}
import com.victorp.chatbot.service.engine.BotEngine
import com.victorp.chatbot.service.facebook.FacebookGraphAPI

/**
 * Manual DI
 * @author victorp
 */
object AppContext extends AppConfig{

  /**
   * Actor system
   */
  val system = ActorSystem("ChatbotSystem")

  /**
   * Actors singletons
   */
  val router = system.actorOf(Props[MsgRouter])
  val fbConnector = system.actorOf(Props(new PlatformProxyConnector(router,s"$facebookProxyHost:$facebookProxyPort")))

  /**
   * Actors prototypes
   */
  def newChatBot(msgPlatform:String,platformUserId:String):FacebookChatbot = {
    new FacebookChatbot(msgPlatform,platformUserId,chatMsgDao,userProfileDao,facebookGraphAPI,botEngine)
  }

  /**
   * Services (singletons)
   */
  val chatMsgDao = new ChatMsgDao(Paths.get(userDataFilePath))
  val userProfileDao = new UserProfileDao(Paths.get(userDataFilePath))
  val facebookGraphAPI = new FacebookGraphAPI(system,facebookAccessToken)
  val botEngine = BotEngine(chatEnginePredefFilePath)


}
