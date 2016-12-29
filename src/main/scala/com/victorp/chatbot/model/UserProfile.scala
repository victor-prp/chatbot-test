package com.victorp.chatbot.model



/**
 * @author victorp
 */
case class UserProfile(id:String,chatProfile:ChatProfile,fbProfile:Option[FacebookProfile]) extends Entity

/**
 * Provided by facebook
 */
case class FacebookProfile(firstName:String, lastName:String,profilePicURL:String, locale: String, timezone: Int, gender:String)

/**
 * Provided by the user via chat
 */
case class ChatProfile(name:String)
