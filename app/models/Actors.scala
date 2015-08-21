package models

import akka.actor.{Actor, Scheduler, Props, ActorSystem, ActorContext}
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import java.io.{File, RandomAccessFile, FileInputStream}
import java.util.Scanner

import controllers._

case class UnsecurePollSMSs(urll: String, country: Int, proceed: Boolean)
case class SecurePollSMSs(urll: String, name: String, pwd: String, proceed: Boolean)
case class SendSMSs(proceed: Boolean)
case class SendSMSs2(proceed: Boolean)
case class PrintSMSs2(proceed: Boolean)
case class UpdatePrgmParms(proceed: Boolean)

object Actors 
{

    val mainPathForFiles = "/home/chuy/Applications/activator-1.2.12/smspollingDBv1/parameters/"
    var periodInMinutesForSMSPolling = 5
//    val periodInMinutesForSMSSending = 1
    var periodInSecondsForSMSSending = 20
    var periodInMinutesForUpdatingParameters = 30
    var country = 1
    var urll = "http://162.242.234.176:9000/processRequestFromRemoteApp/"
    var unsecurePolling2 = false
    var securePolling2   = false
    var securePollingButForTestPrintInsteadOfSendingSMSMessage2 = true
    val param1 = ""
    
    val name = "mexico1"
    val pwd = models.HashGenerator.generateEncryptedPwd("secret")
    
//    import context._
    val system               = ActorSystem("PollSMSAndSendAfterwards")
    val startSMSPolling      = system.actorOf(Props[Actors.StartPolling])
    val pollPendingSMSToSend = system.actorOf(Props[Actors.PollSMSMessagesPendingForSend])
    val sendSMSs             = system.actorOf(Props[Actors.SendSMSMessages])
    val printSMSs            = system.actorOf(Props[Actors.PrintSMSMessages])
    val updateProgramParams  = system.actorOf(Props[Actors.UpdateProgramParameters])
  
  class StartPolling extends Actor
  {
//     import context._
     def receive = 
     {
        case "startUnsecurePolling" =>
          {
              system.scheduler.schedule(Duration(15, SECONDS), 
                                        Duration(periodInMinutesForSMSPolling, MINUTES), 
                                        pollPendingSMSToSend, 
                                        UnsecurePollSMSs(urll, country, unsecurePolling2))
              system.scheduler.schedule(Duration(35, SECONDS), 
                                        Duration(periodInSecondsForSMSSending, SECONDS), 
                                        sendSMSs, 
                                        SendSMSs(unsecurePolling2))                                            
          }
        case "startSecurePollingAndMinuteIncludedInObjectCollected" =>
          {
              system.scheduler.schedule(Duration(15, SECONDS), 
                                        Duration(periodInMinutesForSMSPolling, MINUTES), 
                                        pollPendingSMSToSend, 
                                        SecurePollSMSs(urll, name, pwd, securePolling2))
              system.scheduler.schedule(Duration(35, SECONDS), 
                                        Duration(periodInSecondsForSMSSending, SECONDS), 
                                        sendSMSs, 
                                        SendSMSs2(securePolling2))                                            
          }     
        case "startSecurePollingAndMinuteIncludedInObjectCollectedButPrintOnScreenInsteadOfSendingSMS" =>
          {
              system.scheduler.schedule(Duration(15, SECONDS), 
                                        Duration(periodInMinutesForSMSPolling, MINUTES), 
                                        pollPendingSMSToSend, 
                                        SecurePollSMSs(urll, name, pwd,
                                                       securePollingButForTestPrintInsteadOfSendingSMSMessage2))
              system.scheduler.schedule(Duration(35, SECONDS), 
                                        Duration(periodInSecondsForSMSSending, SECONDS), 
                                        printSMSs, 
                                        PrintSMSs2(securePollingButForTestPrintInsteadOfSendingSMSMessage2))                                            
          }
        case "updatePollingParametersFromInputParameterFile" =>
          {
              system.scheduler.schedule(Duration(5, SECONDS), 
                                        Duration(periodInMinutesForUpdatingParameters, MINUTES), 
                                        updateProgramParams, 
                                        UpdatePrgmParms(true))                                           
          }
     }
  }
     
    class PollSMSMessagesPendingForSend extends Actor
    {
       import context._
       def receive = 
       {
           case UnsecurePollSMSs(urll: String, country: Int, proceed: Boolean) => 
           {
              if(models.DebugVariables.debugM1 == true)
              {
                  println("Actors scala routine, within class PollSMSMessagesPendingForSend on def receive, line 56")
              }
              executeIfAppl(proceed)
              {
                 controllers.SMSHandler.searchForTweetsInMainAppAndStoreThemLocally(urll, country)
              }
           }
           case SecurePollSMSs(urll: String, name: String, pwd: String, proceed: Boolean) => 
           {
              if(models.DebugVariables.debugM1 == true)
              {
                  println("Actors scala routine, within class PollSMSMessagesPendingForSend on def receive, line 67")
              }
              executeIfAppl(proceed)
              {
                  controllers.SMSHandler.searchForTweetsInMainAppAndStoreThemLocally2(urll, name, pwd)
              }
           }
        }
    }
    
    class SendSMSMessages extends Actor
    {
       import context._
       def receive = 
       {
           case SendSMSs(proceed: Boolean) => 
           {
               if(models.DebugVariables.debugM1 == true)
               {
                   println("Actors scala routine, within class SendSMSMessages on def receive, line 56")
               }
               executeIfAppl(proceed)
              {
                  controllers.SMSHandler.sendOutstandingSMSsFromDBValidIfMessagesNotConcatenated
              }
           }
           case SendSMSs2(proceed: Boolean) => 
           {
               if(models.DebugVariables.debugM1 == true)
               {
                   println("Actors scala routine, within class SendSMSMessages on def receive, line 56")
               }
               executeIfAppl(proceed)
               {
                  controllers.SMSHandler.sendOutstandingSMSsFromDBValidIfMessagesNotConcatenated2
               }
           }
        }
    }
    
    class PrintSMSMessages extends Actor
    {
       import context._
       def receive = 
       {
           case SendSMSs(proceed: Boolean) => 
           {
               if(models.DebugVariables.debugM1 == true)
               {
                   println("Actors scala routine, within class SendSMSMessages on def receive, line 56")
               }
               executeIfAppl(proceed)
               {
                   controllers.SMSHandler.sendOutstandingSMSsFromDBValidIfMessagesNotConcatenated
               }
           }
           case PrintSMSs2(proceed: Boolean) => 
           {
               if(models.DebugVariables.debugM1 == true)
               {
                   println("Actors scala routine, within class SendSMSMessages on def receive, line 56")
               }
               executeIfAppl(proceed)
              {
                 controllers.SMSHandler.printOutstandingSMSsFromDBValidIfMessagesNotConcatenated2
              }
           }
         }
        }
    
    
    class UpdateProgramParameters extends Actor
    {
       import context._
       def receive = 
       {
           case UpdatePrgmParms(proceed: Boolean) => 
           {
               if(models.DebugVariables.debugM1 == true)
               {
                   println("Actors scala routine, within class UpdatePrgmParms on def receive, line 164")
               }
               val FILE_NAME_TO_EXTRACT_SEARCH_TEXT = mainPathForFiles + "inputParameterFile.txt"               
               val inputParameterScanner = new Scanner(new FileInputStream(new File(FILE_NAME_TO_EXTRACT_SEARCH_TEXT)))
               val inputParameterListOfStrings = FileExplorer.ConvertScannerToListOfStrings(inputParameterScanner, 
                                                                                         "END OF FILE")
               periodInMinutesForSMSPolling = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "PERIOD_IN_MINUTES_FOR_SMS_POLLING"))
               periodInSecondsForSMSSending = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "PERIOD_IN_SECONDS_FOR_SMS_SENDING"))
               periodInMinutesForUpdatingParameters = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "PERIOD_IN_MINUTES_FOR_UPDATING_PARAMETERS"))
               country = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "COUNTRY"))
               urll = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "POLLING_URL")                                                  
               unsecurePolling2 = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "UNSECURE_POLLING") match
                                  {
                                      case "yes" =>  true
                                      case _     =>  false
                                  }
               securePolling2 = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "SECURE_POLLING") match
                                  {
                                      case "yes" =>  true
                                      case _     =>  false
                                  }
               securePollingButForTestPrintInsteadOfSendingSMSMessage2 = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "SECURE_POLLING_BUT_FOR_TEST_PRINT_INSTEAD_OF_SENDING_SMS_MESSAGE") match
                                  {
                                      case "yes" =>  true
                                      case _     =>  false
                                  }                                                     
           }
        }
    }
    
    def updateProgramParameters3 = 
    {
               if(models.DebugVariables.debugM1 == true)
               {
                   println("Actors scala routine, within def updateProgramParameters3, line 225")
               }
               val FILE_NAME_TO_EXTRACT_SEARCH_TEXT = mainPathForFiles + "inputParameterFile.txt"               
               val inputParameterScanner = new Scanner(new FileInputStream(new File(FILE_NAME_TO_EXTRACT_SEARCH_TEXT)))
               val inputParameterListOfStrings = FileExplorer.ConvertScannerToListOfStrings(inputParameterScanner, 
                                                                                         "END OF FILE")
               periodInMinutesForSMSPolling = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "PERIOD_IN_MINUTES_FOR_SMS_POLLING"))
               periodInSecondsForSMSSending = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "PERIOD_IN_SECONDS_FOR_SMS_SENDING"))
               periodInMinutesForUpdatingParameters = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "PERIOD_IN_MINUTES_FOR_UPDATING_PARAMETERS"))
               country = Integer.parseInt(FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "COUNTRY"))
               urll = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "POLLING_URL")                                                  
               unsecurePolling2 = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "UNSECURE_POLLING") match
                                  {
                                      case "yes" =>  true
                                      case _     =>  false
                                  }
               securePolling2 = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "SECURE_POLLING") match
                                  {
                                      case "yes" =>  true
                                      case _     =>  false
                                  }
               securePollingButForTestPrintInsteadOfSendingSMSMessage2 = FileParamExtracter.EstablishField(
                                                  inputParameterListOfStrings, 
                                                  "SECURE_POLLING_BUT_FOR_TEST_PRINT_INSTEAD_OF_SENDING_SMS_MESSAGE") match
                                  {
                                      case "yes" =>  true
                                      case _     =>  false
                                  }                                                          
    }
    
    private def executeIfAppl(proceed: Boolean)(f: => Unit) = 
    {
      if (proceed == true){f}
    }

    
}
    
    