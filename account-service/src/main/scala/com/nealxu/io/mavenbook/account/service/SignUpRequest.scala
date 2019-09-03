package com.nealxu.io.mavenbook.account.service

case class SignUpRequest(id: String,
                         email: String,
                         name: String,
                         password: String,
                         confirmPassword: String,
                         captchaKey: String,
                         captchaValue: String,
                         activateServiceUrl: String)
