package com.victorp.chatbot.service.facebook

import spray.json.DefaultJsonProtocol

/**
 * @author victorp
 */
trait FacebookGraphProtocol extends DefaultJsonProtocol {

  implicit val facebookProfileJsonProtocol = jsonFormat6(FacebookProfile)
}
