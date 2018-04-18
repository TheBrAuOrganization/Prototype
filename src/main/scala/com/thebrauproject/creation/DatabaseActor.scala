package com.thebrauproject.creation

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Stash}
import akka.pattern.ask
import java.sql._
import java.time.Instant

import com.thebrauproject.elements.{CreatureKafkaPackage, Hero}
import com.thebrauproject.operations.OperationsDb.{Connect, Disconnect}
import com.thebrauproject.operations.OperationsDb._
import com.thebrauproject.kafka.HeroProducer
import com.thebrauproject.kafka._
import com.thebrauproject.util._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class DatabaseActor extends Actor with Stash with ActorLogging {

  val connString = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=mysecretpassword"
  val statementString = "INSERT INTO hero (hero_id, created_at_utc) values (?, ?)"
  var conn: Connection = _
  var stm: PreparedStatement = _
  var producer: ActorRef = _


  override def receive: Receive = disconnected

  def disconnected: Actor.Receive = {
    case Connect =>
      log.info("Request to connect to Database")
      conn = DriverManager.getConnection(connString)
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

    case CreateCreature(creature) =>
      creature match {
        case c: Hero =>
          try {
            //TODO: The PG API need to be changed
            val ack: Future[AckObject] = (producer ? CreatureKafkaPackage[Hero](c.hero_id, c)).mapTo[AckObject]
            ack onComplete {
              case Success(ackObj) =>
                stm = conn.prepareStatement(statementString)
                log.info(s"Data sent to Kafka with Topic: ${ackObj.topic} " +
                  s"Partiton: ${ackObj.partition} and Offset: ${ackObj.partition}")
                stm.setString(1, c.hero_id)
                stm.setTimestamp(2, utc)
                stm.executeUpdate
              case Failure(_) => log.warning("Unable to commit to Kafka.")
            }
          } catch {
            case e: ClassCastException =>
              log.error("The operations is expected with Hero object. Please fix the object to be sent")
              e.printStackTrace()
              log.info("Disconnecting from the Database")
              conn.close()
              context.unbecome()
          }
      }
      case UpdateCreature(creature) => ???
      case DeleteCreature(creature) => ???
      case ReadCreature(creature) => ???
    }

}
