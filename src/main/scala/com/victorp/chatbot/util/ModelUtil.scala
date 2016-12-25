package com.victorp.chatbot.util

import com.victorp.chatbot.dto.{FBBotTextMessage, FBUserTextMessage}
import com.victorp.chatbot.model.MsgPlatform.FACEBOOK
import com.victorp.chatbot.model.{MsgPlatform, ChatMsg}
import com.victorp.chatbot.util.IdUtil._
import com.victorp.chatbot.util.TimeUtil._

/**
 * @author victorp
 */
object ModelUtil {
  def convertUserMsg(msg:FBUserTextMessage):ChatMsg = ChatMsg(nextMsgId(),now(),msg.text,msg.userId,IdUtil.botId,FACEBOOK)
  def convertBotMsg(msg:ChatMsg):FBBotTextMessage = FBBotTextMessage(msg.targetId,msg.text)


}
