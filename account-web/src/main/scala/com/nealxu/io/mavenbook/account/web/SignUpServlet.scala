package com.nealxu.io.mavenbook.account.web

import java.io.IOException

import com.nealxu.io.mavenbook.account.service.{AccountService, AccountServiceException, SignUpRequest}
import javax.servlet.ServletException
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import org.springframework.context.ApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

@SerialVersionUID(4784742296013868199L)
class SignUpServlet extends HttpServlet {

  private var context: ApplicationContext = _

  override def init(): Unit = {
    super.init()
    context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext)
  }


  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val id = req.getParameter("id")
    val email = req.getParameter("email")
    val name = req.getParameter("name")
    val password = req.getParameter("password")
    val confirmPassword = req.getParameter("confirm_password")
    val captchaKey = req.getParameter("captcha_key")
    val captchaValue = req.getParameter("captcha_value")

    def nullOrEmpty(s: String): Boolean = null == s || s.length == 0

    if (nullOrEmpty(id) || nullOrEmpty(email) || nullOrEmpty(name) ||
      nullOrEmpty(password) || nullOrEmpty(confirmPassword) ||
      nullOrEmpty(captchaKey) || nullOrEmpty(captchaValue)) {
      resp.sendError(400, "incomplete parameter")
    } else {
      val service = context.getBean("accountService").asInstanceOf[AccountService]
      val request = SignUpRequest(id, email, name, password, confirmPassword,
        captchaKey, captchaValue, getServletContext.getRealPath("/") + "activate")

      try {
        service.signUp(request)
        resp.getWriter.print("Account is created, please check your mail box for activation link.")
      } catch {
        case _: AccountServiceException =>
          resp.sendError(400, "Unable to activate account")
      }
    }
  }

}
