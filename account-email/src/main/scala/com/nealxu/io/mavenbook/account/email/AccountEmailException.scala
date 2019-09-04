package com.nealxu.io.mavenbook.account.email

class AccountEmailException(msg: String, e: Throwable) extends Exception(msg, e) {

  def this(msg: String) = {
    this(msg, null)
  }

}
