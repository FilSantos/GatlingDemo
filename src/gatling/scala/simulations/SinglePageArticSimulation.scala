package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SinglePageArticSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://api.artic.edu")
    .acceptHeader("application/json")
    .userAgentHeader("Gatling Load Test")

  // Single page only: page=1&limit=1
  val scn = scenario("Single Page - Artworks API")
    .exec(
      http("GET /api/v1/artworks?page=1&limit=1")
        .get("/api/v1/artworks?page=1&limit=1")
        .check(status.is(200))
        .check(jsonPath("$.pagination.current_page").is("1"))
        .check(jsonPath("$.pagination.limit").is("1"))
        .check(jsonPath("$.data[0].id").exists)
        .check(jsonPath("$.data[0].title").exists)
    )

  setUp(
    scn.inject(
      rampUsersPerSec(5).to(20).during(20.seconds),
      constantUsersPerSec(20).during(40.seconds)
    )
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.percentile3.lte(1000), // p95 <= 1000ms
      global.successfulRequests.percent.gte(95),
      forAll.requestsPerSec.gte(10) // throughput goal: at least 10 req/s
    )
}
