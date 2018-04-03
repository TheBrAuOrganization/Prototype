package com.thebrauproject.kafka

import java.util.Properties

import akka.actor.{Actor, ActorSystem, Props}
import com.thebrauproject.modification.RedisDbActor
import com.thebrauproject.operations.OperationsDb.{Connect, RedisObject}
import com.thebrauproject.operations.OperationsKafka.StartConsumer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream.KStream


class HeroConsumer extends Actor {
  val system = ActorSystem("HeroConsumer")

  val redisDB = system.actorOf(Props[RedisDbActor], "dbCreator")

  val builder = new StreamsBuilder()

  val heroes: KStream[String, String] = builder.stream("hero")

  redisDB ! Connect

  heroes.foreach((k, v) => {
    redisDB ! RedisObject(k, v)
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
