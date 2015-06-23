package com.airtonjal.core.graph

import scala.collection.mutable

/**
 * Closeness centrality implementation
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
trait ClosenessCentrality {

  /**
   * Solves the closeness centrality problem
   * @param graph The graph
   * @return A sorted set with a tuple containing the vertex id and the score
   */
  def calculateScores(graph: Graph): Map[Int, Int]

  /**
   * Calculates the distances from a given vertex to every other in the graph
   * @param graph The graph
   * @param vertex The chosen vertex
   * @return A map containing the vertex id as key and the minimum distance as value
   */
  def distances(graph: Graph, vertex: Int): scala.collection.immutable.Map[Int, Int]

  def farness(graph: Graph, v: Int) = distances(graph, v).values.sum
  def closeness(graph: Graph, v: Int) = 1 / farness(graph, v)

}

/** Uses breadth first search to calculate the centrality of each vertex */
class BreadthFirstSearch(graph: Graph) extends ClosenessCentrality {

  /** {@inheritdoc} */
  override def distances(graph: Graph, vertex: Int): scala.collection.immutable.Map[Int, Int] = {
    val queue       = mutable.Queue[Int](vertex)
    val visited     = mutable.Set.empty[Int]
    val distanceMap = mutable.Map.empty[Int, Int]

    var distance = 1
    while(queue.nonEmpty) {
      val v = queue.dequeue()
      visited += v
      val adjacents = graph.edges(v)

      // Found a path, accounts distance
      adjacents.foreach(vertex => distanceMap += vertex -> distance)

      // Enqueues all non-visited adjacent vertexes
      adjacents.diff(visited).foreach(queue.enqueue(_))

      distance += 1
    }

    distanceMap.map(kv => (kv._1, kv._2)).toMap
  }

  /** {@inheritdoc} */
  override def calculateScores(graph: Graph) = graph.edges.keys.map(v => v -> closeness(graph, v)).toMap

}
