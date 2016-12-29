package com.victorp.chatbot.service.dao

import com.victorp.chatbot.model.Entity

import scala.concurrent.Future

/**
 * @author victorp
 */
trait Dao[E <: Entity] {
  def getById(id: Long) : Future[Option[E]]
  def getAll(ids:String*) : Future[Seq[E]]
  def saveNew(entity: E) : Future[Unit]
  def deleteById(id: Long) : Future[Int]
  def updateById(id: Long, entity: E) : Future[Int]
}
