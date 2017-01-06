package com.victorp.chatbot.actor

import com.victorp.chatbot.app.AppContext
import com.victorp.chatbot.dto.{BotTextMsgDTO, UserTextMsgDTO}
import com.victorp.chatbot.model._
import com.victorp.chatbot.service.dao.{ChatMsgDao, UserProfileDao}
import com.victorp.chatbot.service.facebook.{FacebookGraphAPI, FacebookProfile}
import com.victorp.chatbot.util.TimeUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * @author victorp
 */
class FacebookChatbot(val msgPlatform:String,
                      val platformUserId:String,
                      charMsgDao:ChatMsgDao, 
                      userProfileDao:UserProfileDao,
                      facebookGraphAPI:FacebookGraphAPI) extends BaseActor{

  var userProfileOpt:Option[UserProfile] = None

  findUserProfile()

  /**
   * Try to find UserProfile in the store asynchronously
   * and send corresponding msg to self (UserProfileFound or UserProfileNotFound)
   */
  def findUserProfile() = {
    val userProfileFuture:Future[Option[UserProfile]] = userProfileDao.get(platformUserId,msgPlatform)
      
    userProfileFuture onSuccess  {
      case Some(userProfile) => {
        log.debug("User profile found for facebook user: {}",platformUserId)
        self ! UserProfileFound(userProfile)
      }

      case None => {
        log.debug("User profile not found for facebook user: {}",platformUserId)
        self ! UserProfileNotFound()
      }
    }

    userProfileFuture onFailure {
      case exception:Exception => log.error(exception,s"Failed to find user profile userId: $platformUserId, msgPlatform: $msgPlatform")
    }
  }

  def askForFacebookProfile() = {
    val getUserProfileFuture =  facebookGraphAPI.getUserProfile(platformUserId)

    getUserProfileFuture onSuccess{
      case getUserProfile:FacebookProfile => self ! FacebookProfileReceived(getUserProfile)
    }

    getUserProfileFuture onFailure{
      case exception:Exception => log.error(exception,s"Failed to get facebook profile userId: $platformUserId, msgPlatform: $msgPlatform")
    }

  }

  def facebookUser(userProfile:UserProfile):Boolean = true


  def extractFacebookId(userProfile:UserProfile):String =
    userProfile.userId

  def echo(userMsg: UserTextMsgDTO): BotTextMsgDTO = {
    val name:String = chatName.getOrElse("")
    BotTextMsgDTO(userMsg.msgPlatform,userMsg.platformUserId,None,s"Got your message $name : ${userMsg.text}",TimeUtil.now())
  }


  def chatName:Option[String] =  {
    for {
      userProfile <- userProfileOpt
      firstName <- userProfile.userDetails.firstName
    }yield firstName
    
  }


  def toUserDetails(facebookProfile: FacebookProfile): UserDetails = {
    UserDetails(facebookProfile.first_name,
                facebookProfile.last_name,
                facebookProfile.profile_pic,
                facebookProfile.locale,
                facebookProfile.timezone,
                facebookProfile.gender)
  }

  override def receive: Receive = {
    case chatMsg: UserTextMsgDTO => {
      log.debug(s"ChatManager received msg: {}", chatMsg)

      val botMsg = echo(chatMsg)
      AppContext.fbConnector ! botMsg //TODO patch: use pub/sub instead of direct messaging
    }

    case userProfileFound:UserProfileFound => {
      log.debug("User profile found : {}",userProfileFound.userProfile)
      userProfileOpt = Some(userProfileFound.userProfile)
      askForFacebookProfile()
    }

    case userProfileNotFound:UserProfileNotFound => {
      log.debug("User profile not found for facebook user: {}, going to create new profile",platformUserId)
      userProfileOpt = Some(UserProfile(platformUserId,msgPlatform))
      askForFacebookProfile()
    }

    case facebookProfileReceived:FacebookProfileReceived => {
      log.debug("FacebookProfile received from facebook system: {}, going to merge the data to userProfile",facebookProfileReceived.facebookProfile)
      userProfileOpt match {
        case Some(userProfile) => userProfileOpt = Some(userProfile.copy(userDetails = toUserDetails(facebookProfileReceived.facebookProfile)))
        case None => log.error("Incorrect state. It was expected to have userProfile at this stage (FacebookProfileReceived)")
      }
    }
  }




  /**************************
   * Internal API
   *************************/

  /**
   * UserProfile is found in store (meaning it was returning user)
   */
  case class UserProfileFound(userProfile:UserProfile)

  /**
   * UserProfile is not found in store (due to error or the user is first timer)
   */
  case class UserProfileNotFound()
  
  /**
   * FacebookProfile received from facebook system
   */
  case class FacebookProfileReceived(facebookProfile:FacebookProfile)

}
