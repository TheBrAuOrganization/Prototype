package com.thebrauproject.creation

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Stash}
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
  var producer: ActorRef = _

  override def receive: Receive = disconnected

  def disconnected: Actor.Receive = {
    case Connect =>
      log.info("Request to connect to Database")
      conn = DriverManager.getConnection(connString)
      stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      log.info("Database connected")
      producer = context.actorOf(HeroProducer.props)
      log.info("Kafka Producer Actor Created.")
      unstashAll()
      context.become(connected)
    case _ => stash()
  }

  def connected: Actor.Receive = {
    case Disconnect =>
      log.info("Disconnecting from the Database")
      conn.close()
      log.info("Killing producer actor")
      producer ! PoisonPill
      context.unbecome()

    case c: CreateCreature[Hero] =>
      try {
        stm.executeUpdate(s"INSERT INTO hero VALUES('${c.creature.hero_id}', ${Instant.now.getEpochSecond})")
        producer ! CreatureKafkaPackage[Hero](c.creature.hero_id, c.creature)
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
