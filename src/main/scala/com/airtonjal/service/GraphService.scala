package com.airtonjal.service

import akka.actor.{Actor, ActorLogging, ActorRefFactory}
import com.airtonjal.core.graph.{BFSWithState, Edge, Graph}
import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport
import spray.routing.{ExceptionHandler, HttpService, RejectionHandler, RoutingSettings}
import spray.util.LoggingContext

/* Used to mix in Spray's Marshalling Support with json4s */
object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

/**
 * REST graph service
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
class GraphService(var graph: Graph) extends HttpService with Actor with ActorLogging {
  override def receive: Receive = runRoute(listRoute)(ExceptionHandler.default, RejectionHandler.Default, context,
    RoutingSettings.default, LoggingContext.fromActorRefFactory)

  def actorRefFactory: ActorRefFactory = context

  import Json4sProtocol._

  val cc = new BFSWithState(graph)
  cc.calculateScores()

  val listRoute = {
    pathPrefix("graph") {
      path("list") {
        get {
          complete(cc.scores)
        }
      } ~
        path("add" / IntNumber / IntNumber) { (vertex1, vertex2) =>
          post {
            cc + Edge(vertex1, vertex2)
            complete("Edge added succesfully")
        }
      } ~ path("fraud" / IntNumber) { vertex =>
        put {
          cc.fraud(vertex)
        }
      }
    }
  }

}
