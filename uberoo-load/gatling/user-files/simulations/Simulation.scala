import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration._

import java.util.UUID

class LoadTest extends Simulation {

  val meals_url = "http://localhost:8080"
  val orders_url = "http://localhost:8181"

  val consultMeals = scenario("Consult meals")
    .exec(http("Get meals").get(meals_url + "/meals"))
    .repeat(5) {
      exec(session => session.set("clientId", UUID.randomUUID().getMostSignificantBits().toString))
        .pause(5)
        .exec(http("ETA computation").post(orders_url + "/orders")
          .header("Content-Type", "application/json")
          .body(StringBody(session => buildRetrieve(session)))
          .asJSON)
    }

  def buildRetrieve(session: Session): String = {
    val clientId = session("clientId").as[String]
    raw"""{"clientId":"$clientId", "mealId":"1", "restaurantId":"1"}"""
  }
  // Ok up to 730, breaks at 740
  setUp(consultMeals.inject(rampUsers(730) over (2 minutes)))
}
