package com.nealxu.io.mavenbook.account.captcha

trait AccountCaptchaService {

  @throws(classOf[AccountCaptchaException])
  def generateCaptchaKey(): String

  @throws(classOf[AccountCaptchaException])
  def generateCaptchaImage(captchaKey: String): Array[Byte]

  @throws(classOf[AccountCaptchaException])
  def validateCaptcha(captchaKey: String, captchaValue: String): Boolean

  @throws(classOf[AccountCaptchaException])
  def getPreDefinedTexts: List[String]

  @throws(classOf[AccountCaptchaException])
  def setPreDefinedTexts(preDefinedTexts: List[String]): Unit

}
