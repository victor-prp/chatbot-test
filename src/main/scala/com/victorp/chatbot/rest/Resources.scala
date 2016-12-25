package com.victorp.chatbot.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.victorp.chatbot.AppContext
import com.victorp.chatbot.dto.FBUserTextMessage
import com.victorp.chatbot.protocol.FBProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
/**
 * @author victorp
 */
object Resources extends FBProtocol{

  val routes: Route =

    path("chat") {
      post{
        entity(as[FBUserTextMessage]) { fbUserTextMessage =>
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


