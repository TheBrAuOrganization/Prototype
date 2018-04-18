package com.thebrauproject.hero

import akka.actor.{Actor, Props}
import com.thebrauproject.elements.Hero
import com.thebrauproject.util._

class ActorHero extends Actor {
  import ActorHero._
  override def receive: Receive = {
    case GenerateId(value) => sender ! Base64SHA256(value)
    case Modification(hero) => sender !  hero.copy(updated_at = utc)
  }
}

object ActorHero {

  sealed trait Operation
  case class GenerateId(value: String) extends Operation
  case class Modification(hero: Hero) extends Operation

  def props: Props = Props[ActorHero]
}
