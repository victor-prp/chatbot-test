package com.victorp.chatbot.dto

/**
 * @author victorp
 */

/**
 * Sent by the user
 */
case class UserTextMsgDTO(msgPlatform:String,
                          platformUserId:String,
                          platformMsgId:Option[String],
                          text:String,
                          timestamp:Long)

/**
 * Sent by the bot
 */
case class BotTextMsgDTO(msgPlatform:String,
                         platformUserId:String,
                         platformMsgId:Option[String],
                         text:String,
                         timestamp:Long)