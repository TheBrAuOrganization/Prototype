package com.thebrauproject.creation

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props, Stash}
import akka.pattern.ask
import java.sql._
import java.time.Instant

import akka.util.Timeout
import com.thebrauproject.elements.{CreatureKafkaPackage, Hero}
import com.thebrauproject.kafka.HeroProducer
import com.thebrauproject.kafka._
import com.thebrauproject.operations._
import com.thebrauproject.util._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent._
import scala.util.{Failure, Success}

import ExecutionContext.Implicits.global

class EventActor extends Actor with Stash with ActorLogging {

  implicit val timeout: Timeout = 5.seconds
  val producer: ActorRef = context.actorOf(HeroProducer.props, "hero-producer")

  override def receive: Receive = {
    case CreateCreature(creature) =>
      creature match {
        case hero: Hero =>
          try {
            val ack: Future[ProducerStatus] = (producer ? CreatureKafkaPackage[Hero](hero.hero_id, hero)).mapTo[ProducerStatus]
            ack onComplete {
              case Success(producerStatus) =>
                sender ! producerStatus
              case Failure(_) =>
                log.warning("Unable to commit to Kafka.")
                sender ! OperationFailure
            }
          } catch {
            case e: ClassCastException =>
              log.error("The operations is expected with Hero object. Please fix the object to be sent")
              e.printStackTrace()
              log.info("Disconnecting from the Database")

          }
      }
      case UpdateCreature(creature) => ???
      case DeleteCreature(creature) => ???
      case ReadCreature(creature) => ???
    }

}
