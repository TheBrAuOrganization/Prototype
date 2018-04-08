package com.thebrauproject

import java.sql.Timestamp

package object elements {

  trait Creature

  trait Attribute

  trait Action

  case class Hero(id: String,
                  name: String,
                  createAt: Timestamp,
                  modifiedAt: Timestamp,
                  skills: Seq[Skill],
                  roleId: String,
                  deleted: Boolean
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
