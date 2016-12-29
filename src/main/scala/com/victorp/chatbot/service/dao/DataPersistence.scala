package com.victorp.chatbot.service.dao

import java.io.{PrintWriter, File}
import java.nio.file.Path
import java.util.concurrent.locks.{ReentrantReadWriteLock, ReadWriteLock}

import com.victorp.chatbot.model.json._

import scala.io.Source

/**
 * @author victorp
 */
trait DataPersistence {
  /**
   * Must be defined by the user class
   */
  val fullFileName:Path


  val file = new File(fullFileName.toString)



  def writeToFile(string:String) = {
    val pw = new PrintWriter(file)
    try {
      pw.write(string)
    }finally {
      pw.close()
    }

  }

  def readFromFile() : String = {
    Source.fromFile(file.getPath).getLines().mkString
  }
}
