package com.nealxu.io.mavenbook.account.email

trait AccountEmailService {

  @throws(classOf[AccountEmailException])
  def sendMail(to: String, subject: String, htmlText: String): Unit

}
