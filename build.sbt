name := """Knoldus-Dashboard"""

version := "1.0-SNAPSHOT"

import sbt.Keys._
import sbt.Project.projectToRef
import de.johoop.cpd4sbt.CopyPasteDetector._

seq(cpdSettings : _*)
lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  jsManifestFilter := {
    import org.scalajs.core.tools.jsdep.{JSDependencyManifest, JSDependency}

    (seq: Traversable[JSDependencyManifest]) => {
      seq map { manifest =>

        def isOkToInclude(jsDep: JSDependency): Boolean = {
          jsDep.resourceName != "jquery.js"
        }

        new JSDependencyManifest(
          origin = manifest.origin,
          libDeps = manifest.libDeps filter isOkToInclude,
          requiresDOM = manifest.requiresDOM,
          compliantSemantics = manifest.compliantSemantics
        )
      }
    }
  },
  libraryDependencies ++= Seq(
    "org.seleniumhq.selenium" % "selenium-server" % "2.52.0",
    "org.seleniumhq.selenium" % "selenium-firefox-driver" % "2.52.0",
    "org.scalatest" %%% "scalatest" % "2.2.1" % "test",
    "org.scalatestplus" %%% "play" % "1.4.0-M3" % "test",
    "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % "2.52.0",
    "org.webjars" % "jquery" % "1.11.1",
    jdbc,
    cache,
    ws,
    "com.vmunier" %%% "play-scalajs-scripts" % "0.3.0",
    "org.scala-lang" % "scala-xml" % "2.11.0-M4",
    specs2 % Test
    ),
  // Heroku specific
  herokuAppName in Compile := "your-heroku-app-name",
  herokuSkipSubProjects in Compile := false
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  jsManifestFilter := {
    import org.scalajs.core.tools.jsdep.{JSDependencyManifest, JSDependency}

    (seq: Traversable[JSDependencyManifest]) => {
      seq map { manifest =>

        def isOkToInclude(jsDep: JSDependency): Boolean = {
          jsDep.resourceName != "jquery.js"
        }

        new JSDependencyManifest(
          origin = manifest.origin,
          libDeps = manifest.libDeps filter isOkToInclude,
          requiresDOM = manifest.requiresDOM,
          compliantSemantics = manifest.compliantSemantics
        )
      }
    }
  },
  libraryDependencies ++= Seq(


"org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
    "com.lihaoyi" %%% "scalatags" % "0.5.2",
    "com.lihaoyi" %%% "utest" % "0.3.1",
    "com.github.japgolly.scalacss" %%% "ext-scalatags" % "0.4.0",
    "com.github.japgolly.scalacss" %%% "core" % "0.4.0"


  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

scapegoatVersion := "1.1.0"

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

// for Eclipse users
EclipseKeys.skipParents in ThisBuild := false
// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in (server, Compile))





