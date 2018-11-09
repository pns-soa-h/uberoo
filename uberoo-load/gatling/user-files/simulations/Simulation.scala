import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration._

import java.util.UUID

class LoadTest extends Simulation {

  val meals_url = "http://localhost:8080"
  val orders_url = "http://localhost:8181"
  val deliveries_url = "http://localhost:8282"

  val consultMeals = scenario("Consult meals")
	.repeat(10)
	{
		exec(session => session.set("clientId", UUID.randomUUID().toString))
		.exec(http("Get meals").get("/meals"))
			.pause(5)
		.exec(http("ETA computation").post(orders_url + "/orders")
			.body(StringBody(session => buildRetrieve(session))))
	}
    
  def buildRetrieve(session: Session): String = {
  	val clientId = session("clientId").as[String]
  	raw"""'{"clientId":"'$clientId'", "mealId":"1", "restaurantId":"1"}'"""
  }

  setUp(consultMeals.inject(rampUsers(10) over (10 seconds)))
}
