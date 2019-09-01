package com.nealxu.io.mavenbook.account.persist

case class Account(
                    id: String,
                    name: String,
                    email: String,
                    password: String,
                    var activated: Boolean = false
                  )
