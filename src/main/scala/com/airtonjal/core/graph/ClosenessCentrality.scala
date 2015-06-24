package com.airtonjal.core.graph

import com.airtonjal.core.Score

import scala.collection.mutable

/**
 * Closeness centrality implementation
 * @author <a href="mailto:airtonjal@gmail.com">Airton Libório</a>
 */
trait ClosenessCentrality {

  /**
   * Solves the closeness centrality problem
   * @param graph The graph
   * @return A sorted sequence with a tuple containing the vertex id and the score
   */
  def calculateScores(graph: Graph): Seq[Score]

  /**
   * Calculates the distances from a given vertex to every other in the graph
   * @param graph The graph
   * @param vertex The chosen vertex
   * @return A map containing the vertex id as key and the minimum distance as value
   */
  def distances(graph: Graph, vertex: Int): scala.collection.immutable.Map[Int, Int]

  /**
   * Calculates the farness score of a given vertex
   * @param graph The graph
   * @param v The vertex
   * @return The farness score
   */
  def farness(graph: Graph, v: Int): Int = distances(graph, v).values.sum

  /**
   * Calculates the farness scores of each vertex
   * @param graph The graph
   * @return A sorted sequence of scores by farness
   */
  def farnessScores(graph: Graph): Seq[Score] =
    graph.adjacencyMap.keys.map(v => Score(v, farness(graph, v).toFloat)).toSeq.sortBy(_.score)

  /**
   * Calculates the closeness score of a given vertex
   * @param graph The graph
   * @param v The vertex
   * @return The closeness score
   */
  def closeness(graph: Graph, v: Int): Float = 1f / farness(graph, v)

  /**
   * Calculates the closeness scores of each vertex
   * @param graph The graph
   * @return A sorted sequence of scores by closeness
   */
  def closenessScores(graph: Graph): Seq[Score] =
    graph.adjacencyMap.keys.map(v => Score(v, closeness(graph, v))).toSeq.sortBy(_.score).reverse

}

/** Uses breadth first search to calculate the centrality of each vertex */
class BreadthFirstSearch extends ClosenessCentrality {

  /** {@inheritdoc} */
  override def distances(graph: Graph, vertex: Int): scala.collection.immutable.Map[Int, Int] = {
    val queue       = mutable.Queue[Int](vertex)
    val visited     = mutable.Set.empty[Int]
    val distanceMap = mutable.Map.empty[Int, Int]

    var distance = 1
    while(queue.nonEmpty) {
      val v = queue.dequeue()
      visited += v
      val adjacents: Set[Int] = graph.adjacencyMap(v).diff(visited).diff(distanceMap.keySet)

      // Found a path, sets distance and enqueue vertex
      adjacents.foreach{ vertex =>
        distanceMap += vertex -> distance
        queue.enqueue(vertex)
      }

      distance += 1
    }

    distanceMap.map(kv => (kv._1, kv._2)).toMap
  }

  /** {@inheritdoc} */
  override def calculateScores(graph: Graph) = closenessScores(graph)

}
