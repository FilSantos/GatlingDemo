
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SinglePageArticSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://api.artic.edu")
    .acceptHeader("application/json")

  val scn = scenario("Single Page Load Test")
    .exec(
      http("GET single page")
        .get("/api/v1/artworks?page=1&limit=1")
        .check(status.is(200))
        .check(jsonPath("$.data[0].id").exists)
    )

  setUp(
    scn.inject(
      rampUsersPerSec(5).to(20).during(20.seconds),
      constantUsersPerSec(20).during(40.seconds)
    )
  ).protocols(httpProtocol)
}
