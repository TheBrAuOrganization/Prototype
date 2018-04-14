package com.thebrauproject.elements

import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeParseException

import spray.json.DefaultJsonProtocol._
import spray.json.{JsString, JsValue, JsonFormat}
import spray.json._

import com.thebrauproject.util._


object implicits {
  implicit object TimestampJsonFormat extends JsonFormat[Timestamp] {

    override def write(obj: Timestamp): JsValue = {
      JsString(obj.toInstant.toString)
    }

    override def read(json: JsValue): Timestamp = {
      json match {
        case JsString(value) =>
          try
            parseTimestampString(value)
          catch {
            case _: DateTimeParseException =>
              deserializationError("Unable to parse String. Expected: yyyy-MM-ddTHH:mm:ss.SSSZ")
          }
        case _ => deserializationError("Data Format not supported for Timestamp Value")
      }
    }
  }

  implicit val skillFormat = jsonFormat2(Skill)
  implicit val heroFormat = jsonFormat16(Hero)
}
