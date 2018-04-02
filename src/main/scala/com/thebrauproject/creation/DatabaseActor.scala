package com.thebrauproject.creation

import akka.actor.{Actor, ActorLogging, Stash}
import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.time.Instant

import com.thebrauproject.elements.{CreatureKafkaPackage, Hero}
import com.thebrauproject.operations.OperationsDb.{Connect, Disconnect}
import com.thebrauproject.operations.OperationsDb._
import com.thebrauproject.kafka.HeroProducer

class DatabaseActor extends Actor with Stash with ActorLogging {

  val connString = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=mysecretpassword"
  var conn: Connection = _
  var stm: Statement = _

  override def receive: Receive = disconnected

  def disconnected: Actor.Receive = {
    case Connect =>
      log.info("Request to connect to Database")
      conn = DriverManager.getConnection(connString)
      stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      log.info("Database connected")
      unstashAll()
      context.become(connected)
    case _ => stash()
  }

  def connected: Actor.Receive = {
    case Disconnect =>
      log.info("Disconnecting from the Database")
      conn.close()
      context.unbecome()

    case c: CreateCreature[Hero] =>
      try {
        stm.executeUpdate(s"INSERT INTO hero VALUES('${c.creature.id}', ${Instant.now.getEpochSecond})")
        val producer = context.actorOf(HeroProducer.props)
        producer ! CreatureKafkaPackage[Hero](c.creature.id, c.creature)
      } catch {
        case e: ClassCastException =>
          log.error("The operations is expected with Hero object. Please fix the object to be sent")
          e.printStackTrace()
          log.info("Disconnecting from the Database")
          conn.close()
          context.unbecome()
      }
      case c: UpdateCreature[Hero] => ???
      case c: DeleteCreature[Hero] => ???
      case c: ReadCreature[Hero] => ???
    }

}
