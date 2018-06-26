package offline
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
  lazy val schema: profile.SchemaDescription = Array(Authpermission.schema, Authpreference.schema, Authrole.schema, Authrolepermission.schema, Authuser.schema, Authuserpreference.schema, Awardnominationto.schema, Businessunit.schema, Corplevel.schema, Costcenter.schema, Empbio.schema, Emphistory.schema, Employeemilestone.schema, Employeeroster.schema, Emppayroll.schema, Emprelations.schema, Emptag.schema, Functionalarea.schema, Gitcommit.schema, Gitissue.schema, Individualarchetype.schema, Individualbusinessunit.schema, Jiraissue.schema, Jiraparentissue.schema, Kudosto.schema, Managerarchetype.schema, Matrixteam.schema, Matrixteammember.schema, Milestone.schema, Office.schema, Okrkeyresult.schema, Okrobjective.schema, PlayEvolutions.schema, Positiontype.schema, Profitcenter.schema, Ratecard.schema, Ratecardrate.schema, Ratecardrole.schema, Resourcepool.schema, Resourcepoolteam.schema, Scenario.schema, Scenariodetail.schema, Scenariolevel.schema, Surveyanswer.schema, Surveycategory.schema, Surveyquestion.schema, Surveyset.schema, Surveysetinstance.schema, Surveysetperson.schema, Teamdescription.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Authpermission
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param permission Database column permission SqlType(VARCHAR), Length(20,true)
   *  @param description Database column description SqlType(TEXT), Default(None) */
  case class AuthpermissionRow(id: Long, permission: String, description: Option[String] = None)
  /** GetResult implicit for fetching AuthpermissionRow objects using plain SQL queries */
  implicit def GetResultAuthpermissionRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]]): GR[AuthpermissionRow] = GR{
    prs => import prs._
    AuthpermissionRow.tupled((<<[Long], <<[String], <<?[String]))
  }
  /** Table description of table authpermission. Objects of this class serve as prototypes for rows in queries. */
  class Authpermission(_tableTag: Tag) extends profile.api.Table[AuthpermissionRow](_tableTag, Some("offline"), "authpermission") {
    def * = (id, permission, description) <> (AuthpermissionRow.tupled, AuthpermissionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(permission), description).shaped.<>({r=>import r._; _1.map(_=> AuthpermissionRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column permission SqlType(VARCHAR), Length(20,true) */
    val permission: Rep[String] = column[String]("permission", O.Length(20,varying=true))
    /** Database column description SqlType(TEXT), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))

    /** Uniqueness Index over (permission) (database name permission) */
    val index1 = index("permission", permission, unique=true)
  }
  /** Collection-like TableQuery object for table Authpermission */
  lazy val Authpermission = new TableQuery(tag => new Authpermission(tag))

  /** Entity class storing rows of table Authpreference
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(100,true)
   *  @param code Database column code SqlType(VARCHAR), Length(10,true) */
  case class AuthpreferenceRow(id: Long, name: String, code: String)
  /** GetResult implicit for fetching AuthpreferenceRow objects using plain SQL queries */
  implicit def GetResultAuthpreferenceRow(implicit e0: GR[Long], e1: GR[String]): GR[AuthpreferenceRow] = GR{
    prs => import prs._
    AuthpreferenceRow.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table authpreference. Objects of this class serve as prototypes for rows in queries. */
  class Authpreference(_tableTag: Tag) extends profile.api.Table[AuthpreferenceRow](_tableTag, Some("offline"), "authpreference") {
    def * = (id, name, code) <> (AuthpreferenceRow.tupled, AuthpreferenceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(code)).shaped.<>({r=>import r._; _1.map(_=> AuthpreferenceRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column code SqlType(VARCHAR), Length(10,true) */
    val code: Rep[String] = column[String]("code", O.Length(10,varying=true))

    /** Uniqueness Index over (code) (database name code) */
    val index1 = index("code", code, unique=true)
    /** Uniqueness Index over (name) (database name name) */
    val index2 = index("name", name, unique=true)
  }
  /** Collection-like TableQuery object for table Authpreference */
  lazy val Authpreference = new TableQuery(tag => new Authpreference(tag))

  /** Entity class storing rows of table Authrole
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param role Database column role SqlType(VARCHAR), Length(20,true)
   *  @param description Database column description SqlType(TEXT), Default(None)
   *  @param isadmin Database column isAdmin SqlType(BIT), Default(false) */
  case class AuthroleRow(id: Long, role: String, description: Option[String] = None, isadmin: Boolean = false)
  /** GetResult implicit for fetching AuthroleRow objects using plain SQL queries */
  implicit def GetResultAuthroleRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Boolean]): GR[AuthroleRow] = GR{
    prs => import prs._
    AuthroleRow.tupled((<<[Long], <<[String], <<?[String], <<[Boolean]))
  }
  /** Table description of table authrole. Objects of this class serve as prototypes for rows in queries. */
  class Authrole(_tableTag: Tag) extends profile.api.Table[AuthroleRow](_tableTag, Some("offline"), "authrole") {
    def * = (id, role, description, isadmin) <> (AuthroleRow.tupled, AuthroleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(role), description, Rep.Some(isadmin)).shaped.<>({r=>import r._; _1.map(_=> AuthroleRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column role SqlType(VARCHAR), Length(20,true) */
    val role: Rep[String] = column[String]("role", O.Length(20,varying=true))
    /** Database column description SqlType(TEXT), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column isAdmin SqlType(BIT), Default(false) */
    val isadmin: Rep[Boolean] = column[Boolean]("isAdmin", O.Default(false))

    /** Uniqueness Index over (role) (database name role) */
    val index1 = index("role", role, unique=true)
  }
  /** Collection-like TableQuery object for table Authrole */
  lazy val Authrole = new TableQuery(tag => new Authrole(tag))

  /** Entity class storing rows of table Authrolepermission
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param permissionid Database column permissionID SqlType(BIGINT)
   *  @param roleid Database column roleID SqlType(BIGINT) */
  case class AuthrolepermissionRow(id: Long, permissionid: Long, roleid: Long)
  /** GetResult implicit for fetching AuthrolepermissionRow objects using plain SQL queries */
  implicit def GetResultAuthrolepermissionRow(implicit e0: GR[Long]): GR[AuthrolepermissionRow] = GR{
    prs => import prs._
    AuthrolepermissionRow.tupled((<<[Long], <<[Long], <<[Long]))
  }
  /** Table description of table authrolepermission. Objects of this class serve as prototypes for rows in queries. */
  class Authrolepermission(_tableTag: Tag) extends profile.api.Table[AuthrolepermissionRow](_tableTag, Some("offline"), "authrolepermission") {
    def * = (id, permissionid, roleid) <> (AuthrolepermissionRow.tupled, AuthrolepermissionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(permissionid), Rep.Some(roleid)).shaped.<>({r=>import r._; _1.map(_=> AuthrolepermissionRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column permissionID SqlType(BIGINT) */
    val permissionid: Rep[Long] = column[Long]("permissionID")
    /** Database column roleID SqlType(BIGINT) */
    val roleid: Rep[Long] = column[Long]("roleID")

    /** Index over (permissionid) (database name permission) */
    val index1 = index("permission", permissionid)
    /** Index over (roleid) (database name role) */
    val index2 = index("role", roleid)
  }
  /** Collection-like TableQuery object for table Authrolepermission */
  lazy val Authrolepermission = new TableQuery(tag => new Authrolepermission(tag))

  /** Entity class storing rows of table Authuser
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(VARCHAR), Length(20,true)
   *  @param roleid Database column roleID SqlType(BIGINT) */
  case class AuthuserRow(id: Long, username: String, roleid: Long)
  /** GetResult implicit for fetching AuthuserRow objects using plain SQL queries */
  implicit def GetResultAuthuserRow(implicit e0: GR[Long], e1: GR[String]): GR[AuthuserRow] = GR{
    prs => import prs._
    AuthuserRow.tupled((<<[Long], <<[String], <<[Long]))
  }
  /** Table description of table authuser. Objects of this class serve as prototypes for rows in queries. */
  class Authuser(_tableTag: Tag) extends profile.api.Table[AuthuserRow](_tableTag, Some("offline"), "authuser") {
    def * = (id, username, roleid) <> (AuthuserRow.tupled, AuthuserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(username), Rep.Some(roleid)).shaped.<>({r=>import r._; _1.map(_=> AuthuserRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(VARCHAR), Length(20,true) */
    val username: Rep[String] = column[String]("username", O.Length(20,varying=true))
    /** Database column roleID SqlType(BIGINT) */
    val roleid: Rep[Long] = column[Long]("roleID")

    /** Index over (roleid) (database name role) */
    val index1 = index("role", roleid)
    /** Index over (username) (database name user) */
    val index2 = index("user", username)
  }
  /** Collection-like TableQuery object for table Authuser */
  lazy val Authuser = new TableQuery(tag => new Authuser(tag))

  /** Entity class storing rows of table Authuserpreference
   *  @param authpreferenceid Database column authPreferenceid SqlType(BIGINT), Default(0)
   *  @param login Database column login SqlType(VARCHAR), Length(20,true) */
  case class AuthuserpreferenceRow(authpreferenceid: Long = 0L, login: String)
  /** GetResult implicit for fetching AuthuserpreferenceRow objects using plain SQL queries */
  implicit def GetResultAuthuserpreferenceRow(implicit e0: GR[Long], e1: GR[String]): GR[AuthuserpreferenceRow] = GR{
    prs => import prs._
    AuthuserpreferenceRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table authuserpreference. Objects of this class serve as prototypes for rows in queries. */
  class Authuserpreference(_tableTag: Tag) extends profile.api.Table[AuthuserpreferenceRow](_tableTag, Some("offline"), "authuserpreference") {
    def * = (authpreferenceid, login) <> (AuthuserpreferenceRow.tupled, AuthuserpreferenceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(authpreferenceid), Rep.Some(login)).shaped.<>({r=>import r._; _1.map(_=> AuthuserpreferenceRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column authPreferenceid SqlType(BIGINT), Default(0) */
    val authpreferenceid: Rep[Long] = column[Long]("authPreferenceid", O.Default(0L))
    /** Database column login SqlType(VARCHAR), Length(20,true) */
    val login: Rep[String] = column[String]("login", O.Length(20,varying=true))

    /** Primary key of Authuserpreference (database name authuserpreference_PK) */
    val pk = primaryKey("authuserpreference_PK", (authpreferenceid, login))

    /** Foreign key referencing Authpreference (database name AuthUserPreference_ibfk_1) */
    lazy val authpreferenceFk = foreignKey("AuthUserPreference_ibfk_1", authpreferenceid, Authpreference)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Authuserpreference */
  lazy val Authuserpreference = new TableQuery(tag => new Authuserpreference(tag))

  /** Entity class storing rows of table Awardnominationto
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fromperson Database column FromPerson SqlType(VARCHAR), Length(254,true)
   *  @param toperson Database column ToPerson SqlType(VARCHAR), Length(254,true)
   *  @param dateadded Database column DateAdded SqlType(DATE)
   *  @param awarded Database column Awarded SqlType(INT)
   *  @param awardactionby Database column AwardActionBy SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param awardactionon Database column AwardActionOn SqlType(DATE), Default(None)
   *  @param managersfeedback Database column ManagersFeedback SqlType(TEXT), Default(None)
   *  @param nominationfeedback Database column nominationFeedback SqlType(TEXT), Default(None)
   *  @param hrapproved Database column HRApproved SqlType(INT)
   *  @param hractionby Database column HRActionBy SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param hractionon Database column HRActionOn SqlType(DATE), Default(None)
   *  @param rejected Database column Rejected SqlType(BIT)
   *  @param rejectedby Database column RejectedBy SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param rejectedon Database column RejectedOn SqlType(DATE), Default(None)
   *  @param rejectedreason Database column RejectedReason SqlType(VARCHAR), Length(254,true), Default(None) */
  case class AwardnominationtoRow(id: Long, fromperson: String, toperson: String, dateadded: java.sql.Date, awarded: Int, awardactionby: Option[String] = None, awardactionon: Option[java.sql.Date] = None, managersfeedback: Option[String] = None, nominationfeedback: Option[String] = None, hrapproved: Int, hractionby: Option[String] = None, hractionon: Option[java.sql.Date] = None, rejected: Boolean, rejectedby: Option[String] = None, rejectedon: Option[java.sql.Date] = None, rejectedreason: Option[String] = None)
  /** GetResult implicit for fetching AwardnominationtoRow objects using plain SQL queries */
  implicit def GetResultAwardnominationtoRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date], e3: GR[Int], e4: GR[Option[String]], e5: GR[Option[java.sql.Date]], e6: GR[Boolean]): GR[AwardnominationtoRow] = GR{
    prs => import prs._
    AwardnominationtoRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Date], <<[Int], <<?[String], <<?[java.sql.Date], <<?[String], <<?[String], <<[Int], <<?[String], <<?[java.sql.Date], <<[Boolean], <<?[String], <<?[java.sql.Date], <<?[String]))
  }
  /** Table description of table awardnominationto. Objects of this class serve as prototypes for rows in queries. */
  class Awardnominationto(_tableTag: Tag) extends profile.api.Table[AwardnominationtoRow](_tableTag, Some("offline"), "awardnominationto") {
    def * = (id, fromperson, toperson, dateadded, awarded, awardactionby, awardactionon, managersfeedback, nominationfeedback, hrapproved, hractionby, hractionon, rejected, rejectedby, rejectedon, rejectedreason) <> (AwardnominationtoRow.tupled, AwardnominationtoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(fromperson), Rep.Some(toperson), Rep.Some(dateadded), Rep.Some(awarded), awardactionby, awardactionon, managersfeedback, nominationfeedback, Rep.Some(hrapproved), hractionby, hractionon, Rep.Some(rejected), rejectedby, rejectedon, rejectedreason).shaped.<>({r=>import r._; _1.map(_=> AwardnominationtoRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10.get, _11, _12, _13.get, _14, _15, _16)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column FromPerson SqlType(VARCHAR), Length(254,true) */
    val fromperson: Rep[String] = column[String]("FromPerson", O.Length(254,varying=true))
    /** Database column ToPerson SqlType(VARCHAR), Length(254,true) */
    val toperson: Rep[String] = column[String]("ToPerson", O.Length(254,varying=true))
    /** Database column DateAdded SqlType(DATE) */
    val dateadded: Rep[java.sql.Date] = column[java.sql.Date]("DateAdded")
    /** Database column Awarded SqlType(INT) */
    val awarded: Rep[Int] = column[Int]("Awarded")
    /** Database column AwardActionBy SqlType(VARCHAR), Length(254,true), Default(None) */
    val awardactionby: Rep[Option[String]] = column[Option[String]]("AwardActionBy", O.Length(254,varying=true), O.Default(None))
    /** Database column AwardActionOn SqlType(DATE), Default(None) */
    val awardactionon: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("AwardActionOn", O.Default(None))
    /** Database column ManagersFeedback SqlType(TEXT), Default(None) */
    val managersfeedback: Rep[Option[String]] = column[Option[String]]("ManagersFeedback", O.Default(None))
    /** Database column nominationFeedback SqlType(TEXT), Default(None) */
    val nominationfeedback: Rep[Option[String]] = column[Option[String]]("nominationFeedback", O.Default(None))
    /** Database column HRApproved SqlType(INT) */
    val hrapproved: Rep[Int] = column[Int]("HRApproved")
    /** Database column HRActionBy SqlType(VARCHAR), Length(254,true), Default(None) */
    val hractionby: Rep[Option[String]] = column[Option[String]]("HRActionBy", O.Length(254,varying=true), O.Default(None))
    /** Database column HRActionOn SqlType(DATE), Default(None) */
    val hractionon: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("HRActionOn", O.Default(None))
    /** Database column Rejected SqlType(BIT) */
    val rejected: Rep[Boolean] = column[Boolean]("Rejected")
    /** Database column RejectedBy SqlType(VARCHAR), Length(254,true), Default(None) */
    val rejectedby: Rep[Option[String]] = column[Option[String]]("RejectedBy", O.Length(254,varying=true), O.Default(None))
    /** Database column RejectedOn SqlType(DATE), Default(None) */
    val rejectedon: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("RejectedOn", O.Default(None))
    /** Database column RejectedReason SqlType(VARCHAR), Length(254,true), Default(None) */
    val rejectedreason: Rep[Option[String]] = column[Option[String]]("RejectedReason", O.Length(254,varying=true), O.Default(None))

    /** Index over (fromperson) (database name FromPerson) */
    val index1 = index("FromPerson", fromperson)
    /** Index over (toperson) (database name ToPerson) */
    val index2 = index("ToPerson", toperson)
  }
  /** Collection-like TableQuery object for table Awardnominationto */
  lazy val Awardnominationto = new TableQuery(tag => new Awardnominationto(tag))

  /** Entity class storing rows of table Businessunit
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(254,true) */
  case class BusinessunitRow(id: Long, name: String)
  /** GetResult implicit for fetching BusinessunitRow objects using plain SQL queries */
  implicit def GetResultBusinessunitRow(implicit e0: GR[Long], e1: GR[String]): GR[BusinessunitRow] = GR{
    prs => import prs._
    BusinessunitRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table businessunit. Objects of this class serve as prototypes for rows in queries. */
  class Businessunit(_tableTag: Tag) extends profile.api.Table[BusinessunitRow](_tableTag, Some("offline"), "businessunit") {
    def * = (id, name) <> (BusinessunitRow.tupled, BusinessunitRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> BusinessunitRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("name", O.Length(254,varying=true))
  }
  /** Collection-like TableQuery object for table Businessunit */
  lazy val Businessunit = new TableQuery(tag => new Businessunit(tag))

  /** Entity class storing rows of table Corplevel
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param level Database column level SqlType(BIGINT)
   *  @param name Database column name SqlType(VARCHAR), Length(254,true)
   *  @param admin Database column admin SqlType(BIT)
   *  @param ic Database column IC SqlType(BIT), Default(false)
   *  @param nonexempt Database column NonExempt SqlType(BIT), Default(false)
   *  @param archetypeid Database column archetypeID SqlType(BIGINT), Default(None)
   *  @param scope Database column scope SqlType(TEXT), Default(None)
   *  @param complexity Database column complexity SqlType(TEXT), Default(None)
   *  @param supervision Database column supervision SqlType(TEXT), Default(None)
   *  @param knowledge Database column knowledge SqlType(TEXT), Default(None)
   *  @param yearsofexperience Database column yearsOfExperience SqlType(TEXT), Default(None) */
  case class CorplevelRow(id: Long, level: Long, name: String, admin: Boolean, ic: Boolean = false, nonexempt: Boolean = false, archetypeid: Option[Long] = None, scope: Option[String] = None, complexity: Option[String] = None, supervision: Option[String] = None, knowledge: Option[String] = None, yearsofexperience: Option[String] = None)
  /** GetResult implicit for fetching CorplevelRow objects using plain SQL queries */
  implicit def GetResultCorplevelRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Boolean], e3: GR[Option[Long]], e4: GR[Option[String]]): GR[CorplevelRow] = GR{
    prs => import prs._
    CorplevelRow.tupled((<<[Long], <<[Long], <<[String], <<[Boolean], <<[Boolean], <<[Boolean], <<?[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table corplevel. Objects of this class serve as prototypes for rows in queries. */
  class Corplevel(_tableTag: Tag) extends profile.api.Table[CorplevelRow](_tableTag, Some("offline"), "corplevel") {
    def * = (id, level, name, admin, ic, nonexempt, archetypeid, scope, complexity, supervision, knowledge, yearsofexperience) <> (CorplevelRow.tupled, CorplevelRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(level), Rep.Some(name), Rep.Some(admin), Rep.Some(ic), Rep.Some(nonexempt), archetypeid, scope, complexity, supervision, knowledge, yearsofexperience).shaped.<>({r=>import r._; _1.map(_=> CorplevelRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column level SqlType(BIGINT) */
    val level: Rep[Long] = column[Long]("level")
    /** Database column name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("name", O.Length(254,varying=true))
    /** Database column admin SqlType(BIT) */
    val admin: Rep[Boolean] = column[Boolean]("admin")
    /** Database column IC SqlType(BIT), Default(false) */
    val ic: Rep[Boolean] = column[Boolean]("IC", O.Default(false))
    /** Database column NonExempt SqlType(BIT), Default(false) */
    val nonexempt: Rep[Boolean] = column[Boolean]("NonExempt", O.Default(false))
    /** Database column archetypeID SqlType(BIGINT), Default(None) */
    val archetypeid: Rep[Option[Long]] = column[Option[Long]]("archetypeID", O.Default(None))
    /** Database column scope SqlType(TEXT), Default(None) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Default(None))
    /** Database column complexity SqlType(TEXT), Default(None) */
    val complexity: Rep[Option[String]] = column[Option[String]]("complexity", O.Default(None))
    /** Database column supervision SqlType(TEXT), Default(None) */
    val supervision: Rep[Option[String]] = column[Option[String]]("supervision", O.Default(None))
    /** Database column knowledge SqlType(TEXT), Default(None) */
    val knowledge: Rep[Option[String]] = column[Option[String]]("knowledge", O.Default(None))
    /** Database column yearsOfExperience SqlType(TEXT), Default(None) */
    val yearsofexperience: Rep[Option[String]] = column[Option[String]]("yearsOfExperience", O.Default(None))

    /** Foreign key referencing Managerarchetype (database name ARCHETYPE_FK) */
    lazy val managerarchetypeFk = foreignKey("ARCHETYPE_FK", archetypeid, Managerarchetype)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Corplevel */
  lazy val Corplevel = new TableQuery(tag => new Corplevel(tag))

  /** Entity class storing rows of table Costcenter
   *  @param costcenter Database column CostCenter SqlType(BIGINT), PrimaryKey
   *  @param costcentertext Database column CostCenterText SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param account Database column account SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param functionalarea Database column functionalArea SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param businessunit Database column businessUnit SqlType(BIGINT), Default(None)
   *  @param functionalareaid Database column FunctionalAreaID SqlType(BIGINT), Default(None)
   *  @param profitcenterid Database column ProfitCenterID SqlType(BIGINT), Default(None)
   *  @param company Database column company SqlType(VARCHAR), Length(100,true), Default(None) */
  case class CostcenterRow(costcenter: Long, costcentertext: Option[String] = None, account: Option[String] = None, functionalarea: Option[String] = None, businessunit: Option[Long] = None, functionalareaid: Option[Long] = None, profitcenterid: Option[Long] = None, company: Option[String] = None)
  /** GetResult implicit for fetching CostcenterRow objects using plain SQL queries */
  implicit def GetResultCostcenterRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Long]]): GR[CostcenterRow] = GR{
    prs => import prs._
    CostcenterRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[Long], <<?[Long], <<?[Long], <<?[String]))
  }
  /** Table description of table costcenter. Objects of this class serve as prototypes for rows in queries. */
  class Costcenter(_tableTag: Tag) extends profile.api.Table[CostcenterRow](_tableTag, Some("offline"), "costcenter") {
    def * = (costcenter, costcentertext, account, functionalarea, businessunit, functionalareaid, profitcenterid, company) <> (CostcenterRow.tupled, CostcenterRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(costcenter), costcentertext, account, functionalarea, businessunit, functionalareaid, profitcenterid, company).shaped.<>({r=>import r._; _1.map(_=> CostcenterRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column CostCenter SqlType(BIGINT), PrimaryKey */
    val costcenter: Rep[Long] = column[Long]("CostCenter", O.PrimaryKey)
    /** Database column CostCenterText SqlType(VARCHAR), Length(254,true), Default(None) */
    val costcentertext: Rep[Option[String]] = column[Option[String]]("CostCenterText", O.Length(254,varying=true), O.Default(None))
    /** Database column account SqlType(VARCHAR), Length(254,true), Default(None) */
    val account: Rep[Option[String]] = column[Option[String]]("account", O.Length(254,varying=true), O.Default(None))
    /** Database column functionalArea SqlType(VARCHAR), Length(254,true), Default(None) */
    val functionalarea: Rep[Option[String]] = column[Option[String]]("functionalArea", O.Length(254,varying=true), O.Default(None))
    /** Database column businessUnit SqlType(BIGINT), Default(None) */
    val businessunit: Rep[Option[Long]] = column[Option[Long]]("businessUnit", O.Default(None))
    /** Database column FunctionalAreaID SqlType(BIGINT), Default(None) */
    val functionalareaid: Rep[Option[Long]] = column[Option[Long]]("FunctionalAreaID", O.Default(None))
    /** Database column ProfitCenterID SqlType(BIGINT), Default(None) */
    val profitcenterid: Rep[Option[Long]] = column[Option[Long]]("ProfitCenterID", O.Default(None))
    /** Database column company SqlType(VARCHAR), Length(100,true), Default(None) */
    val company: Rep[Option[String]] = column[Option[String]]("company", O.Length(100,varying=true), O.Default(None))

    /** Foreign key referencing Functionalarea (database name CostCenter_ibfk_1) */
    lazy val functionalareaFk = foreignKey("CostCenter_ibfk_1", functionalareaid, Functionalarea)(r => Rep.Some(r.functionalarea), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Profitcenter (database name CostCenter_ibfk_2) */
    lazy val profitcenterFk = foreignKey("CostCenter_ibfk_2", profitcenterid, Profitcenter)(r => Rep.Some(r.profitcenter), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Costcenter */
  lazy val Costcenter = new TableQuery(tag => new Costcenter(tag))

  /** Entity class storing rows of table Empbio
   *  @param login Database column Login SqlType(VARCHAR), PrimaryKey, Length(254,true), Default()
   *  @param bio Database column bio SqlType(TEXT), Default(None)
   *  @param lastupdated Database column lastUpdated SqlType(DATE), Default(None) */
  case class EmpbioRow(login: String = "", bio: Option[String] = None, lastupdated: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching EmpbioRow objects using plain SQL queries */
  implicit def GetResultEmpbioRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Option[java.sql.Date]]): GR[EmpbioRow] = GR{
    prs => import prs._
    EmpbioRow.tupled((<<[String], <<?[String], <<?[java.sql.Date]))
  }
  /** Table description of table empbio. Objects of this class serve as prototypes for rows in queries. */
  class Empbio(_tableTag: Tag) extends profile.api.Table[EmpbioRow](_tableTag, Some("offline"), "empbio") {
    def * = (login, bio, lastupdated) <> (EmpbioRow.tupled, EmpbioRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(login), bio, lastupdated).shaped.<>({r=>import r._; _1.map(_=> EmpbioRow.tupled((_1.get, _2, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Login SqlType(VARCHAR), PrimaryKey, Length(254,true), Default() */
    val login: Rep[String] = column[String]("Login", O.PrimaryKey, O.Length(254,varying=true), O.Default(""))
    /** Database column bio SqlType(TEXT), Default(None) */
    val bio: Rep[Option[String]] = column[Option[String]]("bio", O.Default(None))
    /** Database column lastUpdated SqlType(DATE), Default(None) */
    val lastupdated: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("lastUpdated", O.Default(None))
  }
  /** Collection-like TableQuery object for table Empbio */
  lazy val Empbio = new TableQuery(tag => new Empbio(tag))

  /** Entity class storing rows of table Emphistory
   *  @param personnumber Database column PersonNumber SqlType(BIGINT)
   *  @param login Database column Login SqlType(VARCHAR), PrimaryKey, Length(254,true)
   *  @param firstname Database column Firstname SqlType(VARCHAR), Length(254,true)
   *  @param nickname Database column NickName SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param lastname Database column LastName SqlType(VARCHAR), Length(254,true)
   *  @param managerid Database column ManagerID SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param costcenter Database column CostCenter SqlType(BIGINT)
   *  @param officeid Database column OfficeID SqlType(BIGINT)
   *  @param employeetype Database column EmployeeType SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param hirerehiredate Database column HireRehireDate SqlType(DATE), Default(None)
   *  @param lastseen Database column LastSeen SqlType(DATE), Default(None) */
  case class EmphistoryRow(personnumber: Long, login: String, firstname: String, nickname: Option[String] = None, lastname: String, managerid: Option[String] = None, costcenter: Long, officeid: Long, employeetype: Option[String] = None, hirerehiredate: Option[java.sql.Date] = None, lastseen: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching EmphistoryRow objects using plain SQL queries */
  implicit def GetResultEmphistoryRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[java.sql.Date]]): GR[EmphistoryRow] = GR{
    prs => import prs._
    EmphistoryRow.tupled((<<[Long], <<[String], <<[String], <<?[String], <<[String], <<?[String], <<[Long], <<[Long], <<?[String], <<?[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table emphistory. Objects of this class serve as prototypes for rows in queries. */
  class Emphistory(_tableTag: Tag) extends profile.api.Table[EmphistoryRow](_tableTag, Some("offline"), "emphistory") {
    def * = (personnumber, login, firstname, nickname, lastname, managerid, costcenter, officeid, employeetype, hirerehiredate, lastseen) <> (EmphistoryRow.tupled, EmphistoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(personnumber), Rep.Some(login), Rep.Some(firstname), nickname, Rep.Some(lastname), managerid, Rep.Some(costcenter), Rep.Some(officeid), employeetype, hirerehiredate, lastseen).shaped.<>({r=>import r._; _1.map(_=> EmphistoryRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7.get, _8.get, _9, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PersonNumber SqlType(BIGINT) */
    val personnumber: Rep[Long] = column[Long]("PersonNumber")
    /** Database column Login SqlType(VARCHAR), PrimaryKey, Length(254,true) */
    val login: Rep[String] = column[String]("Login", O.PrimaryKey, O.Length(254,varying=true))
    /** Database column Firstname SqlType(VARCHAR), Length(254,true) */
    val firstname: Rep[String] = column[String]("Firstname", O.Length(254,varying=true))
    /** Database column NickName SqlType(VARCHAR), Length(254,true), Default(None) */
    val nickname: Rep[Option[String]] = column[Option[String]]("NickName", O.Length(254,varying=true), O.Default(None))
    /** Database column LastName SqlType(VARCHAR), Length(254,true) */
    val lastname: Rep[String] = column[String]("LastName", O.Length(254,varying=true))
    /** Database column ManagerID SqlType(VARCHAR), Length(254,true), Default(None) */
    val managerid: Rep[Option[String]] = column[Option[String]]("ManagerID", O.Length(254,varying=true), O.Default(None))
    /** Database column CostCenter SqlType(BIGINT) */
    val costcenter: Rep[Long] = column[Long]("CostCenter")
    /** Database column OfficeID SqlType(BIGINT) */
    val officeid: Rep[Long] = column[Long]("OfficeID")
    /** Database column EmployeeType SqlType(VARCHAR), Length(254,true), Default(None) */
    val employeetype: Rep[Option[String]] = column[Option[String]]("EmployeeType", O.Length(254,varying=true), O.Default(None))
    /** Database column HireRehireDate SqlType(DATE), Default(None) */
    val hirerehiredate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("HireRehireDate", O.Default(None))
    /** Database column LastSeen SqlType(DATE), Default(None) */
    val lastseen: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("LastSeen", O.Default(None))

    /** Foreign key referencing Office (database name EmpHistory_Office_FK) */
    lazy val officeFk = foreignKey("EmpHistory_Office_FK", officeid, Office)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Emphistory */
  lazy val Emphistory = new TableQuery(tag => new Emphistory(tag))

  /** Entity class storing rows of table Employeemilestone
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param milestoneid Database column MilestoneID SqlType(BIGINT)
   *  @param login Database column login SqlType(VARCHAR), Length(20,true)
   *  @param completedon Database column CompletedOn SqlType(DATE)
   *  @param enteredon Database column EnteredOn SqlType(DATE)
   *  @param comments Database column Comments SqlType(TEXT)
   *  @param duration Database column Duration SqlType(INT), Default(Some(0)) */
  case class EmployeemilestoneRow(id: Long, milestoneid: Long, login: String, completedon: java.sql.Date, enteredon: java.sql.Date, comments: String, duration: Option[Int] = Some(0))
  /** GetResult implicit for fetching EmployeemilestoneRow objects using plain SQL queries */
  implicit def GetResultEmployeemilestoneRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date], e3: GR[Option[Int]]): GR[EmployeemilestoneRow] = GR{
    prs => import prs._
    EmployeemilestoneRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Date], <<[java.sql.Date], <<[String], <<?[Int]))
  }
  /** Table description of table employeemilestone. Objects of this class serve as prototypes for rows in queries. */
  class Employeemilestone(_tableTag: Tag) extends profile.api.Table[EmployeemilestoneRow](_tableTag, Some("offline"), "employeemilestone") {
    def * = (id, milestoneid, login, completedon, enteredon, comments, duration) <> (EmployeemilestoneRow.tupled, EmployeemilestoneRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(milestoneid), Rep.Some(login), Rep.Some(completedon), Rep.Some(enteredon), Rep.Some(comments), duration).shaped.<>({r=>import r._; _1.map(_=> EmployeemilestoneRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column MilestoneID SqlType(BIGINT) */
    val milestoneid: Rep[Long] = column[Long]("MilestoneID")
    /** Database column login SqlType(VARCHAR), Length(20,true) */
    val login: Rep[String] = column[String]("login", O.Length(20,varying=true))
    /** Database column CompletedOn SqlType(DATE) */
    val completedon: Rep[java.sql.Date] = column[java.sql.Date]("CompletedOn")
    /** Database column EnteredOn SqlType(DATE) */
    val enteredon: Rep[java.sql.Date] = column[java.sql.Date]("EnteredOn")
    /** Database column Comments SqlType(TEXT) */
    val comments: Rep[String] = column[String]("Comments")
    /** Database column Duration SqlType(INT), Default(Some(0)) */
    val duration: Rep[Option[Int]] = column[Option[Int]]("Duration", O.Default(Some(0)))

    /** Index over (login) (database name login) */
    val index1 = index("login", login)
  }
  /** Collection-like TableQuery object for table Employeemilestone */
  lazy val Employeemilestone = new TableQuery(tag => new Employeemilestone(tag))

  /** Entity class storing rows of table Employeeroster
   *  @param login Database column login SqlType(VARCHAR), PrimaryKey, Length(254,true)
   *  @param corplevelid Database column corpLevelID SqlType(BIGINT) */
  case class EmployeerosterRow(login: String, corplevelid: Long)
  /** GetResult implicit for fetching EmployeerosterRow objects using plain SQL queries */
  implicit def GetResultEmployeerosterRow(implicit e0: GR[String], e1: GR[Long]): GR[EmployeerosterRow] = GR{
    prs => import prs._
    EmployeerosterRow.tupled((<<[String], <<[Long]))
  }
  /** Table description of table employeeroster. Objects of this class serve as prototypes for rows in queries. */
  class Employeeroster(_tableTag: Tag) extends profile.api.Table[EmployeerosterRow](_tableTag, Some("offline"), "employeeroster") {
    def * = (login, corplevelid) <> (EmployeerosterRow.tupled, EmployeerosterRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(login), Rep.Some(corplevelid)).shaped.<>({r=>import r._; _1.map(_=> EmployeerosterRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column login SqlType(VARCHAR), PrimaryKey, Length(254,true) */
    val login: Rep[String] = column[String]("login", O.PrimaryKey, O.Length(254,varying=true))
    /** Database column corpLevelID SqlType(BIGINT) */
    val corplevelid: Rep[Long] = column[Long]("corpLevelID")

    /** Foreign key referencing Corplevel (database name EMPROSTER_FK) */
    lazy val corplevelFk = foreignKey("EMPROSTER_FK", corplevelid, Corplevel)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Employeeroster */
  lazy val Employeeroster = new TableQuery(tag => new Employeeroster(tag))

  /** Entity class storing rows of table Emppayroll
   *  @param login Database column Login SqlType(VARCHAR), PrimaryKey, Length(255,true)
   *  @param crypted Database column crypted SqlType(TEXT) */
  case class EmppayrollRow(login: String, crypted: String)
  /** GetResult implicit for fetching EmppayrollRow objects using plain SQL queries */
  implicit def GetResultEmppayrollRow(implicit e0: GR[String]): GR[EmppayrollRow] = GR{
    prs => import prs._
    EmppayrollRow.tupled((<<[String], <<[String]))
  }
  /** Table description of table emppayroll. Objects of this class serve as prototypes for rows in queries. */
  class Emppayroll(_tableTag: Tag) extends profile.api.Table[EmppayrollRow](_tableTag, Some("offline"), "emppayroll") {
    def * = (login, crypted) <> (EmppayrollRow.tupled, EmppayrollRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(login), Rep.Some(crypted)).shaped.<>({r=>import r._; _1.map(_=> EmppayrollRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column Login SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val login: Rep[String] = column[String]("Login", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column crypted SqlType(TEXT) */
    val crypted: Rep[String] = column[String]("crypted")
  }
  /** Collection-like TableQuery object for table Emppayroll */
  lazy val Emppayroll = new TableQuery(tag => new Emppayroll(tag))

  /** Entity class storing rows of table Emprelations
   *  @param personnumber Database column PersonNumber SqlType(BIGINT)
   *  @param login Database column Login SqlType(VARCHAR), PrimaryKey, Length(254,true), Default()
   *  @param firstname Database column Firstname SqlType(VARCHAR), Length(254,true)
   *  @param nickname Database column NickName SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param lastname Database column LastName SqlType(VARCHAR), Length(254,true)
   *  @param managerid Database column ManagerID SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param directs Database column Directs SqlType(BIGINT)
   *  @param reports Database column Reports SqlType(BIGINT)
   *  @param reportscontractor Database column ReportsContractor SqlType(BIGINT)
   *  @param companycode Database column CompanyCode SqlType(INT)
   *  @param companycodename Database column CompanyCodeName SqlType(VARCHAR), Length(254,true)
   *  @param costcenter Database column CostCenter SqlType(BIGINT)
   *  @param personalarea Database column PersonalArea SqlType(VARCHAR), Length(254,true)
   *  @param personalsubarea Database column PersonalSubArea SqlType(VARCHAR), Length(254,true)
   *  @param employeegroup Database column EmployeeGroup SqlType(VARCHAR), Length(254,true)
   *  @param position Database column Position SqlType(VARCHAR), Length(254,true)
   *  @param agency Database column Agency SqlType(VARCHAR), Length(254,true)
   *  @param executivename Database column ExecutiveName SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param officelocation Database column OfficeLocation SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param officelocation2 Database column OfficeLocation2 SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param officeid Database column OfficeID SqlType(BIGINT), Default(None)
   *  @param employeetype Database column EmployeeType SqlType(VARCHAR), Length(254,true), Default(None) */
  case class EmprelationsRow(personnumber: Long, login: String = "", firstname: String, nickname: Option[String] = None, lastname: String, managerid: Option[String] = None, directs: Long, reports: Long, reportscontractor: Long, companycode: Int, companycodename: String, costcenter: Long, personalarea: String, personalsubarea: String, employeegroup: String, position: String, agency: String, executivename: Option[String] = None, officelocation: Option[String] = None, officelocation2: Option[String] = None, officeid: Option[Long] = None, employeetype: Option[String] = None)
  /** GetResult implicit for fetching EmprelationsRow objects using plain SQL queries */
  implicit def GetResultEmprelationsRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Int], e4: GR[Option[Long]]): GR[EmprelationsRow] = GR{
    prs => import prs._
    EmprelationsRow.tupled((<<[Long], <<[String], <<[String], <<?[String], <<[String], <<?[String], <<[Long], <<[Long], <<[Long], <<[Int], <<[String], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[Long], <<?[String]))
  }
  /** Table description of table emprelations. Objects of this class serve as prototypes for rows in queries. */
  class Emprelations(_tableTag: Tag) extends profile.api.Table[EmprelationsRow](_tableTag, Some("offline"), "emprelations") {
    def * = (personnumber, login, firstname, nickname, lastname, managerid, directs, reports, reportscontractor, companycode, companycodename, costcenter, personalarea, personalsubarea, employeegroup, position, agency, executivename, officelocation, officelocation2, officeid, employeetype) <> (EmprelationsRow.tupled, EmprelationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(personnumber), Rep.Some(login), Rep.Some(firstname), nickname, Rep.Some(lastname), managerid, Rep.Some(directs), Rep.Some(reports), Rep.Some(reportscontractor), Rep.Some(companycode), Rep.Some(companycodename), Rep.Some(costcenter), Rep.Some(personalarea), Rep.Some(personalsubarea), Rep.Some(employeegroup), Rep.Some(position), Rep.Some(agency), executivename, officelocation, officelocation2, officeid, employeetype).shaped.<>({r=>import r._; _1.map(_=> EmprelationsRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get, _18, _19, _20, _21, _22)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PersonNumber SqlType(BIGINT) */
    val personnumber: Rep[Long] = column[Long]("PersonNumber")
    /** Database column Login SqlType(VARCHAR), PrimaryKey, Length(254,true), Default() */
    val login: Rep[String] = column[String]("Login", O.PrimaryKey, O.Length(254,varying=true), O.Default(""))
    /** Database column Firstname SqlType(VARCHAR), Length(254,true) */
    val firstname: Rep[String] = column[String]("Firstname", O.Length(254,varying=true))
    /** Database column NickName SqlType(VARCHAR), Length(254,true), Default(None) */
    val nickname: Rep[Option[String]] = column[Option[String]]("NickName", O.Length(254,varying=true), O.Default(None))
    /** Database column LastName SqlType(VARCHAR), Length(254,true) */
    val lastname: Rep[String] = column[String]("LastName", O.Length(254,varying=true))
    /** Database column ManagerID SqlType(VARCHAR), Length(254,true), Default(None) */
    val managerid: Rep[Option[String]] = column[Option[String]]("ManagerID", O.Length(254,varying=true), O.Default(None))
    /** Database column Directs SqlType(BIGINT) */
    val directs: Rep[Long] = column[Long]("Directs")
    /** Database column Reports SqlType(BIGINT) */
    val reports: Rep[Long] = column[Long]("Reports")
    /** Database column ReportsContractor SqlType(BIGINT) */
    val reportscontractor: Rep[Long] = column[Long]("ReportsContractor")
    /** Database column CompanyCode SqlType(INT) */
    val companycode: Rep[Int] = column[Int]("CompanyCode")
    /** Database column CompanyCodeName SqlType(VARCHAR), Length(254,true) */
    val companycodename: Rep[String] = column[String]("CompanyCodeName", O.Length(254,varying=true))
    /** Database column CostCenter SqlType(BIGINT) */
    val costcenter: Rep[Long] = column[Long]("CostCenter")
    /** Database column PersonalArea SqlType(VARCHAR), Length(254,true) */
    val personalarea: Rep[String] = column[String]("PersonalArea", O.Length(254,varying=true))
    /** Database column PersonalSubArea SqlType(VARCHAR), Length(254,true) */
    val personalsubarea: Rep[String] = column[String]("PersonalSubArea", O.Length(254,varying=true))
    /** Database column EmployeeGroup SqlType(VARCHAR), Length(254,true) */
    val employeegroup: Rep[String] = column[String]("EmployeeGroup", O.Length(254,varying=true))
    /** Database column Position SqlType(VARCHAR), Length(254,true) */
    val position: Rep[String] = column[String]("Position", O.Length(254,varying=true))
    /** Database column Agency SqlType(VARCHAR), Length(254,true) */
    val agency: Rep[String] = column[String]("Agency", O.Length(254,varying=true))
    /** Database column ExecutiveName SqlType(VARCHAR), Length(254,true), Default(None) */
    val executivename: Rep[Option[String]] = column[Option[String]]("ExecutiveName", O.Length(254,varying=true), O.Default(None))
    /** Database column OfficeLocation SqlType(VARCHAR), Length(254,true), Default(None) */
    val officelocation: Rep[Option[String]] = column[Option[String]]("OfficeLocation", O.Length(254,varying=true), O.Default(None))
    /** Database column OfficeLocation2 SqlType(VARCHAR), Length(254,true), Default(None) */
    val officelocation2: Rep[Option[String]] = column[Option[String]]("OfficeLocation2", O.Length(254,varying=true), O.Default(None))
    /** Database column OfficeID SqlType(BIGINT), Default(None) */
    val officeid: Rep[Option[Long]] = column[Option[Long]]("OfficeID", O.Default(None))
    /** Database column EmployeeType SqlType(VARCHAR), Length(254,true), Default(None) */
    val employeetype: Rep[Option[String]] = column[Option[String]]("EmployeeType", O.Length(254,varying=true), O.Default(None))

    /** Foreign key referencing Costcenter (database name CostCenter_FK) */
    lazy val costcenterFk = foreignKey("CostCenter_FK", costcenter, Costcenter)(r => r.costcenter, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Office (database name EMPOFFICE_FK) */
    lazy val officeFk = foreignKey("EMPOFFICE_FK", officeid, Office)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Emprelations */
  lazy val Emprelations = new TableQuery(tag => new Emprelations(tag))

  /** Entity class storing rows of table Emptag
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param login Database column Login SqlType(VARCHAR), Length(254,true)
   *  @param dateadded Database column DateAdded SqlType(DATE)
   *  @param tagtext Database column TagText SqlType(VARCHAR), Length(254,true) */
  case class EmptagRow(id: Long, login: String, dateadded: java.sql.Date, tagtext: String)
  /** GetResult implicit for fetching EmptagRow objects using plain SQL queries */
  implicit def GetResultEmptagRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date]): GR[EmptagRow] = GR{
    prs => import prs._
    EmptagRow.tupled((<<[Long], <<[String], <<[java.sql.Date], <<[String]))
  }
  /** Table description of table emptag. Objects of this class serve as prototypes for rows in queries. */
  class Emptag(_tableTag: Tag) extends profile.api.Table[EmptagRow](_tableTag, Some("offline"), "emptag") {
    def * = (id, login, dateadded, tagtext) <> (EmptagRow.tupled, EmptagRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), Rep.Some(dateadded), Rep.Some(tagtext)).shaped.<>({r=>import r._; _1.map(_=> EmptagRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("Login", O.Length(254,varying=true))
    /** Database column DateAdded SqlType(DATE) */
    val dateadded: Rep[java.sql.Date] = column[java.sql.Date]("DateAdded")
    /** Database column TagText SqlType(VARCHAR), Length(254,true) */
    val tagtext: Rep[String] = column[String]("TagText", O.Length(254,varying=true))

    /** Index over (login) (database name EMP_Login) */
    val index1 = index("EMP_Login", login)
    /** Index over (tagtext) (database name EMP_TAGTEXT) */
    val index2 = index("EMP_TAGTEXT", tagtext)
  }
  /** Collection-like TableQuery object for table Emptag */
  lazy val Emptag = new TableQuery(tag => new Emptag(tag))

  /** Entity class storing rows of table Functionalarea
   *  @param functionalarea Database column functionalArea SqlType(BIGINT), PrimaryKey
   *  @param department Database column department SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param shortname Database column shortname SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param pandlcategory Database column PandLCategory SqlType(VARCHAR), Length(10,true), Default(None)
   *  @param company Database column company SqlType(VARCHAR), Length(100,true), Default(None) */
  case class FunctionalareaRow(functionalarea: Long, department: Option[String] = None, shortname: Option[String] = None, pandlcategory: Option[String] = None, company: Option[String] = None)
  /** GetResult implicit for fetching FunctionalareaRow objects using plain SQL queries */
  implicit def GetResultFunctionalareaRow(implicit e0: GR[Long], e1: GR[Option[String]]): GR[FunctionalareaRow] = GR{
    prs => import prs._
    FunctionalareaRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table functionalarea. Objects of this class serve as prototypes for rows in queries. */
  class Functionalarea(_tableTag: Tag) extends profile.api.Table[FunctionalareaRow](_tableTag, Some("offline"), "functionalarea") {
    def * = (functionalarea, department, shortname, pandlcategory, company) <> (FunctionalareaRow.tupled, FunctionalareaRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(functionalarea), department, shortname, pandlcategory, company).shaped.<>({r=>import r._; _1.map(_=> FunctionalareaRow.tupled((_1.get, _2, _3, _4, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column functionalArea SqlType(BIGINT), PrimaryKey */
    val functionalarea: Rep[Long] = column[Long]("functionalArea", O.PrimaryKey)
    /** Database column department SqlType(VARCHAR), Length(100,true), Default(None) */
    val department: Rep[Option[String]] = column[Option[String]]("department", O.Length(100,varying=true), O.Default(None))
    /** Database column shortname SqlType(VARCHAR), Length(100,true), Default(None) */
    val shortname: Rep[Option[String]] = column[Option[String]]("shortname", O.Length(100,varying=true), O.Default(None))
    /** Database column PandLCategory SqlType(VARCHAR), Length(10,true), Default(None) */
    val pandlcategory: Rep[Option[String]] = column[Option[String]]("PandLCategory", O.Length(10,varying=true), O.Default(None))
    /** Database column company SqlType(VARCHAR), Length(100,true), Default(None) */
    val company: Rep[Option[String]] = column[Option[String]]("company", O.Length(100,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Functionalarea */
  lazy val Functionalarea = new TableQuery(tag => new Functionalarea(tag))

  /** Entity class storing rows of table Gitcommit
   *  @param sha Database column sha SqlType(VARCHAR), PrimaryKey, Length(30,true)
   *  @param message Database column message SqlType(VARCHAR), Length(254,true)
   *  @param committer Database column committer SqlType(VARCHAR), Length(20,true), Default(None)
   *  @param author Database column author SqlType(VARCHAR), Length(20,true), Default(None)
   *  @param commitdate Database column commitDate SqlType(DATE), Default(None)
   *  @param authordate Database column authorDate SqlType(DATE), Default(None) */
  case class GitcommitRow(sha: String, message: String, committer: Option[String] = None, author: Option[String] = None, commitdate: Option[java.sql.Date] = None, authordate: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching GitcommitRow objects using plain SQL queries */
  implicit def GetResultGitcommitRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Option[java.sql.Date]]): GR[GitcommitRow] = GR{
    prs => import prs._
    GitcommitRow.tupled((<<[String], <<[String], <<?[String], <<?[String], <<?[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table gitcommit. Objects of this class serve as prototypes for rows in queries. */
  class Gitcommit(_tableTag: Tag) extends profile.api.Table[GitcommitRow](_tableTag, Some("offline"), "gitcommit") {
    def * = (sha, message, committer, author, commitdate, authordate) <> (GitcommitRow.tupled, GitcommitRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(sha), Rep.Some(message), committer, author, commitdate, authordate).shaped.<>({r=>import r._; _1.map(_=> GitcommitRow.tupled((_1.get, _2.get, _3, _4, _5, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column sha SqlType(VARCHAR), PrimaryKey, Length(30,true) */
    val sha: Rep[String] = column[String]("sha", O.PrimaryKey, O.Length(30,varying=true))
    /** Database column message SqlType(VARCHAR), Length(254,true) */
    val message: Rep[String] = column[String]("message", O.Length(254,varying=true))
    /** Database column committer SqlType(VARCHAR), Length(20,true), Default(None) */
    val committer: Rep[Option[String]] = column[Option[String]]("committer", O.Length(20,varying=true), O.Default(None))
    /** Database column author SqlType(VARCHAR), Length(20,true), Default(None) */
    val author: Rep[Option[String]] = column[Option[String]]("author", O.Length(20,varying=true), O.Default(None))
    /** Database column commitDate SqlType(DATE), Default(None) */
    val commitdate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("commitDate", O.Default(None))
    /** Database column authorDate SqlType(DATE), Default(None) */
    val authordate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("authorDate", O.Default(None))

    /** Index over (committer) (database name committer) */
    val index1 = index("committer", committer)
  }
  /** Collection-like TableQuery object for table Gitcommit */
  lazy val Gitcommit = new TableQuery(tag => new Gitcommit(tag))

  /** Entity class storing rows of table Gitissue
   *  @param id Database column id SqlType(BIGINT), PrimaryKey
   *  @param issuenumber Database column issueNumber SqlType(BIGINT)
   *  @param user Database column user SqlType(VARCHAR), Length(20,true)
   *  @param title Database column title SqlType(VARCHAR), Length(254,true)
   *  @param state Database column state SqlType(VARCHAR), Length(20,true)
   *  @param assignee Database column assignee SqlType(VARCHAR), Length(20,true), Default(None)
   *  @param createdat Database column createdAt SqlType(DATE), Default(None)
   *  @param updatedat Database column updatedAt SqlType(DATE), Default(None)
   *  @param closedat Database column closedAt SqlType(DATE), Default(None)
   *  @param pullurl Database column pullURL SqlType(VARCHAR), Length(254,true), Default(None) */
  case class GitissueRow(id: Long, issuenumber: Long, user: String, title: String, state: String, assignee: Option[String] = None, createdat: Option[java.sql.Date] = None, updatedat: Option[java.sql.Date] = None, closedat: Option[java.sql.Date] = None, pullurl: Option[String] = None)
  /** GetResult implicit for fetching GitissueRow objects using plain SQL queries */
  implicit def GetResultGitissueRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[java.sql.Date]]): GR[GitissueRow] = GR{
    prs => import prs._
    GitissueRow.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[String], <<?[String], <<?[java.sql.Date], <<?[java.sql.Date], <<?[java.sql.Date], <<?[String]))
  }
  /** Table description of table gitissue. Objects of this class serve as prototypes for rows in queries. */
  class Gitissue(_tableTag: Tag) extends profile.api.Table[GitissueRow](_tableTag, Some("offline"), "gitissue") {
    def * = (id, issuenumber, user, title, state, assignee, createdat, updatedat, closedat, pullurl) <> (GitissueRow.tupled, GitissueRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(issuenumber), Rep.Some(user), Rep.Some(title), Rep.Some(state), assignee, createdat, updatedat, closedat, pullurl).shaped.<>({r=>import r._; _1.map(_=> GitissueRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column issueNumber SqlType(BIGINT) */
    val issuenumber: Rep[Long] = column[Long]("issueNumber")
    /** Database column user SqlType(VARCHAR), Length(20,true) */
    val user: Rep[String] = column[String]("user", O.Length(20,varying=true))
    /** Database column title SqlType(VARCHAR), Length(254,true) */
    val title: Rep[String] = column[String]("title", O.Length(254,varying=true))
    /** Database column state SqlType(VARCHAR), Length(20,true) */
    val state: Rep[String] = column[String]("state", O.Length(20,varying=true))
    /** Database column assignee SqlType(VARCHAR), Length(20,true), Default(None) */
    val assignee: Rep[Option[String]] = column[Option[String]]("assignee", O.Length(20,varying=true), O.Default(None))
    /** Database column createdAt SqlType(DATE), Default(None) */
    val createdat: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("createdAt", O.Default(None))
    /** Database column updatedAt SqlType(DATE), Default(None) */
    val updatedat: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("updatedAt", O.Default(None))
    /** Database column closedAt SqlType(DATE), Default(None) */
    val closedat: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("closedAt", O.Default(None))
    /** Database column pullURL SqlType(VARCHAR), Length(254,true), Default(None) */
    val pullurl: Rep[Option[String]] = column[Option[String]]("pullURL", O.Length(254,varying=true), O.Default(None))

    /** Index over (user) (database name user) */
    val index1 = index("user", user)
  }
  /** Collection-like TableQuery object for table Gitissue */
  lazy val Gitissue = new TableQuery(tag => new Gitissue(tag))

  /** Entity class storing rows of table Individualarchetype
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param login Database column login SqlType(VARCHAR), Length(254,true)
   *  @param archetypeid Database column archetypeID SqlType(BIGINT) */
  case class IndividualarchetypeRow(id: Long, login: String, archetypeid: Long)
  /** GetResult implicit for fetching IndividualarchetypeRow objects using plain SQL queries */
  implicit def GetResultIndividualarchetypeRow(implicit e0: GR[Long], e1: GR[String]): GR[IndividualarchetypeRow] = GR{
    prs => import prs._
    IndividualarchetypeRow.tupled((<<[Long], <<[String], <<[Long]))
  }
  /** Table description of table individualarchetype. Objects of this class serve as prototypes for rows in queries. */
  class Individualarchetype(_tableTag: Tag) extends profile.api.Table[IndividualarchetypeRow](_tableTag, Some("offline"), "individualarchetype") {
    def * = (id, login, archetypeid) <> (IndividualarchetypeRow.tupled, IndividualarchetypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), Rep.Some(archetypeid)).shaped.<>({r=>import r._; _1.map(_=> IndividualarchetypeRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("login", O.Length(254,varying=true))
    /** Database column archetypeID SqlType(BIGINT) */
    val archetypeid: Rep[Long] = column[Long]("archetypeID")

    /** Foreign key referencing Managerarchetype (database name ARCHETYPEI_FK) */
    lazy val managerarchetypeFk = foreignKey("ARCHETYPEI_FK", archetypeid, Managerarchetype)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Individualarchetype */
  lazy val Individualarchetype = new TableQuery(tag => new Individualarchetype(tag))

  /** Entity class storing rows of table Individualbusinessunit
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param login Database column login SqlType(VARCHAR), Length(254,true)
   *  @param businessunitid Database column businessUnitID SqlType(BIGINT) */
  case class IndividualbusinessunitRow(id: Long, login: String, businessunitid: Long)
  /** GetResult implicit for fetching IndividualbusinessunitRow objects using plain SQL queries */
  implicit def GetResultIndividualbusinessunitRow(implicit e0: GR[Long], e1: GR[String]): GR[IndividualbusinessunitRow] = GR{
    prs => import prs._
    IndividualbusinessunitRow.tupled((<<[Long], <<[String], <<[Long]))
  }
  /** Table description of table individualbusinessunit. Objects of this class serve as prototypes for rows in queries. */
  class Individualbusinessunit(_tableTag: Tag) extends profile.api.Table[IndividualbusinessunitRow](_tableTag, Some("offline"), "individualbusinessunit") {
    def * = (id, login, businessunitid) <> (IndividualbusinessunitRow.tupled, IndividualbusinessunitRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), Rep.Some(businessunitid)).shaped.<>({r=>import r._; _1.map(_=> IndividualbusinessunitRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("login", O.Length(254,varying=true))
    /** Database column businessUnitID SqlType(BIGINT) */
    val businessunitid: Rep[Long] = column[Long]("businessUnitID")

    /** Foreign key referencing Businessunit (database name BUSUNIT_FK) */
    lazy val businessunitFk = foreignKey("BUSUNIT_FK", businessunitid, Businessunit)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Individualbusinessunit */
  lazy val Individualbusinessunit = new TableQuery(tag => new Individualbusinessunit(tag))

  /** Entity class storing rows of table Jiraissue
   *  @param id Database column id SqlType(VARCHAR), PrimaryKey, Length(20,true)
   *  @param self Database column self SqlType(VARCHAR), Length(2540,true)
   *  @param jirakey Database column jirakey SqlType(VARCHAR), Length(20,true)
   *  @param issuetype Database column issueType SqlType(VARCHAR), Length(20,true)
   *  @param summary Database column summary SqlType(TEXT)
   *  @param parent Database column parent SqlType(VARCHAR), Length(20,true), Default(None)
   *  @param timespent Database column timeSpent SqlType(INT), Default(None)
   *  @param timeestimate Database column timeEstimate SqlType(INT), Default(None)
   *  @param progress Database column progress SqlType(INT), Default(None)
   *  @param progresstotal Database column progressTotal SqlType(INT), Default(None)
   *  @param progresspercent Database column progressPercent SqlType(INT), Default(None)
   *  @param assignee Database column assignee SqlType(VARCHAR), Length(20,true), Default(None)
   *  @param status Database column status SqlType(VARCHAR), Length(20,true), Default(None)
   *  @param projectname Database column projectName SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param createdat Database column createdAt SqlType(DATE)
   *  @param updatedat Database column updatedAt SqlType(DATE), Default(None)
   *  @param resolvedat Database column resolvedAt SqlType(DATE), Default(None) */
  case class JiraissueRow(id: String, self: String, jirakey: String, issuetype: String, summary: String, parent: Option[String] = None, timespent: Option[Int] = None, timeestimate: Option[Int] = None, progress: Option[Int] = None, progresstotal: Option[Int] = None, progresspercent: Option[Int] = None, assignee: Option[String] = None, status: Option[String] = None, projectname: Option[String] = None, createdat: java.sql.Date, updatedat: Option[java.sql.Date] = None, resolvedat: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching JiraissueRow objects using plain SQL queries */
  implicit def GetResultJiraissueRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[java.sql.Date], e4: GR[Option[java.sql.Date]]): GR[JiraissueRow] = GR{
    prs => import prs._
    JiraissueRow.tupled((<<[String], <<[String], <<[String], <<[String], <<[String], <<?[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[String], <<?[String], <<?[String], <<[java.sql.Date], <<?[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table jiraissue. Objects of this class serve as prototypes for rows in queries. */
  class Jiraissue(_tableTag: Tag) extends profile.api.Table[JiraissueRow](_tableTag, Some("offline"), "jiraissue") {
    def * = (id, self, jirakey, issuetype, summary, parent, timespent, timeestimate, progress, progresstotal, progresspercent, assignee, status, projectname, createdat, updatedat, resolvedat) <> (JiraissueRow.tupled, JiraissueRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(self), Rep.Some(jirakey), Rep.Some(issuetype), Rep.Some(summary), parent, timespent, timeestimate, progress, progresstotal, progresspercent, assignee, status, projectname, Rep.Some(createdat), updatedat, resolvedat).shaped.<>({r=>import r._; _1.map(_=> JiraissueRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15.get, _16, _17)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(VARCHAR), PrimaryKey, Length(20,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(20,varying=true))
    /** Database column self SqlType(VARCHAR), Length(2540,true) */
    val self: Rep[String] = column[String]("self", O.Length(2540,varying=true))
    /** Database column jirakey SqlType(VARCHAR), Length(20,true) */
    val jirakey: Rep[String] = column[String]("jirakey", O.Length(20,varying=true))
    /** Database column issueType SqlType(VARCHAR), Length(20,true) */
    val issuetype: Rep[String] = column[String]("issueType", O.Length(20,varying=true))
    /** Database column summary SqlType(TEXT) */
    val summary: Rep[String] = column[String]("summary")
    /** Database column parent SqlType(VARCHAR), Length(20,true), Default(None) */
    val parent: Rep[Option[String]] = column[Option[String]]("parent", O.Length(20,varying=true), O.Default(None))
    /** Database column timeSpent SqlType(INT), Default(None) */
    val timespent: Rep[Option[Int]] = column[Option[Int]]("timeSpent", O.Default(None))
    /** Database column timeEstimate SqlType(INT), Default(None) */
    val timeestimate: Rep[Option[Int]] = column[Option[Int]]("timeEstimate", O.Default(None))
    /** Database column progress SqlType(INT), Default(None) */
    val progress: Rep[Option[Int]] = column[Option[Int]]("progress", O.Default(None))
    /** Database column progressTotal SqlType(INT), Default(None) */
    val progresstotal: Rep[Option[Int]] = column[Option[Int]]("progressTotal", O.Default(None))
    /** Database column progressPercent SqlType(INT), Default(None) */
    val progresspercent: Rep[Option[Int]] = column[Option[Int]]("progressPercent", O.Default(None))
    /** Database column assignee SqlType(VARCHAR), Length(20,true), Default(None) */
    val assignee: Rep[Option[String]] = column[Option[String]]("assignee", O.Length(20,varying=true), O.Default(None))
    /** Database column status SqlType(VARCHAR), Length(20,true), Default(None) */
    val status: Rep[Option[String]] = column[Option[String]]("status", O.Length(20,varying=true), O.Default(None))
    /** Database column projectName SqlType(VARCHAR), Length(100,true), Default(None) */
    val projectname: Rep[Option[String]] = column[Option[String]]("projectName", O.Length(100,varying=true), O.Default(None))
    /** Database column createdAt SqlType(DATE) */
    val createdat: Rep[java.sql.Date] = column[java.sql.Date]("createdAt")
    /** Database column updatedAt SqlType(DATE), Default(None) */
    val updatedat: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("updatedAt", O.Default(None))
    /** Database column resolvedAt SqlType(DATE), Default(None) */
    val resolvedat: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("resolvedAt", O.Default(None))

    /** Index over (jirakey) (database name jiraKey) */
    val index1 = index("jiraKey", jirakey)
    /** Index over (parent) (database name parent) */
    val index2 = index("parent", parent)
  }
  /** Collection-like TableQuery object for table Jiraissue */
  lazy val Jiraissue = new TableQuery(tag => new Jiraissue(tag))

  /** Entity class storing rows of table Jiraparentissue
   *  @param id Database column id SqlType(VARCHAR), PrimaryKey, Length(20,true)
   *  @param jirakey Database column jirakey SqlType(VARCHAR), Length(20,true)
   *  @param self Database column self SqlType(VARCHAR), Length(254,true)
   *  @param summary Database column summary SqlType(TEXT) */
  case class JiraparentissueRow(id: String, jirakey: String, self: String, summary: String)
  /** GetResult implicit for fetching JiraparentissueRow objects using plain SQL queries */
  implicit def GetResultJiraparentissueRow(implicit e0: GR[String]): GR[JiraparentissueRow] = GR{
    prs => import prs._
    JiraparentissueRow.tupled((<<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table jiraparentissue. Objects of this class serve as prototypes for rows in queries. */
  class Jiraparentissue(_tableTag: Tag) extends profile.api.Table[JiraparentissueRow](_tableTag, Some("offline"), "jiraparentissue") {
    def * = (id, jirakey, self, summary) <> (JiraparentissueRow.tupled, JiraparentissueRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(jirakey), Rep.Some(self), Rep.Some(summary)).shaped.<>({r=>import r._; _1.map(_=> JiraparentissueRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(VARCHAR), PrimaryKey, Length(20,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(20,varying=true))
    /** Database column jirakey SqlType(VARCHAR), Length(20,true) */
    val jirakey: Rep[String] = column[String]("jirakey", O.Length(20,varying=true))
    /** Database column self SqlType(VARCHAR), Length(254,true) */
    val self: Rep[String] = column[String]("self", O.Length(254,varying=true))
    /** Database column summary SqlType(TEXT) */
    val summary: Rep[String] = column[String]("summary")

    /** Uniqueness Index over (jirakey) (database name jira) */
    val index1 = index("jira", jirakey, unique=true)
  }
  /** Collection-like TableQuery object for table Jiraparentissue */
  lazy val Jiraparentissue = new TableQuery(tag => new Jiraparentissue(tag))

  /** Entity class storing rows of table Kudosto
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fromperson Database column FromPerson SqlType(VARCHAR), Length(254,true)
   *  @param toperson Database column ToPerson SqlType(VARCHAR), Length(254,true)
   *  @param dateadded Database column DateAdded SqlType(DATE)
   *  @param feedback Database column Feedback SqlType(VARCHAR), Length(254,true)
   *  @param rejected Database column Rejected SqlType(BIT)
   *  @param rejectedby Database column RejectedBy SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param rejectedon Database column RejectedOn SqlType(DATE), Default(None)
   *  @param rejectedreason Database column RejectedReason SqlType(VARCHAR), Length(254,true), Default(None) */
  case class KudostoRow(id: Long, fromperson: String, toperson: String, dateadded: java.sql.Date, feedback: String, rejected: Boolean, rejectedby: Option[String] = None, rejectedon: Option[java.sql.Date] = None, rejectedreason: Option[String] = None)
  /** GetResult implicit for fetching KudostoRow objects using plain SQL queries */
  implicit def GetResultKudostoRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date], e3: GR[Boolean], e4: GR[Option[String]], e5: GR[Option[java.sql.Date]]): GR[KudostoRow] = GR{
    prs => import prs._
    KudostoRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Date], <<[String], <<[Boolean], <<?[String], <<?[java.sql.Date], <<?[String]))
  }
  /** Table description of table kudosto. Objects of this class serve as prototypes for rows in queries. */
  class Kudosto(_tableTag: Tag) extends profile.api.Table[KudostoRow](_tableTag, Some("offline"), "kudosto") {
    def * = (id, fromperson, toperson, dateadded, feedback, rejected, rejectedby, rejectedon, rejectedreason) <> (KudostoRow.tupled, KudostoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(fromperson), Rep.Some(toperson), Rep.Some(dateadded), Rep.Some(feedback), Rep.Some(rejected), rejectedby, rejectedon, rejectedreason).shaped.<>({r=>import r._; _1.map(_=> KudostoRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column FromPerson SqlType(VARCHAR), Length(254,true) */
    val fromperson: Rep[String] = column[String]("FromPerson", O.Length(254,varying=true))
    /** Database column ToPerson SqlType(VARCHAR), Length(254,true) */
    val toperson: Rep[String] = column[String]("ToPerson", O.Length(254,varying=true))
    /** Database column DateAdded SqlType(DATE) */
    val dateadded: Rep[java.sql.Date] = column[java.sql.Date]("DateAdded")
    /** Database column Feedback SqlType(VARCHAR), Length(254,true) */
    val feedback: Rep[String] = column[String]("Feedback", O.Length(254,varying=true))
    /** Database column Rejected SqlType(BIT) */
    val rejected: Rep[Boolean] = column[Boolean]("Rejected")
    /** Database column RejectedBy SqlType(VARCHAR), Length(254,true), Default(None) */
    val rejectedby: Rep[Option[String]] = column[Option[String]]("RejectedBy", O.Length(254,varying=true), O.Default(None))
    /** Database column RejectedOn SqlType(DATE), Default(None) */
    val rejectedon: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("RejectedOn", O.Default(None))
    /** Database column RejectedReason SqlType(VARCHAR), Length(254,true), Default(None) */
    val rejectedreason: Rep[Option[String]] = column[Option[String]]("RejectedReason", O.Length(254,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Kudosto */
  lazy val Kudosto = new TableQuery(tag => new Kudosto(tag))

  /** Entity class storing rows of table Managerarchetype
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(254,true)
   *  @param minspan Database column minSpan SqlType(BIGINT)
   *  @param maxspan Database column maxSpan SqlType(BIGINT), Default(None)
   *  @param maturity Database column maturity SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param timespent Database column timeSpent SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param taskrepeatability Database column taskRepeatability SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param subordinatesskills Database column subordinatesSkills SqlType(VARCHAR), Length(254,true), Default(None) */
  case class ManagerarchetypeRow(id: Long, name: String, minspan: Long, maxspan: Option[Long] = None, maturity: Option[String] = None, timespent: Option[String] = None, taskrepeatability: Option[String] = None, subordinatesskills: Option[String] = None)
  /** GetResult implicit for fetching ManagerarchetypeRow objects using plain SQL queries */
  implicit def GetResultManagerarchetypeRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]], e3: GR[Option[String]]): GR[ManagerarchetypeRow] = GR{
    prs => import prs._
    ManagerarchetypeRow.tupled((<<[Long], <<[String], <<[Long], <<?[Long], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table managerarchetype. Objects of this class serve as prototypes for rows in queries. */
  class Managerarchetype(_tableTag: Tag) extends profile.api.Table[ManagerarchetypeRow](_tableTag, Some("offline"), "managerarchetype") {
    def * = (id, name, minspan, maxspan, maturity, timespent, taskrepeatability, subordinatesskills) <> (ManagerarchetypeRow.tupled, ManagerarchetypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(minspan), maxspan, maturity, timespent, taskrepeatability, subordinatesskills).shaped.<>({r=>import r._; _1.map(_=> ManagerarchetypeRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("name", O.Length(254,varying=true))
    /** Database column minSpan SqlType(BIGINT) */
    val minspan: Rep[Long] = column[Long]("minSpan")
    /** Database column maxSpan SqlType(BIGINT), Default(None) */
    val maxspan: Rep[Option[Long]] = column[Option[Long]]("maxSpan", O.Default(None))
    /** Database column maturity SqlType(VARCHAR), Length(254,true), Default(None) */
    val maturity: Rep[Option[String]] = column[Option[String]]("maturity", O.Length(254,varying=true), O.Default(None))
    /** Database column timeSpent SqlType(VARCHAR), Length(254,true), Default(None) */
    val timespent: Rep[Option[String]] = column[Option[String]]("timeSpent", O.Length(254,varying=true), O.Default(None))
    /** Database column taskRepeatability SqlType(VARCHAR), Length(254,true), Default(None) */
    val taskrepeatability: Rep[Option[String]] = column[Option[String]]("taskRepeatability", O.Length(254,varying=true), O.Default(None))
    /** Database column subordinatesSkills SqlType(VARCHAR), Length(254,true), Default(None) */
    val subordinatesskills: Rep[Option[String]] = column[Option[String]]("subordinatesSkills", O.Length(254,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Managerarchetype */
  lazy val Managerarchetype = new TableQuery(tag => new Managerarchetype(tag))

  /** Entity class storing rows of table Matrixteam
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(254,true)
   *  @param ispe Database column isPE SqlType(BIT), Default(false)
   *  @param owner Database column owner SqlType(VARCHAR), Length(254,true), Default(None) */
  case class MatrixteamRow(id: Long, name: String, ispe: Boolean = false, owner: Option[String] = None)
  /** GetResult implicit for fetching MatrixteamRow objects using plain SQL queries */
  implicit def GetResultMatrixteamRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Boolean], e3: GR[Option[String]]): GR[MatrixteamRow] = GR{
    prs => import prs._
    MatrixteamRow.tupled((<<[Long], <<[String], <<[Boolean], <<?[String]))
  }
  /** Table description of table matrixteam. Objects of this class serve as prototypes for rows in queries. */
  class Matrixteam(_tableTag: Tag) extends profile.api.Table[MatrixteamRow](_tableTag, Some("offline"), "matrixteam") {
    def * = (id, name, ispe, owner) <> (MatrixteamRow.tupled, MatrixteamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ispe), owner).shaped.<>({r=>import r._; _1.map(_=> MatrixteamRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("Name", O.Length(254,varying=true))
    /** Database column isPE SqlType(BIT), Default(false) */
    val ispe: Rep[Boolean] = column[Boolean]("isPE", O.Default(false))
    /** Database column owner SqlType(VARCHAR), Length(254,true), Default(None) */
    val owner: Rep[Option[String]] = column[Option[String]]("owner", O.Length(254,varying=true), O.Default(None))

    /** Uniqueness Index over (name) (database name Name) */
    val index1 = index("Name", name, unique=true)
  }
  /** Collection-like TableQuery object for table Matrixteam */
  lazy val Matrixteam = new TableQuery(tag => new Matrixteam(tag))

  /** Entity class storing rows of table Matrixteammember
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param matrixteammemberid Database column matrixTeamMemberId SqlType(BIGINT)
   *  @param login Database column Login SqlType(VARCHAR), Length(254,true) */
  case class MatrixteammemberRow(id: Long, matrixteammemberid: Long, login: String)
  /** GetResult implicit for fetching MatrixteammemberRow objects using plain SQL queries */
  implicit def GetResultMatrixteammemberRow(implicit e0: GR[Long], e1: GR[String]): GR[MatrixteammemberRow] = GR{
    prs => import prs._
    MatrixteammemberRow.tupled((<<[Long], <<[Long], <<[String]))
  }
  /** Table description of table matrixteammember. Objects of this class serve as prototypes for rows in queries. */
  class Matrixteammember(_tableTag: Tag) extends profile.api.Table[MatrixteammemberRow](_tableTag, Some("offline"), "matrixteammember") {
    def * = (id, matrixteammemberid, login) <> (MatrixteammemberRow.tupled, MatrixteammemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(matrixteammemberid), Rep.Some(login)).shaped.<>({r=>import r._; _1.map(_=> MatrixteammemberRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column matrixTeamMemberId SqlType(BIGINT) */
    val matrixteammemberid: Rep[Long] = column[Long]("matrixTeamMemberId")
    /** Database column Login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("Login", O.Length(254,varying=true))

    /** Foreign key referencing Matrixteam (database name matrixTeamMember_FK) */
    lazy val matrixteamFk = foreignKey("matrixTeamMember_FK", matrixteammemberid, Matrixteam)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Matrixteammember */
  lazy val Matrixteammember = new TableQuery(tag => new Matrixteammember(tag))

  /** Entity class storing rows of table Milestone
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param order Database column Order SqlType(INT)
   *  @param shorttext Database column ShortText SqlType(VARCHAR), Length(10,true)
   *  @param longtext Database column LongText SqlType(TEXT)
   *  @param isautomated Database column IsAutomated SqlType(BIT)
   *  @param starlevel Database column StarLevel SqlType(INT) */
  case class MilestoneRow(id: Long, order: Int, shorttext: String, longtext: String, isautomated: Boolean, starlevel: Int)
  /** GetResult implicit for fetching MilestoneRow objects using plain SQL queries */
  implicit def GetResultMilestoneRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[String], e3: GR[Boolean]): GR[MilestoneRow] = GR{
    prs => import prs._
    MilestoneRow.tupled((<<[Long], <<[Int], <<[String], <<[String], <<[Boolean], <<[Int]))
  }
  /** Table description of table milestone. Objects of this class serve as prototypes for rows in queries. */
  class Milestone(_tableTag: Tag) extends profile.api.Table[MilestoneRow](_tableTag, Some("offline"), "milestone") {
    def * = (id, order, shorttext, longtext, isautomated, starlevel) <> (MilestoneRow.tupled, MilestoneRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(order), Rep.Some(shorttext), Rep.Some(longtext), Rep.Some(isautomated), Rep.Some(starlevel)).shaped.<>({r=>import r._; _1.map(_=> MilestoneRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Order SqlType(INT) */
    val order: Rep[Int] = column[Int]("Order")
    /** Database column ShortText SqlType(VARCHAR), Length(10,true) */
    val shorttext: Rep[String] = column[String]("ShortText", O.Length(10,varying=true))
    /** Database column LongText SqlType(TEXT) */
    val longtext: Rep[String] = column[String]("LongText")
    /** Database column IsAutomated SqlType(BIT) */
    val isautomated: Rep[Boolean] = column[Boolean]("IsAutomated")
    /** Database column StarLevel SqlType(INT) */
    val starlevel: Rep[Int] = column[Int]("StarLevel")

    /** Uniqueness Index over (shorttext) (database name ShortText) */
    val index1 = index("ShortText", shorttext, unique=true)
  }
  /** Collection-like TableQuery object for table Milestone */
  lazy val Milestone = new TableQuery(tag => new Milestone(tag))

  /** Entity class storing rows of table Office
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param city Database column city SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param street Database column street SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param pobox Database column POBox SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param region Database column region SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param zipcode Database column zipCode SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param country Database column country SqlType(VARCHAR), Length(254,true), Default(None) */
  case class OfficeRow(id: Long, city: Option[String] = None, street: Option[String] = None, pobox: Option[String] = None, region: Option[String] = None, zipcode: Option[String] = None, country: Option[String] = None)
  /** GetResult implicit for fetching OfficeRow objects using plain SQL queries */
  implicit def GetResultOfficeRow(implicit e0: GR[Long], e1: GR[Option[String]]): GR[OfficeRow] = GR{
    prs => import prs._
    OfficeRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table office. Objects of this class serve as prototypes for rows in queries. */
  class Office(_tableTag: Tag) extends profile.api.Table[OfficeRow](_tableTag, Some("offline"), "office") {
    def * = (id, city, street, pobox, region, zipcode, country) <> (OfficeRow.tupled, OfficeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), city, street, pobox, region, zipcode, country).shaped.<>({r=>import r._; _1.map(_=> OfficeRow.tupled((_1.get, _2, _3, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column city SqlType(VARCHAR), Length(254,true), Default(None) */
    val city: Rep[Option[String]] = column[Option[String]]("city", O.Length(254,varying=true), O.Default(None))
    /** Database column street SqlType(VARCHAR), Length(254,true), Default(None) */
    val street: Rep[Option[String]] = column[Option[String]]("street", O.Length(254,varying=true), O.Default(None))
    /** Database column POBox SqlType(VARCHAR), Length(254,true), Default(None) */
    val pobox: Rep[Option[String]] = column[Option[String]]("POBox", O.Length(254,varying=true), O.Default(None))
    /** Database column region SqlType(VARCHAR), Length(254,true), Default(None) */
    val region: Rep[Option[String]] = column[Option[String]]("region", O.Length(254,varying=true), O.Default(None))
    /** Database column zipCode SqlType(VARCHAR), Length(254,true), Default(None) */
    val zipcode: Rep[Option[String]] = column[Option[String]]("zipCode", O.Length(254,varying=true), O.Default(None))
    /** Database column country SqlType(VARCHAR), Length(254,true), Default(None) */
    val country: Rep[Option[String]] = column[Option[String]]("country", O.Length(254,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Office */
  lazy val Office = new TableQuery(tag => new Office(tag))

  /** Entity class storing rows of table Okrkeyresult
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param objectiveid Database column objectiveId SqlType(BIGINT)
   *  @param dateadded Database column DateAdded SqlType(DATE)
   *  @param objective Database column Objective SqlType(MEDIUMTEXT), Length(16777215,true)
   *  @param description Database column Description SqlType(TEXT), Default(None)
   *  @param score Database column Score SqlType(INT), Default(None)
   *  @param completed Database column completed SqlType(BIT) */
  case class OkrkeyresultRow(id: Long, objectiveid: Long, dateadded: java.sql.Date, objective: String, description: Option[String] = None, score: Option[Int] = None, completed: Boolean)
  /** GetResult implicit for fetching OkrkeyresultRow objects using plain SQL queries */
  implicit def GetResultOkrkeyresultRow(implicit e0: GR[Long], e1: GR[java.sql.Date], e2: GR[String], e3: GR[Option[String]], e4: GR[Option[Int]], e5: GR[Boolean]): GR[OkrkeyresultRow] = GR{
    prs => import prs._
    OkrkeyresultRow.tupled((<<[Long], <<[Long], <<[java.sql.Date], <<[String], <<?[String], <<?[Int], <<[Boolean]))
  }
  /** Table description of table okrkeyresult. Objects of this class serve as prototypes for rows in queries. */
  class Okrkeyresult(_tableTag: Tag) extends profile.api.Table[OkrkeyresultRow](_tableTag, Some("offline"), "okrkeyresult") {
    def * = (id, objectiveid, dateadded, objective, description, score, completed) <> (OkrkeyresultRow.tupled, OkrkeyresultRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(objectiveid), Rep.Some(dateadded), Rep.Some(objective), description, score, Rep.Some(completed)).shaped.<>({r=>import r._; _1.map(_=> OkrkeyresultRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column objectiveId SqlType(BIGINT) */
    val objectiveid: Rep[Long] = column[Long]("objectiveId")
    /** Database column DateAdded SqlType(DATE) */
    val dateadded: Rep[java.sql.Date] = column[java.sql.Date]("DateAdded")
    /** Database column Objective SqlType(MEDIUMTEXT), Length(16777215,true) */
    val objective: Rep[String] = column[String]("Objective", O.Length(16777215,varying=true))
    /** Database column Description SqlType(TEXT), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("Description", O.Default(None))
    /** Database column Score SqlType(INT), Default(None) */
    val score: Rep[Option[Int]] = column[Option[Int]]("Score", O.Default(None))
    /** Database column completed SqlType(BIT) */
    val completed: Rep[Boolean] = column[Boolean]("completed")
  }
  /** Collection-like TableQuery object for table Okrkeyresult */
  lazy val Okrkeyresult = new TableQuery(tag => new Okrkeyresult(tag))

  /** Entity class storing rows of table Okrobjective
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param login Database column Login SqlType(VARCHAR), Length(254,true)
   *  @param dateadded Database column DateAdded SqlType(DATE)
   *  @param objective Database column Objective SqlType(MEDIUMTEXT), Length(16777215,true)
   *  @param quarterdate Database column QuarterDate SqlType(DATE), Default(None)
   *  @param score Database column Score SqlType(INT), Default(None)
   *  @param completed Database column completed SqlType(BIT)
   *  @param retired Database column retired SqlType(BIT), Default(false) */
  case class OkrobjectiveRow(id: Long, login: String, dateadded: java.sql.Date, objective: String, quarterdate: Option[java.sql.Date] = None, score: Option[Int] = None, completed: Boolean, retired: Boolean = false)
  /** GetResult implicit for fetching OkrobjectiveRow objects using plain SQL queries */
  implicit def GetResultOkrobjectiveRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date], e3: GR[Option[java.sql.Date]], e4: GR[Option[Int]], e5: GR[Boolean]): GR[OkrobjectiveRow] = GR{
    prs => import prs._
    OkrobjectiveRow.tupled((<<[Long], <<[String], <<[java.sql.Date], <<[String], <<?[java.sql.Date], <<?[Int], <<[Boolean], <<[Boolean]))
  }
  /** Table description of table okrobjective. Objects of this class serve as prototypes for rows in queries. */
  class Okrobjective(_tableTag: Tag) extends profile.api.Table[OkrobjectiveRow](_tableTag, Some("offline"), "okrobjective") {
    def * = (id, login, dateadded, objective, quarterdate, score, completed, retired) <> (OkrobjectiveRow.tupled, OkrobjectiveRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), Rep.Some(dateadded), Rep.Some(objective), quarterdate, score, Rep.Some(completed), Rep.Some(retired)).shaped.<>({r=>import r._; _1.map(_=> OkrobjectiveRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("Login", O.Length(254,varying=true))
    /** Database column DateAdded SqlType(DATE) */
    val dateadded: Rep[java.sql.Date] = column[java.sql.Date]("DateAdded")
    /** Database column Objective SqlType(MEDIUMTEXT), Length(16777215,true) */
    val objective: Rep[String] = column[String]("Objective", O.Length(16777215,varying=true))
    /** Database column QuarterDate SqlType(DATE), Default(None) */
    val quarterdate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("QuarterDate", O.Default(None))
    /** Database column Score SqlType(INT), Default(None) */
    val score: Rep[Option[Int]] = column[Option[Int]]("Score", O.Default(None))
    /** Database column completed SqlType(BIT) */
    val completed: Rep[Boolean] = column[Boolean]("completed")
    /** Database column retired SqlType(BIT), Default(false) */
    val retired: Rep[Boolean] = column[Boolean]("retired", O.Default(false))
  }
  /** Collection-like TableQuery object for table Okrobjective */
  lazy val Okrobjective = new TableQuery(tag => new Okrobjective(tag))

  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param hash Database column hash SqlType(VARCHAR), Length(255,true)
   *  @param appliedAt Database column applied_at SqlType(TIMESTAMP)
   *  @param applyScript Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param revertScript Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param state Database column state SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends profile.api.Table[PlayEvolutionsRow](_tableTag, Some("offline"), "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(VARCHAR), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at SqlType(TIMESTAMP) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column state SqlType(VARCHAR), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Length(16777215,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table Positiontype
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param position Database column position SqlType(VARCHAR), Length(254,true)
   *  @param positiontype Database column positiontype SqlType(VARCHAR), Length(20,true), Default(UNKNOWN) */
  case class PositiontypeRow(id: Long, position: String, positiontype: String = "UNKNOWN")
  /** GetResult implicit for fetching PositiontypeRow objects using plain SQL queries */
  implicit def GetResultPositiontypeRow(implicit e0: GR[Long], e1: GR[String]): GR[PositiontypeRow] = GR{
    prs => import prs._
    PositiontypeRow.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table positiontype. Objects of this class serve as prototypes for rows in queries. */
  class Positiontype(_tableTag: Tag) extends profile.api.Table[PositiontypeRow](_tableTag, Some("offline"), "positiontype") {
    def * = (id, position, positiontype) <> (PositiontypeRow.tupled, PositiontypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(position), Rep.Some(positiontype)).shaped.<>({r=>import r._; _1.map(_=> PositiontypeRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column position SqlType(VARCHAR), Length(254,true) */
    val position: Rep[String] = column[String]("position", O.Length(254,varying=true))
    /** Database column positiontype SqlType(VARCHAR), Length(20,true), Default(UNKNOWN) */
    val positiontype: Rep[String] = column[String]("positiontype", O.Length(20,varying=true), O.Default("UNKNOWN"))

    /** Uniqueness Index over (position) (database name position) */
    val index1 = index("position", position, unique=true)
  }
  /** Collection-like TableQuery object for table Positiontype */
  lazy val Positiontype = new TableQuery(tag => new Positiontype(tag))

  /** Entity class storing rows of table Profitcenter
   *  @param profitcenter Database column profitCenter SqlType(BIGINT), PrimaryKey
   *  @param shortname Database column shortname SqlType(VARCHAR), Length(50,true), Default(None) */
  case class ProfitcenterRow(profitcenter: Long, shortname: Option[String] = None)
  /** GetResult implicit for fetching ProfitcenterRow objects using plain SQL queries */
  implicit def GetResultProfitcenterRow(implicit e0: GR[Long], e1: GR[Option[String]]): GR[ProfitcenterRow] = GR{
    prs => import prs._
    ProfitcenterRow.tupled((<<[Long], <<?[String]))
  }
  /** Table description of table profitcenter. Objects of this class serve as prototypes for rows in queries. */
  class Profitcenter(_tableTag: Tag) extends profile.api.Table[ProfitcenterRow](_tableTag, Some("offline"), "profitcenter") {
    def * = (profitcenter, shortname) <> (ProfitcenterRow.tupled, ProfitcenterRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(profitcenter), shortname).shaped.<>({r=>import r._; _1.map(_=> ProfitcenterRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column profitCenter SqlType(BIGINT), PrimaryKey */
    val profitcenter: Rep[Long] = column[Long]("profitCenter", O.PrimaryKey)
    /** Database column shortname SqlType(VARCHAR), Length(50,true), Default(None) */
    val shortname: Rep[Option[String]] = column[Option[String]]("shortname", O.Length(50,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Profitcenter */
  lazy val Profitcenter = new TableQuery(tag => new Profitcenter(tag))

  /** Entity class storing rows of table Ratecard
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param companyname Database column CompanyName SqlType(VARCHAR), Length(254,true) */
  case class RatecardRow(id: Long, companyname: String)
  /** GetResult implicit for fetching RatecardRow objects using plain SQL queries */
  implicit def GetResultRatecardRow(implicit e0: GR[Long], e1: GR[String]): GR[RatecardRow] = GR{
    prs => import prs._
    RatecardRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table ratecard. Objects of this class serve as prototypes for rows in queries. */
  class Ratecard(_tableTag: Tag) extends profile.api.Table[RatecardRow](_tableTag, Some("offline"), "ratecard") {
    def * = (id, companyname) <> (RatecardRow.tupled, RatecardRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(companyname)).shaped.<>({r=>import r._; _1.map(_=> RatecardRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column CompanyName SqlType(VARCHAR), Length(254,true) */
    val companyname: Rep[String] = column[String]("CompanyName", O.Length(254,varying=true))
  }
  /** Collection-like TableQuery object for table Ratecard */
  lazy val Ratecard = new TableQuery(tag => new Ratecard(tag))

  /** Entity class storing rows of table Ratecardrate
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param roleid Database column roleID SqlType(BIGINT)
   *  @param country Database column country SqlType(VARCHAR), Length(254,true)
   *  @param monthlyrate Database column monthlyRate SqlType(DOUBLE)
   *  @param hourlyrate Database column hourlyRate SqlType(DOUBLE) */
  case class RatecardrateRow(id: Long, roleid: Long, country: String, monthlyrate: Double, hourlyrate: Double)
  /** GetResult implicit for fetching RatecardrateRow objects using plain SQL queries */
  implicit def GetResultRatecardrateRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Double]): GR[RatecardrateRow] = GR{
    prs => import prs._
    RatecardrateRow.tupled((<<[Long], <<[Long], <<[String], <<[Double], <<[Double]))
  }
  /** Table description of table ratecardrate. Objects of this class serve as prototypes for rows in queries. */
  class Ratecardrate(_tableTag: Tag) extends profile.api.Table[RatecardrateRow](_tableTag, Some("offline"), "ratecardrate") {
    def * = (id, roleid, country, monthlyrate, hourlyrate) <> (RatecardrateRow.tupled, RatecardrateRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(roleid), Rep.Some(country), Rep.Some(monthlyrate), Rep.Some(hourlyrate)).shaped.<>({r=>import r._; _1.map(_=> RatecardrateRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column roleID SqlType(BIGINT) */
    val roleid: Rep[Long] = column[Long]("roleID")
    /** Database column country SqlType(VARCHAR), Length(254,true) */
    val country: Rep[String] = column[String]("country", O.Length(254,varying=true))
    /** Database column monthlyRate SqlType(DOUBLE) */
    val monthlyrate: Rep[Double] = column[Double]("monthlyRate")
    /** Database column hourlyRate SqlType(DOUBLE) */
    val hourlyrate: Rep[Double] = column[Double]("hourlyRate")

    /** Foreign key referencing Ratecardrole (database name ROLE_FK) */
    lazy val ratecardroleFk = foreignKey("ROLE_FK", roleid, Ratecardrole)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Ratecardrate */
  lazy val Ratecardrate = new TableQuery(tag => new Ratecardrate(tag))

  /** Entity class storing rows of table Ratecardrole
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param ratecardid Database column rateCardID SqlType(BIGINT)
   *  @param level Database column level SqlType(VARCHAR), Length(254,true)
   *  @param corplevelid Database column corpLevelID SqlType(BIGINT), Default(None)
   *  @param yearsmin Database column yearsMin SqlType(BIGINT)
   *  @param yearsmax Database column yearsMax SqlType(BIGINT) */
  case class RatecardroleRow(id: Long, ratecardid: Long, level: String, corplevelid: Option[Long] = None, yearsmin: Long, yearsmax: Long)
  /** GetResult implicit for fetching RatecardroleRow objects using plain SQL queries */
  implicit def GetResultRatecardroleRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]]): GR[RatecardroleRow] = GR{
    prs => import prs._
    RatecardroleRow.tupled((<<[Long], <<[Long], <<[String], <<?[Long], <<[Long], <<[Long]))
  }
  /** Table description of table ratecardrole. Objects of this class serve as prototypes for rows in queries. */
  class Ratecardrole(_tableTag: Tag) extends profile.api.Table[RatecardroleRow](_tableTag, Some("offline"), "ratecardrole") {
    def * = (id, ratecardid, level, corplevelid, yearsmin, yearsmax) <> (RatecardroleRow.tupled, RatecardroleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(ratecardid), Rep.Some(level), corplevelid, Rep.Some(yearsmin), Rep.Some(yearsmax)).shaped.<>({r=>import r._; _1.map(_=> RatecardroleRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column rateCardID SqlType(BIGINT) */
    val ratecardid: Rep[Long] = column[Long]("rateCardID")
    /** Database column level SqlType(VARCHAR), Length(254,true) */
    val level: Rep[String] = column[String]("level", O.Length(254,varying=true))
    /** Database column corpLevelID SqlType(BIGINT), Default(None) */
    val corplevelid: Rep[Option[Long]] = column[Option[Long]]("corpLevelID", O.Default(None))
    /** Database column yearsMin SqlType(BIGINT) */
    val yearsmin: Rep[Long] = column[Long]("yearsMin")
    /** Database column yearsMax SqlType(BIGINT) */
    val yearsmax: Rep[Long] = column[Long]("yearsMax")

    /** Foreign key referencing Corplevel (database name CORPLEVLRC_FK) */
    lazy val corplevelFk = foreignKey("CORPLEVLRC_FK", corplevelid, Corplevel)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Ratecard (database name RATE_FK) */
    lazy val ratecardFk = foreignKey("RATE_FK", ratecardid, Ratecard)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Ratecardrole */
  lazy val Ratecardrole = new TableQuery(tag => new Ratecardrole(tag))

  /** Entity class storing rows of table Resourcepool
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param poolname Database column poolName SqlType(VARCHAR), Length(254,true) */
  case class ResourcepoolRow(id: Long, poolname: String)
  /** GetResult implicit for fetching ResourcepoolRow objects using plain SQL queries */
  implicit def GetResultResourcepoolRow(implicit e0: GR[Long], e1: GR[String]): GR[ResourcepoolRow] = GR{
    prs => import prs._
    ResourcepoolRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table resourcepool. Objects of this class serve as prototypes for rows in queries. */
  class Resourcepool(_tableTag: Tag) extends profile.api.Table[ResourcepoolRow](_tableTag, Some("offline"), "resourcepool") {
    def * = (id, poolname) <> (ResourcepoolRow.tupled, ResourcepoolRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(poolname)).shaped.<>({r=>import r._; _1.map(_=> ResourcepoolRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column poolName SqlType(VARCHAR), Length(254,true) */
    val poolname: Rep[String] = column[String]("poolName", O.Length(254,varying=true))

    /** Uniqueness Index over (poolname) (database name poolName) */
    val index1 = index("poolName", poolname, unique=true)
  }
  /** Collection-like TableQuery object for table Resourcepool */
  lazy val Resourcepool = new TableQuery(tag => new Resourcepool(tag))

  /** Entity class storing rows of table Resourcepoolteam
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param poolid Database column poolId SqlType(BIGINT)
   *  @param teamdescription Database column teamDescription SqlType(VARCHAR), Length(254,true) */
  case class ResourcepoolteamRow(id: Long, poolid: Long, teamdescription: String)
  /** GetResult implicit for fetching ResourcepoolteamRow objects using plain SQL queries */
  implicit def GetResultResourcepoolteamRow(implicit e0: GR[Long], e1: GR[String]): GR[ResourcepoolteamRow] = GR{
    prs => import prs._
    ResourcepoolteamRow.tupled((<<[Long], <<[Long], <<[String]))
  }
  /** Table description of table resourcepoolteam. Objects of this class serve as prototypes for rows in queries. */
  class Resourcepoolteam(_tableTag: Tag) extends profile.api.Table[ResourcepoolteamRow](_tableTag, Some("offline"), "resourcepoolteam") {
    def * = (id, poolid, teamdescription) <> (ResourcepoolteamRow.tupled, ResourcepoolteamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(poolid), Rep.Some(teamdescription)).shaped.<>({r=>import r._; _1.map(_=> ResourcepoolteamRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column poolId SqlType(BIGINT) */
    val poolid: Rep[Long] = column[Long]("poolId")
    /** Database column teamDescription SqlType(VARCHAR), Length(254,true) */
    val teamdescription: Rep[String] = column[String]("teamDescription", O.Length(254,varying=true))

    /** Foreign key referencing Resourcepool (database name ResourcePoolTeam_ibfk_1) */
    lazy val resourcepoolFk = foreignKey("ResourcePoolTeam_ibfk_1", poolid, Resourcepool)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)

    /** Uniqueness Index over (teamdescription) (database name teamDescription) */
    val index1 = index("teamDescription", teamdescription, unique=true)
  }
  /** Collection-like TableQuery object for table Resourcepoolteam */
  lazy val Resourcepoolteam = new TableQuery(tag => new Resourcepoolteam(tag))

  /** Entity class storing rows of table Scenario
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param login Database column login SqlType(VARCHAR), Length(254,true)
   *  @param parent Database column parent SqlType(BIGINT), Default(None)
   *  @param name Database column name SqlType(VARCHAR), Length(254,true)
   *  @param desc Database column desc SqlType(TEXT), Default(None)
   *  @param managerratio Database column managerRatio SqlType(DOUBLE)
   *  @param spanavg Database column spanAvg SqlType(DOUBLE)
   *  @param employeecount Database column employeeCount SqlType(BIGINT)
   *  @param offshorecount Database column offshoreCount SqlType(BIGINT)
   *  @param contractorcount Database column contractorCount SqlType(BIGINT)
   *  @param maxlayers Database column maxLayers SqlType(BIGINT)
   *  @param ispublic Database column isPublic SqlType(BIT), Default(false) */
  case class ScenarioRow(id: Long, login: String, parent: Option[Long] = None, name: String, desc: Option[String] = None, managerratio: Double, spanavg: Double, employeecount: Long, offshorecount: Long, contractorcount: Long, maxlayers: Long, ispublic: Boolean = false)
  /** GetResult implicit for fetching ScenarioRow objects using plain SQL queries */
  implicit def GetResultScenarioRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]], e3: GR[Option[String]], e4: GR[Double], e5: GR[Boolean]): GR[ScenarioRow] = GR{
    prs => import prs._
    ScenarioRow.tupled((<<[Long], <<[String], <<?[Long], <<[String], <<?[String], <<[Double], <<[Double], <<[Long], <<[Long], <<[Long], <<[Long], <<[Boolean]))
  }
  /** Table description of table scenario. Objects of this class serve as prototypes for rows in queries. */
  class Scenario(_tableTag: Tag) extends profile.api.Table[ScenarioRow](_tableTag, Some("offline"), "scenario") {
    def * = (id, login, parent, name, desc, managerratio, spanavg, employeecount, offshorecount, contractorcount, maxlayers, ispublic) <> (ScenarioRow.tupled, ScenarioRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), parent, Rep.Some(name), desc, Rep.Some(managerratio), Rep.Some(spanavg), Rep.Some(employeecount), Rep.Some(offshorecount), Rep.Some(contractorcount), Rep.Some(maxlayers), Rep.Some(ispublic)).shaped.<>({r=>import r._; _1.map(_=> ScenarioRow.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("login", O.Length(254,varying=true))
    /** Database column parent SqlType(BIGINT), Default(None) */
    val parent: Rep[Option[Long]] = column[Option[Long]]("parent", O.Default(None))
    /** Database column name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("name", O.Length(254,varying=true))
    /** Database column desc SqlType(TEXT), Default(None) */
    val desc: Rep[Option[String]] = column[Option[String]]("desc", O.Default(None))
    /** Database column managerRatio SqlType(DOUBLE) */
    val managerratio: Rep[Double] = column[Double]("managerRatio")
    /** Database column spanAvg SqlType(DOUBLE) */
    val spanavg: Rep[Double] = column[Double]("spanAvg")
    /** Database column employeeCount SqlType(BIGINT) */
    val employeecount: Rep[Long] = column[Long]("employeeCount")
    /** Database column offshoreCount SqlType(BIGINT) */
    val offshorecount: Rep[Long] = column[Long]("offshoreCount")
    /** Database column contractorCount SqlType(BIGINT) */
    val contractorcount: Rep[Long] = column[Long]("contractorCount")
    /** Database column maxLayers SqlType(BIGINT) */
    val maxlayers: Rep[Long] = column[Long]("maxLayers")
    /** Database column isPublic SqlType(BIT), Default(false) */
    val ispublic: Rep[Boolean] = column[Boolean]("isPublic", O.Default(false))
  }
  /** Collection-like TableQuery object for table Scenario */
  lazy val Scenario = new TableQuery(tag => new Scenario(tag))

  /** Entity class storing rows of table Scenariodetail
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param scenario Database column scenario SqlType(BIGINT)
   *  @param login Database column login SqlType(VARCHAR), Length(254,true)
   *  @param managerid Database column managerId SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param iscurrent Database column isCurrent SqlType(BIT)
   *  @param iscontractor Database column isContractor SqlType(BIT)
   *  @param isoffshore Database column isOffshore SqlType(BIT)
   *  @param staffnumber Database column staffNumber SqlType(BIGINT)
   *  @param corplevel Database column corpLevel SqlType(BIGINT), Default(None)
   *  @param businessunit Database column businessUnit SqlType(BIGINT), Default(None)
   *  @param archetypeid Database column archeTypeID SqlType(BIGINT), Default(None) */
  case class ScenariodetailRow(id: Long, scenario: Long, login: String, managerid: Option[String] = None, iscurrent: Boolean, iscontractor: Boolean, isoffshore: Boolean, staffnumber: Long, corplevel: Option[Long] = None, businessunit: Option[Long] = None, archetypeid: Option[Long] = None)
  /** GetResult implicit for fetching ScenariodetailRow objects using plain SQL queries */
  implicit def GetResultScenariodetailRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Boolean], e4: GR[Option[Long]]): GR[ScenariodetailRow] = GR{
    prs => import prs._
    ScenariodetailRow.tupled((<<[Long], <<[Long], <<[String], <<?[String], <<[Boolean], <<[Boolean], <<[Boolean], <<[Long], <<?[Long], <<?[Long], <<?[Long]))
  }
  /** Table description of table scenariodetail. Objects of this class serve as prototypes for rows in queries. */
  class Scenariodetail(_tableTag: Tag) extends profile.api.Table[ScenariodetailRow](_tableTag, Some("offline"), "scenariodetail") {
    def * = (id, scenario, login, managerid, iscurrent, iscontractor, isoffshore, staffnumber, corplevel, businessunit, archetypeid) <> (ScenariodetailRow.tupled, ScenariodetailRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(scenario), Rep.Some(login), managerid, Rep.Some(iscurrent), Rep.Some(iscontractor), Rep.Some(isoffshore), Rep.Some(staffnumber), corplevel, businessunit, archetypeid).shaped.<>({r=>import r._; _1.map(_=> ScenariodetailRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7.get, _8.get, _9, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column scenario SqlType(BIGINT) */
    val scenario: Rep[Long] = column[Long]("scenario")
    /** Database column login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("login", O.Length(254,varying=true))
    /** Database column managerId SqlType(VARCHAR), Length(254,true), Default(None) */
    val managerid: Rep[Option[String]] = column[Option[String]]("managerId", O.Length(254,varying=true), O.Default(None))
    /** Database column isCurrent SqlType(BIT) */
    val iscurrent: Rep[Boolean] = column[Boolean]("isCurrent")
    /** Database column isContractor SqlType(BIT) */
    val iscontractor: Rep[Boolean] = column[Boolean]("isContractor")
    /** Database column isOffshore SqlType(BIT) */
    val isoffshore: Rep[Boolean] = column[Boolean]("isOffshore")
    /** Database column staffNumber SqlType(BIGINT) */
    val staffnumber: Rep[Long] = column[Long]("staffNumber")
    /** Database column corpLevel SqlType(BIGINT), Default(None) */
    val corplevel: Rep[Option[Long]] = column[Option[Long]]("corpLevel", O.Default(None))
    /** Database column businessUnit SqlType(BIGINT), Default(None) */
    val businessunit: Rep[Option[Long]] = column[Option[Long]]("businessUnit", O.Default(None))
    /** Database column archeTypeID SqlType(BIGINT), Default(None) */
    val archetypeid: Rep[Option[Long]] = column[Option[Long]]("archeTypeID", O.Default(None))
  }
  /** Collection-like TableQuery object for table Scenariodetail */
  lazy val Scenariodetail = new TableQuery(tag => new Scenariodetail(tag))

  /** Entity class storing rows of table Scenariolevel
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param scenario Database column scenario SqlType(BIGINT)
   *  @param layer Database column layer SqlType(BIGINT)
   *  @param managercount Database column managerCount SqlType(BIGINT)
   *  @param employeecount Database column employeeCount SqlType(BIGINT)
   *  @param offshorecount Database column offshoreCount SqlType(BIGINT)
   *  @param contractorcount Database column contractorCount SqlType(BIGINT)
   *  @param spanperlayeravg Database column spanPerLayerAvg SqlType(DOUBLE)
   *  @param spanperlayermin Database column spanPerLayerMin SqlType(DOUBLE)
   *  @param spanperlayermax Database column spanPerLayerMax SqlType(DOUBLE) */
  case class ScenariolevelRow(id: Long, scenario: Long, layer: Long, managercount: Long, employeecount: Long, offshorecount: Long, contractorcount: Long, spanperlayeravg: Double, spanperlayermin: Double, spanperlayermax: Double)
  /** GetResult implicit for fetching ScenariolevelRow objects using plain SQL queries */
  implicit def GetResultScenariolevelRow(implicit e0: GR[Long], e1: GR[Double]): GR[ScenariolevelRow] = GR{
    prs => import prs._
    ScenariolevelRow.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Double], <<[Double], <<[Double]))
  }
  /** Table description of table scenariolevel. Objects of this class serve as prototypes for rows in queries. */
  class Scenariolevel(_tableTag: Tag) extends profile.api.Table[ScenariolevelRow](_tableTag, Some("offline"), "scenariolevel") {
    def * = (id, scenario, layer, managercount, employeecount, offshorecount, contractorcount, spanperlayeravg, spanperlayermin, spanperlayermax) <> (ScenariolevelRow.tupled, ScenariolevelRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(scenario), Rep.Some(layer), Rep.Some(managercount), Rep.Some(employeecount), Rep.Some(offshorecount), Rep.Some(contractorcount), Rep.Some(spanperlayeravg), Rep.Some(spanperlayermin), Rep.Some(spanperlayermax)).shaped.<>({r=>import r._; _1.map(_=> ScenariolevelRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column scenario SqlType(BIGINT) */
    val scenario: Rep[Long] = column[Long]("scenario")
    /** Database column layer SqlType(BIGINT) */
    val layer: Rep[Long] = column[Long]("layer")
    /** Database column managerCount SqlType(BIGINT) */
    val managercount: Rep[Long] = column[Long]("managerCount")
    /** Database column employeeCount SqlType(BIGINT) */
    val employeecount: Rep[Long] = column[Long]("employeeCount")
    /** Database column offshoreCount SqlType(BIGINT) */
    val offshorecount: Rep[Long] = column[Long]("offshoreCount")
    /** Database column contractorCount SqlType(BIGINT) */
    val contractorcount: Rep[Long] = column[Long]("contractorCount")
    /** Database column spanPerLayerAvg SqlType(DOUBLE) */
    val spanperlayeravg: Rep[Double] = column[Double]("spanPerLayerAvg")
    /** Database column spanPerLayerMin SqlType(DOUBLE) */
    val spanperlayermin: Rep[Double] = column[Double]("spanPerLayerMin")
    /** Database column spanPerLayerMax SqlType(DOUBLE) */
    val spanperlayermax: Rep[Double] = column[Double]("spanPerLayerMax")
  }
  /** Collection-like TableQuery object for table Scenariolevel */
  lazy val Scenariolevel = new TableQuery(tag => new Scenariolevel(tag))

  /** Entity class storing rows of table Surveyanswer
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param set Database column set SqlType(BIGINT)
   *  @param setinstanceperson Database column setInstancePerson SqlType(BIGINT)
   *  @param question Database column question SqlType(BIGINT)
   *  @param value Database column value SqlType(INT) */
  case class SurveyanswerRow(id: Long, set: Long, setinstanceperson: Long, question: Long, value: Int)
  /** GetResult implicit for fetching SurveyanswerRow objects using plain SQL queries */
  implicit def GetResultSurveyanswerRow(implicit e0: GR[Long], e1: GR[Int]): GR[SurveyanswerRow] = GR{
    prs => import prs._
    SurveyanswerRow.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<[Int]))
  }
  /** Table description of table surveyanswer. Objects of this class serve as prototypes for rows in queries. */
  class Surveyanswer(_tableTag: Tag) extends profile.api.Table[SurveyanswerRow](_tableTag, Some("offline"), "surveyanswer") {
    def * = (id, set, setinstanceperson, question, value) <> (SurveyanswerRow.tupled, SurveyanswerRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(set), Rep.Some(setinstanceperson), Rep.Some(question), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> SurveyanswerRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column set SqlType(BIGINT) */
    val set: Rep[Long] = column[Long]("set")
    /** Database column setInstancePerson SqlType(BIGINT) */
    val setinstanceperson: Rep[Long] = column[Long]("setInstancePerson")
    /** Database column question SqlType(BIGINT) */
    val question: Rep[Long] = column[Long]("question")
    /** Database column value SqlType(INT) */
    val value: Rep[Int] = column[Int]("value")

    /** Foreign key referencing Surveyquestion (database name sa_quFK) */
    lazy val surveyquestionFk = foreignKey("sa_quFK", question, Surveyquestion)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Surveyset (database name sa_setFK) */
    lazy val surveysetFk = foreignKey("sa_setFK", set, Surveyset)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Surveysetperson (database name sa_siFK) */
    lazy val surveysetpersonFk = foreignKey("sa_siFK", setinstanceperson, Surveysetperson)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Surveyanswer */
  lazy val Surveyanswer = new TableQuery(tag => new Surveyanswer(tag))

  /** Entity class storing rows of table Surveycategory
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param set Database column set SqlType(BIGINT)
   *  @param name Database column name SqlType(VARCHAR), Length(254,true)
   *  @param description Database column description SqlType(TEXT)
   *  @param disabled Database column disabled SqlType(BIT), Default(false) */
  case class SurveycategoryRow(id: Long, set: Long, name: String, description: String, disabled: Boolean = false)
  /** GetResult implicit for fetching SurveycategoryRow objects using plain SQL queries */
  implicit def GetResultSurveycategoryRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Boolean]): GR[SurveycategoryRow] = GR{
    prs => import prs._
    SurveycategoryRow.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[Boolean]))
  }
  /** Table description of table surveycategory. Objects of this class serve as prototypes for rows in queries. */
  class Surveycategory(_tableTag: Tag) extends profile.api.Table[SurveycategoryRow](_tableTag, Some("offline"), "surveycategory") {
    def * = (id, set, name, description, disabled) <> (SurveycategoryRow.tupled, SurveycategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(set), Rep.Some(name), Rep.Some(description), Rep.Some(disabled)).shaped.<>({r=>import r._; _1.map(_=> SurveycategoryRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column set SqlType(BIGINT) */
    val set: Rep[Long] = column[Long]("set")
    /** Database column name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("name", O.Length(254,varying=true))
    /** Database column description SqlType(TEXT) */
    val description: Rep[String] = column[String]("description")
    /** Database column disabled SqlType(BIT), Default(false) */
    val disabled: Rep[Boolean] = column[Boolean]("disabled", O.Default(false))

    /** Foreign key referencing Surveyset (database name sc_setFK) */
    lazy val surveysetFk = foreignKey("sc_setFK", set, Surveyset)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Surveycategory */
  lazy val Surveycategory = new TableQuery(tag => new Surveycategory(tag))

  /** Entity class storing rows of table Surveyquestion
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param category Database column category SqlType(BIGINT)
   *  @param name Database column name SqlType(VARCHAR), Length(254,true)
   *  @param disabled Database column disabled SqlType(BIT), Default(false) */
  case class SurveyquestionRow(id: Long, category: Long, name: String, disabled: Boolean = false)
  /** GetResult implicit for fetching SurveyquestionRow objects using plain SQL queries */
  implicit def GetResultSurveyquestionRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Boolean]): GR[SurveyquestionRow] = GR{
    prs => import prs._
    SurveyquestionRow.tupled((<<[Long], <<[Long], <<[String], <<[Boolean]))
  }
  /** Table description of table surveyquestion. Objects of this class serve as prototypes for rows in queries. */
  class Surveyquestion(_tableTag: Tag) extends profile.api.Table[SurveyquestionRow](_tableTag, Some("offline"), "surveyquestion") {
    def * = (id, category, name, disabled) <> (SurveyquestionRow.tupled, SurveyquestionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(category), Rep.Some(name), Rep.Some(disabled)).shaped.<>({r=>import r._; _1.map(_=> SurveyquestionRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column category SqlType(BIGINT) */
    val category: Rep[Long] = column[Long]("category")
    /** Database column name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("name", O.Length(254,varying=true))
    /** Database column disabled SqlType(BIT), Default(false) */
    val disabled: Rep[Boolean] = column[Boolean]("disabled", O.Default(false))

    /** Foreign key referencing Surveycategory (database name sq_catFK) */
    lazy val surveycategoryFk = foreignKey("sq_catFK", category, Surveycategory)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Surveyquestion */
  lazy val Surveyquestion = new TableQuery(tag => new Surveyquestion(tag))

  /** Entity class storing rows of table Surveyset
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(254,true)
   *  @param description Database column description SqlType(TEXT)
   *  @param disabled Database column disabled SqlType(BIT), Default(false) */
  case class SurveysetRow(id: Long, name: String, description: String, disabled: Boolean = false)
  /** GetResult implicit for fetching SurveysetRow objects using plain SQL queries */
  implicit def GetResultSurveysetRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Boolean]): GR[SurveysetRow] = GR{
    prs => import prs._
    SurveysetRow.tupled((<<[Long], <<[String], <<[String], <<[Boolean]))
  }
  /** Table description of table surveyset. Objects of this class serve as prototypes for rows in queries. */
  class Surveyset(_tableTag: Tag) extends profile.api.Table[SurveysetRow](_tableTag, Some("offline"), "surveyset") {
    def * = (id, name, description, disabled) <> (SurveysetRow.tupled, SurveysetRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(description), Rep.Some(disabled)).shaped.<>({r=>import r._; _1.map(_=> SurveysetRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("name", O.Length(254,varying=true))
    /** Database column description SqlType(TEXT) */
    val description: Rep[String] = column[String]("description")
    /** Database column disabled SqlType(BIT), Default(false) */
    val disabled: Rep[Boolean] = column[Boolean]("disabled", O.Default(false))
  }
  /** Collection-like TableQuery object for table Surveyset */
  lazy val Surveyset = new TableQuery(tag => new Surveyset(tag))

  /** Entity class storing rows of table Surveysetinstance
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param set Database column set SqlType(BIGINT)
   *  @param forperson Database column forPerson SqlType(VARCHAR), Length(254,true)
   *  @param askedon Database column askedOn SqlType(DATE)
   *  @param closed Database column closed SqlType(BIT), Default(false) */
  case class SurveysetinstanceRow(id: Long, set: Long, forperson: String, askedon: java.sql.Date, closed: Boolean = false)
  /** GetResult implicit for fetching SurveysetinstanceRow objects using plain SQL queries */
  implicit def GetResultSurveysetinstanceRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date], e3: GR[Boolean]): GR[SurveysetinstanceRow] = GR{
    prs => import prs._
    SurveysetinstanceRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Date], <<[Boolean]))
  }
  /** Table description of table surveysetinstance. Objects of this class serve as prototypes for rows in queries. */
  class Surveysetinstance(_tableTag: Tag) extends profile.api.Table[SurveysetinstanceRow](_tableTag, Some("offline"), "surveysetinstance") {
    def * = (id, set, forperson, askedon, closed) <> (SurveysetinstanceRow.tupled, SurveysetinstanceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(set), Rep.Some(forperson), Rep.Some(askedon), Rep.Some(closed)).shaped.<>({r=>import r._; _1.map(_=> SurveysetinstanceRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column set SqlType(BIGINT) */
    val set: Rep[Long] = column[Long]("set")
    /** Database column forPerson SqlType(VARCHAR), Length(254,true) */
    val forperson: Rep[String] = column[String]("forPerson", O.Length(254,varying=true))
    /** Database column askedOn SqlType(DATE) */
    val askedon: Rep[java.sql.Date] = column[java.sql.Date]("askedOn")
    /** Database column closed SqlType(BIT), Default(false) */
    val closed: Rep[Boolean] = column[Boolean]("closed", O.Default(false))

    /** Foreign key referencing Surveyset (database name si_setFK) */
    lazy val surveysetFk = foreignKey("si_setFK", set, Surveyset)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Surveysetinstance */
  lazy val Surveysetinstance = new TableQuery(tag => new Surveysetinstance(tag))

  /** Entity class storing rows of table Surveysetperson
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param personrole Database column personRole SqlType(BIGINT)
   *  @param setinstance Database column setInstance SqlType(BIGINT)
   *  @param answeredon Database column answeredOn SqlType(DATE)
   *  @param frompersonanon Database column fromPersonANON SqlType(VARCHAR), Length(254,true) */
  case class SurveysetpersonRow(id: Long, personrole: Long, setinstance: Long, answeredon: java.sql.Date, frompersonanon: String)
  /** GetResult implicit for fetching SurveysetpersonRow objects using plain SQL queries */
  implicit def GetResultSurveysetpersonRow(implicit e0: GR[Long], e1: GR[java.sql.Date], e2: GR[String]): GR[SurveysetpersonRow] = GR{
    prs => import prs._
    SurveysetpersonRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Date], <<[String]))
  }
  /** Table description of table surveysetperson. Objects of this class serve as prototypes for rows in queries. */
  class Surveysetperson(_tableTag: Tag) extends profile.api.Table[SurveysetpersonRow](_tableTag, Some("offline"), "surveysetperson") {
    def * = (id, personrole, setinstance, answeredon, frompersonanon) <> (SurveysetpersonRow.tupled, SurveysetpersonRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(personrole), Rep.Some(setinstance), Rep.Some(answeredon), Rep.Some(frompersonanon)).shaped.<>({r=>import r._; _1.map(_=> SurveysetpersonRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column personRole SqlType(BIGINT) */
    val personrole: Rep[Long] = column[Long]("personRole")
    /** Database column setInstance SqlType(BIGINT) */
    val setinstance: Rep[Long] = column[Long]("setInstance")
    /** Database column answeredOn SqlType(DATE) */
    val answeredon: Rep[java.sql.Date] = column[java.sql.Date]("answeredOn")
    /** Database column fromPersonANON SqlType(VARCHAR), Length(254,true) */
    val frompersonanon: Rep[String] = column[String]("fromPersonANON", O.Length(254,varying=true))

    /** Foreign key referencing Surveysetinstance (database name ssp_siFK) */
    lazy val surveysetinstanceFk = foreignKey("ssp_siFK", setinstance, Surveysetinstance)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Surveysetperson */
  lazy val Surveysetperson = new TableQuery(tag => new Surveysetperson(tag))

  /** Entity class storing rows of table Teamdescription
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param login Database column Login SqlType(VARCHAR), Length(254,true)
   *  @param dateadded Database column DateAdded SqlType(DATE)
   *  @param tagtext Database column TagText SqlType(VARCHAR), Length(254,true) */
  case class TeamdescriptionRow(id: Long, login: String, dateadded: java.sql.Date, tagtext: String)
  /** GetResult implicit for fetching TeamdescriptionRow objects using plain SQL queries */
  implicit def GetResultTeamdescriptionRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date]): GR[TeamdescriptionRow] = GR{
    prs => import prs._
    TeamdescriptionRow.tupled((<<[Long], <<[String], <<[java.sql.Date], <<[String]))
  }
  /** Table description of table teamdescription. Objects of this class serve as prototypes for rows in queries. */
  class Teamdescription(_tableTag: Tag) extends profile.api.Table[TeamdescriptionRow](_tableTag, Some("offline"), "teamdescription") {
    def * = (id, login, dateadded, tagtext) <> (TeamdescriptionRow.tupled, TeamdescriptionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), Rep.Some(dateadded), Rep.Some(tagtext)).shaped.<>({r=>import r._; _1.map(_=> TeamdescriptionRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Login SqlType(VARCHAR), Length(254,true) */
    val login: Rep[String] = column[String]("Login", O.Length(254,varying=true))
    /** Database column DateAdded SqlType(DATE) */
    val dateadded: Rep[java.sql.Date] = column[java.sql.Date]("DateAdded")
    /** Database column TagText SqlType(VARCHAR), Length(254,true) */
    val tagtext: Rep[String] = column[String]("TagText", O.Length(254,varying=true))

    /** Index over (login) (database name TD_Login) */
    val index1 = index("TD_Login", login)
  }
  /** Collection-like TableQuery object for table Teamdescription */
  lazy val Teamdescription = new TableQuery(tag => new Teamdescription(tag))
}
