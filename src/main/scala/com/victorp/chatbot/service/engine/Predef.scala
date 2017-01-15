package com.victorp.chatbot.service.engine

/**
 * @author victorp
 */
case class PredefMsg(seq:Long, text:String, sender: String) {}

case class Predef(dialogs:Map[String,Map[Long,PredefMsg]],hints:Map[String,String])




