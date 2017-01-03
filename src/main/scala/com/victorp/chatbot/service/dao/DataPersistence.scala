package com.victorp.chatbot.service.dao

import java.io.File
import java.io.{File, PrintWriter}
import java.nio.file.Path

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

  def readFromFile() : Option[String] = {
    if (new File(file.getPath) exists) {
      Some(Source.fromFile(file.getPath).getLines().mkString)
    } else {
      None
    }
  }
}
