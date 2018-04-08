package com.thebrauproject.creation

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

class DatabaseActorSpec extends TestKit(ActorSystem("test-database"))
                    with  ImplicitSender
                    with  FlatSpecLike
                    with  BeforeAndAfterAll
                    with  MustMatchers {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

}
