package com.victorp.chatbot.service.engine.json

import java.io.{FileInputStream, InputStream}

import com.victorp.chatbot.service.engine.{Predef, PredefMsg}
import spray.json._

/**
 * @author victorp
 */
object PredefJsonUtil extends BotEnginePredefProtocol {

  private def inStreamToString(input:InputStream):String = {
    scala.io.Source.fromInputStream(input).mkString
  }
  
  def readClasspath(resource:String): Predef = {
    readInStream(PredefJsonUtil.getClass.getClassLoader.getResourceAsStream(resource))
  }

  def readFile(filePath:String): Predef = {
    readInStream(new FileInputStream(filePath))
  }
  
  def readInStream(json:InputStream):Predef = {
    readJSON(inStreamToString(json))
  }

  def readJSON(json:String):Predef = {
    transform(jsonProtocolPredef.read(json.parseJson))
  }



  def transform(predefDialogs: PredefDialogs):Predef = {
    val dialogs = scala.collection.mutable.Map[String,Map[Long,PredefMsg]]()
    val hints = scala.collection.mutable.Map[String,String]()

    predefDialogs.dialogs.foreach{ dialog:PredefDialog =>
      dialog.hints.foreach( hint => hints(hint) = dialog.name)
      dialogs(dialog.name) = dialog.msgs.map(msg => msg.seq-> msg).toMap
    }

    Predef(dialogs.toMap,hints.toMap)
  }


}
