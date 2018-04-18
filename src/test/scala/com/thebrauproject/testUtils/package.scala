package com.thebrauproject


import java.text.SimpleDateFormat

import com.thebrauproject.elements.{Hero, Skill, Profile}
import com.thebrauproject.util._

package object testUtils {
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'")
  val pythonSkill = Skill("1234", "python")
  val javascriptSkill = Skill("321", "javascript")
  val skills = Seq(pythonSkill, javascriptSkill)
  val profile = Profile("https://www.google.com.br/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwiQp5uruqHaAhWErFkKHdzrCX0QjRx6BAgAEAU&url=https%3A%2F%2Ftemporarytattoos.com%2Fkevin-minion-temporary-tattoo&psig=AOvVaw1IaexXIUjX9OdNIbMKfFQO&ust=1522960021798530",
    "andrepetridis@gmail.com",
    "5511976689677")
  val NOT_DELETED = false
  val hero = Hero("123456789",
    "André Nicolas Petridis de Oiiveira",
    "André",
    "André",
    profile,
    utc,
    utc,
    utc,
    utc,
    utc,
    utc,
    "male",
    "Some",
    "+11:00")
}