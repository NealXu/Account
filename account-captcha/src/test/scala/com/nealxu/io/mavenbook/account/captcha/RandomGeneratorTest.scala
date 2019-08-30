package com.nealxu.io.mavenbook.account.captcha

import org.scalatest.FlatSpec

class RandomGeneratorTest extends FlatSpec {

  "test random" should "be ok" in {
    println(RandomGenerator.getRandomString)
    println(RandomGenerator.getRandomString)
    println(RandomGenerator.getRandomString)
  }

}
