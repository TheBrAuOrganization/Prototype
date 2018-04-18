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
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.concurrent.duration._
import spray.json.DefaultJsonProtocol._
import spray.json._
import com.thebrauproject.modification.RedisDbActor
import com.thebrauproject.creation.EventRouter
import com.thebrauproject.elements._
import com.thebrauproject.kafka.HeroConsumer
import com.thebrauproject.operations._


object HeroServer extends App {
  import com.thebrauproject.elements.implicits._

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val redisActor = system.actorOf(Props[RedisDbActor], "redis")
  val creationDb = system.actorOf(Props[EventRouter], "dbCreator")
  val kafkaConsumer = system.actorOf(Props[HeroConsumer], "kafkaHeroConsumer")

  kafkaConsumer ! StartConsumer
  redisActor ! Connect

  val route =
    pathPrefix("hero"){
      (post & entity(as[Hero])) { hero =>
        creationDb !  CreateCreature[Hero](hero)
        complete {
          Created -> Map(
            "hero_id" -> hero.hero_id,
            "name" -> hero.name,
            "created_at" -> hero.created_at.toInstant.toString,
            "message" -> "A new hero has born").toJson
        }
      } ~
      (get & path(Segment)) { id =>
        implicit val timeout: Timeout = 5.seconds
        val result: Future[Hero] = (redisActor ? id).mapTo[Hero]
        complete(result)
      } ~
      (put & entity(as[Hero])) { hero =>
        creationDb ! UpdateCreature[Hero](hero)
        complete {
          Created -> Map(
            "hero_id" -> hero.hero_id,
            "message" -> "The hero got his stuffs buffed up").toJson
        }
      } ~
      (delete & path(Segment)) { id =>
        implicit val timeout: Timeout = 5.seconds
        val result: Future[Hero] = (redisActor ? id).mapTo[Hero]
        val hero = Await.result(result, 5.seconds)
        creationDb ! DeleteCreature[Hero](hero)
        complete({
          Created -> Map(
            "hero_id" -> hero.hero_id,
            "message" -> "The hero found his way to valhalla").toJson
        })
      }
    }


  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
