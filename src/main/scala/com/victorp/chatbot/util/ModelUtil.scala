package com.victorp.chatbot.util

import com.victorp.chatbot.dto.ChatMessage
import com.victorp.chatbot.model.ChatMsg
import com.victorp.chatbot.util.IdUtil._
import com.victorp.chatbot.util.TimeUtil._

/**
 * @author victorp
 */
object ModelUtil {
  def convert(msg:ChatMessage):ChatMsg = ChatMsg(nextMsgId(),now(),msg.text,msg.userId)

}
