package com.victorp.chatbot.actor

import akka.util.ByteString
import com.victorp.chatbot.dto.ChatMessage
import com.victorp.chatbot.model.{ChatMsg, Chat}
import com.victorp.chatbot.util.IdUtil
import com.victorp.chatbot.util.IdUtil._
import com.victorp.chatbot.util.ModelUtil.convert
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

  def echo(msg: ChatMessage): ChatMsg = {
    ChatMsg(nextMsgId(),now(),s"Got your message: ${msg.text}" ,IdUtil.botId)
  }

  override def receive: Receive = {
    case chatMsg: ChatMessage => {
      log.debug(s"ChatManager received msg: {}", chatMsg)
      chat = chat + convert(chatMsg)
      val botMsg = echo(chatMsg)
      chat = chat + botMsg

      http.singleRequest(HttpRequest(uri = "http://akka.io"))
        .pipeTo(self)
    }


    /**
     * The response is receive as a message to the actor since we called pipeTo(self)
     */
      case HttpResponse(StatusCodes.OK, headers, entity, _) =>
        log.info("Got response, headers: {}" ,headers)

      case HttpResponse(code, _, _, _) =>
        log.info("Request failed, response code: {}",code)
    }



}
