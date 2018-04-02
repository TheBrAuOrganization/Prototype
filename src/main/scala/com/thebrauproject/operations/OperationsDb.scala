package com.thebrauproject.operations

import com.thebrauproject.elements.{Creature, Attribute}

object OperationsDb {
  sealed trait State

  case object Connected extends State
  case object Disconnected extends State

  sealed trait DBOperations
  sealed trait DBCreatureOperation

  object DBOperations {
    case object Create extends DBOperations
    case object Update extends DBOperations
    case object Read extends DBOperations
    case object Delete extends DBOperations
  }


    case class CreateCreature[T <: Creature](creature: T) extends DBCreatureOperation
    case class UpdateCreature[T <: Creature](creature: T) extends DBCreatureOperation
    case class ReadCreature[T <: Creature](creature: T) extends DBCreatureOperation
    case class DeleteCreature[T <: Creature](creature: T) extends DBCreatureOperation

  case object Connect
  case object Disconnect

  case class OperationCreature[T](dBOperations: DBOperations, creature: Option[T])
  case class OperationAttributes(dBOperations: DBOperations, attributes: Option[Attribute])

  sealed trait RedisOperation
  case class RedisObject(key: String, value: String)
}
