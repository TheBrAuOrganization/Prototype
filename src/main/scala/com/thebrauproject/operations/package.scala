package com.thebrauproject

import com.thebrauproject.elements.{Attribute, Creature}

package object operations {
  sealed trait State

  case object Connected extends State
  case object Disconnected extends State

  sealed trait DBOperations
  sealed trait DBCreatureOperation
  sealed trait DBAttributeOperation
  sealed trait DBObjectiveOperation

  object DBOperations {
    case object Create extends DBOperations
    case object Update extends DBOperations
    case object Read extends DBOperations
    case object Delete extends DBOperations
  }

  sealed trait Action

  case object Connect extends Action
  case object Disconnect extends Action

  case class CreateCreature[+T <: Creature](creature: T) extends DBCreatureOperation
  case class UpdateCreature[+T <: Creature](creature: T) extends DBCreatureOperation
  case class ReadCreature[+T <: Creature](creature: T) extends DBCreatureOperation
  case class DeleteCreature[+T <: Creature](creature: T) extends DBCreatureOperation

  //case class OperationCreature[T](dBOperations: DBOperations, creature: Option[T])
  case class OperationAttributes(dBOperations: DBOperations, attributes: Option[Attribute])

  sealed trait RedisOperation
  case class RedisObject(key: String, value: String)

  sealed trait OperationStatus
  case object OperationSuccess extends OperationStatus
  case object OperationFailure  extends OperationStatus

  case object StartConsumer

  case object StartProducer

  sealed trait ProducerStatus

  case object ProducerSuccess extends ProducerStatus
  case object ProducerFailure extends ProducerStatus
}
