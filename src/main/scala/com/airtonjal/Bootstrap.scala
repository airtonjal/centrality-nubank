package com.airtonjal

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.io.IO
import com.airtonjal.core.graph.{Edge, GraphBuilder}
import com.airtonjal.service.GraphService
import spray.can.Http

import scala.io.Source

/**
 * Application entry point
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
object Bootstrap extends App {

  val FILENAME = "edges.txt"
  val stream = Source.fromURL(getClass().getResource("/" + FILENAME))
  val edgeSeq = stream.getLines.toSeq.map(line => line.split(" +")).map(v => Edge(v(0).toInt, v(1).toInt))
  val graph = GraphBuilder.createGraph(edgeSeq)

  implicit val system = ActorSystem("graph-REST-service")
  val log = Logging(system, getClass)

  // Our service implementation
  val graphService = system.actorOf(Props(new GraphService(graph)), "graph-service")

  // Starts http server, hardcoded properties only for the sake of demonstration
  IO(Http) ! Http.Bind(graphService, interface = "127.0.0.1", port = 8080)

}
