package com.airtonjal.service

import akka.actor.{ActorLogging, Actor, ActorRefFactory}
import com.airtonjal.core.graph.{BreadthFirstSearch, Edge, Graph}
import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport
import spray.routing.{RoutingSettings, RejectionHandler, ExceptionHandler, HttpService}
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

  import Json4sProtocol._

  def actorRefFactory: ActorRefFactory = context

  val listRoute = {
    pathPrefix("graph") {
      path("list") {
        get {
          complete(new BreadthFirstSearch().calculateScores(graph))
        }
      } ~
        path("add" / IntNumber / IntNumber) { (vertex1, vertex2) =>
          post {
            graph = graph + Edge(vertex1, vertex2)
            complete("Edge added succesfully")
        }
      } ~ path("fraud" / IntNumber) { vertex =>
        put {
          complete("To be implemented")
        }
      }
    }
  }

}
