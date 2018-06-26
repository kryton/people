
// import de.heikoseeberger.sbtheader.license.GPLv3
import play.twirl.sbt.Import.TwirlKeys


name := """people"""
organization := "com.zilbo"

version := "1.1-SNAPSHOT"

val slickVersion = "3.2.3"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"
lazy val slickGenerate = taskKey[Seq[File]]("slick code generation from an existing database")

libraryDependencies ++= Seq(
  filters,
  ws,
  guice,
  // jdbc,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.typesafe.play" %% "play-slick" % "3.0.1",
  //"com.typesafe.play" %% "play-slick-evolutions" % "3.0.1",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "com.unboundid" % "unboundid-ldapsdk" % "3.2.1",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8",
  "com.sksamuel.scrimage" %% "scrimage-filters" % "2.1.8",
  "org.apache.poi" % "poi" % "3.17",
  "org.apache.poi" % "poi-ooxml" % "3.17",
  "org.apache.poi" % "poi-scratchpad" % "3.17",
  "net.sf.mpxj" % "mpxj" % "7.5.0",
  //"net.sf.mpxj" % "mpxj" % "5.8.0",
  "com.typesafe.play" %% "play-mailer" % "6.0.0",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.0",
  "mysql" % "mysql-connector-java" % "5.1.41",
//  "org.webjars" % "webjars-locator-core" % "0.35",
  "org.webjars" %% "webjars-play" % "2.6.3",
  "com.github.nscala-time" %% "nscala-time" % "2.20.0",
  "org.abstractj.kalium" % "kalium" % "0.4.0",
  "org.webjars" % "x-editable-bootstrap3" % "1.5.1-1",
  //"org.webjars.bower" % "bootstrap-3-datepicker" % "1.5.0",
  "org.webjars.bower" % "gentelella" % "1.4.0",
  "org.webjars" % "font-awesome" % "4.7.0",
  "com.univocity" % "univocity-parsers" % "2.6.4"

)

slickGenerate := {
  import java.io.File
  val dbs = Seq("offline", "project_db")
  val lines = scala.io.Source.fromFile("conf/db.secret").getLines
  val userName = lines.next()
  val password = lines.next()
  val hostnameport = lines.next()
  val jdbcDriver = "com.mysql.jdbc.Driver" // replace if not MySQL
  val slickDriver = "slick.jdbc.MySQLProfile" // replace if not MySQL
  val resultRelativeDir = "app/db" // directory to create output scala slick definitions at
  dbs.map { dbName =>
    val url = s"jdbc:mysql://$hostnameport/$dbName" // adapt as necessary to your system
  val targetPackageName = dbName.replaceAll("_", "") // package name to give it

    (runner in Compile).value.run("slick.codegen.SourceCodeGenerator", (dependencyClasspath in Compile).value.files, Array
    (slickDriver, jdbcDriver, url, resultRelativeDir, targetPackageName, userName, password), streams.value.log)
    //println(s"Result: file://${baseDirectory.value}/$resultFilePath" )

    val resultFilePath = s"$resultRelativeDir/$targetPackageName/Tables.scala" // override the name if you li
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

organizationName := "Ian Holsman"
startYear := Some(2014)
licenses += ("GPL-3.0", new URL("http://www.gnu.org/licenses/gpl-3.0.en.html"))
//headers := Map(
//  "scala" -> GPLv3("2014", "Ian Holsman"),
//  "conf" -> GPLv3("2014", "Ian Holsman", "#"),
//  "html" -> GPLv3("2014", "Ian Holsman", "@*")
// )

//TwirlKeys.compileTemplates  += baseDirectory.value / "internal"
//val internalDir = baseDirectory / "internal"

//sourceDirectory in Compile <<= baseDirectory(_ / "internal")
//sourceDirectories in (Compile, TwirlKeys.compileTemplates) := Seq((sourceDirectory in Compile).value),

sourceDirectories in (Compile,TwirlKeys.compileTemplates)  ++= Seq(baseDirectory.value / "internal")

sourceDirectories in (Compile, TwirlKeys.compileTemplates) ++= (unmanagedSourceDirectories in Compile).value // ++ internalDir.value

// unmanagedSources.in(Compile, createHeaders) ++= sources.in(Compile, TwirlKeys.compileTemplates).value
