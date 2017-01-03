package com.victorp.chatbot.service.dao

import java.nio.file.Path

import com.victorp.chatbot.model.UserProfile
import com.victorp.chatbot.model.json.{JsonDB, ModelJsonProtocol, UserData}
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author victorp
 */
class UserProfileDao(val fullFileName:Path) extends BaseDao{

  def extractUserProfile(userId: String, msgPlatform: String)(db:JsonDB):Option[UserProfile] = {
    extractUser(userId, msgPlatform, db).map(_.userProfile)
  }

  def get(userId:String, msgPlatform:String):Future[Option[UserProfile]] = {
    getFromDB{
      extractUserProfile(userId,msgPlatform)
    }
  }


  def updateUserProfile(newUserProfile: UserProfile)(db:JsonDB):JsonDB = {
    val userDataOpt:Option[UserData] = extractUser(newUserProfile.userId,newUserProfile.msgPlatform,db)

    val updatedUserData:UserData =
      userDataOpt match {
        case None => UserData(newUserProfile)
        case Some(userData) => userData.copy(userProfile = newUserProfile)
      }

    val updatedUsers = updateUser(db.usersData, updatedUserData)
    JsonDB(updatedUsers)
  }

  def save(userProfile:UserProfile):Future[Unit] = {
    updateDB{
      updateUserProfile(userProfile)
    }
  }

  def extractAll(db:JsonDB):Seq[UserProfile] = {
    db.usersData.map(_.userProfile)
  }

  def getAll():Future[Seq[UserProfile]] = {
    getFromDB{
      extractAll
    }
  }


}
