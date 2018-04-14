package com.thebrauproject.hero

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.thebrauproject.elements.{Hero, Skill}
import com.thebrauproject.testUtils.hero
import com.thebrauproject.util.Base64SHA256
import com.thebrauproject.hero.ActorHero.{GenerateId, Modification}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

class ActorHeroSpec extends TestKit(ActorSystem("test-actor-hero"))
                    with  ImplicitSender
                    with  FlatSpecLike
                    with  BeforeAndAfterAll
                    with  MustMatchers {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "HeroActor" should "Generate Hash code SHA256 Base64 for the New Id of an Actor" in {
    val sender = TestProbe()

    val actorHero = system.actorOf(ActorHero.props)

    sender.send(actorHero, GenerateId(hero.name + hero.created_at))

    val state = sender.expectMsgType[String]

    state must equal(Base64SHA256(hero.name + hero.created_at))
  }

  it should "update the Modified At field if the Modification Request is called" in {
    val sender = TestProbe()

    val actorHero = system.actorOf(ActorHero.props)
    val previous = hero.updated_at
    sender.send(actorHero, Modification(hero))

    val state = sender.expectMsgType[Hero]

    state.updated_at must not equal hero.updated_at

  }

}
