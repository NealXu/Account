package com.nealxu.io.mavenbook.account.persist

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfterEach
import org.scalatest.BeforeAndAfterAll
import org.springframework.context.support.ClassPathXmlApplicationContext

class AccountPersistServiceTest extends FlatSpec with BeforeAndAfterEach with BeforeAndAfterAll {

//  private val filePath = "E:\\tmp\\account.xml"
//  private val accountPersistService = AccountPersistServiceImpl(filePath)
  private var accountPersistService: AccountPersistService = _

  override def beforeAll(): Unit = {
    val ctx = new ClassPathXmlApplicationContext("account-persist.xml")
    accountPersistService = ctx.getBean("accountPersistService").asInstanceOf[AccountPersistService]
  }

  override def beforeEach(): Unit = {
    accountPersistService.deleteAllAccount()
  }

  "Create Account" should "be ok" in {
    val account01 = Account("001", "neal", "nealxu@163.com", "123456")
    accountPersistService.createAccount(account01)
    val account02 = Account("002", "suya", "suya@163.com", "123456")
    accountPersistService.createAccount(account02)
  }

  "Read Account" should "be ok" in {
    val account01 = Account("001", "xuqi", "xuqi@163.com", "123456")
    accountPersistService.createAccount(account01)
    val account02 = Account("002", "suya", "suya@163.com", "123456")
    accountPersistService.createAccount(account02)
    val account03 = Account("003", "xusunuo", "xusunuo@163.com", "123456")
    accountPersistService.createAccount(account03)
    val account04 = Account("004", "xusuche", "xusuche@163.com", "123456")
    accountPersistService.createAccount(account04)

    val queryAccount = accountPersistService.readAccount("002")
    assertResult(account02)(queryAccount)
  }

  "Update Account" should "be ok" in {
    val account01 = Account("001", "xuqi", "xuqi@163.com", "123456")
    accountPersistService.createAccount(account01)
    val account02 = Account("002", "suya", "suya@163.com", "123456")
    accountPersistService.createAccount(account02)
    val account03 = Account("003", "xusunuo", "xusunuo@163.com", "123456")
    accountPersistService.createAccount(account03)
    val account04 = Account("004", "xusuche", "xusuche@163.com", "123456")
    accountPersistService.createAccount(account04)

    val queryAccount01 = accountPersistService.readAccount("002")
    assertResult(account02)(queryAccount01)

    val account05 = Account("004", "xusuche", "xusuche@163.com", "789", activated = true)
    accountPersistService.updateAccount(account05)
    val queryAccount02 = accountPersistService.readAccount("004")
    assertResult(account05)(queryAccount02)
  }

  "Delete Account" should "be ok" in {
    val account01 = Account("001", "xuqi", "xuqi@163.com", "123456")
    accountPersistService.createAccount(account01)
    val account02 = Account("002", "suya", "suya@163.com", "123456")
    accountPersistService.createAccount(account02)
    val account03 = Account("003", "xusunuo", "xusunuo@163.com", "123456")
    accountPersistService.createAccount(account03)
    val account04 = Account("004", "xusuche", "xusuche@163.com", "123456")
    accountPersistService.createAccount(account04)

    accountPersistService.deleteAccount("001")
    val queryAccount01 = accountPersistService.readAccount("001")
    assertResult(null)(queryAccount01)

    accountPersistService.deleteAccount("002")
    val queryAccount02 = accountPersistService.readAccount("002")
    assertResult(null)(queryAccount02)
  }
}
