import play.api.Application
import play.api.GlobalSettings
import play.api.Play.current
import play.api.db.DB
import scala.slick.driver.PostgresDriver.simple._
import org.postgresql.ds.PGSimpleDataSource
import scala.concurrent.Future
import play.api.Play.current

import controllers._
import models._

object Global extends GlobalSettings
{
  override def onStart(app: Application) 
  {
//      val unsecurePolling = false
//      val securePolling   = false
//      val securePollingButForTestPrintInsteadOfSendingSMSMessage = true
      
      Database.forDataSource(DB.getDataSource()) withSession
      {
         implicit session =>
/*
 * OJO= the following code should be expanded to include all Tables once I have them properly
 *      defined in Slick.   Once the Aplication goes into production, it could be removed.
 */
             import scala.slick.jdbc.meta.MTable
               if (MTable.getTables(models.SMSMessages.smsMessages.baseTableRow.tableName).list.isEmpty) 
               {
                   models.SMSMessages.smsMessages.ddl.create
                }
               if (MTable.getTables(models.SMSMessages2.smsMessages2.baseTableRow.tableName).list.isEmpty) 
               {
                   models.SMSMessages2.smsMessages2.ddl.create
                }
      }
      
      models.Actors.updateProgramParameters3
      
      
//      if (unsecurePolling == true)
//      {
          models.Actors.startSMSPolling ! "startUnsecurePolling"
//      }
//      if (securePolling == true)
//      {
        models.Actors.startSMSPolling ! "startSecurePollingAndMinuteIncludedInObjectCollected"
//      }
//      if (securePollingButForTestPrintInsteadOfSendingSMSMessage == true)
//      {
        models.Actors.startSMSPolling ! "startSecurePollingAndMinuteIncludedInObjectCollectedButPrintOnScreenInsteadOfSendingSMS"
//      }
  }
}