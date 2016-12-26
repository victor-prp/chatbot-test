package com.victorp.chatbot.actor

import akka.actor.ActorRef
import akka.http.javadsl.model.StatusCodes
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializerSettings, ActorMaterializer}
import akka.util.ByteString
import com.victorp.chatbot.dto.FBUserTextMessage
import com.victorp.chatbot.model.ChatMsg
import com.victorp.chatbot.protocol.FBProtocol
import com.victorp.chatbot.util.ModelUtil

/**
 * @author victorp
 */
class FBConnector(val router:ActorRef, val fbProxyAddress:String ) extends BaseActor with FBProtocol{
  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))
  import akka.pattern.pipe
  import context.dispatcher

  val http = Http(context.system)

  override def receive: Receive ={
    case userMsg: FBUserTextMessage => {
      log.debug("user msg received: {}", userMsg)
      router ! ModelUtil.convertUserMsg(userMsg)
    }

    case botMsg: ChatMsg => {
      log.debug("bot msg received: {}", botMsg)
      val fbBotMsg = ModelUtil.convertBotMsg(botMsg)
      val json = botTextMessage.write(fbBotMsg).toString()
      log.info("sending botMsg: {}",json)
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
