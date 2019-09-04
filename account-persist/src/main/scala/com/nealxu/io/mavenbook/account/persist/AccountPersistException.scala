package com.nealxu.io.mavenbook.account.persist

class AccountPersistException(msg: String, e: Throwable) extends Exception(msg, e) {

  def this(msg: String) = {
    this(msg, null)
  }

}
