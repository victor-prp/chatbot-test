package com.victorp.chatbot.service.dao

import java.nio.file.Path

import com.victorp.chatbot.model.ChatMsg
import com.victorp.chatbot.model.json.{UserData, JsonDB}

import scala.concurrent.Future

/**
 * @author victorp
 */
class ChatMsgDao(val fullFileName:Path) extends BaseDao[ChatMsg]{

  override def saveNewToDB(chatMsg: ChatMsg, db: JsonDB): JsonDB = {
    val updatedUseData:UserData =
     db.usersData.get(chatMsg.userId) match  {
      case None => UserData(chatMsgs = List(chatMsg))
      case Some(userData) => UserData(chatMsgs = chatMsg::userData.chatMsgs)
    }
    JsonDB(db.usersData + (chatMsg.userId -> updatedUseData))
  }

  override def getAllFromDB(db: JsonDB, ids:String*): Seq[ChatMsg] = {
    val userId:String = ids match {
      case head::Nil => head
      case _ => throw new RuntimeException(s"ids must contain exactly one id representing the userId, but was ${ids.size}")
    }

    val userDataOpt = db.usersData.get(userId)
    userDataOpt match {
      case None => List()
      case Some(userData) => userData.chatMsgs
    }
  }



  override def updateById(id: Long, entity: ChatMsg): Future[Int] = ???

  override def deleteById(id: Long): Future[Int] = ???

  override def getById(id: Long): Future[Option[ChatMsg]] = ???

}
