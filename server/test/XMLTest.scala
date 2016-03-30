
import controllers.Application
import org.scalatest.FunSuite

/**
  * Created by deepak on 29/3/16.
  */
class XMLTest extends FunSuite{

  val testObject = new Application

  test("correct number of warnings")
  {
    val result=testObject.getWarnings("/home/deepak/ScalaJSWithXML")
    assert(result==="14")

  }

  test("File not found")
  {
    val result=testObject.getWarnings("/home/deepak/sbt-play-scalajs")
  assert("File Not Found"===result)

  }

  test("Paths exist in app.conf") {
    val result = testObject.getProjectPathsFromConfig
    assert(result === Array("/home/knoldus/WebPage", "/home/knoldus/Desktop/PlayScalaJsShowcase/play-scalajs-showcase",
      "/home/knoldus/Desktop/PlayScalaJsShowcase/play-scalajs-showcas", "/home/knoldus/IdeaProjects/test2-sonu"))


  }
}
