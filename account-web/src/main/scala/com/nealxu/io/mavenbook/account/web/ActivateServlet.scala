package com.nealxu.io.mavenbook.account.web

import com.nealxu.io.mavenbook.account.service.{AccountService, AccountServiceException}
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

@SerialVersionUID(3668445055149826106L)
class ActivateServlet extends HttpServlet {

  private var context: ApplicationContext = _

  override def init(): Unit = {
    super.init()
    context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext)
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val key = req.getParameter("key")
    if (key == null || key.length == 0) {
      resp.sendError(400, "No activation key provided.")
    } else {
      val service = context.getBean("accountService").asInstanceOf[AccountService]

      try {
        service.activate(key)
        resp.getWriter.write("Account is activated, now you can login.")
      } catch {
        case _: AccountServiceException =>
          resp.sendError(400, "Unable to activate account")
      }
    }
  }
}

