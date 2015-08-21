package models

import org.postgresql.ds.PGSimpleDataSource
import scala.slick.driver.PostgresDriver.simple._
import play.api.db.DB
import play.api.Play.current

case class SMSMessage(id: Option[Int],
                      tweetRandID: Int,
                      internalTweetCons: Int,
                      userID : Int,
                      country: Int,
                      mobile: String,
                      processed: Int,
                      message: String,
                      hour: Int,
                      dayOfYear: Int,
                      dayOfMonth: Int,
                      monthOfYear: Int,
                      year: Int)  
                      
case class SMSMessage2(id: Option[Int],
                      tweetRandID: Int,
                      internalTweetCons: Int,
                      userID : Int,
                      country: Int,
                      mobile: String,
                      processed: Int,
                      message: String,
                      minute: Int,
                      hour: Int,
                      dayOfYear: Int,
                      dayOfMonth: Int,
                      monthOfYear: Int,
                      year: Int)  
                       
case class SMSMessageForStorage(userID : Int,
                                country: Int,
                                mobile: String,
                                processed: Int,
                                message: String,
                                hour: Int,
                                dayOfYear: Int,
                                dayOfMonth: Int,
                                monthOfYear: Int,
                                year: Int)  
                                
case class SMSMessageForStorage2(userID : Int,
                                country: Int,
                                mobile: String,
                                processed: Int,
                                message: String,
                                minute: Int,
                                hour: Int,
                                dayOfYear: Int,
                                dayOfMonth: Int,
                                monthOfYear: Int,
                                year: Int)  

class SMSMessages(tag: Tag) extends Table[SMSMessage](tag, "smsmessages") 
{
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def tweetRandID = column[Int]("tweetRandID")
    def internalTweetCons = column[Int]("internalTweetCons")
    def userID = column[Int]("userID")
    def country = column[Int]("country") 
    def mobile = column[String]("mobile")
    def processed = column[Int]("processed")
    def message = column[String]("message")
    def hour = column[Int]("hour")
    def dayOfYear = column[Int]("dayOfYear")
    def dayOfMonth = column[Int]("dayOfMonth")
    def monthOfYear = column[Int]("monthOfYear")
    def year = column[Int]("year")
    def * = (id, tweetRandID, internalTweetCons, userID, country, mobile, processed, 
             message, hour, dayOfYear, dayOfMonth, monthOfYear, 
             year) <> (SMSMessage.tupled, SMSMessage.unapply)
}

class SMSMessages2(tag: Tag) extends Table[SMSMessage2](tag, "smsmessages2") 
{
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def tweetRandID = column[Int]("tweetRandID")
    def internalTweetCons = column[Int]("internalTweetCons")
    def userID = column[Int]("userID")
    def country = column[Int]("country") 
    def mobile = column[String]("mobile")
    def processed = column[Int]("processed")
    def message = column[String]("message")
    def minute = column[Int]("minute")
    def hour = column[Int]("hour")
    def dayOfYear = column[Int]("dayOfYear")
    def dayOfMonth = column[Int]("dayOfMonth")
    def monthOfYear = column[Int]("monthOfYear")
    def year = column[Int]("year")
    def * = (id, tweetRandID, internalTweetCons, userID, country, mobile, processed, 
             message, minute, hour, dayOfYear, dayOfMonth, monthOfYear, 
             year) <> (SMSMessage2.tupled, SMSMessage2.unapply)
} 

object SMSMessages 
{ 
    val smsMessages = TableQuery[SMSMessages]

    def existsInDB(randCode: Int): Boolean =
    {
      getSMSMessageByRandCode(randCode) match
      {
        case Some(x)  => {true}
        case None     => {false}
      }
    }

    def getSMSMessageByRandCode(randCode: Int): Option[SMSMessage] =
    {
      Database.forDataSource(DB.getDataSource()) withSession
      {
         implicit session =>
           val smss = smsMessages filter (_.tweetRandID === randCode)
           smss.list.headOption
      }
    }
    
    def getAll: List[SMSMessage] =
    {
      Database.forDataSource(DB.getDataSource()) withSession
      {
        implicit session =>
           smsMessages.list
      }
    }

    def getAllTweetsWithSameRandTweetCode(randCode: Int): List[SMSMessage] =
    {
        Database.forDataSource(DB.getDataSource()) withSession
        {
            implicit session =>
               val smss = smsMessages filter (_.tweetRandID === randCode)
               smss.list
         }          
    }
    
  def deleteSMSMessage(smsMssg: SMSMessage) = 
  {
      Database.forDataSource(DB.getDataSource()) withSession
      {
        implicit session =>
             (smsMessages filter(_.id === smsMssg.id)).delete
      } 
  }
     
   
    def addSMSMessages(smsMessage: SMSMessageForStorage) =
    {
        def produceUniqueRandomCode(): Int =
        {
            val randCode = models.RandomCode.generateRandomCode(5).toInt    
            if (DebugVariables.debugM3b == true)
            {
                println("SMSMessages scala routine, line 112, randCode = " + randCode)
            }
            val existsInDB = models.SMSMessages.existsInDB(randCode)          
            existsInDB match
            {
              case true =>
              {
                  produceUniqueRandomCode
              }
              case false =>
              {
                 randCode
              }
            }
        }
        if (DebugVariables.debugM3b == true)
        {
           println("SMSMessages scala routine, line 129, about to call produceUniqueRandomCode function ")
        }      
        val randomIDForThisTweet = produceUniqueRandomCode
        val smsMessageToInsertInDB = SMSMessage(None,
                                                randomIDForThisTweet,
                                                1,
                                                smsMessage.userID,
                                                smsMessage.country,
                                                smsMessage.mobile,
                                                smsMessage.processed,
                                                smsMessage.message,
                                                smsMessage.hour,
                                                smsMessage.dayOfYear,
                                                smsMessage.dayOfMonth,
                                                smsMessage.monthOfYear,
                                                smsMessage.year)   
      def loop (remainingSMSMessage: SMSMessage, 
                acc: List[SMSMessage],
                maxNumberOfBreakoutsPerSMSMessage: Int,
                presentBreakoutIteration: Int): List[SMSMessage] =
      {
          val maxMssgLength = 159
          
          ((remainingSMSMessage.message.length > maxMssgLength),
           (presentBreakoutIteration <= maxNumberOfBreakoutsPerSMSMessage)) match
           {
              case (true, true) =>
              {
                    val newSMSMssgToAppend = new SMSMessage(None,
                                              remainingSMSMessage.tweetRandID,
                                              remainingSMSMessage.internalTweetCons,
                                              remainingSMSMessage.userID,
                                              remainingSMSMessage.country,
                                              remainingSMSMessage.mobile,
                                              remainingSMSMessage.processed,
                                              remainingSMSMessage.message.substring(0, maxMssgLength - 1),
                                              remainingSMSMessage.hour,
                                              remainingSMSMessage.dayOfYear,
                                              remainingSMSMessage.dayOfMonth,
                                              remainingSMSMessage.monthOfYear,
                                              remainingSMSMessage.year)
                     val remSMSMssg = new SMSMessage(None,
                                              remainingSMSMessage.tweetRandID,
                                              remainingSMSMessage.internalTweetCons + 1, 
                                              remainingSMSMessage.userID,
                                              remainingSMSMessage.country,
                                              remainingSMSMessage.mobile,
                                              remainingSMSMessage.processed,
                                              remainingSMSMessage.message.substring(maxMssgLength - 1,
                                                               remainingSMSMessage.message.length - 1),
                                              remainingSMSMessage.hour,
                                              remainingSMSMessage.dayOfYear,
                                              remainingSMSMessage.dayOfMonth,
                                              remainingSMSMessage.monthOfYear,
                                              remainingSMSMessage.year)
                      loop(remSMSMssg, 
                           newSMSMssgToAppend :: acc,
                           maxNumberOfBreakoutsPerSMSMessage,
                           presentBreakoutIteration + 1)           
                 }
              case (false, true) => {remainingSMSMessage :: acc}
              case (_ , false)   => {acc}
          }
      }
      val smsMessageToInsertInDBList = loop (smsMessageToInsertInDB, List(), 1, 1)                                               
                                                
      Database.forDataSource(DB.getDataSource()) withSession
      {
        implicit session => 
            if (DebugVariables.debugM3 == true)
            {
              println("SMSMessage scala routine line 48, within addSMSMessages function smsMessage to insert in DB = " + smsMessage)
            }
            for (smsMssg <- smsMessageToInsertInDBList)
            {
                 smsMessages += smsMssg
            }
      }
    }              // def addSMSMessagePerUser.....
    
}

object SMSMessages2 
{ 
    val smsMessages2 = TableQuery[SMSMessages2]

    def existsInDB(randCode: Int): Boolean =
    {
      getSMSMessageByRandCode(randCode) match
      {
        case Some(x)  => {true}
        case None     => {false}
      }
    }

    def getSMSMessageByRandCode(randCode: Int): Option[SMSMessage2] =
    {
      Database.forDataSource(DB.getDataSource()) withSession
      {
         implicit session =>
           val smss = smsMessages2 filter (_.tweetRandID === randCode)
           smss.list.headOption
      }
    }
    
    def getAll: List[SMSMessage2] =
    {
      Database.forDataSource(DB.getDataSource()) withSession
      {
        implicit session =>
           smsMessages2.list
      }
    }

    def getAllTweetsWithSameRandTweetCode(randCode: Int): List[SMSMessage2] =
    {
        Database.forDataSource(DB.getDataSource()) withSession
        {
            implicit session =>
               val smss = smsMessages2 filter (_.tweetRandID === randCode)
               smss.list
         }          
    }
    
  def deleteSMSMessage(smsMssg: SMSMessage2) = 
  {
      Database.forDataSource(DB.getDataSource()) withSession
      {
        implicit session =>
             (smsMessages2 filter(_.id === smsMssg.id)).delete
      } 
  }
     
    
    def addSMSMessages(smsMessage: SMSMessageForStorage2) =
    {
        def produceUniqueRandomCode(): Int =
        {
            val randCode = models.RandomCode.generateRandomCode(5).toInt    
            if (DebugVariables.debugM3b == true)
            {
                println("SMSMessages scala routine, line 318, randCode = " + randCode)
            }
            val existsInDB = models.SMSMessages2.existsInDB(randCode)          
            existsInDB match
            {
              case true =>
              {
                  produceUniqueRandomCode
              }
              case false =>
              {
                 randCode
              }
            }
        }
        if (DebugVariables.debugM3b == true)
        {
           println("SMSMessages scala routine, line 335, about to call produceUniqueRandomCode function ")
        }      
        val randomIDForThisTweet = produceUniqueRandomCode
        val smsMessageToInsertInDB = SMSMessage2(None,
                                                randomIDForThisTweet,
                                                1,
                                                smsMessage.userID,
                                                smsMessage.country,
                                                smsMessage.mobile,
                                                smsMessage.processed,
                                                smsMessage.message,
                                                smsMessage.minute,
                                                smsMessage.hour,
                                                smsMessage.dayOfYear,
                                                smsMessage.dayOfMonth,
                                                smsMessage.monthOfYear,
                                                smsMessage.year)   
      def loop (remainingSMSMessage: SMSMessage2, 
                acc: List[SMSMessage2],
                maxNumberOfBreakoutsPerSMSMessage: Int,
                presentBreakoutIteration: Int): List[SMSMessage2] =
      {
          val maxMssgLength = 159
          
          ((remainingSMSMessage.message.length > maxMssgLength),
           (presentBreakoutIteration <= maxNumberOfBreakoutsPerSMSMessage)) match
           {
              case (true, true) =>
              {
                    val newSMSMssgToAppend = new SMSMessage2(None,
                                              remainingSMSMessage.tweetRandID,
                                              remainingSMSMessage.internalTweetCons,
                                              remainingSMSMessage.userID,
                                              remainingSMSMessage.country,
                                              remainingSMSMessage.mobile,
                                              remainingSMSMessage.processed,
                                              remainingSMSMessage.message.substring(0, maxMssgLength - 1),
                                              remainingSMSMessage.minute,
                                              remainingSMSMessage.hour,
                                              remainingSMSMessage.dayOfYear,
                                              remainingSMSMessage.dayOfMonth,
                                              remainingSMSMessage.monthOfYear,
                                              remainingSMSMessage.year)
                     val remSMSMssg = new SMSMessage2(None,
                                              remainingSMSMessage.tweetRandID,
                                              remainingSMSMessage.internalTweetCons + 1, 
                                              remainingSMSMessage.userID,
                                              remainingSMSMessage.country,
                                              remainingSMSMessage.mobile,
                                              remainingSMSMessage.processed,
                                              remainingSMSMessage.message.substring(maxMssgLength - 1,
                                                               remainingSMSMessage.message.length - 1),
                                              remainingSMSMessage.minute,
                                              remainingSMSMessage.hour,
                                              remainingSMSMessage.dayOfYear,
                                              remainingSMSMessage.dayOfMonth,
                                              remainingSMSMessage.monthOfYear,
                                              remainingSMSMessage.year)
                      loop(remSMSMssg, 
                           newSMSMssgToAppend :: acc,
                           maxNumberOfBreakoutsPerSMSMessage,
                           presentBreakoutIteration + 1)           
                 }
              case (false, true) => {remainingSMSMessage :: acc}
              case (_ , false)   => {acc}
          }
      }
      val smsMessageToInsertInDBList = loop (smsMessageToInsertInDB, List(), 1, 1)                                               
                                                
      Database.forDataSource(DB.getDataSource()) withSession
      {
        implicit session => 
            if (DebugVariables.debugM3 == true)
            {
              println("SMSMessage scala routine line 409, within addSMSMessages function smsMessage to insert in DB = " + smsMessage)
            }
            for (smsMssg <- smsMessageToInsertInDBList)
            {
                 smsMessages2 += smsMssg
            }
      }
    }              // def addSMSMessagePerUser.....
    
}