package com.nealxu.io.mavenbook.account.email

import com.icegreen.greenmail.util.{GreenMail, GreenMailUtil, ServerSetup}
import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfterAll
import org.springframework.context.support.ClassPathXmlApplicationContext

class AccountEmailServiceTest extends FlatSpec with BeforeAndAfterAll {

  private var greenMail: GreenMail = _

  @throws(classOf[Exception])
  private def startMailServer(): Unit = {
    greenMail = new GreenMail(ServerSetup.SMTP)
    greenMail.setUser("xuqixq@sina.com", "123456")
    greenMail.start()
  }

  override def beforeAll(): Unit = {
    startMailServer()
  }

  override def afterAll(): Unit = {
    greenMail.stop()
  }

  "sendMail" should "work as expecting" in {
    val ctx = new ClassPathXmlApplicationContext("account-email.xml")
    val accountEmailService = ctx.getBean("accountEmailService").asInstanceOf[AccountEmailService]
    val subject = "Test Subject"
    val htmlText = "<h3>Test</h3>"
    val to = "test2@gmail.com"
    accountEmailService.sendMail(to, subject, htmlText)
    greenMail.waitForIncomingEmail(2000, 1)

    val msgs = greenMail.getReceivedMessages

    assertResult(1)(msgs.length)
    assertResult(subject)(msgs(0).getSubject)
    assertResult(htmlText)(GreenMailUtil.getBody(msgs(0)).trim)
  }

}
