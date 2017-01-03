package com.victorp.chatbot.service.dao

import java.util

import com.victorp.chatbot.model.{UserProfile, Entity}
import com.victorp.chatbot.model.json.{UserData, JsonDB, ModelJsonProtocol}

import scala.collection.JavaConverters._
import scala.concurrent.{Future, ExecutionContext}

import spray.json._

/**
 * @author victorp
 */
trait BaseDao extends Dao with ModelJsonProtocol with DataPersistence {
  import ExecutionContext.Implicits.global

  if (!file.exists()) {
    createEmptyDB()
  }


  def createEmptyDB(): Unit = With writeLock{
    file.createNewFile()
    writeToFile(dbJsonProtocol.write(JsonDB()).toString())
  }


  protected final def not(newUserData: UserData)(oldUserData: UserData):Boolean = {
    newUserData.userProfile.userId != oldUserData.userProfile.userId  ||
      newUserData.userProfile.msgPlatform != oldUserData.userProfile.msgPlatform
  }

  protected final def updateUser(users: List[UserData], updatedUserData: UserData): List[UserData] = {
    updatedUserData :: users.filter(not(updatedUserData))
  }

  final def updateDB(update:  JsonDB => JsonDB ): Future[Unit] = Future{
    With writeLock{
      val jsonFromFile = readFromFile()
      val db: JsonDB = jsonToDB(jsonFromFile)
      val updatedDB = update(db)
      val updateJson = dbJsonProtocol.write(updatedDB).toString()
      writeToFile(updateJson)
    }
  }


  def jsonToDB(jsonFromFileOpt:Option[String]): JsonDB = {
    jsonFromFileOpt match{
      case Some(jsonFromFile) => dbJsonProtocol.read(jsonFromFile.parseJson)
      case None => JsonDB()
    }
  }

  final def getFromDB[T](extractFromDB:  JsonDB => T ): Future[T] = Future{
    With writeLock{
      val jsonFromFile = readFromFile()
      val db = jsonToDB(jsonFromFile)
      extractFromDB(db)
    }
  }

  def extractUser(userId:String,msgPlatform:String,db:JsonDB): Option[UserData] = {
    val users =
    for {
      userData <- db.usersData
      if userData.userProfile.userId == userId
      if userData.userProfile.msgPlatform == msgPlatform
    }yield userData

    users.toList match {
      case Nil => None
      case head::Nil => Some(head)
      case _ => throw new IllegalStateException(s"Only one user expected to be in the store with userId:$userId , msgPlatform:$msgPlatform  but was ${users.size}")
    }
  }

}