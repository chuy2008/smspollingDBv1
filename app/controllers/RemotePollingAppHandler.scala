package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

import models._

object RemotePollingAppHandler extends Controller
{
/*
   def mobilePhoneHandlerTest1 = Action(parse.json)
   {
     request =>
         println("MobilePhone scala routine, line 12, request content type = " + request.contentType)
         println("MobilePhone scala routine, line 13, request body = " + request.body)
         println("MobilePhone scala routine, line 14, request body to String = " + request.toString)
         var timePeriod = ""
         (request.body \ "TimePeriod").asOpt[String].map { timPer => timePeriod = timPer}.getOrElse 
         {
             BadRequest("Missing parameter [name]")
         }        
         
          println("MobilePhone Scala routine, line 25, timePeriod = " + timePeriod )
          println("MobilePhone Scala routine, line 26, rateCriteria = " + rateCriteria )

         val mappedTweets =  models.AppTweetsRecentRankDB.readTop(custKey.toInt, rateCriteria)
          
          def selectTopTweetsAndConvertToJson(acc: Seq[JsValue], 
                                              numberOfTweetsRemaining: Int,
                                              tweetsToWorkOn: List[Map[String, String]]
                                              ): Seq[JsValue] =
          {
              if (!tweetsToWorkOn.headOption.isEmpty && numberOfTweetsRemaining > 0 )
              {
                 selectTopTweetsAndConvertToJson(
                   acc ++ Seq(Json.toJson(Map("custName" -> custName, 
                                              "retweetCount" -> tweetsToWorkOn.head("RETWEET COUNT ="),
                                              "followdsCount" -> tweetsToWorkOn.head("followersCount="),
                                              "usersOpinion" -> tweetsToWorkOn.head("userOpinion="),
                                              "text" -> tweetsToWorkOn.head("TEXT =")))),
                                           numberOfTweetsRemaining - 1,
                                           tweetsToWorkOn.tail)                            
              }
              else {acc}        
          }
          val tweetsToTransmitToRemoteApp = selectTopTweetsAndConvertToJson(
                                                      Seq(), 5, mappedTweets)
            
          Ok(Json.toJson(Map("tweets" -> tweetsToTransmitToPhone))) 
          
          

                 
   }
 */
}