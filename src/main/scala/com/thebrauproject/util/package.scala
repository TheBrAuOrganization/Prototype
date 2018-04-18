package com.thebrauproject

import java.security.MessageDigest
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import com.google.common.io.BaseEncoding

package object util {

  def utc: Timestamp = Timestamp.from(Instant.now)

  def Base64SHA256(string: String): String =
    BaseEncoding
      .base64()
      .encode(MessageDigest.getInstance("SHA-256")
      .digest(string.getBytes))


  def generateIdWithPrefix(prefix: String): String =
    prefix + "_" + UUID.randomUUID().toString

  def parseTimestampString(timestamp: String): Timestamp = {
    try
      Timestamp.from(Instant.parse(timestamp))
    catch {
      case _: Exception => null
    }
  }

}
