package com.victorp.chatbot.dto

/**
 * @author victorp
 */

/**
 * TEXT Message from a user
 */
final case class FBUserTextMessage(userId:String,text:String) {}

/**
 * TEXT Message from a bot
 */
case class FBBotTextMessage(userId:String,text:String) {}




