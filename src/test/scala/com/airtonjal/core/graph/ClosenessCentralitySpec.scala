package com.airtonjal.core.graph

import org.scalatest.{Matchers, FlatSpec}

/**
 * Closeness centrality spec
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
class ClosenessCentralitySpec extends FlatSpec with Matchers {

  val edges = Seq(Edge(4, 5), Edge(1, 2), Edge(2, 3))
  val graph = GraphBuilder.createGraph(edges)
  var scores = new BreadthFirstSearch().farnessScores(graph)

  "Closeness algorithm" should "calculate scores correctly" in {
    var scoreMap = scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (3.0)
    scoreMap(2) should be (2.0)
    scoreMap(3) should be (3.0)
    scoreMap(4) should be (1.0)
    scoreMap(5) should be (1.0)

    scores = new BreadthFirstSearch().farnessScores(graph + Edge(1, 3))
    scoreMap = scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (2.0)
    scoreMap(2) should be (2.0)
    scoreMap(3) should be (2.0)
    scoreMap(4) should be (1.0)
    scoreMap(5) should be (1.0)
  }

  "Closeness algorithm" should "calculate farness correctly" in {
    val farness1 = new BreadthFirstSearch().farness(graph, 1)
    val farness2 = new BreadthFirstSearch().farness(graph, 2)
    val farness3 = new BreadthFirstSearch().farness(graph, 3)
    val farness4 = new BreadthFirstSearch().farness(graph, 4)
    val farness5 = new BreadthFirstSearch().farness(graph, 5)

    farness1 should be (3)
    farness2 should be (2)
    farness3 should be (3)
    farness4 should be (1)
    farness5 should be (1)
  }

  "Closeness algorithm" should "calculate closeness correctly" in {
    val closeness1 = new BreadthFirstSearch().closeness(graph, 1)
    val closeness2 = new BreadthFirstSearch().closeness(graph, 2)
    val closeness3 = new BreadthFirstSearch().closeness(graph, 3)
    val closeness4 = new BreadthFirstSearch().closeness(graph, 4)
    val closeness5 = new BreadthFirstSearch().closeness(graph, 5)

    closeness1 should be (1f/3)
    closeness2 should be (1f/2)
    closeness3 should be (1f/3)
    closeness4 should be (1f/1)
    closeness5 should be (1f/1)
  }

}
