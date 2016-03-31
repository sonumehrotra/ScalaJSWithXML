package com.knoldus

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object DashboardClient extends js.JSApp {
  @JSExport
  def main(): Unit = {

    val dashboard = new Dashboard
    dom.document.getElementById("bodyContent").appendChild(dashboard.bodyFrag.render)
  }

  /*
  * Renders the table with the name of project and corresponding number of warnings
  * */
  @JSExport
  def displayDashboardTable(message: js.Array[String], warning: js.Array[String],
    scapegoatResult: js.Array[String], scoverageResult: js.Array[String],
    cpdResult: js.Array[String]): Unit = {

    val dashboard = new Dashboard(message, warning, scapegoatResult, scoverageResult, cpdResult)
    dom.document.getElementById("bodyContent").appendChild(dashboard.displayWarningFrag.render)
  }
}

class Dashboard[Builder, Output <: FragT, FragT](message: js.Array[String] = js.Array(""), warning: js.Array[String] = js.Array(""),
    scapegoatResult: js.Array[String] = js.Array(""), scoverageResult: js.Array[String] = js.Array(""),
    cpdResult: js.Array[String] = js.Array("")) {

  val projectFrag = for (i <- 0 to message.length - 1) yield {
    div(
      `class` := "col-md-4",
      div(
        `class` := (if (i == 0) "ibox" else "ibox float-e-margins"),
        div(
          `class` := "ibox-title",
          h3(`class` := "text-center", message(i))
        ),
        div(
          `class` := "ibox-content",
          table(
            `class` := (if (i == 0) "table borderless table-hover" else "table borderless table-hover no-margins"),
            tbody(
              tr(
                td("Style Warning"),
                td(`class` := "text-success", warning(i))
              ),
              tr(
                td("Code Analysis"),
                td(`class` := "text-success", "" + scapegoatResult(i))
              ),
              tr(
                td("Test Coverage"),
                td(`class` := "text-success", scoverageResult(i))
              ),
              tr(
                td("Copy Paste Detector"),
                td(`class` := "text-success", cpdResult(i))
              )
            )
          )
        )
      )
    )
  }

  val bodyFrag =
    div(
      div(
        `class` := "row border-bottom",
        div(
          `class` := "col-md-12",
          div(
            `class` := "col-md-2 image-alignment",
            img(width := "161", src := "/assets/images/knoldus.jpg")
          ),
          div(`class` := "col-md-10")
        )
      ),
      div(
        `class` := "row border-bottom",
        div(
          `class` := "col-md-12",
          div(
            `class` := "col-md-12",
            h1(`class` := "text-center", "Dashboard")
          )
        )
      )
    )

  val displayWarningFrag =
    div(
      `class` := "row wrapper-content",
      div(`class` := "col-md-12"), projectFrag
    )
}
