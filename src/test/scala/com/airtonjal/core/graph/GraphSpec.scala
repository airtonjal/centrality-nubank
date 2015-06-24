package com.airtonjal.core.graph

import org.scalatest.{Matchers, FlatSpec}

/**
 * Graph spec
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
class GraphSpec extends FlatSpec with Matchers {

  val edges = Seq(Edge(4, 5), Edge(2, 3), Edge(3, 1))
  val graph = GraphBuilder.createGraph(edges)

  "A graph " should "handle repeated edges" in {
    graph.adjacencyMap.size should be (5)
    (graph + Edge(0, 1) + Edge(0, 1) + Edge(1, 0)).adjacencyMap.size should be (6)
  }

  it should "produce IllegalArgumentException when empty set is provided" in {
    intercept[IllegalArgumentException] {
      GraphBuilder.createGraph(Seq.empty[Edge])
    }
  }

}
