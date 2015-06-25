package com.airtonjal.service

import akka.actor.Props
import org.scalatest.{Matchers, FlatSpec}
import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

/**
 * Graph REST service tests
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
class GraphServiceSpec extends FlatSpec with ScalatestRouteTest with Matchers {

  import akka.testkit.TestActorRef
  private val graphActor = system.actorOf(Props[GraphServiceImpl], "graph-service")
  private val actorRef = TestActorRef[GraphServiceImpl]
  private val graphService = actorRef.underlyingActor

  "Graph service" should "return OK status" in {
    Get("/graph/list") ~> graphService.graphRoute ~> check {
      status should equal(StatusCodes.OK)
    }
  }

  "Graph service" should "add edges" in {
    Post("/graph/list/add/1/2") ~> graphService.graphRoute ~> check {
      status should equal(StatusCodes.OK)
    }
  }

}
