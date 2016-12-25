package com.victorp.chatbot.actor

import akka.actor.ActorRef
import akka.util.ByteString
import com.victorp.chatbot.AppContext
import com.victorp.chatbot.dto.FBUserTextMessage
import com.victorp.chatbot.model.{ChatMsg, Chat}
import com.victorp.chatbot.util.IdUtil
import com.victorp.chatbot.util.IdUtil._
import com.victorp.chatbot.util.ModelUtil.convertUserMsg
import com.victorp.chatbot.util.TimeUtil._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.ActorMaterializerSettings
/**
 * @author victorp
 */
class Chatbot(val consumerId:String) extends BaseActor{

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)


  var chat:Chat = Chat()

  def echo(userMsg: ChatMsg): ChatMsg = {
    ChatMsg(nextMsgId(),now(),s"Got your message: ${userMsg.text}" ,IdUtil.botId,userMsg.sourceId,userMsg.platform)
  }

  override def receive: Receive = {
    case chatMsg: ChatMsg => {
      log.debug(s"ChatManager received msg: {}", chatMsg)
      chat = chat + chatMsg

      val botMsg = echo(chatMsg)
      AppContext.fbConnector ! botMsg //TODO patch: use pub/sub instead of direct messaging
    }


//    /**
//     * The response is receive as a message to the actor since we called pipeTo(self)
//     */
//      case HttpResponse(StatusCodes.OK, headers, entity, _) =>
//        log.info("Got response, headers: {}" ,headers)
//
//      case HttpResponse(code, _, _, _) =>
//        log.info("Request failed, response code: {}",code)
    }



}
