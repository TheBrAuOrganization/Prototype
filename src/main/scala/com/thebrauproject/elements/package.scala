package com.thebrauproject

import java.sql.Timestamp

package object elements {

  trait Creature

  trait Attribute

  trait Action

  case class Hero(hero_id: String,
                  name: String,
                  first_name: String,
                  nickname: String,
                  profile_photo: String,
                  profile_email: String,
                  profile_phone: String,
                  created_at: Timestamp,
                  updated_at: Timestamp,
                  subscribed_at: Timestamp,
                  unsubscribed_at: Timestamp,
                  inactivated_at: Timestamp,
                  birthday: Timestamp,
                  gender: String,
                  bio: String,
                  utc: String
                 ) extends Creature

  case class Role(id: String,
                  name: String,
                  description: String,
                  skills: Seq[Skill]) extends Attribute

  case class Skill(id: String,
                   name: String) extends Attribute

  case class Task(title: String,
                  text: String,
                  skill: Seq[Skill]) extends Action

  case class CreatureKafkaPackage[T](creatureId: String,
                                     creature: T)
}
