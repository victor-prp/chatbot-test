package com.victorp.chatbot.model



/**
 * The unique id of the profile is represented by the pair: (id,msgPlatform)
 *
 * @author victorp
 */
case class UserProfile(userId:String, //id provided by the messaging platform
                       msgPlatform: String,//messaging platform name (FACEBOOK, LINE etc)
                       userDetails:UserDetails = UserDetails()) extends Entity{

  def firstName(newfirstName:String):UserProfile = {
    val newUserDetails = this.userDetails.copy(firstName = Some(newfirstName))
    this.copy(userDetails = newUserDetails)
  }

  def lastName(newLastName:String):UserProfile = {
    val newUserDetails = this.userDetails.copy(lastName = Some(newLastName))
    this.copy(userDetails = newUserDetails)
  }

  def profilePicURL(newProfilePicURL:String):UserProfile = {
    val newUserDetails = this.userDetails.copy(profilePicURL = Some(newProfilePicURL))
    this.copy(userDetails = newUserDetails)
  }

  def locale(newLocale:String):UserProfile = {
    val newUserDetails = this.userDetails.copy(locale = Some(newLocale))
    this.copy(userDetails = newUserDetails)
  }

  def timezone(newTimezone:Int):UserProfile = {
    val newUserDetails = this.userDetails.copy(timezone = Some(newTimezone))
    this.copy(userDetails = newUserDetails)
  }

  def gender(newGender:String):UserProfile = {
    val newUserDetails = this.userDetails.copy(gender = Some(newGender))
    this.copy(userDetails = newUserDetails)
  }
}



/**
 * Basic details provided by messaging platform of the user
 */
case class UserDetails(firstName:Option[String] = None,
                       lastName:Option[String]= None,
                       profilePicURL:Option[String]= None,
                       locale: Option[String]= None,
                       timezone: Option[Int]= None,
                       gender:Option[String]= None)


