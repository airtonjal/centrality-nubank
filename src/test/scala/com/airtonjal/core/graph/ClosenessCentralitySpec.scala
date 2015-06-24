package com.airtonjal.core.graph

import org.scalatest.{Matchers, FlatSpec}

/**
 * Closeness centrality spec
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
class ClosenessCentralitySpec extends FlatSpec with Matchers {

  val edges = Seq(Edge(4, 5), Edge(1, 2), Edge(2, 3))
  val graph = GraphBuilder.createGraph(edges)

  "A graph " should "set scores correctly" in {
    var scores = new BreadthFirstSearch().farnessScores(graph)

    var scoreMap = scores.toMap

    scoreMap(1) should be (3.0)
    scoreMap(2) should be (2.0)
    scoreMap(3) should be (3.0)
    scoreMap(4) should be (1.0)
    scoreMap(5) should be (1.0)

    scores = new BreadthFirstSearch().farnessScores(graph + Edge(1, 3))
    scoreMap = scores.toMap

    scoreMap(1) should be (2.0)
    scoreMap(2) should be (2.0)
    scoreMap(3) should be (2.0)
    scoreMap(4) should be (1.0)
    scoreMap(5) should be (1.0)

  }

}
