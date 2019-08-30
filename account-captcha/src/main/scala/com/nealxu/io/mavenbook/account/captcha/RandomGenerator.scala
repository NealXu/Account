package com.nealxu.io.mavenbook.account.captcha

import scala.util.Random

object RandomGenerator {

  private val range = "01234567890abcdefghijklmnopqrstuvwxyz"

  def getRandomString: String = {
    (1 to 8).map(_ => range.charAt(Random.nextInt(range.length))).mkString

  }

}
