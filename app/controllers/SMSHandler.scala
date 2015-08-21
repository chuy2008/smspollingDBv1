package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.ws._
import scala.concurrent.Future
import play.api.Play.current
import scala.xml._
import scala.util.{Success, Failure}

import play.api.libs.json._
import play.api.libs.functional.syntax._

import models._
import views._

case class ReceivedSMS(
                  userID: String, 
                  country: String,
                  mobile: String,
                  processed: String,
                  message: String,
                  hour: String,
                  dayOfYear: String,
                  dayOfMonth: String,
                  monthOfYear: String,
                  year: String)
                  
// includes minutes...
                  
case class ReceivedSMS2(
                  userID: String, 
                  country: String,
                  mobile: String,
                  processed: String,
                  message: String,
                  minute: String,
                  hour: String,
                  dayOfYear: String,
                  dayOfMonth: String,
                  monthOfYear: String,
                  year: String)
                  

object SMSHandler extends Controller 
{
   implicit val smsReceivedReads: Reads[ReceivedSMS] = (
     (JsPath \ "userID").read[String] and 
     (JsPath \ "country").read[String] and      
     (JsPath \ "mobile").read[String] and      
     (JsPath \ "processed").read[String] and 
     (JsPath \ "message").read[String] and 
     (JsPath \ "hour").read[String] and 
     (JsPath \ "dayOfYear").read[String] and      
     (JsPath \ "dayOfMonth").read[String] and 
     (JsPath \ "monthOfYear").read[String] and 
     (JsPath \ "year").read[String]
   )(ReceivedSMS.apply _)
   
   implicit val smsReceivedReads2: Reads[ReceivedSMS2] = (
     (JsPath \ "userID").read[String] and 
     (JsPath \ "country").read[String] and      
     (JsPath \ "mobile").read[String] and      
     (JsPath \ "processed").read[String] and 
     (JsPath \ "message").read[String] and
     (JsPath \ "minute").read[String] and      
     (JsPath \ "hour").read[String] and 
     (JsPath \ "dayOfYear").read[String] and      
     (JsPath \ "dayOfMonth").read[String] and 
     (JsPath \ "monthOfYear").read[String] and 
     (JsPath \ "year").read[String]
   )(ReceivedSMS2.apply _)

  
   def searchForTweetsInMainAppAndStoreThemLocally(urll: String, country: Int) = 
   {
     if (models.DebugVariables.debugM2 == true)
     {
         println("***** here I am on SMSHandler.... *********")
     }
     implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
     val url2 = urll + country.toString
     WS.url(url2).get.onComplete 
     { 
         case Success(wsResp) => 
         {
            def loop(remainingJsValueToWorkWith: play.api.libs.json.JsValue): Boolean =
            {
                if (models.DebugVariables.debugM2 == true)
                {
                   println("**** SMSHandler Scala file, within searchForTweetsInMainAppAndStoreThemLocally function, on loop function ****")
                   println(" received parameter in loop function = " + remainingJsValueToWorkWith)
                   println("*************************************************************************")              
                }              
                remainingJsValueToWorkWith.head.validate[ReceivedSMS] match               
                {
                  case s: JsSuccess[ReceivedSMS] => 
                  {
                     def storeCollectedTweet(presTwt: ReceivedSMS) =
                     {
                         def extractLocalNumber(phone: String): String =
                         {
                             if (phone.length > 10)
                             {
                                phone.substring(phone.length - 10, phone.length)
                             }
                             else{phone}
                         }         
                         val smsMessageForStorage = new SMSMessageForStorage(presTwt.userID.toInt,
                                                                  presTwt.country.toInt,
                                                                  extractLocalNumber(presTwt.mobile),
                                                                  presTwt.processed.toInt,
                                                                  presTwt.message,
                                                                  presTwt.hour.toInt,
                                                                  presTwt.dayOfYear.toInt,
                                                                  presTwt.dayOfMonth.toInt,
                                                                  presTwt.monthOfYear.toInt,
                                                                  presTwt.year.toInt)
                         models.SMSMessages.addSMSMessages(smsMessageForStorage)
                     }
                     if (models.DebugVariables.debugM2 == true)
                     {
                        println("**** SMSHandler Scala file, within searchForTweetsInMainAppAndStoreThemLocally function, on loop function ****")
                        println(" present tweets head value = " + s.get)
                        println("this is the next parameter to be passed to loop = " + remainingJsValueToWorkWith.tail.getOrElse(Json.toJson("leaveLoop")))
                        println("*************************************************************************")
                        storeCollectedTweet(s.get)                    
                     }                    
                     loop(remainingJsValueToWorkWith.tail.getOrElse(Json.toJson("leaveLoop")))
                  }
                  case e: JsError           => 
                  {
                     true                  
                  }                                         
                }
             }
             loop((wsResp.json \ "tweets").getOrElse(Json.toJson("leaveLoop")))
         }
         case Failure(t) => 
         {
                   
         }       
     }
   }   //    def searchForTweetsInMainAppAndStoreThemLocally ...
   
   def searchForTweetsInMainAppAndStoreThemLocally2(urll: String, name: String, pwd: String) = 
   {
     if (models.DebugVariables.debugM2 == true)
     {
         println("***** here I am on SMSHandler line 152.... *********")
     }
     implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
     val url2 = urll
/*
 *  OJO= have to post here RemoteAppName and Password...     
 */
//Json.toJson(Map("Message" -> status._1, "DestinationPhone" -> status._2,     
//     WS.url(url2)   get.onComplete 
//     WS.url(url2).get.onComplete
     WS.url(url2).post(Json.toJson(Map("remoteAppID" -> name, "password" -> pwd))).onComplete
     { 
         case Success(wsResp) => 
         {
            def loop(remainingJsValueToWorkWith: play.api.libs.json.JsValue): Boolean =
            {
                if (models.DebugVariables.debugM2 == true)
                {
                   println("**** SMSHandler Scala file, within searchForTweetsInMainAppAndStoreThemLocally2 function, on loop function ****")
                   println(" received parameter in loop function = " + remainingJsValueToWorkWith)
                   println("*************************************************************************")              
                }              
                remainingJsValueToWorkWith.head.validate[ReceivedSMS2] match               
                {
                  case s: JsSuccess[ReceivedSMS2] => 
                  {
                     def storeCollectedTweet(presTwt: ReceivedSMS2) =
                     {
                         def extractLocalNumber(phone: String): String =
                         {
                             if (phone.length > 10)
                             {
                                phone.substring(phone.length - 10, phone.length)
                             }
                             else{phone}
                         }         
                         val smsMessageForStorage = new SMSMessageForStorage2(presTwt.userID.toInt,
                                                                  presTwt.country.toInt,
                                                                  extractLocalNumber(presTwt.mobile),
                                                                  presTwt.processed.toInt,
                                                                  presTwt.message,
                                                                  presTwt.minute.toInt,
                                                                  presTwt.hour.toInt,
                                                                  presTwt.dayOfYear.toInt,
                                                                  presTwt.dayOfMonth.toInt,
                                                                  presTwt.monthOfYear.toInt,
                                                                  presTwt.year.toInt)
                         models.SMSMessages2.addSMSMessages(smsMessageForStorage)
                     }
                     if (models.DebugVariables.debugM2 == true)
                     {
                        println("**** SMSHandler Scala file, within searchForTweetsInMainAppAndStoreThemLocally function, on loop function ****")
                        println(" present tweets head value = " + s.get)
                        println("this is the next parameter to be passed to loop = " + remainingJsValueToWorkWith.tail.getOrElse(Json.toJson("leaveLoop")))
                        println("*************************************************************************")
                        storeCollectedTweet(s.get)                    
                     }                    
                     loop(remainingJsValueToWorkWith.tail.getOrElse(Json.toJson("leaveLoop")))
                  }
                  case e: JsError           => 
                  {
                     true                  
                  }                                         
                }
             }
             loop((wsResp.json \ "tweets").getOrElse(Json.toJson("leaveLoop")))
         }
         case Failure(t) => 
         {
                   
         }       
     }
   }   //    def searchForTweetsInMainAppAndStoreThemLocally ...

/*      
   def sendOutstandingSMSsFromDB =
   {
       val outstandingSMSMessages = models.SMSMessages.getAll
       for (smsMssg <- outstandingSMSMessages)
       {
          val unOrderedSmallTwtLst = models.SMSMessages.getAllTweetsWithSameRandTweetCode(smsMssg.tweetRandID)
          val orderedSmallTwtLst = models.SMSMessageSorter.isortMinOnTop(unOrderedSmallTwtLst)
          for (smsMssg2 <- orderedSmallTwtLst)
          {
              if (models.DebugVariables.debugM2 == true)
              {
                  println("SMSHandler scala routine, line 120, message to be sent = " + smsMssg2.message)
               }
               sendSMSViaPost4(smsMssg2.mobile, smsMssg2.message,
                               11, smsMssg2.hour, smsMssg2.year, smsMssg2.monthOfYear,
                               smsMssg2.dayOfMonth)
          }  
       }
       for (smsMssg <- outstandingSMSMessages)
       {
           models.SMSMessages.deleteSMSMessage(smsMssg)
       }
   }
 */
   
/*
 *   the following method is valid if there is no concatenation of SMS Messages, namely
 *   that the random code is unique per SMS Message.
 *   In case the above assumption does not hold, I have to devise another scheme to fetch
 *   orderely (via consecutive numbers) SMS's belonging to the same Random Code
 *     
 */
   def sendOutstandingSMSsFromDBValidIfMessagesNotConcatenated =
   {
      val outstandingSMSMessages = models.SMSMessages.getAll     
      outstandingSMSMessages.headOption match
      {
         case Some(x) =>
         {
            sendSMSViaPost4(x.mobile, x.message,
                            11, x.hour, x.year, x.monthOfYear,
                            x.dayOfMonth)
            models.SMSMessages.deleteSMSMessage(x)
         }       
         case None    => {}         
      }
   }
   
   def sendOutstandingSMSsFromDBValidIfMessagesNotConcatenated2 =
   {
      val outstandingSMSMessages = models.SMSMessages2.getAll     
      outstandingSMSMessages.headOption match
      {
         case Some(x) =>
         {
            sendSMSViaPost4(x.mobile, x.message,
                            x.minute, x.hour, x.year, x.monthOfYear,
                            x.dayOfMonth)
            models.SMSMessages2.deleteSMSMessage(x)
         }       
         case None    => {}         
      }
   }   
 
   def printOutstandingSMSsFromDBValidIfMessagesNotConcatenated2 = 
   {
      val outstandingSMSMessages = models.SMSMessages2.getAll     
      outstandingSMSMessages.headOption match
      {
         case Some(x) =>
         {
            printSMSsOnScreen(x.mobile, x.message,
                            x.minute, x.hour, x.year, x.monthOfYear,
                            x.dayOfMonth)
            models.SMSMessages2.deleteSMSMessage(x)
         }       
         case None    => {}         
      }
   }     
   
   
    def sendSMSViaPost4(phone: String, content: String,
                        minute: Int, hour: Int, year: Int, month: Int, day:Int) = 
    {
        def assembleDateForSMS(minute: Int, hour: Int, 
                               year: Int, month: Int, day: Int): String =
        {
              val minutee = if (minute.toString.length < 2) "0" + minute.toString else minute.toString
              val houree  = if (hour.toString.length < 2) "0" + hour.toString else hour.toString
              val monthh   = if (month.toString.length < 2) "0" + month.toString else month.toString
              val dayy     = if (day.toString.length < 2) "0" + day.toString else day.toString
              year.toString + "-" + monthh + "-" + dayy + " " + houree + ":" + minutee + ":" + 11
        }                                       
        val datee = assembleDateForSMS(minute, hour, year, month, day)
        val strToConvertToXML = "<request><Index>-1</Index><Phones><Phone>" + phone + 
                                "</Phone>" + "</Phones><Sca></Sca><Content>" + content + 
                                 "</Content><Length>" + content.length.toString + "</Length>" + 
                                 "<Reserved>1</Reserved><Date>" + 
                                 datee + "</Date></request>"
        val data2 = scala.xml.XML.loadString(strToConvertToXML)
        if (models.DebugVariables.debugM2 == true)
        {
             println("SMSHandler scala routine, line 151, val data2 in xml format = " + data2)
         }
         val futureResponse: Future[WSResponse] = WS.url("http://192.168.1.1/api/sms/send-sms").post(data2)
      }   // def sendSMSViaPost4...
    
    def printSMSsOnScreen(phone: String, content: String,
                        minute: Int, hour: Int, year: Int, month: Int, day:Int) = 
    {
        def assembleDateForSMS(minute: Int, hour: Int, 
                               year: Int, month: Int, day: Int): String =
        {
              val minutee = if (minute.toString.length < 2) "0" + minute.toString else minute.toString
              val houree  = if (hour.toString.length < 2) "0" + hour.toString else hour.toString
              val monthh   = if (month.toString.length < 2) "0" + month.toString else month.toString
              val dayy     = if (day.toString.length < 2) "0" + day.toString else day.toString
              year.toString + "-" + monthh + "-" + dayy + " " + houree + ":" + minutee + ":" + 11
        }                                       
        val datee = assembleDateForSMS(minute, hour, year, month, day)
        val strToConvertToXML = "<request><Index>-1</Index><Phones><Phone>" + phone + 
                                "</Phone>" + "</Phones><Sca></Sca><Content>" + content + 
                                 "</Content><Length>" + content.length.toString + "</Length>" + 
                                 "<Reserved>1</Reserved><Date>" + 
                                 datee + "</Date></request>"
        val data2 = scala.xml.XML.loadString(strToConvertToXML)
        if (models.DebugVariables.debugM2 == true)
        {
             println("SMSHandler scala routine, line 151, val data2 in xml format = " + data2)
         }
         println("************** SMS Handler scala routine, line 357, about to print an SMS Message *******")
         println("****************** instead of sending it via the BAM *********************************")
         println("val data2 in xml format = " + data2)
         println("***************************** end ************************************")
//         val futureResponse: Future[WSResponse] = WS.url("http://192.168.1.1/api/sms/send-sms").post(data2)
      }   // def sendSMSViaPost4...
     
}