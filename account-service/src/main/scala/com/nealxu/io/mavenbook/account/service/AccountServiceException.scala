package com.nealxu.io.mavenbook.account.service

class AccountServiceException(msg: String, e: Exception) extends Exception(msg, e) {

  def this(msg: String) = {
    this(msg, null)
  }

}
