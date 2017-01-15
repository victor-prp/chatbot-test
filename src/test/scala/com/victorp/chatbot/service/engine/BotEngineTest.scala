package com.victorp.chatbot.service.engine

import com.victorp.chatbot.model.{ChatMsg, MsgPlatform}
import com.victorp.chatbot.service.engine.json.PredefJsonUtil
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * @author victorp
 */

@RunWith(classOf[JUnitRunner])
class BotEngineTest extends FunSuite with BeforeAndAfter  {


  test("stroke dialog logic - happy flow") {

    val predef:Predef = PredefJsonUtil.readClasspath("com/victorp/chatbot/service/engine/predef-stroke.json")

    new BotEngine(predef) with UserContext {

      override val userId: String = "test-user-1"
      override val platform: String = MsgPlatform.FACEBOOK

      val initState = ChatState()
      val dialogMsgs:Map[Long,PredefMsg] = predef.dialogs.get("stroke").get


      /**
       * 1. user: "Hi bot"
       * 2. bot: "Hi. How can I help you?"
       */
      val (state1,botMsg1) = this.nextMsg(initState,transform(dialogMsgs.get(1).get))
      assert(botMsg1.get.text === dialogMsgs.get(2).get.text)
      assert(botMsg1.get.sequenceNumber === 2)
      assert(botMsg1.get.sentBy === dialogMsgs.get(2).get.sender)

      /**
       * 3. user: "I think I have a stroke"
       * 4. bot: "Please call immediately 911"
       */
      val (state2,botMsg2) = this.nextMsg(state1,transform(dialogMsgs.get(3).get))
      assert(botMsg2.get.text === dialogMsgs.get(4).get.text)
      assert(botMsg2.get.sequenceNumber === 4)
      assert(botMsg2.get.sentBy === dialogMsgs.get(4).get.sender)


      /**
       * 5. user: "Hi bot"
       * 6. bot: "Hi. How can I help you?"
       */
      val firstMsg = transform(dialogMsgs.get(1).get).copy(sequenceNumber = 5)
      val (state3,botMsg3) = this.nextMsg(state2,firstMsg)
      assert(botMsg3.get.text === dialogMsgs.get(2).get.text)
      assert(botMsg3.get.sequenceNumber === 6)
      assert(botMsg3.get.sentBy === dialogMsgs.get(2).get.sender)
    }
  }




  def  now():Long = System.currentTimeMillis()

  trait UserContext {
    val userId:String
    val platform:String
    def transform(predefMsg: PredefMsg): ChatMsg = {
      ChatMsg(userId, platform,predefMsg.seq,predefMsg.text,now(),predefMsg.sender,None)
    }
  }

  val stroke = "stroke"
}
