
import controllers.Application
import org.scalatest.FunSuite

class ApplicationTest extends FunSuite {

  val testObject = new Application

  test("correct number of warnings") {
    val result = testObject.findScalastyleWarnings("/home/knoldus/WebPage")
    assert(result === "20")
  }

  test("File not found") {
    val result = testObject.findScalastyleWarnings("/home/knoldus/WebPag")
    assert("N/A" === result)
  }

  test("Paths exist in app.conf") {
    val result = testObject.findProjectPathsFromConfig
    assert(result === Array("/home/knoldus/WebPage", "/home/knoldus/Desktop/PlayScalaJsShowcase/play-scalajs-showcase",
      "/home/knoldus/Desktop/PlayScalaJsShowcase/play-scalajs-showcas", "/home/knoldus/RahulSonuPlayTest"))
  }

  test("Code analysis details") {
    val result = testObject.findScapegoatWarnings("/home/knoldus/WebPage")
    assert(List("warns = 12", "errors = 3", "infos = 23") === result)
  }

  test("Test coverage details") {
    val result = testObject.findScoverageReport("/home/knoldus/WebPage")
    assert("Statement Coverage = 73.08, Branch Coverage = 41.46" === result)
  }

  test("Copy Paste Detector details") {
    val result = testObject.findCopyPasteDetectorReport("/home/knoldus/WebPage")
    assert("17 Files" === result)
  }

}
