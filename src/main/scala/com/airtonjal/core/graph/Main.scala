package com.airtonjal.core.graph

/**
 * Created by airton on 6/18/15.
 * @author <a href="mailto:airton.liborio@webradar.com">Airton Libório</a>
 */
object Main {

  def main(args: Array[String]) {
    val edges = Seq(Edge(0, 1), Edge(1, 0), Edge(1, 0), Edge(4, 5), Edge(2, 3), Edge(3, 1))

    val graph = GraphBuilder.createGraph(edges)

    graph.edges.foreach(println(_))

    println

    (graph + Edge(1, 6)).edges.foreach(println(_))

//    println(graph.isAdjacent(0, 1))
//    println(graph.isAdjacent(1, 0))
  }

}
