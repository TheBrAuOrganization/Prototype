package com.thebrauproject

package object elements {

  trait Creature

  trait Attribute

  trait Objective

  case class Hero(id: String,
                  name: String,
                  skills: Seq[Skill],
                  role: Role) extends Creature

  case class Role(id: String,
                  name: String,
                  description: String,
                  skills: Seq[Skill]) extends Attribute

  case class Skill(id: String,
                   name: String) extends Attribute

  case class Task(title: String,
                  text: String,
                  skill: Seq[Skill]) extends Objective

  case class CreatureKafkaPackage[T](creatureId: String,
                                     creature: T)
}
