package com.victorp.chatbot.service.dao

import java.nio.file.Path

import com.victorp.chatbot.model.UserProfile
import com.victorp.chatbot.model.json.{JsonDB, ModelJsonProtocol, UserData}
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author victorp
 */
class UserProfileDao(val fullFileName:Path) extends BaseDao[UserProfile] {

  def saveNewToDB(userProfile: UserProfile,db: JsonDB):JsonDB = {
    JsonDB(db.usersData + (userProfile.id -> UserData(Some(userProfile))))
  }


  def getAllFromDB(db: JsonDB,ids:String*): Seq[UserProfile] = {
    val relevantUserProfiles =
    for {
      userData <- db.usersData.values
      userProfile <- userData.userProfile
    }yield userProfile

    relevantUserProfiles.toList
  }


  override def getById(id: Long): Future[Option[UserProfile]] = ???

  override def updateById(id: Long, row: UserProfile): Future[Int] = ???

  override def deleteById(id: Long): Future[Int] = ???


}
