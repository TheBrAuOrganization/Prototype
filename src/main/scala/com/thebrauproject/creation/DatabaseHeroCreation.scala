package com.thebrauproject.creation

import akka.actor.{Actor, ActorLogging, Stash}
import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.time.Instant

import com.thebrauproject.elements.{CreatureKafkaPackage, Hero}
import com.thebrauproject.operations.OperationsDb
import com.thebrauproject.kafka.HeroProducer

class DatabaseHeroCreation extends Actor with Stash with ActorLogging {

  val connString = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=mysecretpassword"
  var conn: Connection = _
  var stm: Statement = _

  override def receive: Receive = disconnected

  def disconnected: Actor.Receive = {
    case OperationsDb.Connect =>
      log.info("Request to connect to Database")
      conn = DriverManager.getConnection(connString)
      stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      log.info("Database connected")
      unstashAll()
      context.become(connected)
    case _ => stash()
  }

  def connected: Actor.Receive = {
    case OperationsDb.Disconnect =>
      log.info("Disconnecting from the Database")
      conn.close()
      context.unbecome()
    case op: OperationsDb.OperationCreature[Hero] =>
      log.info(s"Execution operation: ${op.dBOperations.getClass.getName}")
      op.dBOperations match {
        case OperationsDb.DBOperations.Create =>
          try {
            stm.executeUpdate(s"INSERT INTO hero VALUES('${op.creature.get.id}', ${Instant.now.getEpochSecond})")
            val producer = context.actorOf(HeroProducer.props)
            producer ! CreatureKafkaPackage[Hero](op.creature.get.id, op.creature.get)
          } catch {
            case e: ClassCastException =>
              log.error("The operations is expected with Hero object. Please fix the object to be sent")
              e.printStackTrace()
              log.info("Disconnecting from the Database")
              conn.close()
              context.unbecome()
          }
        case OperationsDb.DBOperations.Update => ???
        case OperationsDb.DBOperations.Delete => ???
        case OperationsDb.DBOperations.Read => ???
      }
  }
}
