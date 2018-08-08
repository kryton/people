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
  lazy val schema: profile.SchemaDescription = Array(Authpermission.schema, Authpreference.schema, Authrole.schema, Authrolepermission.schema, Authuser.schema, Authuserpreference.schema, Awardnominationto.schema, Businessunit.schema, Corplevel.schema, Costcenter.schema, Empbio.schema, Empefficiency.schema, Emphistory.schema, Employeemilestone.schema, Employeeroster.schema, Emppayroll.schema, Emprelations.schema, Emptag.schema, Featureflag.schema, Functionalarea.schema, Gitcommit.schema, Gitissue.schema, Individualarchetype.schema, Individualbusinessunit.schema, Jiraissue.schema, Jiraparentissue.schema, Kudosto.schema, Managedclient.schema, Managedclientproductfeature.schema, Managerarchetype.schema, Matrixteam.schema, Matrixteammember.schema, Milestone.schema, Office.schema, Okrkeyresult.schema, Okrobjective.schema, PlayEvolutions.schema, Positiontype.schema, Productfeature.schema, Productfeatureflag.schema, Producttrack.schema, Producttrackfeature.schema, Profitcenter.schema, Project.schema, Projectarea.schema, Projectdependency.schema, Projectfeature.schema, Projecthighlight.schema, Projectperson.schema, Projectpersontype.schema, Projectrelease.schema, Projectrole.schema, Ratecard.schema, Ratecardrate.schema, Ratecardrole.schema, Releaseauthorization.schema, Releaseauthorizationtype.schema, Releasetype.schema, Releasetypeauthorizationpeople.schema, Resourcepool.schema, Resourceteam.schema, Resourceteamproductfeature.schema, Resourceteamproject.schema, Roadmapslack.schema, Scenario.schema, Scenariodetail.schema, Scenariolevel.schema, Stage.schema, Statuscolor.schema, Statusupdate.schema, Surveyanswer.schema, Surveycategory.schema, Surveyquestion.schema, Surveyset.schema, Surveysetinstance.schema, Surveysetperson.schema, Systemrole.schema, Teamdescription.schema).reduceLeft(_ ++ _)
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
  /** Table description of table AuthPermission. Objects of this class serve as prototypes for rows in queries. */
  class Authpermission(_tableTag: Tag) extends profile.api.Table[AuthpermissionRow](_tableTag, Some("offline"), "AuthPermission") {
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
  /** Table description of table AuthPreference. Objects of this class serve as prototypes for rows in queries. */
  class Authpreference(_tableTag: Tag) extends profile.api.Table[AuthpreferenceRow](_tableTag, Some("offline"), "AuthPreference") {
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
  /** Table description of table AuthRole. Objects of this class serve as prototypes for rows in queries. */
  class Authrole(_tableTag: Tag) extends profile.api.Table[AuthroleRow](_tableTag, Some("offline"), "AuthRole") {
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
  /** Table description of table AuthRolePermission. Objects of this class serve as prototypes for rows in queries. */
  class Authrolepermission(_tableTag: Tag) extends profile.api.Table[AuthrolepermissionRow](_tableTag, Some("offline"), "AuthRolePermission") {
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
  /** Table description of table AuthUser. Objects of this class serve as prototypes for rows in queries. */
  class Authuser(_tableTag: Tag) extends profile.api.Table[AuthuserRow](_tableTag, Some("offline"), "AuthUser") {
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
  /** Table description of table AuthUserPreference. Objects of this class serve as prototypes for rows in queries. */
  class Authuserpreference(_tableTag: Tag) extends profile.api.Table[AuthuserpreferenceRow](_tableTag, Some("offline"), "AuthUserPreference") {
    def * = (authpreferenceid, login) <> (AuthuserpreferenceRow.tupled, AuthuserpreferenceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(authpreferenceid), Rep.Some(login)).shaped.<>({r=>import r._; _1.map(_=> AuthuserpreferenceRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column authPreferenceid SqlType(BIGINT), Default(0) */
    val authpreferenceid: Rep[Long] = column[Long]("authPreferenceid", O.Default(0L))
    /** Database column login SqlType(VARCHAR), Length(20,true) */
    val login: Rep[String] = column[String]("login", O.Length(20,varying=true))

    /** Primary key of Authuserpreference (database name AuthUserPreference_PK) */
    val pk = primaryKey("AuthUserPreference_PK", (authpreferenceid, login))

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
  /** Table description of table BusinessUnit. Objects of this class serve as prototypes for rows in queries. */
  class Businessunit(_tableTag: Tag) extends profile.api.Table[BusinessunitRow](_tableTag, Some("offline"), "BusinessUnit") {
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
  /** Table description of table CorpLevel. Objects of this class serve as prototypes for rows in queries. */
  class Corplevel(_tableTag: Tag) extends profile.api.Table[CorplevelRow](_tableTag, Some("offline"), "CorpLevel") {
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
  /** Table description of table CostCenter. Objects of this class serve as prototypes for rows in queries. */
  class Costcenter(_tableTag: Tag) extends profile.api.Table[CostcenterRow](_tableTag, Some("offline"), "CostCenter") {
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
  /** Table description of table EmpBio. Objects of this class serve as prototypes for rows in queries. */
  class Empbio(_tableTag: Tag) extends profile.api.Table[EmpbioRow](_tableTag, Some("offline"), "EmpBio") {
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

  /** Entity class storing rows of table Empefficiency
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param month Database column month SqlType(INT)
   *  @param efficiency Database column efficiency SqlType(DECIMAL), Default(None) */
  case class EmpefficiencyRow(id: Long, month: Int, efficiency: Option[scala.math.BigDecimal] = None)
  /** GetResult implicit for fetching EmpefficiencyRow objects using plain SQL queries */
  implicit def GetResultEmpefficiencyRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Option[scala.math.BigDecimal]]): GR[EmpefficiencyRow] = GR{
    prs => import prs._
    EmpefficiencyRow.tupled((<<[Long], <<[Int], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table EmpEfficiency. Objects of this class serve as prototypes for rows in queries. */
  class Empefficiency(_tableTag: Tag) extends profile.api.Table[EmpefficiencyRow](_tableTag, Some("offline"), "EmpEfficiency") {
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
  /** Table description of table EmpHistory. Objects of this class serve as prototypes for rows in queries. */
  class Emphistory(_tableTag: Tag) extends profile.api.Table[EmphistoryRow](_tableTag, Some("offline"), "EmpHistory") {
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
  /** Table description of table EmployeeMilestone. Objects of this class serve as prototypes for rows in queries. */
  class Employeemilestone(_tableTag: Tag) extends profile.api.Table[EmployeemilestoneRow](_tableTag, Some("offline"), "EmployeeMilestone") {
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
  /** Table description of table EmployeeRoster. Objects of this class serve as prototypes for rows in queries. */
  class Employeeroster(_tableTag: Tag) extends profile.api.Table[EmployeerosterRow](_tableTag, Some("offline"), "EmployeeRoster") {
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
  /** Table description of table EmpPayroll. Objects of this class serve as prototypes for rows in queries. */
  class Emppayroll(_tableTag: Tag) extends profile.api.Table[EmppayrollRow](_tableTag, Some("offline"), "EmpPayroll") {
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
  /** Table description of table EmpRelations. Objects of this class serve as prototypes for rows in queries. */
  class Emprelations(_tableTag: Tag) extends profile.api.Table[EmprelationsRow](_tableTag, Some("offline"), "EmpRelations") {
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
  /** Table description of table EmpTag. Objects of this class serve as prototypes for rows in queries. */
  class Emptag(_tableTag: Tag) extends profile.api.Table[EmptagRow](_tableTag, Some("offline"), "EmpTag") {
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

  /** Entity class storing rows of table Featureflag
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0) */
  case class FeatureflagRow(id: Int, name: String, ordering: Long = 0L)
  /** GetResult implicit for fetching FeatureflagRow objects using plain SQL queries */
  implicit def GetResultFeatureflagRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[FeatureflagRow] = GR{
    prs => import prs._
    FeatureflagRow.tupled((<<[Int], <<[String], <<[Long]))
  }
  /** Table description of table featureflag. Objects of this class serve as prototypes for rows in queries. */
  class Featureflag(_tableTag: Tag) extends profile.api.Table[FeatureflagRow](_tableTag, Some("offline"), "featureflag") {
    def * = (id, name, ordering) <> (FeatureflagRow.tupled, FeatureflagRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> FeatureflagRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Long] = column[Long]("ordering", O.Default(0L))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Featureflag */
  lazy val Featureflag = new TableQuery(tag => new Featureflag(tag))

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
  /** Table description of table FunctionalArea. Objects of this class serve as prototypes for rows in queries. */
  class Functionalarea(_tableTag: Tag) extends profile.api.Table[FunctionalareaRow](_tableTag, Some("offline"), "FunctionalArea") {
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
  /** Table description of table GitCommit. Objects of this class serve as prototypes for rows in queries. */
  class Gitcommit(_tableTag: Tag) extends profile.api.Table[GitcommitRow](_tableTag, Some("offline"), "GitCommit") {
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
  /** Table description of table GitIssue. Objects of this class serve as prototypes for rows in queries. */
  class Gitissue(_tableTag: Tag) extends profile.api.Table[GitissueRow](_tableTag, Some("offline"), "GitIssue") {
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
  /** Table description of table IndividualArchetype. Objects of this class serve as prototypes for rows in queries. */
  class Individualarchetype(_tableTag: Tag) extends profile.api.Table[IndividualarchetypeRow](_tableTag, Some("offline"), "IndividualArchetype") {
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
  /** Table description of table IndividualBusinessUnit. Objects of this class serve as prototypes for rows in queries. */
  class Individualbusinessunit(_tableTag: Tag) extends profile.api.Table[IndividualbusinessunitRow](_tableTag, Some("offline"), "IndividualBusinessUnit") {
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
  /** Table description of table JiraIssue. Objects of this class serve as prototypes for rows in queries. */
  class Jiraissue(_tableTag: Tag) extends profile.api.Table[JiraissueRow](_tableTag, Some("offline"), "JiraIssue") {
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
  /** Table description of table JiraParentIssue. Objects of this class serve as prototypes for rows in queries. */
  class Jiraparentissue(_tableTag: Tag) extends profile.api.Table[JiraparentissueRow](_tableTag, Some("offline"), "JiraParentIssue") {
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
  /** Table description of table KudosTo. Objects of this class serve as prototypes for rows in queries. */
  class Kudosto(_tableTag: Tag) extends profile.api.Table[KudostoRow](_tableTag, Some("offline"), "KudosTo") {
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

  /** Entity class storing rows of table Managedclient
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param ismanaged Database column isManaged SqlType(BIT), Default(Some(true))
   *  @param msprojectname Database column msprojectname SqlType(VARCHAR), Length(30,true), Default(None) */
  case class ManagedclientRow(id: Int, name: String, ismanaged: Option[Boolean] = Some(true), msprojectname: Option[String] = None)
  /** GetResult implicit for fetching ManagedclientRow objects using plain SQL queries */
  implicit def GetResultManagedclientRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Boolean]], e3: GR[Option[String]]): GR[ManagedclientRow] = GR{
    prs => import prs._
    ManagedclientRow.tupled((<<[Int], <<[String], <<?[Boolean], <<?[String]))
  }
  /** Table description of table managedclient. Objects of this class serve as prototypes for rows in queries. */
  class Managedclient(_tableTag: Tag) extends profile.api.Table[ManagedclientRow](_tableTag, Some("offline"), "managedclient") {
    def * = (id, name, ismanaged, msprojectname) <> (ManagedclientRow.tupled, ManagedclientRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), ismanaged, msprojectname).shaped.<>({r=>import r._; _1.map(_=> ManagedclientRow.tupled((_1.get, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column isManaged SqlType(BIT), Default(Some(true)) */
    val ismanaged: Rep[Option[Boolean]] = column[Option[Boolean]]("isManaged", O.Default(Some(true)))
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
  case class ManagedclientproductfeatureRow(id: Int, productfeatureid: Int, managedclientid: Int, allocation: scala.math.BigDecimal = scala.math.BigDecimal("1.00"))
  /** GetResult implicit for fetching ManagedclientproductfeatureRow objects using plain SQL queries */
  implicit def GetResultManagedclientproductfeatureRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal]): GR[ManagedclientproductfeatureRow] = GR{
    prs => import prs._
    ManagedclientproductfeatureRow.tupled((<<[Int], <<[Int], <<[Int], <<[scala.math.BigDecimal]))
  }
  /** Table description of table managedclientproductfeature. Objects of this class serve as prototypes for rows in queries. */
  class Managedclientproductfeature(_tableTag: Tag) extends profile.api.Table[ManagedclientproductfeatureRow](_tableTag, Some("offline"), "managedclientproductfeature") {
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
  /** Table description of table ManagerArchetype. Objects of this class serve as prototypes for rows in queries. */
  class Managerarchetype(_tableTag: Tag) extends profile.api.Table[ManagerarchetypeRow](_tableTag, Some("offline"), "ManagerArchetype") {
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
   *  @param owner Database column owner SqlType(VARCHAR), Length(254,true), Default(None)
   *  @param parent Database column parent SqlType(BIGINT), Default(None) */
  case class MatrixteamRow(id: Long, name: String, ispe: Boolean = false, owner: Option[String] = None, parent: Option[Long] = None)
  /** GetResult implicit for fetching MatrixteamRow objects using plain SQL queries */
  implicit def GetResultMatrixteamRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Boolean], e3: GR[Option[String]], e4: GR[Option[Long]]): GR[MatrixteamRow] = GR{
    prs => import prs._
    MatrixteamRow.tupled((<<[Long], <<[String], <<[Boolean], <<?[String], <<?[Long]))
  }
  /** Table description of table MatrixTeam. Objects of this class serve as prototypes for rows in queries. */
  class Matrixteam(_tableTag: Tag) extends profile.api.Table[MatrixteamRow](_tableTag, Some("offline"), "MatrixTeam") {
    def * = (id, name, ispe, owner, parent) <> (MatrixteamRow.tupled, MatrixteamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ispe), owner, parent).shaped.<>({r=>import r._; _1.map(_=> MatrixteamRow.tupled((_1.get, _2.get, _3.get, _4, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(254,true) */
    val name: Rep[String] = column[String]("Name", O.Length(254,varying=true))
    /** Database column isPE SqlType(BIT), Default(false) */
    val ispe: Rep[Boolean] = column[Boolean]("isPE", O.Default(false))
    /** Database column owner SqlType(VARCHAR), Length(254,true), Default(None) */
    val owner: Rep[Option[String]] = column[Option[String]]("owner", O.Length(254,varying=true), O.Default(None))
    /** Database column parent SqlType(BIGINT), Default(None) */
    val parent: Rep[Option[Long]] = column[Option[Long]]("parent", O.Default(None))

    /** Foreign key referencing Matrixteam (database name PARENT) */
    lazy val matrixteamFk = foreignKey("PARENT", parent, Matrixteam)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

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
  /** Table description of table MatrixTeamMember. Objects of this class serve as prototypes for rows in queries. */
  class Matrixteammember(_tableTag: Tag) extends profile.api.Table[MatrixteammemberRow](_tableTag, Some("offline"), "MatrixTeamMember") {
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
  /** Table description of table Milestone. Objects of this class serve as prototypes for rows in queries. */
  class Milestone(_tableTag: Tag) extends profile.api.Table[MilestoneRow](_tableTag, Some("offline"), "Milestone") {
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
  /** Table description of table Office. Objects of this class serve as prototypes for rows in queries. */
  class Office(_tableTag: Tag) extends profile.api.Table[OfficeRow](_tableTag, Some("offline"), "Office") {
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
  /** Table description of table OKRKeyResult. Objects of this class serve as prototypes for rows in queries. */
  class Okrkeyresult(_tableTag: Tag) extends profile.api.Table[OkrkeyresultRow](_tableTag, Some("offline"), "OKRKeyResult") {
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
  /** Table description of table OKRObjective. Objects of this class serve as prototypes for rows in queries. */
  class Okrobjective(_tableTag: Tag) extends profile.api.Table[OkrobjectiveRow](_tableTag, Some("offline"), "OKRObjective") {
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
   *  @param applyScript Database column apply_script SqlType(TEXT), Default(None)
   *  @param revertScript Database column revert_script SqlType(TEXT), Default(None)
   *  @param state Database column state SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem SqlType(TEXT), Default(None) */
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
  /** Table description of table PositionType. Objects of this class serve as prototypes for rows in queries. */
  class Positiontype(_tableTag: Tag) extends profile.api.Table[PositiontypeRow](_tableTag, Some("offline"), "PositionType") {
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

  /** Entity class storing rows of table Productfeature
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(200,true)
   *  @param execsummary Database column execSummary SqlType(TEXT)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0)
   *  @param stageid Database column stageId SqlType(INT), Default(1)
   *  @param msprojectname Database column msprojectname SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param isactive Database column isActive SqlType(BIT), Default(Some(true))
   *  @param iscid Database column isCID SqlType(BIT), Default(Some(false))
   *  @param isanchor Database column isAnchor SqlType(BIT), Default(Some(false))
   *  @param mspriority Database column msPriority SqlType(INT), Default(Some(0))
   *  @param start Database column start SqlType(DATE), Default(None)
   *  @param finish Database column finish SqlType(DATE), Default(None) */
  case class ProductfeatureRow(id: Int, name: String, execsummary: String, ordering: Long = 0L, stageid: Int = 1, msprojectname: Option[String] = None, isactive: Option[Boolean] = Some(true), iscid: Option[Boolean] = Some(false), isanchor: Option[Boolean] = Some(false), mspriority: Option[Int] = Some(0), start: Option[java.sql.Date] = None, finish: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching ProductfeatureRow objects using plain SQL queries */
  implicit def GetResultProductfeatureRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long], e3: GR[Option[String]], e4: GR[Option[Boolean]], e5: GR[Option[Int]], e6: GR[Option[java.sql.Date]]): GR[ProductfeatureRow] = GR{
    prs => import prs._
    ProductfeatureRow.tupled((<<[Int], <<[String], <<[String], <<[Long], <<[Int], <<?[String], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Int], <<?[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table productfeature. Objects of this class serve as prototypes for rows in queries. */
  class Productfeature(_tableTag: Tag) extends profile.api.Table[ProductfeatureRow](_tableTag, Some("offline"), "productfeature") {
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
    val ordering: Rep[Long] = column[Long]("ordering", O.Default(0L))
    /** Database column stageId SqlType(INT), Default(1) */
    val stageid: Rep[Int] = column[Int]("stageId", O.Default(1))
    /** Database column msprojectname SqlType(VARCHAR), Length(200,true), Default(None) */
    val msprojectname: Rep[Option[String]] = column[Option[String]]("msprojectname", O.Length(200,varying=true), O.Default(None))
    /** Database column isActive SqlType(BIT), Default(Some(true)) */
    val isactive: Rep[Option[Boolean]] = column[Option[Boolean]]("isActive", O.Default(Some(true)))
    /** Database column isCID SqlType(BIT), Default(Some(false)) */
    val iscid: Rep[Option[Boolean]] = column[Option[Boolean]]("isCID", O.Default(Some(false)))
    /** Database column isAnchor SqlType(BIT), Default(Some(false)) */
    val isanchor: Rep[Option[Boolean]] = column[Option[Boolean]]("isAnchor", O.Default(Some(false)))
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
  case class ProductfeatureflagRow(id: Int, productfeatureid: Int, featureflagid: Int)
  /** GetResult implicit for fetching ProductfeatureflagRow objects using plain SQL queries */
  implicit def GetResultProductfeatureflagRow(implicit e0: GR[Int]): GR[ProductfeatureflagRow] = GR{
    prs => import prs._
    ProductfeatureflagRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table productfeatureflag. Objects of this class serve as prototypes for rows in queries. */
  class Productfeatureflag(_tableTag: Tag) extends profile.api.Table[ProductfeatureflagRow](_tableTag, Some("offline"), "productfeatureflag") {
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
  case class ProducttrackRow(id: Int, name: String, ordering: Long = 0L, msprojectname: Option[String] = None)
  /** GetResult implicit for fetching ProducttrackRow objects using plain SQL queries */
  implicit def GetResultProducttrackRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long], e3: GR[Option[String]]): GR[ProducttrackRow] = GR{
    prs => import prs._
    ProducttrackRow.tupled((<<[Int], <<[String], <<[Long], <<?[String]))
  }
  /** Table description of table producttrack. Objects of this class serve as prototypes for rows in queries. */
  class Producttrack(_tableTag: Tag) extends profile.api.Table[ProducttrackRow](_tableTag, Some("offline"), "producttrack") {
    def * = (id, name, ordering, msprojectname) <> (ProducttrackRow.tupled, ProducttrackRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering), msprojectname).shaped.<>({r=>import r._; _1.map(_=> ProducttrackRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column Ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Long] = column[Long]("Ordering", O.Default(0L))
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
  case class ProducttrackfeatureRow(id: Int, productfeatureid: Int, producttrackid: Int, allocation: scala.math.BigDecimal, priority: Int)
  /** GetResult implicit for fetching ProducttrackfeatureRow objects using plain SQL queries */
  implicit def GetResultProducttrackfeatureRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal]): GR[ProducttrackfeatureRow] = GR{
    prs => import prs._
    ProducttrackfeatureRow.tupled((<<[Int], <<[Int], <<[Int], <<[scala.math.BigDecimal], <<[Int]))
  }
  /** Table description of table producttrackfeature. Objects of this class serve as prototypes for rows in queries. */
  class Producttrackfeature(_tableTag: Tag) extends profile.api.Table[ProducttrackfeatureRow](_tableTag, Some("offline"), "producttrackfeature") {
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

  /** Entity class storing rows of table Profitcenter
   *  @param profitcenter Database column profitCenter SqlType(BIGINT), PrimaryKey
   *  @param shortname Database column shortname SqlType(VARCHAR), Length(50,true), Default(None) */
  case class ProfitcenterRow(profitcenter: Long, shortname: Option[String] = None)
  /** GetResult implicit for fetching ProfitcenterRow objects using plain SQL queries */
  implicit def GetResultProfitcenterRow(implicit e0: GR[Long], e1: GR[Option[String]]): GR[ProfitcenterRow] = GR{
    prs => import prs._
    ProfitcenterRow.tupled((<<[Long], <<?[String]))
  }
  /** Table description of table ProfitCenter. Objects of this class serve as prototypes for rows in queries. */
  class Profitcenter(_tableTag: Tag) extends profile.api.Table[ProfitcenterRow](_tableTag, Some("offline"), "ProfitCenter") {
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

  /** Entity class storing rows of table Project
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(200,true)
   *  @param execsummary Database column ExecSummary SqlType(MEDIUMTEXT), Length(16777215,true)
   *  @param currentstatusid Database column currentStatusId SqlType(INT)
   *  @param started Database column started SqlType(DATE), Default(None)
   *  @param finished Database column finished SqlType(DATE), Default(None)
   *  @param isactive Database column isActive SqlType(BIT), Default(true)
   *  @param productfeatureid Database column productFeatureId SqlType(INT), Default(1)
   *  @param msprojectname Database column msprojectname SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param constrainttype Database column constraintType SqlType(CHAR), Length(4,false), Default(ASAP)
   *  @param constraintdate Database column constraintDate SqlType(DATE), Default(None) */
  case class ProjectRow(id: Int, name: String, execsummary: String, currentstatusid: Int, started: Option[java.sql.Date] = None, finished: Option[java.sql.Date] = None, isactive: Boolean = true, productfeatureid: Int = 1, msprojectname: Option[String] = None, constrainttype: String = "ASAP", constraintdate: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Date]], e3: GR[Boolean], e4: GR[Option[String]]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Int], <<[String], <<[String], <<[Int], <<?[java.sql.Date], <<?[java.sql.Date], <<[Boolean], <<[Int], <<?[String], <<[String], <<?[java.sql.Date]))
  }
  /** Table description of table project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends profile.api.Table[ProjectRow](_tableTag, Some("offline"), "project") {
    def * = (id, name, execsummary, currentstatusid, started, finished, isactive, productfeatureid, msprojectname, constrainttype, constraintdate) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(execsummary), Rep.Some(currentstatusid), started, finished, Rep.Some(isactive), Rep.Some(productfeatureid), msprojectname, Rep.Some(constrainttype), constraintdate).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7.get, _8.get, _9, _10.get, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(200,true) */
    val name: Rep[String] = column[String]("Name", O.Length(200,varying=true))
    /** Database column ExecSummary SqlType(MEDIUMTEXT), Length(16777215,true) */
    val execsummary: Rep[String] = column[String]("ExecSummary", O.Length(16777215,varying=true))
    /** Database column currentStatusId SqlType(INT) */
    val currentstatusid: Rep[Int] = column[Int]("currentStatusId")
    /** Database column started SqlType(DATE), Default(None) */
    val started: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("started", O.Default(None))
    /** Database column finished SqlType(DATE), Default(None) */
    val finished: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("finished", O.Default(None))
    /** Database column isActive SqlType(BIT), Default(true) */
    val isactive: Rep[Boolean] = column[Boolean]("isActive", O.Default(true))
    /** Database column productFeatureId SqlType(INT), Default(1) */
    val productfeatureid: Rep[Int] = column[Int]("productFeatureId", O.Default(1))
    /** Database column msprojectname SqlType(VARCHAR), Length(200,true), Default(None) */
    val msprojectname: Rep[Option[String]] = column[Option[String]]("msprojectname", O.Length(200,varying=true), O.Default(None))
    /** Database column constraintType SqlType(CHAR), Length(4,false), Default(ASAP) */
    val constrainttype: Rep[String] = column[String]("constraintType", O.Length(4,varying=false), O.Default("ASAP"))
    /** Database column constraintDate SqlType(DATE), Default(None) */
    val constraintdate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("constraintDate", O.Default(None))

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
  case class ProjectareaRow(id: Int, projectid: Int, areaid: Int)
  /** GetResult implicit for fetching ProjectareaRow objects using plain SQL queries */
  implicit def GetResultProjectareaRow(implicit e0: GR[Int]): GR[ProjectareaRow] = GR{
    prs => import prs._
    ProjectareaRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table projectarea. Objects of this class serve as prototypes for rows in queries. */
  class Projectarea(_tableTag: Tag) extends profile.api.Table[ProjectareaRow](_tableTag, Some("offline"), "projectarea") {
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

  /** Entity class storing rows of table Projectdependency
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fromproject Database column fromProject SqlType(INT)
   *  @param toproject Database column toProject SqlType(INT)
   *  @param dependencytype Database column dependencytype SqlType(CHAR), Length(2,false), Default(FS) */
  case class ProjectdependencyRow(id: Long, fromproject: Int, toproject: Int, dependencytype: String = "FS")
  /** GetResult implicit for fetching ProjectdependencyRow objects using plain SQL queries */
  implicit def GetResultProjectdependencyRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[String]): GR[ProjectdependencyRow] = GR{
    prs => import prs._
    ProjectdependencyRow.tupled((<<[Long], <<[Int], <<[Int], <<[String]))
  }
  /** Table description of table ProjectDependency. Objects of this class serve as prototypes for rows in queries. */
  class Projectdependency(_tableTag: Tag) extends profile.api.Table[ProjectdependencyRow](_tableTag, Some("offline"), "ProjectDependency") {
    def * = (id, fromproject, toproject, dependencytype) <> (ProjectdependencyRow.tupled, ProjectdependencyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(fromproject), Rep.Some(toproject), Rep.Some(dependencytype)).shaped.<>({r=>import r._; _1.map(_=> ProjectdependencyRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column fromProject SqlType(INT) */
    val fromproject: Rep[Int] = column[Int]("fromProject")
    /** Database column toProject SqlType(INT) */
    val toproject: Rep[Int] = column[Int]("toProject")
    /** Database column dependencytype SqlType(CHAR), Length(2,false), Default(FS) */
    val dependencytype: Rep[String] = column[String]("dependencytype", O.Length(2,varying=false), O.Default("FS"))

    /** Foreign key referencing Project (database name ProjectDependency_ibfk_1) */
    lazy val projectFk1 = foreignKey("ProjectDependency_ibfk_1", fromproject, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Project (database name ProjectDependency_ibfk_2) */
    lazy val projectFk2 = foreignKey("ProjectDependency_ibfk_2", toproject, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Projectdependency */
  lazy val Projectdependency = new TableQuery(tag => new Projectdependency(tag))

  /** Entity class storing rows of table Projectfeature
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param releaseid Database column releaseId SqlType(INT)
   *  @param parentfeatureid Database column parentFeatureId SqlType(INT), Default(None)
   *  @param feature Database column feature SqlType(VARCHAR), Length(45,true)
   *  @param status Database column status SqlType(INT), Default(None)
   *  @param timeline Database column timeLine SqlType(DATE), Default(None)
   *  @param timelinestring Database column timeLineString SqlType(VARCHAR), Length(45,true), Default(None)
   *  @param hidden Database column hidden SqlType(BIT), Default(Some(false)) */
  case class ProjectfeatureRow(id: Int, projectid: Int, releaseid: Int, parentfeatureid: Option[Int] = None, feature: String, status: Option[Int] = None, timeline: Option[java.sql.Date] = None, timelinestring: Option[String] = None, hidden: Option[Boolean] = Some(false))
  /** GetResult implicit for fetching ProjectfeatureRow objects using plain SQL queries */
  implicit def GetResultProjectfeatureRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[String], e3: GR[Option[java.sql.Date]], e4: GR[Option[String]], e5: GR[Option[Boolean]]): GR[ProjectfeatureRow] = GR{
    prs => import prs._
    ProjectfeatureRow.tupled((<<[Int], <<[Int], <<[Int], <<?[Int], <<[String], <<?[Int], <<?[java.sql.Date], <<?[String], <<?[Boolean]))
  }
  /** Table description of table projectfeature. Objects of this class serve as prototypes for rows in queries. */
  class Projectfeature(_tableTag: Tag) extends profile.api.Table[ProjectfeatureRow](_tableTag, Some("offline"), "projectfeature") {
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
    /** Database column hidden SqlType(BIT), Default(Some(false)) */
    val hidden: Rep[Option[Boolean]] = column[Option[Boolean]]("hidden", O.Default(Some(false)))

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
  case class ProjecthighlightRow(id: Int, projectid: Int, updated: java.sql.Date, memo: String)
  /** GetResult implicit for fetching ProjecthighlightRow objects using plain SQL queries */
  implicit def GetResultProjecthighlightRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[String]): GR[ProjecthighlightRow] = GR{
    prs => import prs._
    ProjecthighlightRow.tupled((<<[Int], <<[Int], <<[java.sql.Date], <<[String]))
  }
  /** Table description of table projecthighlight. Objects of this class serve as prototypes for rows in queries. */
  class Projecthighlight(_tableTag: Tag) extends profile.api.Table[ProjecthighlightRow](_tableTag, Some("offline"), "projecthighlight") {
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
  case class ProjectpersonRow(id: Int, projectid: Int, projectpersontypeid: Int, alias: String, started: Option[java.sql.Date] = None, finished: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching ProjectpersonRow objects using plain SQL queries */
  implicit def GetResultProjectpersonRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Date]]): GR[ProjectpersonRow] = GR{
    prs => import prs._
    ProjectpersonRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<?[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table projectperson. Objects of this class serve as prototypes for rows in queries. */
  class Projectperson(_tableTag: Tag) extends profile.api.Table[ProjectpersonRow](_tableTag, Some("offline"), "projectperson") {
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
  case class ProjectpersontypeRow(id: Int, name: String, ordering: Long = 0L)
  /** GetResult implicit for fetching ProjectpersontypeRow objects using plain SQL queries */
  implicit def GetResultProjectpersontypeRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[ProjectpersontypeRow] = GR{
    prs => import prs._
    ProjectpersontypeRow.tupled((<<[Int], <<[String], <<[Long]))
  }
  /** Table description of table projectpersontype. Objects of this class serve as prototypes for rows in queries. */
  class Projectpersontype(_tableTag: Tag) extends profile.api.Table[ProjectpersontypeRow](_tableTag, Some("offline"), "projectpersontype") {
    def * = (id, name, ordering) <> (ProjectpersontypeRow.tupled, ProjectpersontypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> ProjectpersontypeRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Long] = column[Long]("ordering", O.Default(0L))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Projectpersontype */
  lazy val Projectpersontype = new TableQuery(tag => new Projectpersontype(tag))

  /** Entity class storing rows of table Projectrelease
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param deployed Database column Deployed SqlType(DATETIME), Default(None)
   *  @param planned Database column Planned SqlType(DATETIME), Default(None)
   *  @param releasetypeid Database column ReleaseTypeId SqlType(INT)
   *  @param changeticket Database column changeticket SqlType(VARCHAR), Length(50,true), Default(None)
   *  @param releasefileurl Database column releasefileURL SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param releasenotes Database column releasenotes SqlType(TEXT), Default(None)
   *  @param rolledback Database column RolledBack SqlType(DATETIME), Default(None) */
  case class ProjectreleaseRow(id: Int, name: String, deployed: Option[java.sql.Timestamp] = None, planned: Option[java.sql.Timestamp] = None, releasetypeid: Int, changeticket: Option[String] = None, releasefileurl: Option[String] = None, releasenotes: Option[String] = None, rolledback: Option[java.sql.Timestamp] = None)
  /** GetResult implicit for fetching ProjectreleaseRow objects using plain SQL queries */
  implicit def GetResultProjectreleaseRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[String]]): GR[ProjectreleaseRow] = GR{
    prs => import prs._
    ProjectreleaseRow.tupled((<<[Int], <<[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Int], <<?[String], <<?[String], <<?[String], <<?[java.sql.Timestamp]))
  }
  /** Table description of table projectrelease. Objects of this class serve as prototypes for rows in queries. */
  class Projectrelease(_tableTag: Tag) extends profile.api.Table[ProjectreleaseRow](_tableTag, Some("offline"), "projectrelease") {
    def * = (id, name, deployed, planned, releasetypeid, changeticket, releasefileurl, releasenotes, rolledback) <> (ProjectreleaseRow.tupled, ProjectreleaseRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), deployed, planned, Rep.Some(releasetypeid), changeticket, releasefileurl, releasenotes, rolledback).shaped.<>({r=>import r._; _1.map(_=> ProjectreleaseRow.tupled((_1.get, _2.get, _3, _4, _5.get, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column Deployed SqlType(DATETIME), Default(None) */
    val deployed: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("Deployed", O.Default(None))
    /** Database column Planned SqlType(DATETIME), Default(None) */
    val planned: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("Planned", O.Default(None))
    /** Database column ReleaseTypeId SqlType(INT) */
    val releasetypeid: Rep[Int] = column[Int]("ReleaseTypeId")
    /** Database column changeticket SqlType(VARCHAR), Length(50,true), Default(None) */
    val changeticket: Rep[Option[String]] = column[Option[String]]("changeticket", O.Length(50,varying=true), O.Default(None))
    /** Database column releasefileURL SqlType(VARCHAR), Length(255,true), Default(None) */
    val releasefileurl: Rep[Option[String]] = column[Option[String]]("releasefileURL", O.Length(255,varying=true), O.Default(None))
    /** Database column releasenotes SqlType(TEXT), Default(None) */
    val releasenotes: Rep[Option[String]] = column[Option[String]]("releasenotes", O.Default(None))
    /** Database column RolledBack SqlType(DATETIME), Default(None) */
    val rolledback: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("RolledBack", O.Default(None))

    /** Foreign key referencing Releasetype (database name projectrelease_ibfk_1) */
    lazy val releasetypeFk = foreignKey("projectrelease_ibfk_1", releasetypeid, Releasetype)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Projectrelease */
  lazy val Projectrelease = new TableQuery(tag => new Projectrelease(tag))

  /** Entity class storing rows of table Projectrole
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param projectid Database column projectId SqlType(INT)
   *  @param roleid Database column roleId SqlType(INT)
   *  @param updated Database column updated SqlType(DATE)
   *  @param headcount Database column headCount SqlType(DECIMAL), Default(None) */
  case class ProjectroleRow(id: Int, projectid: Int, roleid: Int, updated: java.sql.Date, headcount: Option[scala.math.BigDecimal] = None)
  /** GetResult implicit for fetching ProjectroleRow objects using plain SQL queries */
  implicit def GetResultProjectroleRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[Option[scala.math.BigDecimal]]): GR[ProjectroleRow] = GR{
    prs => import prs._
    ProjectroleRow.tupled((<<[Int], <<[Int], <<[Int], <<[java.sql.Date], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table projectrole. Objects of this class serve as prototypes for rows in queries. */
  class Projectrole(_tableTag: Tag) extends profile.api.Table[ProjectroleRow](_tableTag, Some("offline"), "projectrole") {
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

  /** Entity class storing rows of table Ratecard
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param companyname Database column CompanyName SqlType(VARCHAR), Length(254,true) */
  case class RatecardRow(id: Long, companyname: String)
  /** GetResult implicit for fetching RatecardRow objects using plain SQL queries */
  implicit def GetResultRatecardRow(implicit e0: GR[Long], e1: GR[String]): GR[RatecardRow] = GR{
    prs => import prs._
    RatecardRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table RateCard. Objects of this class serve as prototypes for rows in queries. */
  class Ratecard(_tableTag: Tag) extends profile.api.Table[RatecardRow](_tableTag, Some("offline"), "RateCard") {
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
  /** Table description of table RateCardRate. Objects of this class serve as prototypes for rows in queries. */
  class Ratecardrate(_tableTag: Tag) extends profile.api.Table[RatecardrateRow](_tableTag, Some("offline"), "RateCardRate") {
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
  /** Table description of table RateCardRole. Objects of this class serve as prototypes for rows in queries. */
  class Ratecardrole(_tableTag: Tag) extends profile.api.Table[RatecardroleRow](_tableTag, Some("offline"), "RateCardRole") {
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

  /** Entity class storing rows of table Releaseauthorization
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param releaseid Database column ReleaseId SqlType(INT)
   *  @param releaseauthorityid Database column ReleaseAuthorityId SqlType(INT)
   *  @param login Database column login SqlType(VARCHAR), Length(20,true)
   *  @param approveddate Database column approvedDate SqlType(DATETIME), Default(None)
   *  @param rejecteddate Database column rejectedDate SqlType(DATETIME), Default(None)
   *  @param isapproved Database column isApproved SqlType(BIT), Default(None)
   *  @param notes Database column notes SqlType(TEXT), Default(None) */
  case class ReleaseauthorizationRow(id: Int, releaseid: Int, releaseauthorityid: Int, login: String, approveddate: Option[java.sql.Timestamp] = None, rejecteddate: Option[java.sql.Timestamp] = None, isapproved: Option[Boolean] = None, notes: Option[String] = None)
  /** GetResult implicit for fetching ReleaseauthorizationRow objects using plain SQL queries */
  implicit def GetResultReleaseauthorizationRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[Boolean]], e4: GR[Option[String]]): GR[ReleaseauthorizationRow] = GR{
    prs => import prs._
    ReleaseauthorizationRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[Boolean], <<?[String]))
  }
  /** Table description of table ReleaseAuthorization. Objects of this class serve as prototypes for rows in queries. */
  class Releaseauthorization(_tableTag: Tag) extends profile.api.Table[ReleaseauthorizationRow](_tableTag, Some("offline"), "ReleaseAuthorization") {
    def * = (id, releaseid, releaseauthorityid, login, approveddate, rejecteddate, isapproved, notes) <> (ReleaseauthorizationRow.tupled, ReleaseauthorizationRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(releaseid), Rep.Some(releaseauthorityid), Rep.Some(login), approveddate, rejecteddate, isapproved, notes).shaped.<>({r=>import r._; _1.map(_=> ReleaseauthorizationRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column ReleaseId SqlType(INT) */
    val releaseid: Rep[Int] = column[Int]("ReleaseId")
    /** Database column ReleaseAuthorityId SqlType(INT) */
    val releaseauthorityid: Rep[Int] = column[Int]("ReleaseAuthorityId")
    /** Database column login SqlType(VARCHAR), Length(20,true) */
    val login: Rep[String] = column[String]("login", O.Length(20,varying=true))
    /** Database column approvedDate SqlType(DATETIME), Default(None) */
    val approveddate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("approvedDate", O.Default(None))
    /** Database column rejectedDate SqlType(DATETIME), Default(None) */
    val rejecteddate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("rejectedDate", O.Default(None))
    /** Database column isApproved SqlType(BIT), Default(None) */
    val isapproved: Rep[Option[Boolean]] = column[Option[Boolean]]("isApproved", O.Default(None))
    /** Database column notes SqlType(TEXT), Default(None) */
    val notes: Rep[Option[String]] = column[Option[String]]("notes", O.Default(None))

    /** Foreign key referencing Releaseauthorizationtype (database name ReleaseAuth_type) */
    lazy val releaseauthorizationtypeFk = foreignKey("ReleaseAuth_type", releaseauthorityid, Releaseauthorizationtype)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projectrelease (database name ReleaseAuth_rel) */
    lazy val projectreleaseFk = foreignKey("ReleaseAuth_rel", releaseid, Projectrelease)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Releaseauthorization */
  lazy val Releaseauthorization = new TableQuery(tag => new Releaseauthorization(tag))

  /** Entity class storing rows of table Releaseauthorizationtype
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param description Database column description SqlType(TEXT), Default(None) */
  case class ReleaseauthorizationtypeRow(id: Int, name: String, description: Option[String] = None)
  /** GetResult implicit for fetching ReleaseauthorizationtypeRow objects using plain SQL queries */
  implicit def GetResultReleaseauthorizationtypeRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ReleaseauthorizationtypeRow] = GR{
    prs => import prs._
    ReleaseauthorizationtypeRow.tupled((<<[Int], <<[String], <<?[String]))
  }
  /** Table description of table ReleaseAuthorizationType. Objects of this class serve as prototypes for rows in queries. */
  class Releaseauthorizationtype(_tableTag: Tag) extends profile.api.Table[ReleaseauthorizationtypeRow](_tableTag, Some("offline"), "ReleaseAuthorizationType") {
    def * = (id, name, description) <> (ReleaseauthorizationtypeRow.tupled, ReleaseauthorizationtypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), description).shaped.<>({r=>import r._; _1.map(_=> ReleaseauthorizationtypeRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column description SqlType(TEXT), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))

    /** Uniqueness Index over (name) (database name name) */
    val index1 = index("name", name, unique=true)
  }
  /** Collection-like TableQuery object for table Releaseauthorizationtype */
  lazy val Releaseauthorizationtype = new TableQuery(tag => new Releaseauthorizationtype(tag))

  /** Entity class storing rows of table Releasetype
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param emailalias Database column emailAlias SqlType(VARCHAR), Length(255,true) */
  case class ReleasetypeRow(id: Int, name: String, emailalias: String)
  /** GetResult implicit for fetching ReleasetypeRow objects using plain SQL queries */
  implicit def GetResultReleasetypeRow(implicit e0: GR[Int], e1: GR[String]): GR[ReleasetypeRow] = GR{
    prs => import prs._
    ReleasetypeRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table ReleaseType. Objects of this class serve as prototypes for rows in queries. */
  class Releasetype(_tableTag: Tag) extends profile.api.Table[ReleasetypeRow](_tableTag, Some("offline"), "ReleaseType") {
    def * = (id, name, emailalias) <> (ReleasetypeRow.tupled, ReleasetypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(emailalias)).shaped.<>({r=>import r._; _1.map(_=> ReleasetypeRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column emailAlias SqlType(VARCHAR), Length(255,true) */
    val emailalias: Rep[String] = column[String]("emailAlias", O.Length(255,varying=true))

    /** Uniqueness Index over (name) (database name name) */
    val index1 = index("name", name, unique=true)
  }
  /** Collection-like TableQuery object for table Releasetype */
  lazy val Releasetype = new TableQuery(tag => new Releasetype(tag))

  /** Entity class storing rows of table Releasetypeauthorizationpeople
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param releasetypeid Database column ReleaseTypeId SqlType(INT)
   *  @param releaseauthorityid Database column ReleaseAuthorityId SqlType(INT)
   *  @param login Database column login SqlType(VARCHAR), Length(20,true)
   *  @param isprimary Database column isPrimary SqlType(BIT) */
  case class ReleasetypeauthorizationpeopleRow(id: Int, releasetypeid: Int, releaseauthorityid: Int, login: String, isprimary: Boolean)
  /** GetResult implicit for fetching ReleasetypeauthorizationpeopleRow objects using plain SQL queries */
  implicit def GetResultReleasetypeauthorizationpeopleRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean]): GR[ReleasetypeauthorizationpeopleRow] = GR{
    prs => import prs._
    ReleasetypeauthorizationpeopleRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[Boolean]))
  }
  /** Table description of table ReleaseTypeAuthorizationPeople. Objects of this class serve as prototypes for rows in queries. */
  class Releasetypeauthorizationpeople(_tableTag: Tag) extends profile.api.Table[ReleasetypeauthorizationpeopleRow](_tableTag, Some("offline"), "ReleaseTypeAuthorizationPeople") {
    def * = (id, releasetypeid, releaseauthorityid, login, isprimary) <> (ReleasetypeauthorizationpeopleRow.tupled, ReleasetypeauthorizationpeopleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(releasetypeid), Rep.Some(releaseauthorityid), Rep.Some(login), Rep.Some(isprimary)).shaped.<>({r=>import r._; _1.map(_=> ReleasetypeauthorizationpeopleRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column ReleaseTypeId SqlType(INT) */
    val releasetypeid: Rep[Int] = column[Int]("ReleaseTypeId")
    /** Database column ReleaseAuthorityId SqlType(INT) */
    val releaseauthorityid: Rep[Int] = column[Int]("ReleaseAuthorityId")
    /** Database column login SqlType(VARCHAR), Length(20,true) */
    val login: Rep[String] = column[String]("login", O.Length(20,varying=true))
    /** Database column isPrimary SqlType(BIT) */
    val isprimary: Rep[Boolean] = column[Boolean]("isPrimary")

    /** Foreign key referencing Releaseauthorizationtype (database name ReleaseAuthorizationType_fk1) */
    lazy val releaseauthorizationtypeFk = foreignKey("ReleaseAuthorizationType_fk1", releaseauthorityid, Releaseauthorizationtype)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Releasetype (database name ReleaseAuthorizationPPL_fk1) */
    lazy val releasetypeFk = foreignKey("ReleaseAuthorizationPPL_fk1", releasetypeid, Releasetype)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Releasetypeauthorizationpeople */
  lazy val Releasetypeauthorizationpeople = new TableQuery(tag => new Releasetypeauthorizationpeople(tag))

  /** Entity class storing rows of table Resourcepool
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0)
   *  @param poolsize Database column poolsize SqlType(INT UNSIGNED), Default(0) */
  case class ResourcepoolRow(id: Int, name: String, ordering: Long = 0L, poolsize: Long = 0L)
  /** GetResult implicit for fetching ResourcepoolRow objects using plain SQL queries */
  implicit def GetResultResourcepoolRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[ResourcepoolRow] = GR{
    prs => import prs._
    ResourcepoolRow.tupled((<<[Int], <<[String], <<[Long], <<[Long]))
  }
  /** Table description of table resourcepool. Objects of this class serve as prototypes for rows in queries. */
  class Resourcepool(_tableTag: Tag) extends profile.api.Table[ResourcepoolRow](_tableTag, Some("offline"), "resourcepool") {
    def * = (id, name, ordering, poolsize) <> (ResourcepoolRow.tupled, ResourcepoolRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering), Rep.Some(poolsize)).shaped.<>({r=>import r._; _1.map(_=> ResourcepoolRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Long] = column[Long]("ordering", O.Default(0L))
    /** Database column poolsize SqlType(INT UNSIGNED), Default(0) */
    val poolsize: Rep[Long] = column[Long]("poolsize", O.Default(0L))

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
  case class ResourceteamRow(id: Int, name: String, ordering: Long = 0L, teamsize: Long = 0L, resourcepoolid: Option[Int] = None, minimum: Long = 0L, maximum: Option[Long] = None, msprojectname: String = "", pplteamname: Option[String] = None)
  /** GetResult implicit for fetching ResourceteamRow objects using plain SQL queries */
  implicit def GetResultResourceteamRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long], e3: GR[Option[Int]], e4: GR[Option[Long]], e5: GR[Option[String]]): GR[ResourceteamRow] = GR{
    prs => import prs._
    ResourceteamRow.tupled((<<[Int], <<[String], <<[Long], <<[Long], <<?[Int], <<[Long], <<?[Long], <<[String], <<?[String]))
  }
  /** Table description of table resourceteam. Objects of this class serve as prototypes for rows in queries. */
  class Resourceteam(_tableTag: Tag) extends profile.api.Table[ResourceteamRow](_tableTag, Some("offline"), "resourceteam") {
    def * = (id, name, ordering, teamsize, resourcepoolid, minimum, maximum, msprojectname, pplteamname) <> (ResourceteamRow.tupled, ResourceteamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering), Rep.Some(teamsize), resourcepoolid, Rep.Some(minimum), maximum, Rep.Some(msprojectname), pplteamname).shaped.<>({r=>import r._; _1.map(_=> ResourceteamRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7, _8.get, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Long] = column[Long]("ordering", O.Default(0L))
    /** Database column teamsize SqlType(INT UNSIGNED), Default(0) */
    val teamsize: Rep[Long] = column[Long]("teamsize", O.Default(0L))
    /** Database column resourcepoolId SqlType(INT), Default(None) */
    val resourcepoolid: Rep[Option[Int]] = column[Option[Int]]("resourcepoolId", O.Default(None))
    /** Database column minimum SqlType(INT UNSIGNED), Default(0) */
    val minimum: Rep[Long] = column[Long]("minimum", O.Default(0L))
    /** Database column maximum SqlType(INT UNSIGNED), Default(None) */
    val maximum: Rep[Option[Long]] = column[Option[Long]]("maximum", O.Default(None))
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
  case class ResourceteamproductfeatureRow(id: Int, featuresize: Long = 0L, maxdevs: Long = 0L, resourceteamid: Int, productfeatureid: Int, featuresizeremaining: Option[scala.math.BigDecimal] = Some(scala.math.BigDecimal("0.00")))
  /** GetResult implicit for fetching ResourceteamproductfeatureRow objects using plain SQL queries */
  implicit def GetResultResourceteamproductfeatureRow(implicit e0: GR[Int], e1: GR[Long], e2: GR[Option[scala.math.BigDecimal]]): GR[ResourceteamproductfeatureRow] = GR{
    prs => import prs._
    ResourceteamproductfeatureRow.tupled((<<[Int], <<[Long], <<[Long], <<[Int], <<[Int], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table resourceteamproductfeature. Objects of this class serve as prototypes for rows in queries. */
  class Resourceteamproductfeature(_tableTag: Tag) extends profile.api.Table[ResourceteamproductfeatureRow](_tableTag, Some("offline"), "resourceteamproductfeature") {
    def * = (id, featuresize, maxdevs, resourceteamid, productfeatureid, featuresizeremaining) <> (ResourceteamproductfeatureRow.tupled, ResourceteamproductfeatureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(featuresize), Rep.Some(maxdevs), Rep.Some(resourceteamid), Rep.Some(productfeatureid), featuresizeremaining).shaped.<>({r=>import r._; _1.map(_=> ResourceteamproductfeatureRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column featureSize SqlType(INT UNSIGNED), Default(0) */
    val featuresize: Rep[Long] = column[Long]("featureSize", O.Default(0L))
    /** Database column maxDevs SqlType(INT UNSIGNED), Default(0) */
    val maxdevs: Rep[Long] = column[Long]("maxDevs", O.Default(0L))
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

  /** Entity class storing rows of table Resourceteamproject
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param featuresize Database column featureSize SqlType(INT UNSIGNED), Default(0)
   *  @param maxdevs Database column maxDevs SqlType(INT UNSIGNED), Default(0)
   *  @param resourceteamid Database column resourceTeamId SqlType(INT)
   *  @param projectid Database column projectId SqlType(INT)
   *  @param featuresizeremaining Database column featureSizeRemaining SqlType(DECIMAL), Default(Some(0.00)) */
  case class ResourceteamprojectRow(id: Int, featuresize: Long = 0L, maxdevs: Long = 0L, resourceteamid: Int, projectid: Int, featuresizeremaining: Option[scala.math.BigDecimal] = Some(scala.math.BigDecimal("0.00")))
  /** GetResult implicit for fetching ResourceteamprojectRow objects using plain SQL queries */
  implicit def GetResultResourceteamprojectRow(implicit e0: GR[Int], e1: GR[Long], e2: GR[Option[scala.math.BigDecimal]]): GR[ResourceteamprojectRow] = GR{
    prs => import prs._
    ResourceteamprojectRow.tupled((<<[Int], <<[Long], <<[Long], <<[Int], <<[Int], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table ResourceTeamProject. Objects of this class serve as prototypes for rows in queries. */
  class Resourceteamproject(_tableTag: Tag) extends profile.api.Table[ResourceteamprojectRow](_tableTag, Some("offline"), "ResourceTeamProject") {
    def * = (id, featuresize, maxdevs, resourceteamid, projectid, featuresizeremaining) <> (ResourceteamprojectRow.tupled, ResourceteamprojectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(featuresize), Rep.Some(maxdevs), Rep.Some(resourceteamid), Rep.Some(projectid), featuresizeremaining).shaped.<>({r=>import r._; _1.map(_=> ResourceteamprojectRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column featureSize SqlType(INT UNSIGNED), Default(0) */
    val featuresize: Rep[Long] = column[Long]("featureSize", O.Default(0L))
    /** Database column maxDevs SqlType(INT UNSIGNED), Default(0) */
    val maxdevs: Rep[Long] = column[Long]("maxDevs", O.Default(0L))
    /** Database column resourceTeamId SqlType(INT) */
    val resourceteamid: Rep[Int] = column[Int]("resourceTeamId")
    /** Database column projectId SqlType(INT) */
    val projectid: Rep[Int] = column[Int]("projectId")
    /** Database column featureSizeRemaining SqlType(DECIMAL), Default(Some(0.00)) */
    val featuresizeremaining: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("featureSizeRemaining", O.Default(Some(scala.math.BigDecimal("0.00"))))

    /** Foreign key referencing Project (database name resourceteamprojectfeature_ibfk_2) */
    lazy val projectFk = foreignKey("resourceteamprojectfeature_ibfk_2", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Resourceteam (database name resourceteamprojectfeature_ibfk_1) */
    lazy val resourceteamFk = foreignKey("resourceteamprojectfeature_ibfk_1", resourceteamid, Resourceteam)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Resourceteamproject */
  lazy val Resourceteamproject = new TableQuery(tag => new Resourceteamproject(tag))

  /** Entity class storing rows of table Roadmapslack
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param ordering Database column ordering SqlType(INT)
   *  @param efficiency Database column efficiency SqlType(DECIMAL), Default(None) */
  case class RoadmapslackRow(id: Long, ordering: Int, efficiency: Option[scala.math.BigDecimal] = None)
  /** GetResult implicit for fetching RoadmapslackRow objects using plain SQL queries */
  implicit def GetResultRoadmapslackRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Option[scala.math.BigDecimal]]): GR[RoadmapslackRow] = GR{
    prs => import prs._
    RoadmapslackRow.tupled((<<[Long], <<[Int], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table RoadmapSlack. Objects of this class serve as prototypes for rows in queries. */
  class Roadmapslack(_tableTag: Tag) extends profile.api.Table[RoadmapslackRow](_tableTag, Some("offline"), "RoadmapSlack") {
    def * = (id, ordering, efficiency) <> (RoadmapslackRow.tupled, RoadmapslackRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(ordering), efficiency).shaped.<>({r=>import r._; _1.map(_=> RoadmapslackRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column ordering SqlType(INT) */
    val ordering: Rep[Int] = column[Int]("ordering")
    /** Database column efficiency SqlType(DECIMAL), Default(None) */
    val efficiency: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("efficiency", O.Default(None))

    /** Uniqueness Index over (ordering) (database name ordering) */
    val index1 = index("ordering", ordering, unique=true)
  }
  /** Collection-like TableQuery object for table Roadmapslack */
  lazy val Roadmapslack = new TableQuery(tag => new Roadmapslack(tag))

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
  /** Table description of table Scenario. Objects of this class serve as prototypes for rows in queries. */
  class Scenario(_tableTag: Tag) extends profile.api.Table[ScenarioRow](_tableTag, Some("offline"), "Scenario") {
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
  /** Table description of table ScenarioDetail. Objects of this class serve as prototypes for rows in queries. */
  class Scenariodetail(_tableTag: Tag) extends profile.api.Table[ScenariodetailRow](_tableTag, Some("offline"), "ScenarioDetail") {
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
  /** Table description of table ScenarioLevel. Objects of this class serve as prototypes for rows in queries. */
  class Scenariolevel(_tableTag: Tag) extends profile.api.Table[ScenariolevelRow](_tableTag, Some("offline"), "ScenarioLevel") {
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

  /** Entity class storing rows of table Stage
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column ordering SqlType(INT UNSIGNED), Default(0) */
  case class StageRow(id: Int, name: String, ordering: Long = 0L)
  /** GetResult implicit for fetching StageRow objects using plain SQL queries */
  implicit def GetResultStageRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[StageRow] = GR{
    prs => import prs._
    StageRow.tupled((<<[Int], <<[String], <<[Long]))
  }
  /** Table description of table stage. Objects of this class serve as prototypes for rows in queries. */
  class Stage(_tableTag: Tag) extends profile.api.Table[StageRow](_tableTag, Some("offline"), "stage") {
    def * = (id, name, ordering) <> (StageRow.tupled, StageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> StageRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("name", O.Length(45,varying=true))
    /** Database column ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Long] = column[Long]("ordering", O.Default(0L))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Stage */
  lazy val Stage = new TableQuery(tag => new Stage(tag))

  /** Entity class storing rows of table Statuscolor
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param cssclassname Database column cssClassName SqlType(VARCHAR), Length(20,true)
   *  @param isontrack Database column IsOnTrack SqlType(BIT), Default(Some(false))
   *  @param iscomplete Database column IsComplete SqlType(BIT), Default(Some(false))
   *  @param hasissues Database column HasIssues SqlType(BIT), Default(Some(false))
   *  @param isatrisk Database column IsAtRisk SqlType(BIT), Default(Some(false))
   *  @param isonhold Database column IsOnHold SqlType(BIT), Default(Some(false))
   *  @param hasnotstarted Database column HasNotStarted SqlType(BIT), Default(Some(false)) */
  case class StatuscolorRow(id: Int, name: String, cssclassname: String, isontrack: Option[Boolean] = Some(false), iscomplete: Option[Boolean] = Some(false), hasissues: Option[Boolean] = Some(false), isatrisk: Option[Boolean] = Some(false), isonhold: Option[Boolean] = Some(false), hasnotstarted: Option[Boolean] = Some(false))
  /** GetResult implicit for fetching StatuscolorRow objects using plain SQL queries */
  implicit def GetResultStatuscolorRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Boolean]]): GR[StatuscolorRow] = GR{
    prs => import prs._
    StatuscolorRow.tupled((<<[Int], <<[String], <<[String], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Boolean], <<?[Boolean]))
  }
  /** Table description of table statuscolor. Objects of this class serve as prototypes for rows in queries. */
  class Statuscolor(_tableTag: Tag) extends profile.api.Table[StatuscolorRow](_tableTag, Some("offline"), "statuscolor") {
    def * = (id, name, cssclassname, isontrack, iscomplete, hasissues, isatrisk, isonhold, hasnotstarted) <> (StatuscolorRow.tupled, StatuscolorRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(cssclassname), isontrack, iscomplete, hasissues, isatrisk, isonhold, hasnotstarted).shaped.<>({r=>import r._; _1.map(_=> StatuscolorRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column cssClassName SqlType(VARCHAR), Length(20,true) */
    val cssclassname: Rep[String] = column[String]("cssClassName", O.Length(20,varying=true))
    /** Database column IsOnTrack SqlType(BIT), Default(Some(false)) */
    val isontrack: Rep[Option[Boolean]] = column[Option[Boolean]]("IsOnTrack", O.Default(Some(false)))
    /** Database column IsComplete SqlType(BIT), Default(Some(false)) */
    val iscomplete: Rep[Option[Boolean]] = column[Option[Boolean]]("IsComplete", O.Default(Some(false)))
    /** Database column HasIssues SqlType(BIT), Default(Some(false)) */
    val hasissues: Rep[Option[Boolean]] = column[Option[Boolean]]("HasIssues", O.Default(Some(false)))
    /** Database column IsAtRisk SqlType(BIT), Default(Some(false)) */
    val isatrisk: Rep[Option[Boolean]] = column[Option[Boolean]]("IsAtRisk", O.Default(Some(false)))
    /** Database column IsOnHold SqlType(BIT), Default(Some(false)) */
    val isonhold: Rep[Option[Boolean]] = column[Option[Boolean]]("IsOnHold", O.Default(Some(false)))
    /** Database column HasNotStarted SqlType(BIT), Default(Some(false)) */
    val hasnotstarted: Rep[Option[Boolean]] = column[Option[Boolean]]("HasNotStarted", O.Default(Some(false)))

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
  case class StatusupdateRow(id: Int, projectid: Int, statusid: Int, updated: java.sql.Date, comments: Option[String] = None, issues: Option[String] = None, risks: Option[String] = None, dependencies: Option[String] = None)
  /** GetResult implicit for fetching StatusupdateRow objects using plain SQL queries */
  implicit def GetResultStatusupdateRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[Option[String]]): GR[StatusupdateRow] = GR{
    prs => import prs._
    StatusupdateRow.tupled((<<[Int], <<[Int], <<[Int], <<[java.sql.Date], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table statusupdate. Objects of this class serve as prototypes for rows in queries. */
  class Statusupdate(_tableTag: Tag) extends profile.api.Table[StatusupdateRow](_tableTag, Some("offline"), "statusupdate") {
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
  /** Table description of table SurveyAnswer. Objects of this class serve as prototypes for rows in queries. */
  class Surveyanswer(_tableTag: Tag) extends profile.api.Table[SurveyanswerRow](_tableTag, Some("offline"), "SurveyAnswer") {
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
  /** Table description of table SurveyCategory. Objects of this class serve as prototypes for rows in queries. */
  class Surveycategory(_tableTag: Tag) extends profile.api.Table[SurveycategoryRow](_tableTag, Some("offline"), "SurveyCategory") {
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
  /** Table description of table SurveyQuestion. Objects of this class serve as prototypes for rows in queries. */
  class Surveyquestion(_tableTag: Tag) extends profile.api.Table[SurveyquestionRow](_tableTag, Some("offline"), "SurveyQuestion") {
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
  /** Table description of table SurveySet. Objects of this class serve as prototypes for rows in queries. */
  class Surveyset(_tableTag: Tag) extends profile.api.Table[SurveysetRow](_tableTag, Some("offline"), "SurveySet") {
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
  /** Table description of table SurveySetInstance. Objects of this class serve as prototypes for rows in queries. */
  class Surveysetinstance(_tableTag: Tag) extends profile.api.Table[SurveysetinstanceRow](_tableTag, Some("offline"), "SurveySetInstance") {
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
  /** Table description of table SurveySetPerson. Objects of this class serve as prototypes for rows in queries. */
  class Surveysetperson(_tableTag: Tag) extends profile.api.Table[SurveysetpersonRow](_tableTag, Some("offline"), "SurveySetPerson") {
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

  /** Entity class storing rows of table Systemrole
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column Name SqlType(VARCHAR), Length(45,true)
   *  @param ordering Database column Ordering SqlType(INT UNSIGNED), Default(0) */
  case class SystemroleRow(id: Int, name: String, ordering: Long = 0L)
  /** GetResult implicit for fetching SystemroleRow objects using plain SQL queries */
  implicit def GetResultSystemroleRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[SystemroleRow] = GR{
    prs => import prs._
    SystemroleRow.tupled((<<[Int], <<[String], <<[Long]))
  }
  /** Table description of table systemrole. Objects of this class serve as prototypes for rows in queries. */
  class Systemrole(_tableTag: Tag) extends profile.api.Table[SystemroleRow](_tableTag, Some("offline"), "systemrole") {
    def * = (id, name, ordering) <> (SystemroleRow.tupled, SystemroleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(ordering)).shaped.<>({r=>import r._; _1.map(_=> SystemroleRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Name SqlType(VARCHAR), Length(45,true) */
    val name: Rep[String] = column[String]("Name", O.Length(45,varying=true))
    /** Database column Ordering SqlType(INT UNSIGNED), Default(0) */
    val ordering: Rep[Long] = column[Long]("Ordering", O.Default(0L))

    /** Uniqueness Index over (name) (database name Name_UNIQUE) */
    val index1 = index("Name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Systemrole */
  lazy val Systemrole = new TableQuery(tag => new Systemrole(tag))

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
  /** Table description of table TeamDescription. Objects of this class serve as prototypes for rows in queries. */
  class Teamdescription(_tableTag: Tag) extends profile.api.Table[TeamdescriptionRow](_tableTag, Some("offline"), "TeamDescription") {
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
