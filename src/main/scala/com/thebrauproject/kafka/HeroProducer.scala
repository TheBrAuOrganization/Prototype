package com.thebrauproject.kafka

import java.util.Properties

import akka.actor.{Actor, ActorLogging, Props}
import com.thebrauproject.elements.{CreatureKafkaPackage, Hero}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import net.liftweb.json._
import net.liftweb.json.Serialization.write

object HeroProducer {

  val porperties = new Properties()
  porperties.put("bootstrap.servers", "localhost:9092")
  porperties.put("client.id", "ScalaProducerExample")
  porperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  porperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  def props = Props(new HeroProducer(porperties))
}


class HeroProducer(props: Properties) extends Actor with ActorLogging {

  override def receive: Receive = {
    case c: CreatureKafkaPackage[Hero] =>
      implicit val formats = DefaultFormats
      val producer = new KafkaProducer[String, String](props)
      log.info(s"Hero: ${c.creature.name} with id: ${c.creatureId} is going to be send to Kafka")
      producer.send(new ProducerRecord[String, String]("hero", c.creatureId, write(c.creature)))
  }

}
