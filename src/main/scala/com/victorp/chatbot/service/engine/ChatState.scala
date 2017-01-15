package com.victorp.chatbot.service.engine

import com.victorp.chatbot.model.{ChatMsg, UserProfile}

/**
 * @author victorp
 */
case class ChatState( userProfileOpt:Option[UserProfile] = None,
                      chatMsgs:List[ChatMsg] = List(),
                      dialogName:Option[String] = None, //currently used for predefined dialogs
                      msgsOffset:Long = 0
                      ) {

  def withResetOffset(): ChatState = {
    copy(msgsOffset = chatMsgs.size)
  }

  def withAdjustedOffset(adjustOffsetBy:Long): ChatState = {
    copy(msgsOffset = msgsOffset + adjustOffsetBy)
  }

  def lastSeq:Long = {
    chatMsgs match {
      case first::_ => first.sequenceNumber
      case _ => 0
    }
  }
}
