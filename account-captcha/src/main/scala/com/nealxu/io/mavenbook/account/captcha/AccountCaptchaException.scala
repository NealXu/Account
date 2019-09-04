package com.nealxu.io.mavenbook.account.captcha

class AccountCaptchaException(msg: String, e: Throwable) extends Exception(msg, e) {

  def this(msg: String) = {
    this(msg, null)
  }

}
