package com.nealxu.io.mavenbook.account.persist

import java.io.{File, FileOutputStream, OutputStreamWriter, Writer}
import org.dom4j.{Document, DocumentException, DocumentFactory, Element}
import org.dom4j.io.{OutputFormat, SAXReader, XMLWriter}
import scala.collection.JavaConverters._

class AccountPersistServiceImpl(file: String) extends AccountPersistService {

  import AccountPersistServiceImpl._

  private val reader: SAXReader = new SAXReader()

  @throws(classOf[AccountPersistException])
  def createAccount(account: Account): Account = {
    val (doc, root) = getDocAndRootElement
    val accountsEle = root.element(ELEMENT_ACCOUNTS)
    val accountEle = buildAccountElement(accountsEle)

    updateAccount2XmlFile(doc, accountEle, account)
  }

  @throws(classOf[AccountPersistException])
  def readAccount(id: String): Account = {
    val (_, root) = getDocAndRootElement
    val accountEles = root.element(ELEMENT_ACCOUNTS).elements().asScala.asInstanceOf[Seq[Element]]
    accountEles.find(_.elementText(ELEMENT_ACCOUNT_ID) == id) match {
      case Some(a) => buildAccount(a)
      case None => null
    }
  }

  @throws(classOf[AccountPersistException])
  def updateAccount(account: Account): Account = {
    val (doc, root) = getDocAndRootElement
    val accountEles = root.element(ELEMENT_ACCOUNTS).elements().asScala.asInstanceOf[Seq[Element]]
    accountEles.find(_.elementText(ELEMENT_ACCOUNT_ID) == account.id) match {
      case Some(a) =>
        updateAccount2XmlFile(doc, a, account)
      case None => null
    }
  }

  @throws(classOf[AccountPersistException])
  def deleteAccount(id: String): Unit = {
    val (doc, root) = getDocAndRootElement
    val accountEles = root.element(ELEMENT_ACCOUNTS).elements().asScala.asInstanceOf[Seq[Element]]
    accountEles.find(_.elementText(ELEMENT_ACCOUNT_ID) == id) match {
      case Some(a) =>
        deleteAccountFromXml(doc, a)
      case None =>
    }
  }

  override def deleteAllAccount(): Unit = {
    val dataFile = new File(file)
    if (dataFile.exists()) dataFile.delete()
  }

  @throws(classOf[AccountPersistException])
  private def readDocument(): Document = {
    val dataFile = new File(file)

    if (!dataFile.exists()) {
      dataFile.getParentFile.mkdirs()
      val doc = DocumentFactory.getInstance().createDocument()
      val rootEle = doc.addElement(ELEMENT_ROOT)
      rootEle.addElement(ELEMENT_ACCOUNTS)
      writeDocument(doc)
    }

    try {
      reader.read(new File(file))
    } catch {
      case e: DocumentException =>
        throw new AccountPersistException("Unable to read persist data xml", e)
    }
  }


  private def getDocAndRootElement: (Document, Element) = {
    val doc = readDocument()
    val rootElement = doc.getRootElement
    (doc, rootElement)
  }

  @throws(classOf[AccountPersistException])
  private def writeDocument(doc: Document): Unit = {
    var out: Option[Writer] = None
    try {
      out = Some(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))
      val writer = new XMLWriter(out.get, OutputFormat.createPrettyPrint())
      writer.write(doc)
    } catch {
      case e: Exception =>
        throw new AccountPersistException("Unable to write persist data xml", e)
    } finally {
      try {
        if (out.isDefined) out.get.close()
      } catch {
        case e:Exception =>
          throw new AccountPersistException("Unable to close persist data xml writer", e)
      }
    }
  }

  @throws(classOf[AccountPersistException])
  private def buildAccount(element: Element): Account = {
    try {
      val id = element.elementText(ELEMENT_ACCOUNT_ID)
      val name = element.elementText(ELEMENT_ACCOUNT_NAME)
      val email = element.elementText(ELEMENT_ACCOUNT_EMAIL)
      val password = element.elementText(ELEMENT_ACCOUNT_PASSWORD)
      val activated = element.elementText(ELEMENT_ACCOUNT_ACTIVATED).equals("true")
      Account(id, name, email, password, activated)
    } catch {
      case e: Exception =>
        throw new AccountPersistException("Unable to build account from xml element", e)
    }
  }

  private def buildAccountElement(accountsEle: Element): Element = {
    val accountEle = accountsEle.addElement(ELEMENT_ACCOUNT)

    accountEle.addElement(ELEMENT_ACCOUNT_ID)
    accountEle.addElement(ELEMENT_ACCOUNT_NAME)
    accountEle.addElement(ELEMENT_ACCOUNT_EMAIL)
    accountEle.addElement(ELEMENT_ACCOUNT_PASSWORD)
    accountEle.addElement(ELEMENT_ACCOUNT_ACTIVATED)

    accountEle
  }

  private def updateAccountElement(accountEle: Element, account: Account): Unit = {
    accountEle.element(ELEMENT_ACCOUNT_ID).setText(account.id)
    accountEle.element(ELEMENT_ACCOUNT_NAME).setText(account.name)
    accountEle.element(ELEMENT_ACCOUNT_EMAIL).setText(account.email)
    accountEle.element(ELEMENT_ACCOUNT_PASSWORD).setText(account.password)
    accountEle.element(ELEMENT_ACCOUNT_ACTIVATED).setText(account.activated.toString)
  }

  private def updateAccount2XmlFile(document: Document, accountEle: Element, account: Account): Account = {
    updateAccountElement(accountEle, account)
    writeDocument(document)
    account
  }

  private def deleteAccountFromXml(document: Document, accountEle: Element): Unit = {
    accountEle.getParent.remove(accountEle)
    writeDocument(document)
  }

}

object AccountPersistServiceImpl {

  val ELEMENT_ROOT = "account-persist"
  val ELEMENT_ACCOUNTS = "accounts"
  val ELEMENT_ACCOUNT = "account"
  val ELEMENT_ACCOUNT_ID = "id"
  val ELEMENT_ACCOUNT_NAME = "name"
  val ELEMENT_ACCOUNT_EMAIL = "email"
  val ELEMENT_ACCOUNT_PASSWORD = "password"
  val ELEMENT_ACCOUNT_ACTIVATED = "activated"

  def apply(file: String): AccountPersistServiceImpl = new AccountPersistServiceImpl(file)
}
