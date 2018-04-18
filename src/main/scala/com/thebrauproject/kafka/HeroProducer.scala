package com.thebrauproject.kafka

import java.util.Properties

import akka.actor.{Actor, ActorLogging, Props}
import com.thebrauproject.elements.{CreatureKafkaPackage, Hero}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import spray.json._

object HeroProducer {


  val properties = new Properties()
  properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  properties.put(ProducerConfig.CLIENT_ID_CONFIG, "ScalaProducerExample")
  properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  properties.put(ProducerConfig.ACKS_CONFIG, "all")

  def props = Props(new HeroProducer(properties))
}

class HeroProducer(props: Properties) extends Actor with ActorLogging {

  import com.thebrauproject.elements.implicits._

  override def receive: Receive = {
    case c: CreatureKafkaPackage[Hero] =>
      val producer = new KafkaProducer[String, String](props)
      log.info(s"Hero: ${c.creature.name} with id: ${c.creatureId} is going to be send to Kafka")
      val ack = producer
        .send(new ProducerRecord[String, String]("hero", c.creatureId, c.creature.toJson.compactPrint)).get()
      sender ! AckObject(ack.topic(), ack.offset(), ack.partition())
  }

}
