package controllers

import javax.inject.Inject

import models.people.{EmployeeRepo, OfficeRepo}
import models.product.ProductTrackRepo
import org.scalatestplus.play._
import play.api.db.slick.DatabaseConfigProvider
import play.api.test._
import play.api.test.Helpers._
import play.db.NamedDatabase

import scala.concurrent.ExecutionContext

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
/*
class HomeControllerSpec @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider,
 @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
 productTrackRepo: ProductTrackRepo,
 resourcePoolRepo: ResourcePoolRepo,
 officeRepo: OfficeRepo,
 employeeRepo: EmployeeRepo
 // resourcePoolRepository: ResourcePoolRepository

)(implicit ec: ExecutionContext) extends PlaySpec with OneAppPerTest {

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = app.injector.instanceOf[HomeController]
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the router" in {
      // Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
  }
}
*/
