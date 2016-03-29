package com.knoldus

import scala.scalajs.js
import org.scalajs.dom
import scalatags.JsDom.all._
import scala.scalajs.js.annotation.JSExport

@JSExport
object DashboardClient extends js.JSApp{
  @JSExport
  def main():Unit={

    val displayFrag = div(
      h1("Knoldus-Dashboard",textAlign:="center",textDecoration:="underline",color:="blue")
    )
    dom.document.getElementById("content").appendChild(displayFrag.render)
  }

  @JSExport
  def displayXML(message:js.Array[String],warning:js.Array[String]):Unit={

    def innterTable = for(i <- 0 to message.length-1) yield {
        tr(
          td(message(i)),
          td(warning(i))
        )
      }

    val displayFrag =div(table( borderSpacing:="20px",
      tr(
        th("Name"),
        th("Warning ")
      ),
      innterTable
      )
    )
    dom.document.getElementById("content").appendChild(displayFrag.render)
  }
}