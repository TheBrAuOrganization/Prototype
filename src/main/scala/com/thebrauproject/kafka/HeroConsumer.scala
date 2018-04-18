package com.thebrauproject.kafka

import java.util.Properties

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.thebrauproject.creation.CreateHero
import com.thebrauproject.elements.Hero
import com.thebrauproject.modification.RedisDbActor
import com.thebrauproject.operations._
import com.thebrauproject.elements.implicits._
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream.KStream
import spray.json.DefaultJsonProtocol._
import spray.json._


class HeroConsumer extends Actor {
  val system = ActorSystem("HeroConsumer")

  val redisDB: ActorRef = system.actorOf(Props[RedisDbActor], "dbRedisCreator")
  val postgresDB: ActorRef = system.actorOf(Props[CreateHero], "dbPostgresCreator")

  val builder = new StreamsBuilder()

  val heroes: KStream[String, String] = builder.stream("hero")

  redisDB ! Connect
  postgresDB ! Connect

  heroes.foreach((k, v) => {
    redisDB ! RedisObject(k, v)
    postgresDB ! v.parseJson.convertTo[Hero]
  })

  override def receive: Receive = {
    case StartConsumer =>
      val props = new Properties()
      props.put("bootstrap.servers", "localhost:9092")
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "hero-consumer")
      props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass.getName)
      props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass.getName)

      val stream = new KafkaStreams(builder.build(), props)
      stream.start()
  }
}
