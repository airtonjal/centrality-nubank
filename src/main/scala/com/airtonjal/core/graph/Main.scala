package com.airtonjal.core.graph

import scala.io.Source

/**
 * Application entry point
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
object Main {

  val FILENAME = "edges.txt"

  def main (args: Array[String]): Unit = {
    val stream = Source.fromURL(Source.getClass().getResource("/" + FILENAME))
    val edgeSeq = stream.getLines.toSeq.map(line => line.split(" +")).map(v => Edge(v(0).toInt, v(1).toInt))

    val graph = GraphBuilder.createGraph(edgeSeq)
    print(graph)


//    BreadthFirstSearch.closenessCentrality(graph)
    new BreadthFirstSearch(graph).distances(graph, graph.edges.head._1)
  }

  def test() {
    val edges = Seq(Edge(0, 1), Edge(1, 0), Edge(1, 0), Edge(4, 5), Edge(2, 3), Edge(3, 1))

    val graph = GraphBuilder.createGraph(edges)

    graph.edges.foreach(println(_))

    println

    (graph + Edge(1, 6)).edges.foreach(println(_))

//    println(graph.isAdjacent(0, 1))
//    println(graph.isAdjacent(1, 0))
  }

}
