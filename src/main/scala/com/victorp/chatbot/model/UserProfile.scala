package com.victorp.chatbot.model



/**
 * @author victorp
 */
case class UserProfile(id:String,
                       chatProfile:Option[ChatProfile]= None,
                       fbProfile:Option[FacebookProfile]= None) extends Entity

/**
 * Provided by facebook
 */
case class FacebookProfile(facebookUserId:String,
                           firstName:Option[String] = None,
                           lastName:Option[String]= None,
                           profilePicURL:Option[String]= None,
                           locale: Option[String]= None,
                           timezone: Option[Int]= None,
                           gender:Option[String]= None)

/**
 * Used while chatting with a user
 * The info is provided by the user via chat or got from some msg platform
 */
case class ChatProfile(name:Option[String])
