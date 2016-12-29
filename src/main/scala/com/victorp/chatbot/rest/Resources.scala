package com.victorp.chatbot.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.victorp.chatbot.app.AppContext
import com.victorp.chatbot.dto.UserTextMsgDTO
import com.victorp.chatbot.dto.json.DtoJsonProtocol
/**
 * @author victorp
 */
object Resources extends DtoJsonProtocol{

  val routes: Route =

    path("chat") {
      post{
        entity(as[UserTextMsgDTO]) { fbUserTextMessage =>
          AppContext.fbConnector ! fbUserTextMessage
          complete(StatusCodes.Accepted,"message accepted")
        }
      }
    } ~
    path("alive") {
      get {
        complete {
          "alive"
        }
      }
    }

}


