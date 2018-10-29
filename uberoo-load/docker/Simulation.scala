import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class LoadTest extends Simulation {

  val meals_url = "http://localhost:8080"
  val orders_url = "http://localhost:8181"
  val deliveries_url = "http://localhost:8282"

  val consultMeals = scenario("Consult meals")
    .exec(http("Get meals")
      .get(meals_url + "/meals")
      .check(jsonPath("$")
        .saveAs("mealList")))
  //    .pause(1)
  //    .exec(http("Compute ETA")
  //      .post(eta_url + "/eta")
  //      .body(StringBody("""{ "calculateETA": { "meals": ${mealList} } }""")))

  setUp(consultMeals.inject(atOnceUsers(1)))
}
