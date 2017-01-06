package com.victorp.chatbot.service.facebook

/**
 * @author victorp
 */

case class FacebookProfile(first_name:Option[String],
                           last_name:Option[String],
                           profile_pic:Option[String],
                           locale:Option[String],
                           timezone:Option[Int],
                           gender:Option[String])


