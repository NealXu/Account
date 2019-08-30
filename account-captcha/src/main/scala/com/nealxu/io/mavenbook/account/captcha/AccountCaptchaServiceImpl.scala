package com.nealxu.io.mavenbook.account.captcha

import java.io.ByteArrayOutputStream
import java.util.Properties

import com.google.code.kaptcha.impl.DefaultKaptcha
import com.google.code.kaptcha.util.Config
import javax.imageio.ImageIO
import org.springframework.beans.factory.InitializingBean

import scala.collection.mutable

class AccountCaptchaServiceImpl
  extends AccountCaptchaService with InitializingBean {

  private var producer: DefaultKaptcha = _

  private val captchaMap = new mutable.HashMap[String, String]()

  private var textCount = 0

  private var preDefinedTexts: List[String] = Nil

  /**
   * initialize captcha generator with default configuration
   *
   * @throws java.lang.Exception
   */
  @throws(classOf[Exception])
  override def afterPropertiesSet(): Unit = {
    producer = new DefaultKaptcha
    producer.setConfig(new Config(new Properties()))
  }

  @throws(classOf[AccountCaptchaException])
  def generateCaptchaKey(): String = {
    val key = RandomGenerator.getRandomString
    val value = getCaptchaText
    captchaMap.put(key, value)
    key
  }

  @throws(classOf[AccountCaptchaException])
  def generateCaptchaImage(captchaKey: String): Array[Byte] = {
    captchaMap.get(captchaKey) match {
      case None =>
        throw new AccountCaptchaException(s"Captcha key $captchaKey not found!")
      case Some(text) =>
        val image = producer.createImage(text)
        val out = new ByteArrayOutputStream()
        try {
          ImageIO.write(image, "jpg", out)
          out.toByteArray
        } catch {
          case e: Exception =>
            throw new AccountCaptchaException("Failed to write captcha stream!", e)
        }
    }

  }

  @throws(classOf[AccountCaptchaException])
  def validateCaptcha(captchaKey: String, captchaValue: String): Boolean = {
    captchaMap.get(captchaKey) match {
      case None =>
        throw new AccountCaptchaException(s"Captcha key $captchaKey not found!")
      case Some(text) =>
        val result = text == captchaValue
        if (result) captchaMap.remove(captchaKey)
        result
    }
  }

  @throws(classOf[AccountCaptchaException])
  def getPreDefinedTexts: List[String] = {
    preDefinedTexts
  }

  @throws(classOf[AccountCaptchaException])
  def setPreDefinedTexts(preDefinedTexts: List[String]): Unit = {
    this.preDefinedTexts = preDefinedTexts
  }


  def getCaptchaText: String = {
    if (preDefinedTexts.nonEmpty) {
      val text = preDefinedTexts(textCount)
      textCount = (textCount + 1) % preDefinedTexts.length
      text
    } else {
      producer.createText()
    }
  }

}
