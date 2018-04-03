package com.thebrauproject.creation

import akka.actor.{Actor, ActorLogging, Props}
import com.thebrauproject.operations.OperationsDb._
import com.thebrauproject.operations.OperationsDb.Connect

class DatabaseRouter extends Actor with ActorLogging {
  override def receive: Receive = {
    case op: DBCreatureOperation =>
      log.info(s"Operation with Creature")
      val databaseOperation = context.actorOf(Props[DatabaseActor], "DatabaseHero")
      databaseOperation ! Connect
      databaseOperation ! op
    case op: DBAttributeOperation => ???
    case op: DBObjectiveOperation => ???
  }
}
