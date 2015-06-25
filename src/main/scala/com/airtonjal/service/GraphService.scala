package com.airtonjal.service

import akka.actor.{Actor, ActorLogging, ActorRefFactory}
import com.airtonjal.core.graph.{GraphBuilder, BFSWithState, Edge}
import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport
import spray.routing.{ExceptionHandler, HttpService, RejectionHandler, RoutingSettings}
import spray.util.LoggingContext

import scala.io.Source

/* Used to mix in Spray's Marshalling Support with json4s */
object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

class GraphServiceImpl extends GraphService {
  override def receive: Receive = runRoute(graphRoute)(ExceptionHandler.default, RejectionHandler.Default, context,
    RoutingSettings.default, LoggingContext.fromActorRefFactory)

  def actorRefFactory: ActorRefFactory = context
}

/**
 * REST graph service
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
trait GraphService extends HttpService with Actor with ActorLogging {
  val FILENAME = "edges.txt"
  val stream = Source.fromURL(getClass().getResource("/" + FILENAME))
  val edgeSeq = stream.getLines().toSeq.map(line => line.split(" +")).map(v => Edge(v(0).toInt, v(1).toInt))
  val cc = new BFSWithState(GraphBuilder.createGraph(edgeSeq))
  cc.calculateScores()

  import Json4sProtocol._

  val graphRoute = {
    pathPrefix("graph") {
      path("list") {
        get {
          complete {
            log.info("Listing scores")
            cc.scores
          }
        }
      } ~ path("add" / IntNumber / IntNumber) { (vertex1, vertex2) =>
        post {
          complete {
            val edge = Edge(vertex1, vertex2)
            log.info("Adding edge " + edge)
            cc + edge
            "Edge " + edge + "added succesfully"
          }
        }
      } ~ path("fraud" / IntNumber) { vertex =>
        put {
          complete{
            log.info("Marking node " + vertex + " as fraudulent")
            cc.fraud(vertex)
            "Vertex " + vertex +  " marked as fraudulent"
          }
        }
      }
    }
  }

}
