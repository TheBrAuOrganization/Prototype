package com.thebrauproject

import java.security.MessageDigest
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import akka.actor.Actor
import com.google.common.io.BaseEncoding
import com.thebrauproject.elements.{Creature, Hero}
import net.liftweb.json._

package object util {

  def utc: Timestamp = Timestamp.from(Instant.now)

  def Base64SHA256(string: String): String =
    BaseEncoding
      .base64()
      .encode(MessageDigest.getInstance("SHA-256")
      .digest(string.getBytes))

  def parseJson[T: Manifest](json: Option[String]): Option[T] = {
    implicit val formats = DefaultFormats
    try
      Some(parse(json.get).extract[T])
    catch {
      case _: Exception => None
    }
  }

  def generateIdWithPrefix(prefix: String): String = prefix + "_" + UUID.randomUUID().toString

}
