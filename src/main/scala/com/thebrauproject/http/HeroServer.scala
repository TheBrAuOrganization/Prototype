package com.thebrauproject.http


import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.io.StdIn
import scala.concurrent.Future
import scala.concurrent.duration._

import spray.json.DefaultJsonProtocol._
import spray.json._

import com.thebrauproject.modification.RedisDbActor
import com.thebrauproject.creation.DatabaseRouter
import com.thebrauproject.elements._
import com.thebrauproject.operations.OperationsDb._
import com.thebrauproject.kafka.HeroConsumer
import com.thebrauproject.operations.OperationsDb.Connect
import com.thebrauproject.operations.OperationsKafka.StartConsumer


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
  redisActor ! Connect

  val route =
    pathPrefix("hero"){
      (post & entity(as[Hero])) { hero =>
        creationDb !  CreateCreature[Hero](hero)
        complete {
          Created -> Map("id" -> hero.hero_id).toJson
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
