import akka.actor.{ActorSystem, Props}
import com.thebrauproject.creation.DatabaseRouter
import com.thebrauproject.elements.{Hero, Role, Skill}
import com.thebrauproject.operations.OperationsDb.CreateCreature
import com.thebrauproject.kafka.HeroConsumer
import com.thebrauproject.operations.OperationsKafka.StartConsumer

import scala.util.Random

object execution extends App {

  val system = ActorSystem("TheProduct")

  val creationDb = system.actorOf(Props[DatabaseRouter], "dbCreator")
  val kafkaConsumer = system.actorOf(Props[HeroConsumer], "kafkaHeroConsumer")

  val pythonSkill = Skill("1234", "python")
  val javascriptSkill = Skill("321", "javascript")
  val skills = Seq(pythonSkill, javascriptSkill)
  val role = Role("4545", "full stacker developer", "Faz a porra toda", skills)
  val hero = Hero(Random.nextInt(99999).toString, "Eta Danado", skills, role)

  kafkaConsumer ! StartConsumer

  Thread.sleep(1000)

  creationDb ! CreateCreature[Hero](hero)

}
