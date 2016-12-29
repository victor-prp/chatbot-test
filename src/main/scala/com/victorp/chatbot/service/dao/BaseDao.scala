package com.victorp.chatbot.service.dao

import java.util

import com.victorp.chatbot.model.{UserProfile, Entity}
import com.victorp.chatbot.model.json.{JsonDB, ModelJsonProtocol}

import scala.collection.JavaConverters._
import scala.concurrent.{Future, ExecutionContext}

import spray.json._

/**
 * @author victorp
 */
trait BaseDao[E <: Entity] extends Dao[E] with ModelJsonProtocol with DataPersistence {
  import ExecutionContext.Implicits.global

  if (!file.exists()) {
    createEmptyDB()
  }


  def createEmptyDB(): Unit = With writeLock{
    file.createNewFile()
    writeToFile(jsonDB.write(JsonDB()).toString())
  }


  def saveNewToDB(entity: E,db: JsonDB):JsonDB
  def getAllFromDB(db: JsonDB,ids:String*): Seq[E]


  override final def saveNew(entity: E): Future[Unit] = Future{
    With writeLock{
      val jsonFromFile = readFromFile()
      val json = jsonFromFile.parseJson
      val db = jsonDB.read(json)
      val updatedDB = saveNewToDB(entity,db)
      val updateJson = jsonDB.write(updatedDB).toString()
      writeToFile(updateJson)
    }
  }



  override def getAll(ids:String*): Future[Seq[E]] = Future{
    With writeLock{
      val jsonFromFile = readFromFile()
      val db = jsonDB.read(jsonFromFile.parseJson)
      getAllFromDB(db, ids:_*)
    }
  }

}