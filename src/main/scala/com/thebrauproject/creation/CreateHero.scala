package com.thebrauproject.creation

import java.sql.{Connection, DriverManager, PreparedStatement}

import akka.actor.{Actor, ActorLogging, Stash}
import com.thebrauproject.elements.Hero
import com.thebrauproject.operations._
import com.thebrauproject.util.utc

class CreateHero extends Actor with Stash with ActorLogging{

  val connString = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=mysecretpassword"
  val statementString = "INSERT INTO hero (hero_id, created_at_utc) values (?, ?)"
  var conn: Connection = _
  var stm: PreparedStatement = _

  override def receive: Receive = disconnected

  def disconnected: Actor.Receive = {
    case Connect =>
      log.info("Request to connect to Database")
      conn = DriverManager.getConnection(connString)
      log.info("Database connected")
      unstashAll()
      context.become(connected)
    case _ => stash()
  }

  def connected: Actor.Receive = {
    case Disconnect =>
      log.info("Disconnecting from the Database")
      conn.close()
      context.unbecome()
    case h: Hero =>
      stm = conn.prepareStatement(statementString)
      stm.setString(1, h.hero_id)
      stm.setTimestamp(2, utc)
      stm.executeUpdate
  }
}
