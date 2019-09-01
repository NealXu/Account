package com.nealxu.io.mavenbook

trait AccountService {

  @throws(classOf[AccountServiceException])
  def generateCaptchaKey(): String

  @throws(classOf[AccountServiceException])
  def generateCaptchaImage(captchaKey: String): Array[Byte]

  @throws(classOf[AccountServiceException])
  def signUp(signUpRequest: SignUpRequest): Unit

  @throws(classOf[AccountServiceException])
  def activate(activationNumber: String): Unit

  @throws(classOf[AccountServiceException])
  def login(id: String, password: String): Unit
}
