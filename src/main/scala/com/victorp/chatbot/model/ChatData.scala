package com.victorp.chatbot.model

/**
 * @author victorp
 */
case class Chat(msgs:List[ChatMsg] = List()) {
  def add(msg:ChatMsg):Chat = {
    Chat(msg::msgs)
  }

  def +(msg:ChatMsg):Chat = add(msg)
}

/**
 * @param id chat line id
 * @param timestamp server timestamp
 * @param text the text written within the line
 * @param sourceId entity id who was actually generated this msg (the bot also has id)
 */
case class ChatMsg(id:String,timestamp:Long,text:String,sourceId:String)
