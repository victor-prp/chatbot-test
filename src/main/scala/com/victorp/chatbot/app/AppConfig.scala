package com.victorp.chatbot.app

import java.net.URL

import com.typesafe.config.ConfigFactory

/**
 * @author victorp
 */
trait AppConfig {
  private val defaultConfig = ConfigFactory.load()
  private val config = ConfigFactory.parseURL(new URL("file:/seemedics/conf/akka/application.conf")).withFallback(defaultConfig)


  def chatServerPort: Int = {
    config.getInt("chat-server.port")
  }

  def chatServerHost: String = {
    config.getString("chat-server.host")
  }


  def facebookProxyPort: Int = {
    config.getInt("facebook.proxy.port")
  }

  def facebookProxyHost: String = {
    config.getString("facebook.proxy.host")
  }

  def userDataFilePath: String = {
    config.getString("local-storage.user-data.file-name")

  }
}
