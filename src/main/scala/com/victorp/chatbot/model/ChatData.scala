package com.victorp.chatbot.model

/**
 * @author victorp
 */



/**
 * Sent by the user
 */
case class ChatMsg(id: String,userId:String,
                   text: String,
                   timestamp: Long,
                   sentBy:String,
                   platform: String,
                   platformUserId:String,
                   platformMsgId:Option[String] ) extends Entity


