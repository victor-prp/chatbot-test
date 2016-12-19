package com.victorp.chatbot.model

/**
 * @author victorp
 */
case class Chat(chatLines:List[ChatLine])

case class ChatLine(timestamp:Long,text:String)
