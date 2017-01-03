package com.victorp.chatbot.service.dao

import java.nio.file.Path

import com.victorp.chatbot.model.json.{JsonDB, UserData}
import com.victorp.chatbot.model.{ChatMsg, UserProfile}

import scala.concurrent.Future
/**
 * @author victorp
 */


class ChatMsgDao(val fullFileName:Path) extends BaseDao{


  private def extractMsgsFromDB(userId:String,msgPlatform:String,db:JsonDB):Seq[ChatMsg] = {
    val userDataOpt:Option[UserData] = extractUser(userId,msgPlatform,db)

    userDataOpt match {
     case None => List()
     case Some(userDetails) => userDetails.chatMsgs
   }

  }

  def getAll(userId:String,msgPlatform:String):Future[Seq[ChatMsg]] = {
    getFromDB(extractMsgsFromDB(userId,msgPlatform,_))
  }



  private def addMsg(msg: ChatMsg)( db: JsonDB): JsonDB = {
    val userDataOpt:Option[UserData] = extractUser(msg.userId,msg.msgPlatform,db)

    val updatedUserData:UserData =
      userDataOpt match {
      case None => createNewUserWithMsg(msg)
      case Some(userData) => userData.copy(chatMsgs= msg::userData.chatMsgs)
    }

    val updatedUsers = updateUser(db.usersData, updatedUserData)
    JsonDB(updatedUsers)
  }



  private def createNewUserWithMsg(msg: ChatMsg): UserData = {
    UserData(UserProfile(userId = msg.userId, msgPlatform = msg.msgPlatform), List(msg))
  }

  /**
   * Saves new message
   */
  def save(msg:ChatMsg):Future[Unit] = {
    updateDB(addMsg(msg))
  }


}
