package com.nealxu.io.mavenbook

case class SignUpRequest(id: String,
                         email: String,
                         name: String,
                         password: String,
                         confirmPassword: String,
                         captchaKey: String,
                         captchaValue: String) {

  def getActivateServiceUrl: String = {
    "https://github.nealxu.io/"
  }
}
