package com.thebrauproject

package object kafka {
  case class AckObject(topic: String,
                       partition: Long,
                       offset: Int)
}
