package com.thebrauproject.modification

import akka.actor.{Actor, ActorLogging, Stash}
import com.redis.RedisClient
import com.thebrauproject.elements.Hero
import com.thebrauproject.operations.OperationsDb._

import com.thebrauproject.util._


class RedisDbActor extends Actor with Stash with ActorLogging{

  val connString = "localhost"
  val connPort = 6379

  var redisConn: RedisClient = _

  override def receive: Receive = disconnected

  def connected: Actor.Receive = {
    case Disconnect =>
      log.info("Disconnectiong from the database")
      redisConn.disconnect
      context.unbecome()
    case r: RedisObject =>
      redisConn.set(r.key, r.value)
      log.info("Data was written to Redis db")
    case s: String =>
      sender ! parseJson[Hero](redisConn.get(s))
  }

  def disconnected: Actor.Receive = {
    case Connect =>
      log.info("Connecting to Redis Database")
      redisConn = new RedisClient(connString, connPort)
      unstashAll()
      context.become(connected)
    case _ => stash()
  }

}
