package com.airtonjal.core.graph

import org.scalatest.{Matchers, FlatSpec}

/**
 * Closeness centrality spec
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
class ClosenessCentralitySpec extends FlatSpec with Matchers {

  val edges = Seq(Edge(4, 5), Edge(1, 2), Edge(2, 3))
  val graph = GraphBuilder.createGraph(edges)
  var scores = new BFS().farnessScores(graph)

  "Closeness algorithm" should "calculate scores correctly" in {
    var scoreMap = scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (3)
    scoreMap(2) should be (2)
    scoreMap(3) should be (3)
    scoreMap(4) should be (1)
    scoreMap(5) should be (1)

    scores = new BFS().farnessScores(graph + Edge(1, 3))
    scoreMap = scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (2)
    scoreMap(2) should be (2)
    scoreMap(3) should be (2)
    scoreMap(4) should be (1)
    scoreMap(5) should be (1)
  }

  "Closeness algorithm" should "calculate farness correctly" in {
    val farness1 = new BFS().farness(graph, 1)
    val farness2 = new BFS().farness(graph, 2)
    val farness3 = new BFS().farness(graph, 3)
    val farness4 = new BFS().farness(graph, 4)
    val farness5 = new BFS().farness(graph, 5)

    farness1 should be (3)
    farness2 should be (2)
    farness3 should be (3)
    farness4 should be (1)
    farness5 should be (1)
  }

  "Closeness algorithm" should "calculate closeness correctly" in {
    val closeness1 = new BFS().closeness(graph, 1)
    val closeness2 = new BFS().closeness(graph, 2)
    val closeness3 = new BFS().closeness(graph, 3)
    val closeness4 = new BFS().closeness(graph, 4)
    val closeness5 = new BFS().closeness(graph, 5)

    closeness1 should be (1f/3)
    closeness2 should be (1f/2)
    closeness3 should be (1f/3)
    closeness4 should be (1f/1)
    closeness5 should be (1f/1)
  }

  var bfsWithState = new BFSWithState(graph)
  bfsWithState.calculateScores()

  "BFSWithState" should "recalculate closeness after adding edge" in {
    var scoreMap = bfsWithState.scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (1f / 3)
    scoreMap(2) should be (1f / 2)
    scoreMap(3) should be (1f / 3)
    scoreMap(4) should be (1f / 1)
    scoreMap(5) should be (1f / 1)

    bfsWithState + Edge(1, 3)
    scoreMap = bfsWithState.scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (1f / 2)
    scoreMap(2) should be (1f / 2)
    scoreMap(3) should be (1f / 2)
    scoreMap(4) should be (1f / 1)
    scoreMap(5) should be (1f / 1)

    bfsWithState + Edge(1, 4)
    scoreMap = bfsWithState.scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (1f / 5)
    scoreMap(2) should be (1f / 7)
    scoreMap(3) should be (1f / 7)
    scoreMap(4) should be (1f / 6)
    scoreMap(5) should be (1f / 9)
  }

  "BFSWithState" should "recalculate closeness after node is marked as fraudulent" in {
    bfsWithState.fraud(1)
    val scoreMap = bfsWithState.scores.map(s => s.vertex -> s.score).toMap

    scoreMap(1) should be (0f)
    scoreMap(2) should be (1f / 14)
    scoreMap(3) should be (1f / 14)
    scoreMap(4) should be (1f / 12)
    scoreMap(5) should be (3f / 36)
  }

}
