package com.victorp.chatbot.transform

import com.victorp.chatbot.dto.{UserTextMsgDTO, BotTextMsgDTO}
import com.victorp.chatbot.model.{SentBy, ChatMsg}

/**
 * @author victorp
 */
object TransformChatMsg {


  def toBotMsgDto(chatMsg:ChatMsg):BotTextMsgDTO = {
    BotTextMsgDTO(chatMsg.msgPlatform,chatMsg.userId,None,chatMsg.text,chatMsg.timestamp)
  }

  def transformWithSeq(seq:Long)(userTextMsgDTO: UserTextMsgDTO):ChatMsg = {
    ChatMsg(userTextMsgDTO.platformUserId,userTextMsgDTO.msgPlatform,seq,userTextMsgDTO.text,userTextMsgDTO.timestamp,SentBy.USER,userTextMsgDTO.platformMsgId)
  }
}
