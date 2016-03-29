package controllers

import java.io.FileNotFoundException
import javax.inject._
import com.typesafe.config.ConfigFactory
import play.api.mvc._
import scala.collection.mutable.ListBuffer
import scala.xml.XML._

class Application @Inject() extends Controller {

  def index = Action {

    val warning:ListBuffer[String] = ListBuffer()
    val arrList = getProjectPathsFromConfig
    val nameOfProjects:List[String]= findProjectName(arrList)
    for(i <- 0 to arrList.length-1){
      warning += getWarnings(arrList(i))
    }
    Ok(views.html.index(nameOfProjects,warning.toList))
  }

  def getProjectPathsFromConfig: Array[String] = {

    val projectPath = ConfigFactory.load.getList("project-paths").unwrapped().toArray()
    projectPath.map(name => name.toString)
  }

  def getWarnings(file:String):String={

    try {
      val xml = loadFile(file + "/target/scalastyle-result.xml")
      val result = for {
        check <- xml \\ "error"
        if ((check \ "@severity").text == "warning")
        node <- check
      } yield node
      result.length.toString
    }
    catch {
      case ex:FileNotFoundException => "File Not Found"
      case _ => "Error"
    }
  }

  def findProjectName(arrList:Array[String]):List[String]={

    val tempNameList:ListBuffer[String]=ListBuffer()
    for(i <- 0 to arrList.length-1){
      tempNameList += arrList(i).split('/').last
    }
    tempNameList.toList
  }

}

