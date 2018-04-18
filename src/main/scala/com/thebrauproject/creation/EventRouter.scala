package com.thebrauproject.creation

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import com.thebrauproject.operations._

class EventRouter extends Actor with ActorLogging {
  val databaseOperation = context.actorOf(Props[EventActor], "DatabaseHero")
  override def receive: Receive = {
    case op: DBCreatureOperation =>
      log.info(s"Operation with Creature")
      databaseOperation ! Connect
      databaseOperation ! op
    case op: DBAttributeOperation => ???
    case op: DBObjectiveOperation => ???
  }
}
