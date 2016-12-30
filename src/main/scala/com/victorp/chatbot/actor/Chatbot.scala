package com.victorp.chatbot.actor

import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.victorp.chatbot.app.AppContext
import com.victorp.chatbot.dto.{UserTextMsgDTO, BotTextMsgDTO}
import com.victorp.chatbot.model._
import com.victorp.chatbot.service.dao.{UserProfileDao, ChatMsgDao}
import com.victorp.chatbot.util.{IdUtil, TimeUtil}

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
/**
 * Currently I assume it is FACEBOOK msgPlatform
 * @author victorp
 */
class Chatbot(val msgPlatform:String,val platformUserId:String,charMsgDao:ChatMsgDao, userProfileDao:UserProfileDao) extends BaseActor{

  var userProfile:Option[UserProfile] = None

  findUserProfile()

  /**
   * Try to find UserProfile in the store asynchronously
   * and send corresponding msg to self (UserProfileFound or UserProfileNotFound)
   */
  def findUserProfile() = {
    val userProfiles:Future[Seq[UserProfile]] =
    for {
      userProfiles: Seq[UserProfile] <- userProfileDao.getAll()
    } yield userProfiles.filter(facebookId)

    userProfiles onSuccess  {
      case head::Nil => {
        log.debug("User profile found for facebook user: {}",platformUserId)
        self ! UserProfileFound(head)
      }

      case Nil => {
        log.debug("User profile not found for facebook user: {}",platformUserId)
        self ! UserProfileNotFound()
      }

      case _ => {
        log.error("Logical ERROR: Multiple profiles found for facebook user: {}",platformUserId)
      }
    }
  }

  def facebookId(userProfile:UserProfile):Boolean = extractFacebookId(userProfile) match {
    case Some(someFacebookId) if someFacebookId == platformUserId => true
    case _ => false

  }

  def extractFacebookId(userProfile:UserProfile):Option[String] =
    userProfile.fbProfile match {
      case None => None
      case Some(fbProfile) => Some(fbProfile.facebookUserId)
    }


  def echo(userMsg: UserTextMsgDTO): BotTextMsgDTO = {
    val name:String = chatName.getOrElse("")
    BotTextMsgDTO(userMsg.msgPlatform,userMsg.platformUserId,None,s"Got your message $name : ${userMsg.text}",TimeUtil.now())
  }


  def chatName:Option[String] =  {
    for {
      userP <- userProfile
      chatP <- userP.chatProfile
      theName <- chatP.name
    }yield theName
  }

  override def receive: Receive = {
    case chatMsg: UserTextMsgDTO => {
      log.debug(s"ChatManager received msg: {}", chatMsg)

      val botMsg = echo(chatMsg)
      AppContext.fbConnector ! botMsg //TODO patch: use pub/sub instead of direct messaging
    }

    case userProfileFound:UserProfileFound => {
      log.debug("User profile found : {}",userProfileFound.userProfile)
      userProfile = Some(userProfileFound.userProfile)
    }

    case userProfileNotFound:UserProfileNotFound => {
      log.debug("User profile not found for facebook user: {}, going to create new profile",platformUserId)
      val fbProfile = FacebookProfile(facebookUserId = platformUserId)
      userProfile = Some(UserProfile(IdUtil.nextUserId(),None,Some(fbProfile)))
    }
  }




  /**
   * Internal API
   */
  case class UserProfileFound(userProfile:UserProfile)
  case class UserProfileNotFound()



}
