package controllers

import java.io.FileNotFoundException
import javax.inject._
import com.typesafe.config.ConfigFactory
import play.api.mvc._
import scala.collection.mutable.ListBuffer
import scala.xml.XML._

class Application @Inject() extends Controller {

  /*
 * Sends the list of project names and number of warnings to the view
 * */
  def index: Action[AnyContent] = Action {

    val warning: ListBuffer[String] = ListBuffer()
    val scapegoatResult: ListBuffer[List[String]] = ListBuffer()
    val scoverageResult: ListBuffer[String] = ListBuffer()
    val cpdResult: ListBuffer[String] = ListBuffer()
    val projectList = findProjectPathsFromConfig
    val nameOfProjects: List[String] = findProjectName(projectList)
    val time = ConfigFactory.load.getInt("time")
    for (i <- 0 to projectList.length - 1) {
      warning += findScalastyleWarnings(projectList(i))
      scapegoatResult += findScapegoatWarnings(projectList(i))
      scoverageResult += findScoverageReport(projectList(i))
      cpdResult += findCopyPasteDetectorReport(projectList(i))
    }
    Ok(views.html.index(nameOfProjects, warning.toList, scapegoatResult.toList, scoverageResult.toList, cpdResult.toList, time))
  }

  /*
  * Selects all project path from config file and return an array of paths
  * */
  def findProjectPathsFromConfig: Array[String] = {

    val projectPath = ConfigFactory.load.getList("project-paths").unwrapped().toArray()
    projectPath.map(name => name.toString)
  }

  /*
  * Calculates total number of warnings in the XML file
  * */
  def findScalastyleWarnings(file: String): String = {

    try {
      val scalastyleXml = loadFile(file + "/target/scalastyle-result.xml")
      val result = for {
        check <- scalastyleXml \\ "error"
        if ((check \ "@severity").text == "warning")
        node <- check
      } yield node
      result.length.toString
    } catch {
      case ex: FileNotFoundException => "N/A"
      case _ => "Error"
    }
  }

  /*
* returns a list of project names
* */
  def findProjectName(projectList: Array[String]): List[String] = {

    val tempNameList: ListBuffer[String] = ListBuffer()
    for (i <- 0 to projectList.length - 1) {
      tempNameList += projectList(i).split('/').last
    }
    tempNameList.toList
  }

  def findScapegoatWarnings(file: String): List[String] = {

    try {
      val scapegoatXml = loadFile(file + "/target/scala-2.11/scapegoat-report/scapegoat.xml")
      val warns: String = "warns = " + (scapegoatXml \\ "scapegoat" \ "@warns").toString
      val errors: String = "errors = " + (scapegoatXml \\ "scapegoat" \ "@errors").toString
      val infos: String = "infos = " + (scapegoatXml \\ "scapegoat" \ "@infos").toString
      List(warns, errors, infos)
    } catch {
      case ex: FileNotFoundException => List("N/A")
      case _ => List("Error")
    }

  }

  def findScoverageReport(file: String): String = {

    try {
      val scoverageXml = loadFile(file + "/target/scala-2.11/scoverage-report/scoverage.xml")
      val coveragePercent = (scoverageXml \\ "scoverage" \ "@statement-rate").toString
      val branchPercent = (scoverageXml \\ "scoverage" \ "@branch-rate").toString
      "Statement Coverage = " + coveragePercent + ", Branch Coverage = " + branchPercent
    } catch {
      case ex: FileNotFoundException => "N/A"
      case _ => "Error"
    }
  }

  def findCopyPasteDetectorReport(file: String): String = {

    try {
      val cpdXml = loadFile(file + "/target/scala-2.11/cpd/cpd.xml")
      val cpdCount = (cpdXml \\ "duplication").length.toString
      cpdCount + " Files"
    } catch {
      case ex: FileNotFoundException => "N/A"
      case _ => "Error"
    }

  }

}

