package com.victorp.chatbot.service.facebook


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializerSettings, ActorMaterializer}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import spray.json._
/**
 * @author victorp
 */
class FacebookGraphAPI(val actorSystem:ActorSystem, val facebookAccessToken:String) extends FacebookGraphProtocol{

  import ExecutionContext.Implicits.global

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(actorSystem))(actorSystem)

  val http = Http(actorSystem)

  def getUserProfile(userId:String):Future[FacebookProfile] = {
    def toFacebookProfile(response: HttpResponse): Future[FacebookProfile] = {

      response.status match {
        case StatusCodes.OK => {
          for {
            entity <- response.entity.toStrict(5 second)
          }yield facebookProfileJsonProtocol.read(entity.data.utf8String.parseJson)
        }
        case _ => Future.failed(new RuntimeException(s"Failed to get facebook profile for user:$userId, unexpected http status code: ${response.status} "))
      }
    }

    actorSystem.log.debug("getting facebook user profile for userId: {}", userId)
    val responseFuture = http.singleRequest( HttpRequest(uri = s"https://graph.facebook.com/v2.6/$userId?access_token=$facebookAccessToken",
                        method = HttpMethods.GET))

    val result:Future[FacebookProfile] =
    for {
      httpResponse <- responseFuture
      facebookProfile <- toFacebookProfile(httpResponse)
    }yield facebookProfile
    result


  }

}


