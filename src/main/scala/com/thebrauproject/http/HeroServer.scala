package com.thebrauproject.http

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.util.Timeout
import com.thebrauproject.creation.DatabaseRouter
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn
import com.thebrauproject.modification.RedisDbActor
import com.thebrauproject.elements.{Hero, Skill}
import com.thebrauproject.kafka.HeroConsumer
import com.thebrauproject.operations.OperationsKafka.StartConsumer
import spray.json._

object HeroServer extends App {
  import com.thebrauproject.elements.implicits._

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val redisActor = system.actorOf(Props[RedisDbActor], "redis")
  val creationDb = system.actorOf(Props[DatabaseRouter], "dbCreator")
  val kafkaConsumer = system.actorOf(Props[HeroConsumer], "kafkaHeroConsumer")

  kafkaConsumer ! StartConsumer

  val route =
    pathPrefix("hero"){
      (post & entity(as[Hero])) { hero =>
        creationDb ! hero
        complete {
          Created -> Map("id" -> hero.id).toJson
        }
      } ~
        (get & path(Segment)) { id =>
          implicit val timeout: Timeout = 5.seconds
          val result: Future[Hero] = (redisActor ? id).mapTo[Hero]
          complete(result)
        }
    }


  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
