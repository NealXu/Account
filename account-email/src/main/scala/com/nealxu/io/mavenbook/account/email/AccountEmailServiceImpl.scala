package com.nealxu.io.mavenbook.account.email

import javax.mail.MessagingException
//import javax.mail.internet.MimeMessage

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

class AccountEmailServiceImpl extends AccountEmailService {

  private var javaMailSender: JavaMailSender = _
  private var systemEmail: String = _

  @throws(classOf[AccountEmailException])
  override def sendMail(to: String, subject: String, htmlText: String): Unit = {
    try {
      val msg = javaMailSender.createMimeMessage()
      val msgHelper = new MimeMessageHelper(msg)

      msgHelper.setFrom(systemEmail)
      msgHelper.setTo(to)
      msgHelper.setSubject(subject)
      msgHelper.setText(htmlText, true)

      javaMailSender.send(msg)
    } catch {
      case e: MessagingException =>
        throw new AccountEmailException("Failed to send mail.", e)
    }

  }

  def getJavaMailSender: JavaMailSender = javaMailSender

  def setJavaMailSender(javaMailSender: JavaMailSender): Unit = {
    this.javaMailSender = javaMailSender
  }

  def getSystemEmail: String = systemEmail

  def setSystemEmail(systemEmail: String): Unit = {
    this.systemEmail = systemEmail
  }

}
