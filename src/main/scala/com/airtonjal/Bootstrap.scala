package com.airtonjal

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.io.IO
import com.airtonjal.service.GraphServiceImpl
import spray.can.Http

/**
 * Application entry point
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
object Bootstrap extends App {

  implicit val system = ActorSystem("graph-REST-service")
  val log = Logging(system, getClass)

  // Our service implementation
  val graphService = system.actorOf(Props[GraphServiceImpl], "graph-service")

  // Starts http server, hardcoded properties only for the sake of demonstration
  IO(Http) ! Http.Bind(graphService, interface = "127.0.0.1", port = 8080)

}
