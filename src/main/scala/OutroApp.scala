import akka.actor.{ActorSystem, Props}
import com.thebrauproject.creation.DatabaseRouter
import com.thebrauproject.kafka.HeroConsumer
import com.thebrauproject.operations.OperationsKafka.StartConsumer

object OutroApp extends App {

  val system = ActorSystem("TheProduct")

  val kafkaConsumer = system.actorOf(Props[HeroConsumer], "kafkaHeroConsumer")

  kafkaConsumer ! StartConsumer

}
