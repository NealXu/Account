package com.nealxu.io.mavenbook.account.captcha

import java.io.{File, FileOutputStream, OutputStream}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpec
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

class AccountCaptchaServiceTest extends FlatSpec with BeforeAndAfterAll {

  private var service: AccountCaptchaService = _

  override def beforeAll(): Unit = {
    val ctx: ApplicationContext = new ClassPathXmlApplicationContext("account-captcha.xml")
    service = ctx.getBean("accountCaptchaService").asInstanceOf[AccountCaptchaService]
  }

  "test generate captcha" should "be ok" in {
    val key = service.generateCaptchaKey()
    assertResult(8)(key.length)

    val imageBytes = service.generateCaptchaImage(key)
    assert(imageBytes.length > 0)

    val imageFile = new File(s"target/$key.jpg")
    var output: OutputStream = null
    try {
      output = new FileOutputStream(imageFile)
      output.write(imageBytes)
    } finally {
      if (output != null) output.close()
    }

    assert(imageFile.exists() && imageFile.length() > 0)
  }

  "test validate captcha correct" should "be ok" in {
    val text01 = "12345"
    val text02 = "abcde"
    val preDefinedTexts = List(text01, text02)
    service.setPreDefinedTexts(preDefinedTexts)

    var key = service.generateCaptchaKey()
    service.generateCaptchaImage(key)
    assert(service.validateCaptcha(key, text01))

    key = service.generateCaptchaKey()
    service.generateCaptchaImage(key)
    assert(service.validateCaptcha(key, text02))
  }

  "test validate captcha incorrect" should "be ok" in {
    val text = "12345"
    val preDefinedTexts = List(text)
    service.setPreDefinedTexts(preDefinedTexts)

    val key = service.generateCaptchaKey()
    service.generateCaptchaImage(key)
    assert(!service.validateCaptcha(key, "67890"))

  }
}
