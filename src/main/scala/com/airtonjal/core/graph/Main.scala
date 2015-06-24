package com.airtonjal.core.graph

import scala.io.Source

/**
 * Application entry point
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
object Main {

  val FILENAME = "edges.txt"

  def main (args: Array[String]): Unit = {
    val stream = Source.fromURL(getClass().getResource("/" + FILENAME))
    val edgeSeq = stream.getLines.toSeq.map(line => line.split(" +")).map(v => Edge(v(0).toInt, v(1).toInt))

    val graph = GraphBuilder.createGraph(Seq(Edge(4, 5), Edge(1, 2), Edge(2, 3), Edge(1, 3)))
    println(graph)
//    BreadthFirstSearch.closenessCentrality(graph)
//    new BreadthFirstSearch(graph).distances(graph, graph.edges.head._1)
    print(new BFS().farnessScores(graph).mkString("\n"))
  }

}
