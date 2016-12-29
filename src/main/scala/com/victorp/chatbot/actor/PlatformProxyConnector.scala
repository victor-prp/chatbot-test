package com.victorp.chatbot.actor

import akka.actor.ActorRef
import akka.http.javadsl.model.StatusCodes
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import com.victorp.chatbot.dto.json.DtoJsonProtocol
import com.victorp.chatbot.dto.{BotTextMsgDTO, UserTextMsgDTO}

/**
 * @author victorp
 */
class PlatformProxyConnector(val router:ActorRef, val fbProxyAddress:String ) extends BaseActor with DtoJsonProtocol{
  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))
  import akka.pattern.pipe
  import context.dispatcher

  val http = Http(context.system)

  override def receive: Receive ={
    case userMsg: UserTextMsgDTO => {
      log.debug("user msg received: {}", userMsg)
      router ! userMsg
    }

    case botMsg: BotTextMsgDTO => {
      log.debug("bot msg received: {}", botMsg)
      val json = botTextMessage.write(botMsg).toString()
      log.debug("sending botMsg: {}",json)
      val body = HttpEntity.Strict(ContentTypes.`application/json`, data = ByteString(json))
      http.singleRequest( HttpRequest(uri = s"http://$fbProxyAddress/proxy",
                          method = HttpMethods.POST,entity = body))
              .pipeTo(self)
    }

    case HttpResponse(StatusCodes.OK, headers, entity, _) => {
      log.debug("Got SUCCESS from Proxy : {}",entity)

    }

    case HttpResponse(status, headers, entity, _) => {
      log.debug("Got unexpected status from Proxy, status:{}, entity:{}",status,entity)
    }


  }

}
