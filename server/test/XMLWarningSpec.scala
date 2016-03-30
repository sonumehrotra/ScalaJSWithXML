package controllers

/**
  * Created by deepak on 29/3/16.
  */

import java.util.concurrent.TimeUnit

import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.{FlatSpec, FunSuite}
import org.scalatest.tagobjects.FirefoxBrowser


class XMLWarningSpec extends FlatSpec {


  val port = 9000
  val logUrl = "http://localhost:" + port
  val driver = new FirefoxDriver()
  "Admin " should "successfully hit the url" in {

    driver.get(logUrl)
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)

  }

  "User" should "get the correct project name" in {
    assert(driver.findElementByCssSelector("table tr:nth-child(2) td").getText==="WebPage")

  }

  "User" should "get the file not found" in {
    assert(driver.findElementByCssSelector("table tr:nth-child(2) td:nth-child(2)").getText==="File Not Found")

  }
  "User" should "get the number of warnings" in {
    assert(driver.findElementByCssSelector("table tr:nth-child(6) td:nth-child(2)").getText==="14")

  }

}
