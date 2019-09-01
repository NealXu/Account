package com.nealxu.io.mavenbook

import com.nealxu.io.mavenbook.account.captcha.{AccountCaptchaException, AccountCaptchaService, RandomGenerator}
import com.nealxu.io.mavenbook.account.email.{AccountEmailException, AccountEmailService}
import com.nealxu.io.mavenbook.account.persist.{Account, AccountPersistException, AccountPersistService}
import scala.collection.mutable

class AccountServiceImpl(
                          accountEmailService: AccountEmailService,
                          accountPersistService: AccountPersistService,
                          accountCaptchaService: AccountCaptchaService
                        ) extends AccountService {

  private val activationMap: mutable.Map[String, String] = new mutable.HashMap

  @throws(classOf[AccountServiceException])
  def generateCaptchaKey(): String = {
    try {
      accountCaptchaService.generateCaptchaKey()
    } catch {
      case e: AccountCaptchaException =>
        throw new AccountServiceException("Unable to generate Captcha key", e)
    }
  }

  @throws(classOf[AccountServiceException])
  def generateCaptchaImage(captchaKey: String): Array[Byte] = {

    try {
      accountCaptchaService.generateCaptchaImage(captchaKey)
    } catch {
      case e: AccountCaptchaException =>
        throw new AccountServiceException("Unable to generate Captcha image", e)
    }
  }

  @throws(classOf[AccountServiceException])
  def signUp(signUpRequest: SignUpRequest): Unit = {
    try {
      if (!signUpRequest.password.equals(signUpRequest.confirmPassword)) {
        throw new AccountServiceException("2 passwords do not match.")
      }

      if (!accountCaptchaService.validateCaptcha(signUpRequest.captchaKey, signUpRequest.captchaValue)) {
        throw new AccountServiceException("Incorrect Captcha.")
      }

      val account = Account(
        signUpRequest.id,
        signUpRequest.name,
        signUpRequest.email,
        signUpRequest.password
      )

      accountPersistService.createAccount(account)

      val activationId = RandomGenerator.getRandomString
      activationMap.put(activationId, account.id)
      val link = signUpRequest.getActivateServiceUrl + s"?key=$activationId"

      accountEmailService.sendMail(account.email, "Please Activate Your Account", link)
    } catch {
      case e: AccountCaptchaException =>
        throw new AccountServiceException("Unable to validate captcha.", e)
      case e: AccountPersistException =>
        throw new AccountServiceException("Unable to create account.", e)
      case e: AccountEmailException =>
        throw new AccountServiceException("Unable to send activation mail.", e)
    }

  }

  @throws(classOf[AccountServiceException])
  def activate(activationNumber: String): Unit = {
    val accountId = activationMap.get(activationNumber) match {
      case None =>
        throw new AccountServiceException("Invalid account activation ID.")
      case Some(id) => id
    }

    try {
      val account = accountPersistService.readAccount(accountId)
      account.activated = true
      accountPersistService.updateAccount(account)
    } catch {
      case _: AccountPersistException =>
        throw new AccountServiceException(s"Unable to activate account $accountId")
    }
  }

  @throws(classOf[AccountServiceException])
  def login(id: String, password: String): Unit = {
    try {
      val account = accountPersistService.readAccount(id)

      if (account == null) {
        throw new AccountServiceException(s"Account does not exist.")
      }

      if (!account.activated) {
        throw new AccountServiceException(s"Account is disabled.")
      }

      if (!account.password.equals(password)) {
        throw new AccountServiceException(s"Incorrect password.")
      }
    } catch {
      case e: AccountPersistException =>
        throw new AccountServiceException(s"Unable to log in.", e)
    }
  }

}
