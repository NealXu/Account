package com.nealxu.io.mavenbook.account.persist

trait AccountPersistService {

  @throws(classOf[AccountPersistException])
  def createAccount(account: Account): Account

  @throws(classOf[AccountPersistException])
  def readAccount(id: String): Account

  @throws(classOf[AccountPersistException])
  def updateAccount(account: Account): Account

  @throws(classOf[AccountPersistException])
  def deleteAccount(id: String): Unit

  def deleteAllAccount(): Unit

}
