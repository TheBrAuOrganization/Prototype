package com.thebrauproject

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.thebrauproject.elements.{Hero, Skill}
import com.thebrauproject.util.utc

package object testUtils {
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'")
  val pythonSkill = Skill("1234", "python")
  val javascriptSkill = Skill("321", "javascript")
  val skills = Seq(pythonSkill, javascriptSkill)
  val NOT_DELETED = false
  val hero = Hero("pgU0wEI32OXvL3Dy/6Loq211xEPY5l7KSCgVJMDq0KA=",
                  "John Potato",
                  new Timestamp(dateFormat.parse("2018-04-08T07:23:27.219Z").getTime),
                  utc,
                  skills,
                  "cAUvzWwgfa+CRtGLocgxeujnCdRn/bf3mEshVP5tSh0",
                  NOT_DELETED)
}
