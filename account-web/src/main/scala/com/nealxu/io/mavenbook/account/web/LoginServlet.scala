package com.nealxu.io.mavenbook.account.web

import java.io.IOException

import com.nealxu.io.mavenbook.account.service.{AccountService, AccountServiceException}
import javax.servlet.ServletException
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import org.springframework.context.ApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

@SerialVersionUID(929160785365121624L)
class LoginServlet extends HttpServlet {

  private var context: ApplicationContext = _

  @throws(classOf[ServletException])
  override def init(): Unit = {
    super.init()
    context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext)
  }


  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val id = req.getParameter("id")
    val password = req.getParameter("password")

    println(s"id=$id  password=$password")

    if (id == null || id.length == 0 || password == null || password.length == 0) {
      println("incomplete parameter")
      resp.sendError(400, "incomplete parameter")
    } else {

      try {
        val service = context.getBean("accountService").asInstanceOf[AccountService]

        println(s"context is null(${context == null})")
        println(s"service is null(${service == null})")

        service.login(id, password)
        println("Login Successful!")
        resp.getWriter.write("Login Successful!")
      } catch {
        case e: AccountServiceException =>
          println(e.getMessage)
          resp.sendError(400, e.getMessage)
      }
    }
  }
}
