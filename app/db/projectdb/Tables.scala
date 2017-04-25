package projectdb
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Empefficiency.schema, Featureflag.schema, Managedclient.schema, Managedclientproductfeature.schema, PlayEvolutions.schema, Productfeature.schema, Productfeatureflag.schema, Producttrack.schema, Producttrackfeature.schema, Project.schema, Projectarea.schema, Projectfeature.schema, Projecthighlight.schema, Projectperson.schema, Projectpersontype.schema, Projectrelease.schema, Projectrole.schema, Resourcepool.schema, Resourceteam.schema, Resourceteamproductfeature.schema, Stage.schema, Statuscolor.schema, Statusupdate.schema, Systemrole.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Empefficiency
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param month Database column month SqlType(INT)
   *  @param efficiency Database column efficiency SqlType(DECIMAL), Default(None) */
  final case class EmpefficiencyRow(id: Long, month: Int, efficiency: Option[scala.math.BigDecimal] = None)
  /** GetResult implicit for fetching EmpefficiencyRow objects using plain SQL queries */
  implicit def GetResultEmpefficiencyRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Option[scala.math.BigDecimal]]): GR[EmpefficiencyRow] = GR{
    prs => import prs._
    EmpefficiencyRow.tupled((<<[Long], <<[Int], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table EmpEfficiency. Objects of this class serve as prototypes for rows in queries. */
  class Empefficiency(_tableTag: Tag) extends profile.api.Table[EmpefficiencyRow](_tableTag, Some("project_db"), "EmpEfficiency") {
    def * = (id, month, efficiency) <> (EmpefficiencyRow.tupled, EmpefficiencyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(month), efficiency).shaped.<>({r=>import r._; _1.map(_=> EmpefficiencyRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column month SqlType(INT) */
    val month: Rep[Int] = column[Int]("month")
    /** Database column efficiency SqlType(DECIMAL), Default(None) */
    val efficiency: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("efficiency", O.Default(None))

    /** Uniqueness Index over (month) (database name month) */
    val index1 = index("month", month, unique=true)
  }
  /** Collection-like TableQuery object for table Empefficiency */
  lazy val Empefficiency = new TableQuery(tag => new Empefficiency(tag))

  /** Entity class storing rows of table Featureflag
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0) */
  final case class FeatureflagRow(id: Int, name: String, ordering: Int = 0)
  /** GetResult implicit for fetching FeatureflagRow objects using plain SQL queries */
  implicit def GetResultFeatureflagRow(implicit e0: GR[Int], e1: GR[String]): GR[FeatureflagRow] = GR{
    prs => import prs._
    FeatureflagRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table featureflag. Objects of this class serve as prototypes for rows in queries. */
  class Featureflag(_tableTag: Tag) extends profile.api.Table[FeatureflagRow](_tableTag, Some("project_db"), "featureflag") {
    def * = (id, name, ordering) <> (FeatureflagRow.tupled, FeatureflagRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> FeatureflagRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("ordering", O.Default(0))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Featureflag */
  lazy val Featureflag = new TableQuery(tag => new Featureflag(tag))

  /** Entity class storing rows of table Managedclient
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param ismanaged Database column isManaged SqlType(BIT)
   *  @param msprojectname Database column msprojectname SqlType(VARCHAR), Length(30,true), Default(None) */
  final case class ManagedclientRow(id: Int, name: String, ismanaged: Option[Boolean], msprojectname: Option[String] = None)
  /** GetResult implicit for fetching ManagedclientRow objects using plain SQL queries */
  implicit def GetResultManagedclientRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Boolean]], e3: GR[Option[String]]): GR[ManagedclientRow] = GR{
    prs => import prs._
    ManagedclientRow.tupled((<<[Int], <<[String], <<?[Boolean], <<?[String]))
  }
  /** Table description of table managedclient. Objects of this class serve as prototypes for rows in queries. */
  class Managedclient(_tableTag: Tag) extends profile.api.Table[ManagedclientRow](_tableTag, Some("project_db"), "managedclient") {
    def * = (id, name, ismanaged, msprojectname) <> (ManagedclientRow.tupled, ManagedclientRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), ismanaged, msprojectname).shaped.<>({r=>import r._; _1.map(_=> ManagedclientRow.tupled((_1.get, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column isManaged SqlType(BIT) */
    val ismanaged: Rep[Option[Boolean]] = column[Option[Boolean]]("isManaged")
    /** Database column msprojectname SqlType(VARCHAR), Length(30,true), Default(None) */
    val msprojectname: Rep[Option[String]] = column[Option[String]]("msprojectname", O.Length(30,varying=true), O.Default(None))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Managedclient */
  lazy val Managedclient = new TableQuery(tag => new Managedclient(tag))

  /** Entity class storing rows of table Managedclientproductfeature
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param productfeatureid Database column productFeatureId SqlType(INT)
   *  @param managedclientid Database column managedClientId SqlType(INT)
   *  @param allocation Database column allocation SqlType(DECIMAL UNSIGNED), Default(1.00) */
  final case class ManagedclientproductfeatureRow(id: Int, productfeatureid: Int, managedclientid: Int, allocation: scala.math.BigDecimal = scala.math.BigDecimal("1.00"))
  /** GetResult implicit for fetching ManagedclientproductfeatureRow objects using plain SQL queries */
  implicit def GetResultManagedclientproductfeatureRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal]): GR[ManagedclientproductfeatureRow] = GR{
    prs => import prs._
    ManagedclientproductfeatureRow.tupled((<<[Int], <<[Int], <<[Int], <<[scala.math.BigDecimal]))
  }
  /** Table description of table managedclientproductfeature. Objects of this class serve as prototypes for rows in queries. */
  class Managedclientproductfeature(_tableTag: Tag) extends profile.api.Table[ManagedclientproductfeatureRow](_tableTag, Some("project_db"), "managedclientproductfeature") {
    def * = (id, productfeatureid, managedclientid, allocation) <> (ManagedclientproductfeatureRow.tupled, ManagedclientproductfeatureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(productfeatureid), Rep.Some(managedclientid), Rep.Some(allocation)).shaped.<>({r=>import r._; _1.map(_=> ManagedclientproductfeatureRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column productFeatureId SqlType(INT) */
    val productfeatureid: Rep[Int] = column[Int]("productFeatureId")
    /** Database column managedClientId SqlType(INT) */
    val managedclientid: Rep[Int] = column[Int]("managedClientId")
    /** Database column allocation SqlType(DECIMAL UNSIGNED), Default(1.00) */
    val allocation: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("allocation", O.Default(scala.math.BigDecimal("1.00")))

    /** Foreign key referencing Managedclient (database name ManagedClientProductFeature_ibfk_2) */
    lazy val managedclientFk = foreignKey("ManagedClientProductFeature_ibfk_2", managedclientid, Managedclient)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Productfeature (database name ManagedClientProductFeature_ibfk_1) */
    lazy val productfeatureFk = foreignKey("ManagedClientProductFeature_ibfk_1", productfeatureid, Productfeature)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Managedclientproductfeature */
  lazy val Managedclientproductfeature = new TableQuery(tag => new Managedclientproductfeature(tag))

  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param hash Database column hash SqlType(VARCHAR), Length(255,true)
   *  @param appliedAt Database column applied_at SqlType(TIMESTAMP)
   *  @param applyScript Database column apply_script SqlType(TEXT), Default(None)
   *  @param revertScript Database column revert_script SqlType(TEXT), Default(None)
   *  @param state Database column state SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem SqlType(TEXT), Default(None) */
  final case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends profile.api.Table[PlayEvolutionsRow](_tableTag, Some("project_db"), "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(VARCHAR), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at SqlType(TIMESTAMP) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script SqlType(TEXT), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Default(None))
    /** Database column revert_script SqlType(TEXT), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Default(None))
    /** Database column state SqlType(VARCHAR), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem SqlType(TEXT), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table Productfeature
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(200,true)
   *  @param execsummary Database column execSummary SqlType(TEXT)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0)
   *  @param stageid Database column stageId SqlType(INT), Default(1)
   *  @param msprojectname Database column msprojectname SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param isactive Database column isActive SqlType(BIT)
   *  @param iscid Database column isCID SqlType(BIT)
   *  @param isanchor Database column isAnchor SqlType(BIT)
   *  @param mspriority Database column msPriority SqlType(INT), Default(Some(0))
   *  @param start Database column start SqlType(DATE), Default(None)
   *  @param finish Database column finish SqlType(DATE), Default(None) */
  final case class ProductfeatureRow(id: Int, name: String, execsummary: String, ordering: Int = 0, stageid: Int = 1, msprojectname: Option[String] = None, isactive: Option[Boolean], iscid: Option[Boolean], isanchor: Option[Boolean], mspriority: Option[Int] = Some(0), start: Option[java.sql.Date] = None, finish: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching ProductfeatureRow objects using plain SQL queries */
  implicit def GetResultProductfeatureRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Boolean]], e4: GR[Option[Int]], e5: GR[Option[java.sql.Date]]): GR[ProductfeatureRow] = GR{
    prs => import prs._
    ProductfeatureRow.tupled((<<[Int], <<[String], <<[String], <<[Int], <<[Int], <<?[String], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Int], <<?[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table productfeature. Objects of this class serve as prototypes for rows in queries. */
  class Productfeature(_tableTag: Tag) extends profile.api.Table[ProductfeatureRow](_tableTag, Some("project_db"), "productfeature") {
    def * = (id, name, execsummary, ordering, stageid, msprojectname, isactive, iscid, isanchor, mspriority, start, finish) <> (ProductfeatureRow.tupled, ProductfeatureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(execsummary), Rep.Some(ordering), Rep.Some(stageid), msprojectname, isactive, iscid, isanchor, mspriority, start, finish).shaped.<>({r=>import r._; _1.map(_=> ProductfeatureRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column execSummary SqlType(TEXT) */
    val execsummary: Rep[String] = column[String]("execSummary")
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("ordering", O.Default(0))
    /** Database column stageId SqlType(INT), Default(1) */
    val stageid: Rep[Int] = column[Int]("stageId", O.Default(1))
    /** Database column msprojectname SqlType(VARCHAR), Length(200,true), Default(None) */
    val msprojectname: Rep[Option[String]] = column[Option[String]]("msprojectname", O.Length(200,varying=true), O.Default(None))
    /** Database column isActive SqlType(BIT) */
    val isactive: Rep[Option[Boolean]] = column[Option[Boolean]]("isActive")
    /** Database column isCID SqlType(BIT) */
    val iscid: Rep[Option[Boolean]] = column[Option[Boolean]]("isCID")
    /** Database column isAnchor SqlType(BIT) */
    val isanchor: Rep[Option[Boolean]] = column[Option[Boolean]]("isAnchor")
    /** Database column msPriority SqlType(INT), Default(Some(0)) */
    val mspriority: Rep[Option[Int]] = column[Option[Int]]("msPriority", O.Default(Some(0)))
    /** Database column start SqlType(DATE), Default(None) */
    val start: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("start", O.Default(None))
    /** Database column finish SqlType(DATE), Default(None) */
    val finish: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("finish", O.Default(None))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Productfeature */
  lazy val Productfeature = new TableQuery(tag => new Productfeature(tag))

  /** Entity class storing rows of table Productfeatureflag
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param productfeatureid Database column productFeatureId SqlType(INT)
   *  @param featureflagid Database column featureFlagId SqlType(INT) */
  final case class ProductfeatureflagRow(id: Int, productfeatureid: Int, featureflagid: Int)
  /** GetResult implicit for fetching ProductfeatureflagRow objects using plain SQL queries */
  implicit def GetResultProductfeatureflagRow(implicit e0: GR[Int]): GR[ProductfeatureflagRow] = GR{
    prs => import prs._
    ProductfeatureflagRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table productfeatureflag. Objects of this class serve as prototypes for rows in queries. */
  class Productfeatureflag(_tableTag: Tag) extends profile.api.Table[ProductfeatureflagRow](_tableTag, Some("project_db"), "productfeatureflag") {
    def * = (id, productfeatureid, featureflagid) <> (ProductfeatureflagRow.tupled, ProductfeatureflagRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(productfeatureid), Rep.Some(featureflagid)).shaped.<>({r=>import r._; _1.map(_=> ProductfeatureflagRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column productFeatureId SqlType(INT) */
    val productfeatureid: Rep[Int] = column[Int]("productFeatureId")
    /** Database column featureFlagId SqlType(INT) */
    val featureflagid: Rep[Int] = column[Int]("featureFlagId")

    /** Foreign key referencing Featureflag (database name productfeatureflag_ibfk_2) */
    lazy val featureflagFk = foreignKey("productfeatureflag_ibfk_2", featureflagid, Featureflag)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Productfeature (database name productfeatureflag_ibfk_1) */
    lazy val productfeatureFk = foreignKey("productfeatureflag_ibfk_1", productfeatureid, Productfeature)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Productfeatureflag */
  lazy val Productfeatureflag = new TableQuery(tag => new Productfeatureflag(tag))

  /** Entity class storing rows of table Producttrack
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column Ordering SqlType(INT UNSIGNED), Default(0)
   *  @param msprojectname Database column msprojectname SqlType(VARCHAR), Length(30,true), Default(None) */
  final case class ProducttrackRow(id: Int, name: String, ordering: Int = 0, msprojectname: Option[String] = None)
  /** GetResult implicit for fetching ProducttrackRow objects using plain SQL queries */
  implicit def GetResultProducttrackRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ProducttrackRow] = GR{
    prs => import prs._
    ProducttrackRow.tupled((<<[Int], <<[String], <<[Int], <<?[String]))
  }
  /** Table description of table producttrack. Objects of this class serve as prototypes for rows in queries. */
  class Producttrack(_tableTag: Tag) extends profile.api.Table[ProducttrackRow](_tableTag, Some("project_db"), "producttrack") {
    def * = (id, name, ordering, msprojectname) <> (ProducttrackRow.tupled, ProducttrackRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering), msprojectname).shaped.<>({r=>import r._; _1.map(_=> ProducttrackRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column Ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("Ordering", O.Default(0))
    /** Database column msprojectname SqlType(VARCHAR), Length(30,true), Default(None) */
    val msprojectname: Rep[Option[String]] = column[Option[String]]("msprojectname", O.Length(30,varying=true), O.Default(None))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Producttrack */
  lazy val Producttrack = new TableQuery(tag => new Producttrack(tag))

  /** Entity class storing rows of table Producttrackfeature
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param productfeatureid Database column productFeatureId SqlType(INT)
   *  @param producttrackid Database column productTrackId SqlType(INT)
   *  @param allocation Database column allocation SqlType(DECIMAL UNSIGNED)
   *  @param priority Database column priority SqlType(INT) */
  final case class ProducttrackfeatureRow(id: Int, productfeatureid: Int, producttrackid: Int, allocation: scala.math.BigDecimal, priority: Int)
  /** GetResult implicit for fetching ProducttrackfeatureRow objects using plain SQL queries */
  implicit def GetResultProducttrackfeatureRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal]): GR[ProducttrackfeatureRow] = GR{
    prs => import prs._
    ProducttrackfeatureRow.tupled((<<[Int], <<[Int], <<[Int], <<[scala.math.BigDecimal], <<[Int]))
  }
  /** Table description of table producttrackfeature. Objects of this class serve as prototypes for rows in queries. */
  class Producttrackfeature(_tableTag: Tag) extends profile.api.Table[ProducttrackfeatureRow](_tableTag, Some("project_db"), "producttrackfeature") {
    def * = (id, productfeatureid, producttrackid, allocation, priority) <> (ProducttrackfeatureRow.tupled, ProducttrackfeatureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(productfeatureid), Rep.Some(producttrackid), Rep.Some(allocation), Rep.Some(priority)).shaped.<>({r=>import r._; _1.map(_=> ProducttrackfeatureRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column productFeatureId SqlType(INT) */
    val productfeatureid: Rep[Int] = column[Int]("productFeatureId")
    /** Database column productTrackId SqlType(INT) */
    val producttrackid: Rep[Int] = column[Int]("productTrackId")
    /** Database column allocation SqlType(DECIMAL UNSIGNED) */
    val allocation: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("allocation")
    /** Database column priority SqlType(INT) */
    val priority: Rep[Int] = column[Int]("priority")

    /** Foreign key referencing Productfeature (database name producttrackfeature_ibfk_1) */
    lazy val productfeatureFk = foreignKey("producttrackfeature_ibfk_1", productfeatureid, Productfeature)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Producttrack (database name producttrackfeature_ibfk_2) */
    lazy val producttrackFk = foreignKey("producttrackfeature_ibfk_2", producttrackid, Producttrack)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Producttrackfeature */
  lazy val Producttrackfeature = new TableQuery(tag => new Producttrackfeature(tag))

  /** Entity class storing rows of table Project
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param execsummary Database column ExecSummary SqlType(TEXT)
   *  @param currentstatusid Database column currentStatusId SqlType(INT)
   *  @param started Database column started SqlType(DATE), Default(None)
   *  @param finished Database column finished SqlType(DATE), Default(None)
   *  @param isactive Database column isActive SqlType(BIT)
   *  @param productfeatureid Database column productFeatureId SqlType(INT), Default(1) */
  final case class ProjectRow(id: Int, name: String, execsummary: String, currentstatusid: Int, started: Option[java.sql.Date] = None, finished: Option[java.sql.Date] = None, isactive: Boolean, productfeatureid: Int = 1)
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Date]], e3: GR[Boolean]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Int], <<[String], <<[String], <<[Int], <<?[java.sql.Date], <<?[java.sql.Date], <<[Boolean], <<[Int]))
  }
  /** Table description of table project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends profile.api.Table[ProjectRow](_tableTag, Some("project_db"), "project") {
    def * = (id, name, execsummary, currentstatusid, started, finished, isactive, productfeatureid) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(execsummary), Rep.Some(currentstatusid), started, finished, Rep.Some(isactive), Rep.Some(productfeatureid)).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column ExecSummary SqlType(TEXT) */
    val execsummary: Rep[String] = column[String]("ExecSummary")
    /** Database column currentStatusId SqlType(INT) */
    val currentstatusid: Rep[Int] = column[Int]("currentStatusId")
    /** Database column started SqlType(DATE), Default(None) */
    val started: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("started", O.Default(None))
    /** Database column finished SqlType(DATE), Default(None) */
    val finished: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("finished", O.Default(None))
    /** Database column isActive SqlType(BIT) */
    val isactive: Rep[Boolean] = column[Boolean]("isActive")
    /** Database column productFeatureId SqlType(INT), Default(1) */
    val productfeatureid: Rep[Int] = column[Int]("productFeatureId", O.Default(1))

    /** Foreign key referencing Statuscolor (database name Project_ibfk_1) */
    lazy val statuscolorFk = foreignKey("Project_ibfk_1", currentstatusid, Statuscolor)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (name) (database name Name) */
    val index1 = index("Name", name)
  }
  /** Collection-like TableQuery object for table Project */
  lazy val Project = new TableQuery(tag => new Project(tag))

  /** Entity class storing rows of table Projectarea
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param areaid Database column areaId SqlType(INT) */
  final case class ProjectareaRow(id: Int, projectid: Int, areaid: Int)
  /** GetResult implicit for fetching ProjectareaRow objects using plain SQL queries */
  implicit def GetResultProjectareaRow(implicit e0: GR[Int]): GR[ProjectareaRow] = GR{
    prs => import prs._
    ProjectareaRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table projectarea. Objects of this class serve as prototypes for rows in queries. */
  class Projectarea(_tableTag: Tag) extends profile.api.Table[ProjectareaRow](_tableTag, Some("project_db"), "projectarea") {
    def * = (id, projectid, areaid) <> (ProjectareaRow.tupled, ProjectareaRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectid), Rep.Some(areaid)).shaped.<>({r=>import r._; _1.map(_=> ProjectareaRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(INT) */
    val projectid: Rep[Int] = column[Int]("projectId")
    /** Database column areaId SqlType(INT) */
    val areaid: Rep[Int] = column[Int]("areaId")

    /** Foreign key referencing Project (database name ProjectArea_ibfk_1) */
    lazy val projectFk = foreignKey("ProjectArea_ibfk_1", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (areaid) (database name areaId) */
    val index1 = index("areaId", areaid)
  }
  /** Collection-like TableQuery object for table Projectarea */
  lazy val Projectarea = new TableQuery(tag => new Projectarea(tag))

  /** Entity class storing rows of table Projectfeature
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param releaseid Database column releaseId SqlType(INT)
   *  @param parentfeatureid Database column parentFeatureId SqlType(INT), Default(None)
   *  @param feature Database column feature SqlType(VARCHAR), Length(45,true)
   *  @param status Database column status SqlType(INT), Default(None)
   *  @param timeline Database column timeLine SqlType(DATE), Default(None)
   *  @param timelinestring Database column timeLineString SqlType(VARCHAR), Length(45,true), Default(None)
   *  @param hidden Database column hidden SqlType(BIT) */
  final case class ProjectfeatureRow(id: Int, projectid: Int, releaseid: Int, parentfeatureid: Option[Int] = None, feature: String, status: Option[Int] = None, timeline: Option[java.sql.Date] = None, timelinestring: Option[String] = None, hidden: Option[Boolean])
  /** GetResult implicit for fetching ProjectfeatureRow objects using plain SQL queries */
  implicit def GetResultProjectfeatureRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[String], e3: GR[Option[java.sql.Date]], e4: GR[Option[String]], e5: GR[Option[Boolean]]): GR[ProjectfeatureRow] = GR{
    prs => import prs._
    ProjectfeatureRow.tupled((<<[Int], <<[Int], <<[Int], <<?[Int], <<[String], <<?[Int], <<?[java.sql.Date], <<?[String], <<?[Boolean]))
  }
  /** Table description of table projectfeature. Objects of this class serve as prototypes for rows in queries. */
  class Projectfeature(_tableTag: Tag) extends profile.api.Table[ProjectfeatureRow](_tableTag, Some("project_db"), "projectfeature") {
    def * = (id, projectid, releaseid, parentfeatureid, feature, status, timeline, timelinestring, hidden) <> (ProjectfeatureRow.tupled, ProjectfeatureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectid), Rep.Some(releaseid), parentfeatureid, Rep.Some(feature), status, timeline, timelinestring, hidden).shaped.<>({r=>import r._; _1.map(_=> ProjectfeatureRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(INT) */
    val projectid: Rep[Int] = column[Int]("projectId")
    /** Database column releaseId SqlType(INT) */
    val releaseid: Rep[Int] = column[Int]("releaseId")
    /** Database column parentFeatureId SqlType(INT), Default(None) */
    val parentfeatureid: Rep[Option[Int]] = column[Option[Int]]("parentFeatureId", O.Default(None))
    /** Database column feature SqlType(VARCHAR), Length(45,true) */
    val feature: Rep[String] = column[String]("feature", O.Length(45,varying=true))
    /** Database column status SqlType(INT), Default(None) */
    val status: Rep[Option[Int]] = column[Option[Int]]("status", O.Default(None))
    /** Database column timeLine SqlType(DATE), Default(None) */
    val timeline: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("timeLine", O.Default(None))
    /** Database column timeLineString SqlType(VARCHAR), Length(45,true), Default(None) */
    val timelinestring: Rep[Option[String]] = column[Option[String]]("timeLineString", O.Length(45,varying=true), O.Default(None))
    /** Database column hidden SqlType(BIT) */
    val hidden: Rep[Option[Boolean]] = column[Option[Boolean]]("hidden")

    /** Foreign key referencing Project (database name ProjectFeature_ibfk_1) */
    lazy val projectFk = foreignKey("ProjectFeature_ibfk_1", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projectfeature (database name ProjectFeature_ibfk_3) */
    lazy val projectfeatureFk = foreignKey("ProjectFeature_ibfk_3", parentfeatureid, Projectfeature)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projectrelease (database name ProjectFeature_ibfk_2) */
    lazy val projectreleaseFk = foreignKey("ProjectFeature_ibfk_2", releaseid, Projectrelease)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Projectfeature */
  lazy val Projectfeature = new TableQuery(tag => new Projectfeature(tag))

  /** Entity class storing rows of table Projecthighlight
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param updated Database column updated SqlType(DATE)
   *  @param memo Database column memo SqlType(TEXT) */
  final case class ProjecthighlightRow(id: Int, projectid: Int, updated: java.sql.Date, memo: String)
  /** GetResult implicit for fetching ProjecthighlightRow objects using plain SQL queries */
  implicit def GetResultProjecthighlightRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[String]): GR[ProjecthighlightRow] = GR{
    prs => import prs._
    ProjecthighlightRow.tupled((<<[Int], <<[Int], <<[java.sql.Date], <<[String]))
  }
  /** Table description of table projecthighlight. Objects of this class serve as prototypes for rows in queries. */
  class Projecthighlight(_tableTag: Tag) extends profile.api.Table[ProjecthighlightRow](_tableTag, Some("project_db"), "projecthighlight") {
    def * = (id, projectid, updated, memo) <> (ProjecthighlightRow.tupled, ProjecthighlightRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectid), Rep.Some(updated), Rep.Some(memo)).shaped.<>({r=>import r._; _1.map(_=> ProjecthighlightRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(INT) */
    val projectid: Rep[Int] = column[Int]("projectId")
    /** Database column updated SqlType(DATE) */
    val updated: Rep[java.sql.Date] = column[java.sql.Date]("updated")
    /** Database column memo SqlType(TEXT) */
    val memo: Rep[String] = column[String]("memo")

    /** Foreign key referencing Project (database name ProjectHighlight_ibfk_1) */
    lazy val projectFk = foreignKey("ProjectHighlight_ibfk_1", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Projecthighlight */
  lazy val Projecthighlight = new TableQuery(tag => new Projecthighlight(tag))

  /** Entity class storing rows of table Projectperson
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param projectpersontypeid Database column projectPersonTypeId SqlType(INT)
   *  @param alias Database column alias SqlType(VARCHAR), Length(20,true)
   *  @param started Database column started SqlType(DATE), Default(None)
   *  @param finished Database column finished SqlType(DATE), Default(None) */
  final case class ProjectpersonRow(id: Int, projectid: Int, projectpersontypeid: Int, alias: String, started: Option[java.sql.Date] = None, finished: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching ProjectpersonRow objects using plain SQL queries */
  implicit def GetResultProjectpersonRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Date]]): GR[ProjectpersonRow] = GR{
    prs => import prs._
    ProjectpersonRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<?[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table projectperson. Objects of this class serve as prototypes for rows in queries. */
  class Projectperson(_tableTag: Tag) extends profile.api.Table[ProjectpersonRow](_tableTag, Some("project_db"), "projectperson") {
    def * = (id, projectid, projectpersontypeid, alias, started, finished) <> (ProjectpersonRow.tupled, ProjectpersonRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectid), Rep.Some(projectpersontypeid), Rep.Some(alias), started, finished).shaped.<>({r=>import r._; _1.map(_=> ProjectpersonRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(INT) */
    val projectid: Rep[Int] = column[Int]("projectId")
    /** Database column projectPersonTypeId SqlType(INT) */
    val projectpersontypeid: Rep[Int] = column[Int]("projectPersonTypeId")
    /** Database column alias SqlType(VARCHAR), Length(20,true) */
    val alias: Rep[String] = column[String]("alias", O.Length(20,varying=true))
    /** Database column started SqlType(DATE), Default(None) */
    val started: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("started", O.Default(None))
    /** Database column finished SqlType(DATE), Default(None) */
    val finished: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("finished", O.Default(None))

    /** Foreign key referencing Project (database name ProjectPerson_ibfk_1) */
    lazy val projectFk = foreignKey("ProjectPerson_ibfk_1", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projectpersontype (database name ProjectPerson_ibfk_2) */
    lazy val projectpersontypeFk = foreignKey("ProjectPerson_ibfk_2", projectpersontypeid, Projectpersontype)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Projectperson */
  lazy val Projectperson = new TableQuery(tag => new Projectperson(tag))

  /** Entity class storing rows of table Projectpersontype
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0) */
  final case class ProjectpersontypeRow(id: Int, name: String, ordering: Int = 0)
  /** GetResult implicit for fetching ProjectpersontypeRow objects using plain SQL queries */
  implicit def GetResultProjectpersontypeRow(implicit e0: GR[Int], e1: GR[String]): GR[ProjectpersontypeRow] = GR{
    prs => import prs._
    ProjectpersontypeRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table projectpersontype. Objects of this class serve as prototypes for rows in queries. */
  class Projectpersontype(_tableTag: Tag) extends profile.api.Table[ProjectpersontypeRow](_tableTag, Some("project_db"), "projectpersontype") {
    def * = (id, name, ordering) <> (ProjectpersontypeRow.tupled, ProjectpersontypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> ProjectpersontypeRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("ordering", O.Default(0))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Projectpersontype */
  lazy val Projectpersontype = new TableQuery(tag => new Projectpersontype(tag))

  /** Entity class storing rows of table Projectrelease
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param deployed Database column Deployed SqlType(DATE), Default(None) */
  final case class ProjectreleaseRow(id: Int, name: String, deployed: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching ProjectreleaseRow objects using plain SQL queries */
  implicit def GetResultProjectreleaseRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Date]]): GR[ProjectreleaseRow] = GR{
    prs => import prs._
    ProjectreleaseRow.tupled((<<[Int], <<[String], <<?[java.sql.Date]))
  }
  /** Table description of table projectrelease. Objects of this class serve as prototypes for rows in queries. */
  class Projectrelease(_tableTag: Tag) extends profile.api.Table[ProjectreleaseRow](_tableTag, Some("project_db"), "projectrelease") {
    def * = (id, name, deployed) <> (ProjectreleaseRow.tupled, ProjectreleaseRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), deployed).shaped.<>({r=>import r._; _1.map(_=> ProjectreleaseRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column Deployed SqlType(DATE), Default(None) */
    val deployed: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("Deployed", O.Default(None))
  }
  /** Collection-like TableQuery object for table Projectrelease */
  lazy val Projectrelease = new TableQuery(tag => new Projectrelease(tag))

  /** Entity class storing rows of table Projectrole
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param roleid Database column roleId SqlType(INT)
   *  @param updated Database column updated SqlType(DATE)
   *  @param headcount Database column headCount SqlType(DECIMAL), Default(None) */
  final case class ProjectroleRow(id: Int, projectid: Int, roleid: Int, updated: java.sql.Date, headcount: Option[scala.math.BigDecimal] = None)
  /** GetResult implicit for fetching ProjectroleRow objects using plain SQL queries */
  implicit def GetResultProjectroleRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[Option[scala.math.BigDecimal]]): GR[ProjectroleRow] = GR{
    prs => import prs._
    ProjectroleRow.tupled((<<[Int], <<[Int], <<[Int], <<[java.sql.Date], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table projectrole. Objects of this class serve as prototypes for rows in queries. */
  class Projectrole(_tableTag: Tag) extends profile.api.Table[ProjectroleRow](_tableTag, Some("project_db"), "projectrole") {
    def * = (id, projectid, roleid, updated, headcount) <> (ProjectroleRow.tupled, ProjectroleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectid), Rep.Some(roleid), Rep.Some(updated), headcount).shaped.<>({r=>import r._; _1.map(_=> ProjectroleRow.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(INT) */
    val projectid: Rep[Int] = column[Int]("projectId")
    /** Database column roleId SqlType(INT) */
    val roleid: Rep[Int] = column[Int]("roleId")
    /** Database column updated SqlType(DATE) */
    val updated: Rep[java.sql.Date] = column[java.sql.Date]("updated")
    /** Database column headCount SqlType(DECIMAL), Default(None) */
    val headcount: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("headCount", O.Default(None))

    /** Foreign key referencing Project (database name ProjectRole_ibfk_1) */
    lazy val projectFk = foreignKey("ProjectRole_ibfk_1", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Systemrole (database name ProjectRole_ibfk_2) */
    lazy val systemroleFk = foreignKey("ProjectRole_ibfk_2", roleid, Systemrole)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Projectrole */
  lazy val Projectrole = new TableQuery(tag => new Projectrole(tag))

  /** Entity class storing rows of table Resourcepool
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0)
   *  @param poolsize Database column poolsize SqlType(INT UNSIGNED), Default(0) */
  final case class ResourcepoolRow(id: Int, name: String, ordering: Int = 0, poolsize: Int = 0)
  /** GetResult implicit for fetching ResourcepoolRow objects using plain SQL queries */
  implicit def GetResultResourcepoolRow(implicit e0: GR[Int], e1: GR[String]): GR[ResourcepoolRow] = GR{
    prs => import prs._
    ResourcepoolRow.tupled((<<[Int], <<[String], <<[Int], <<[Int]))
  }
  /** Table description of table resourcepool. Objects of this class serve as prototypes for rows in queries. */
  class Resourcepool(_tableTag: Tag) extends profile.api.Table[ResourcepoolRow](_tableTag, Some("project_db"), "resourcepool") {
    def * = (id, name, ordering, poolsize) <> (ResourcepoolRow.tupled, ResourcepoolRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering), Rep.Some(poolsize)).shaped.<>({r=>import r._; _1.map(_=> ResourcepoolRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("ordering", O.Default(0))
    /** Database column poolsize SqlType(INT UNSIGNED), Default(0) */
    val poolsize: Rep[Int] = column[Int]("poolsize", O.Default(0))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Resourcepool */
  lazy val Resourcepool = new TableQuery(tag => new Resourcepool(tag))

  /** Entity class storing rows of table Resourceteam
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0)
   *  @param teamsize Database column teamsize SqlType(INT UNSIGNED), Default(0)
   *  @param resourcepoolid Database column resourcepoolId SqlType(INT), Default(None)
   *  @param minimum Database column minimum SqlType(INT UNSIGNED), Default(0)
   *  @param maximum Database column maximum SqlType(INT UNSIGNED), Default(None)
   *  @param msprojectname Database column msprojectname SqlType(VARCHAR), Length(30,true), Default()
   *  @param pplteamname Database column pplteamname SqlType(VARCHAR), Length(100,true), Default(None) */
  final case class ResourceteamRow(id: Int, name: String, ordering: Int = 0, teamsize: Int = 0, resourcepoolid: Option[Int] = None, minimum: Int = 0, maximum: Option[Int] = None, msprojectname: String = "", pplteamname: Option[String] = None)
  /** GetResult implicit for fetching ResourceteamRow objects using plain SQL queries */
  implicit def GetResultResourceteamRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[Option[String]]): GR[ResourceteamRow] = GR{
    prs => import prs._
    ResourceteamRow.tupled((<<[Int], <<[String], <<[Int], <<[Int], <<?[Int], <<[Int], <<?[Int], <<[String], <<?[String]))
  }
  /** Table description of table resourceteam. Objects of this class serve as prototypes for rows in queries. */
  class Resourceteam(_tableTag: Tag) extends profile.api.Table[ResourceteamRow](_tableTag, Some("project_db"), "resourceteam") {
    def * = (id, name, ordering, teamsize, resourcepoolid, minimum, maximum, msprojectname, pplteamname) <> (ResourceteamRow.tupled, ResourceteamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering), Rep.Some(teamsize), resourcepoolid, Rep.Some(minimum), maximum, Rep.Some(msprojectname), pplteamname).shaped.<>({r=>import r._; _1.map(_=> ResourceteamRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7, _8.get, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("ordering", O.Default(0))
    /** Database column teamsize SqlType(INT UNSIGNED), Default(0) */
    val teamsize: Rep[Int] = column[Int]("teamsize", O.Default(0))
    /** Database column resourcepoolId SqlType(INT), Default(None) */
    val resourcepoolid: Rep[Option[Int]] = column[Option[Int]]("resourcepoolId", O.Default(None))
    /** Database column minimum SqlType(INT UNSIGNED), Default(0) */
    val minimum: Rep[Int] = column[Int]("minimum", O.Default(0))
    /** Database column maximum SqlType(INT UNSIGNED), Default(None) */
    val maximum: Rep[Option[Int]] = column[Option[Int]]("maximum", O.Default(None))
    /** Database column msprojectname SqlType(VARCHAR), Length(30,true), Default() */
    val msprojectname: Rep[String] = column[String]("msprojectname", O.Length(30,varying=true), O.Default(""))
    /** Database column pplteamname SqlType(VARCHAR), Length(100,true), Default(None) */
    val pplteamname: Rep[Option[String]] = column[Option[String]]("pplteamname", O.Length(100,varying=true), O.Default(None))

    /** Foreign key referencing Resourcepool (database name resourceteam_ibfk_1) */
    lazy val resourcepoolFk = foreignKey("resourceteam_ibfk_1", resourcepoolid, Resourcepool)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Resourceteam */
  lazy val Resourceteam = new TableQuery(tag => new Resourceteam(tag))

  /** Entity class storing rows of table Resourceteamproductfeature
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param featuresize Database column featureSize SqlType(INT UNSIGNED), Default(0)
   *  @param maxdevs Database column maxDevs SqlType(INT UNSIGNED), Default(0)
   *  @param resourceteamid Database column resourceTeamId SqlType(INT)
   *  @param productfeatureid Database column productFeatureId SqlType(INT)
   *  @param featuresizeremaining Database column featureSizeRemaining SqlType(DECIMAL), Default(Some(0.00)) */
  final case class ResourceteamproductfeatureRow(id: Int, featuresize: Int = 0, maxdevs: Int = 0, resourceteamid: Int, productfeatureid: Int, featuresizeremaining: Option[scala.math.BigDecimal] = Some(scala.math.BigDecimal("0.00")))
  /** GetResult implicit for fetching ResourceteamproductfeatureRow objects using plain SQL queries */
  implicit def GetResultResourceteamproductfeatureRow(implicit e0: GR[Int], e1: GR[Option[scala.math.BigDecimal]]): GR[ResourceteamproductfeatureRow] = GR{
    prs => import prs._
    ResourceteamproductfeatureRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table resourceteamproductfeature. Objects of this class serve as prototypes for rows in queries. */
  class Resourceteamproductfeature(_tableTag: Tag) extends profile.api.Table[ResourceteamproductfeatureRow](_tableTag, Some("project_db"), "resourceteamproductfeature") {
    def * = (id, featuresize, maxdevs, resourceteamid, productfeatureid, featuresizeremaining) <> (ResourceteamproductfeatureRow.tupled, ResourceteamproductfeatureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(featuresize), Rep.Some(maxdevs), Rep.Some(resourceteamid), Rep.Some(productfeatureid), featuresizeremaining).shaped.<>({r=>import r._; _1.map(_=> ResourceteamproductfeatureRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column featureSize SqlType(INT UNSIGNED), Default(0) */
    val featuresize: Rep[Int] = column[Int]("featureSize", O.Default(0))
    /** Database column maxDevs SqlType(INT UNSIGNED), Default(0) */
    val maxdevs: Rep[Int] = column[Int]("maxDevs", O.Default(0))
    /** Database column resourceTeamId SqlType(INT) */
    val resourceteamid: Rep[Int] = column[Int]("resourceTeamId")
    /** Database column productFeatureId SqlType(INT) */
    val productfeatureid: Rep[Int] = column[Int]("productFeatureId")
    /** Database column featureSizeRemaining SqlType(DECIMAL), Default(Some(0.00)) */
    val featuresizeremaining: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("featureSizeRemaining", O.Default(Some(scala.math.BigDecimal("0.00"))))

    /** Foreign key referencing Productfeature (database name resourceteamproductfeature_ibfk_2) */
    lazy val productfeatureFk = foreignKey("resourceteamproductfeature_ibfk_2", productfeatureid, Productfeature)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Resourceteam (database name resourceteamproductfeature_ibfk_1) */
    lazy val resourceteamFk = foreignKey("resourceteamproductfeature_ibfk_1", resourceteamid, Resourceteam)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Resourceteamproductfeature */
  lazy val Resourceteamproductfeature = new TableQuery(tag => new Resourceteamproductfeature(tag))

  /** Entity class storing rows of table Stage
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0) */
  final case class StageRow(id: Int, name: String, ordering: Int = 0)
  /** GetResult implicit for fetching StageRow objects using plain SQL queries */
  implicit def GetResultStageRow(implicit e0: GR[Int], e1: GR[String]): GR[StageRow] = GR{
    prs => import prs._
    StageRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table stage. Objects of this class serve as prototypes for rows in queries. */
  class Stage(_tableTag: Tag) extends profile.api.Table[StageRow](_tableTag, Some("project_db"), "stage") {
    def * = (id, name, ordering) <> (StageRow.tupled, StageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> StageRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("ordering", O.Default(0))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Stage */
  lazy val Stage = new TableQuery(tag => new Stage(tag))

  /** Entity class storing rows of table Statuscolor
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param cssclassname Database column cssClassName SqlType(VARCHAR), Length(20,true)
   *  @param isontrack Database column IsOnTrack SqlType(BIT)
   *  @param iscomplete Database column IsComplete SqlType(BIT)
   *  @param hasissues Database column HasIssues SqlType(BIT)
   *  @param isatrisk Database column IsAtRisk SqlType(BIT)
   *  @param isonhold Database column IsOnHold SqlType(BIT)
   *  @param hasnotstarted Database column HasNotStarted SqlType(BIT) */
  final case class StatuscolorRow(id: Int, name: String, cssclassname: String, isontrack: Option[Boolean], iscomplete: Option[Boolean], hasissues: Option[Boolean], isatrisk: Option[Boolean], isonhold: Option[Boolean], hasnotstarted: Option[Boolean])
  /** GetResult implicit for fetching StatuscolorRow objects using plain SQL queries */
  implicit def GetResultStatuscolorRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Boolean]]): GR[StatuscolorRow] = GR{
    prs => import prs._
    StatuscolorRow.tupled((<<[Int], <<[String], <<[String], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Boolean]))
  }
  /** Table description of table statuscolor. Objects of this class serve as prototypes for rows in queries. */
  class Statuscolor(_tableTag: Tag) extends profile.api.Table[StatuscolorRow](_tableTag, Some("project_db"), "statuscolor") {
    def * = (id, name, cssclassname, isontrack, iscomplete, hasissues, isatrisk, isonhold, hasnotstarted) <> (StatuscolorRow.tupled, StatuscolorRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(cssclassname), isontrack, iscomplete, hasissues, isatrisk, isonhold, hasnotstarted).shaped.<>({r=>import r._; _1.map(_=> StatuscolorRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column cssClassName SqlType(VARCHAR), Length(20,true) */
    val cssclassname: Rep[String] = column[String]("cssClassName", O.Length(20,varying=true))
    /** Database column IsOnTrack SqlType(BIT) */
    val isontrack: Rep[Option[Boolean]] = column[Option[Boolean]]("IsOnTrack")
    /** Database column IsComplete SqlType(BIT) */
    val iscomplete: Rep[Option[Boolean]] = column[Option[Boolean]]("IsComplete")
    /** Database column HasIssues SqlType(BIT) */
    val hasissues: Rep[Option[Boolean]] = column[Option[Boolean]]("HasIssues")
    /** Database column IsAtRisk SqlType(BIT) */
    val isatrisk: Rep[Option[Boolean]] = column[Option[Boolean]]("IsAtRisk")
    /** Database column IsOnHold SqlType(BIT) */
    val isonhold: Rep[Option[Boolean]] = column[Option[Boolean]]("IsOnHold")
    /** Database column HasNotStarted SqlType(BIT) */
    val hasnotstarted: Rep[Option[Boolean]] = column[Option[Boolean]]("HasNotStarted")

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Statuscolor */
  lazy val Statuscolor = new TableQuery(tag => new Statuscolor(tag))

  /** Entity class storing rows of table Statusupdate
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param statusid Database column statusId SqlType(INT)
   *  @param updated Database column updated SqlType(DATE)
   *  @param comments Database column comments SqlType(TEXT), Default(None)
   *  @param issues Database column issues SqlType(TEXT), Default(None)
   *  @param risks Database column risks SqlType(TEXT), Default(None)
   *  @param dependencies Database column dependencies SqlType(TEXT), Default(None) */
  final case class StatusupdateRow(id: Int, projectid: Int, statusid: Int, updated: java.sql.Date, comments: Option[String] = None, issues: Option[String] = None, risks: Option[String] = None, dependencies: Option[String] = None)
  /** GetResult implicit for fetching StatusupdateRow objects using plain SQL queries */
  implicit def GetResultStatusupdateRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[Option[String]]): GR[StatusupdateRow] = GR{
    prs => import prs._
    StatusupdateRow.tupled((<<[Int], <<[Int], <<[Int], <<[java.sql.Date], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table statusupdate. Objects of this class serve as prototypes for rows in queries. */
  class Statusupdate(_tableTag: Tag) extends profile.api.Table[StatusupdateRow](_tableTag, Some("project_db"), "statusupdate") {
    def * = (id, projectid, statusid, updated, comments, issues, risks, dependencies) <> (StatusupdateRow.tupled, StatusupdateRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectid), Rep.Some(statusid), Rep.Some(updated), comments, issues, risks, dependencies).shaped.<>({r=>import r._; _1.map(_=> StatusupdateRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(INT) */
    val projectid: Rep[Int] = column[Int]("projectId")
    /** Database column statusId SqlType(INT) */
    val statusid: Rep[Int] = column[Int]("statusId")
    /** Database column updated SqlType(DATE) */
    val updated: Rep[java.sql.Date] = column[java.sql.Date]("updated")
    /** Database column comments SqlType(TEXT), Default(None) */
    val comments: Rep[Option[String]] = column[Option[String]]("comments", O.Default(None))
    /** Database column issues SqlType(TEXT), Default(None) */
    val issues: Rep[Option[String]] = column[Option[String]]("issues", O.Default(None))
    /** Database column risks SqlType(TEXT), Default(None) */
    val risks: Rep[Option[String]] = column[Option[String]]("risks", O.Default(None))
    /** Database column dependencies SqlType(TEXT), Default(None) */
    val dependencies: Rep[Option[String]] = column[Option[String]]("dependencies", O.Default(None))

    /** Foreign key referencing Project (database name StatusUpdate_ibfk_1) */
    lazy val projectFk = foreignKey("StatusUpdate_ibfk_1", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Statuscolor (database name StatusUpdate_ibfk_2) */
    lazy val statuscolorFk = foreignKey("StatusUpdate_ibfk_2", statusid, Statuscolor)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Statusupdate */
  lazy val Statusupdate = new TableQuery(tag => new Statusupdate(tag))

  /** Entity class storing rows of table Systemrole
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column Ordering SqlType(INT UNSIGNED), Default(0) */
  final case class SystemroleRow(id: Int, name: String, ordering: Int = 0)
  /** GetResult implicit for fetching SystemroleRow objects using plain SQL queries */
  implicit def GetResultSystemroleRow(implicit e0: GR[Int], e1: GR[String]): GR[SystemroleRow] = GR{
    prs => import prs._
    SystemroleRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table systemrole. Objects of this class serve as prototypes for rows in queries. */
  class Systemrole(_tableTag: Tag) extends profile.api.Table[SystemroleRow](_tableTag, Some("project_db"), "systemrole") {
    def * = (id, name, ordering) <> (SystemroleRow.tupled, SystemroleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> SystemroleRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column Ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Int] = column[Int]("Ordering", O.Default(0))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Systemrole */
  lazy val Systemrole = new TableQuery(tag => new Systemrole(tag))
}
