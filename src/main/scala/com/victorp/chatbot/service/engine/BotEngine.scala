package com.victorp.chatbot.service.engine

import com.victorp.chatbot.model.{ChatMsg, SentBy}
import com.victorp.chatbot.service.engine.json.PredefJsonUtil

/**
 * @author victorp
 */
class BotEngine(val predef:Predef) {


  val tryToRephrase = ("Sorry, didn't get you. Please try to rephrase",2L)



  private def newMsg(botText:String,adjustOffset:Long, dialog: Option[String],chatState:ChatState,userMsg:ChatMsg): (ChatState, Option[ChatMsg]) = {
    val botMsg = ChatMsg(userMsg.userId,userMsg.msgPlatform,userMsg.sequenceNumber+1,botText,System.currentTimeMillis(),SentBy.BOT,None)
    val newState = chatState.copy(dialogName = dialog).copy(chatMsgs = botMsg::userMsg::chatState.chatMsgs)
    (newState.withAdjustedOffset(adjustOffset),Some(botMsg))
  }



  private def nextBotMsg(predefMsgs: Map[Long,PredefMsg],chatState:ChatState,userMsg:ChatMsg):(String,Long) = {
    val predefSeq = userMsg.sequenceNumber - chatState.msgsOffset

    predefMsgs.get(predefSeq+1) match {
      case Some(botMsg) => (botMsg.text,0)
      case None => tryToRephrase
    }

  }

  private def noMatch(chatState:ChatState,userMsg:ChatMsg): (ChatState, Option[ChatMsg]) = {
    newMsg(tryToRephrase._1,tryToRephrase._2,chatState.dialogName,chatState,userMsg)
  }


  private def startDialog(dialog :String,predefMsgs: Map[Long,PredefMsg],chatState:ChatState,userMsg:ChatMsg): (ChatState, Option[ChatMsg]) = {
    val (text,adjustOffset) = nextBotMsg(predefMsgs,chatState,userMsg)
    newMsg(text,adjustOffset,Some(dialog),chatState,userMsg)
  }



  private def predefDialog(dialog: String): Map[Long, PredefMsg] = {
    predef.dialogs.getOrElse(dialog, throw new IllegalStateException(s"Cannot find dialog: $dialog in predefDialogs "))
  }

  private def newDialog(chatState:ChatState,userMsg:ChatMsg): (ChatState, Option[ChatMsg]) = {
    predef.hints.get(userMsg.text) match {
      case None => noMatch(chatState:ChatState,userMsg:ChatMsg)
      case Some(dialog) => startDialog(dialog,predefDialog(dialog),chatState,userMsg)
    }
  }

  private def contDailog(dialog:String,chatState:ChatState,userMsg:ChatMsg): (ChatState, Option[ChatMsg]) = {
    predef.dialogs.get(dialog) match {
      case None => throw new IllegalStateException(s"Cannot find dialog: $dialog in predefDialogs ")
      case Some(predefMsgs) => {
        val (text,adjustOssfet) = nextBotMsg(predefMsgs,chatState,userMsg)
        newMsg(text,adjustOssfet,Some(dialog),chatState,userMsg)
      }
    }

  }

  private def contOrResetDialog(dialog: String,chatState:ChatState,userMsg:ChatMsg): (ChatState, Option[ChatMsg]) = {
    predef.hints.get(userMsg.text) match {
      case None => contDailog(dialog,chatState,userMsg)
      case Some(newDialog) => startDialog(newDialog,predefDialog(newDialog),chatState.withResetOffset(),userMsg)
    }
  }



  /**
   * @return new state and new bot message that should be sent to the user
   */
  def nextMsg(chatState:ChatState,userMsg:ChatMsg):(ChatState,Option[ChatMsg]) = {

    chatState.dialogName match {
      case None => newDialog(chatState,userMsg)
      case Some(dialog) => contOrResetDialog(dialog,chatState,userMsg)
    }
  }

}

object BotEngine{
  def apply(predef:Predef):BotEngine = { new BotEngine(predef)}

  def apply(predefJsonFilePath:String):BotEngine = {
    BotEngine(PredefJsonUtil.readFile(predefJsonFilePath))
  }

}
