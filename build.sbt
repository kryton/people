import de.heikoseeberger.sbtheader.license.GPLv3
import play.twirl.sbt.Import.TwirlKeys

name := """people"""
organization := "com.zilbo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

val slickVersion = "3.2.0"

libraryDependencies ++= Seq(
  filters,
  ws,
  // jdbc,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0-M2" % Test,
  "com.typesafe.play" %% "play-slick" % "3.0.0-M4",
  //"com.typesafe.play" %% "play-slick-evolutions" % "3.0.0-M3",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "com.unboundid" % "unboundid-ldapsdk" % "3.2.1",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8",
  "com.sksamuel.scrimage" %% "scrimage-filters" % "2.1.8",
  "org.apache.poi" % "poi" % "3.15",
  "org.apache.poi" % "poi-ooxml" % "3.15",
  "org.apache.poi" % "poi-scratchpad" % "3.15",
  "com.typesafe.play" %% "play-mailer" % "6.0.0-M1",
//"be.objectify" %% "deadbolt-scala" % "2.5.1",
 // "org.slf4j" % "slf4j-nop" % "1.6.4",
 // "com.typesafe.slick" %% "slick-hikaricp" %  slickVersion
  "mysql" % "mysql-connector-java" % "5.1.41",
  "org.webjars" %% "webjars-play" % "2.6.0-M1",
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",
  //"org.webjars" % "html5shiv" % "3.7.2",
  //"org.webjars" % "requirejs" % "2.1.16",
  //"org.webjars" % "respond" % "1.4.2",
  //"org.webjars" % "bootstrap" % "3.3.7-1",
  //"org.webjars" % "Bootstrap-3-Typeahead" % "3.1.1",
  "org.abstractj.kalium" % "kalium" % "0.4.0",
  "org.webjars" % "x-editable-bootstrap3" % "1.5.1-1",
  //"org.webjars.bower" % "bootstrap-3-datepicker" % "1.5.0",
  "org.webjars.bower" % "gentelella" % "1.4.0",
  "org.webjars" % "font-awesome" % "4.7.0"
  //"org.webjars" % "jquery" % "3.2.0",
  //"org.webjars" % "jquery-ui" % "1.12.1"

)

lazy val slickGenerate = taskKey[Seq[File]]("slick code generation from an existing database")

slickGenerate := {
  import java.io.File
  val dbs = Seq("offline","project_db")
  val lines = scala.io.Source.fromFile("conf/db.secret").getLines
  val userName = lines.next()
  val password = lines.next()
  val hostnameport = lines.next()
  val jdbcDriver = "com.mysql.jdbc.Driver"         // replace if not MySQL
  val slickDriver = "slick.jdbc.MySQLProfile"     // replace if not MySQL
  val resultRelativeDir = "app/db"  // directory to create output scala slick definitions at
  dbs.map { dbName =>
    val url = s"jdbc:mysql://$hostnameport/$dbName" // adapt as necessary to your system
    val targetPackageName =  dbName.replaceAll("_","") // package name to give it

    (runner in Compile).value.run("slick.codegen.SourceCodeGenerator", (dependencyClasspath in Compile).value.files, Array
    (slickDriver, jdbcDriver, url, resultRelativeDir, targetPackageName, userName, password), streams.value.log)
    //println(s"Result: file://${baseDirectory.value}/$resultFilePath" )

    val resultFilePath = s"$resultRelativeDir/$targetPackageName/Tables.scala"              // override the name if you li
    println(s"Slick Generated - $resultFilePath")
    file(resultFilePath)
  }
  Seq.empty
  //Seq(file(resultFilePath))
}
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.zilbo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.zilbo.binders._"

headers := Map(
  "scala" -> GPLv3("2014","Ian Holsman"),
  "conf" -> GPLv3("2014", "Ian Holsman","#"),
  "html" -> GPLv3("2014", "Ian Holsman","@*")
)

unmanagedSources.in(Compile, createHeaders) ++= sources.in(Compile, TwirlKeys.compileTemplates).value
