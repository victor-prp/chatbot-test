package com.victorp.chatbot.model

/**
 * The unique id of chat message is represented by the tuple: (userId,msgPlatform,sequenceNumber)
 *
 * @author victorp
 */

/**
 * Sent by the user or the bot
 */
case class ChatMsg(userId:String, //provides by the messaging platform
                   msgPlatform: String, //messaging platform name (FACEBOOK, LINE etc)
                   sequenceNumber: Long,// starting from 1 this number represents
                                        // the location of the message (according to system).
                                        // the scope is the user and the platform
                   text: String, //the test of this message
                   timestamp: Long, // when the message was generated
                   sentBy:String,// 'user' or 'bot'
                   platformMsgId:Option[String] // some platforms may provide unique msg id for each message
                    ) extends Entity


