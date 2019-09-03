package com.nealxu.io.mavenbook.account.web

import java.io.IOException

import com.nealxu.io.mavenbook.account.service.{AccountService, AccountServiceException}
import javax.servlet.ServletException
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import org.springframework.context.ApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

@SerialVersionUID(5274323889605521606L)
class CaptchaImageServlet extends HttpServlet {

  private var context: ApplicationContext = _

  override def init(): Unit = {
    super.init()
    context = WebApplicationContextUtils.getWebApplicationContext(getServletContext)
  }


  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val key = req.getParameter("key")

    if (key == null || key.length == 0) {
      resp.sendError(400, "No Captcha Key Found")
    } else {
      val service = context.getBean("accountService").asInstanceOf[AccountService]

      try {
        resp.setContentType("image/jpeg")
        val out = resp.getOutputStream
        out.write(service.generateCaptchaImage(key))
        out.close()
      } catch {
        case e: AccountServiceException =>
          resp.sendError(400, e.getMessage)
      }
    }
  }
}
