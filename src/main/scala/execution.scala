import akka.actor.{ActorSystem, Props}
import com.thebrauproject.creation.{DatabaseActor, DatabaseRouter}
import com.thebrauproject.elements.{Hero, Role, Skill}
import com.thebrauproject.operations.OperationsDb.DBOperations.Create
import com.thebrauproject.operations.OperationsDb.{Connect, CreateCreature, Disconnect, OperationCreature}

import scala.util.Random

object execution extends App {

  val system = ActorSystem("TheProduct")

  val creationDb = system.actorOf(Props[DatabaseRouter], "dbCreator")

  val pythonSkill = Skill("1234", "python")
  val javascriptSkill = Skill("321", "javascript")
  val skills = Seq(pythonSkill, javascriptSkill)
  val role = Role("4545", "full stacker developer", "Faz a porra toda", skills)
  val hero = Hero(Random.nextInt(99999).toString, "Eta Danado", skills, role)

  creationDb ! CreateCreature[Hero](hero)

}
