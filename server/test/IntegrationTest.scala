package controllers

/**
 * Created by deepak on 29/3/16.
 */

import java.util.concurrent.TimeUnit

import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.FlatSpec

class IntegrationTest extends FlatSpec {

  val port = 9000
  val logUrl = "http://localhost:" + port
  val driver = new FirefoxDriver()
  "User " should "successfully hit the url" in {

    driver.get(logUrl)
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)

  }

  "User" should "get the correct project name" in {
    assert(driver.findElementByClassName(".text-center").getText === "WebPage")

  }

  "User" should "get the file not found" in {
    assert(driver.findElementByCssSelector("table tr:nth-child(2) td:nth-child(2)").getText === "20")

  }
  "User" should "get the number of warnings" in {
    assert(driver.findElementByCssSelector("table tr:nth-child(6) td:nth-child(2)").getText === "File Not Found")

  }

}
