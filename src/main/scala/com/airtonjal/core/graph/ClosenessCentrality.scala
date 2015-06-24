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
class BFS extends ClosenessCentrality {

  /** {@inheritdoc} */
  override def distances(graph: Graph, vertex: Int): Map[Int, Int] = {
    var around      = mutable.Set[Int](vertex)
    val visited     = mutable.Set.empty[Int]
    val distanceMap = mutable.Map.empty[Int, Int]

    var distance = 1
    while(around.nonEmpty) {
      visited ++= around

      around = around.flatMap(v => graph.adjacencyMap(v).diff(visited).diff(distanceMap.keySet))

      // Found a path, sets distance
      around.foreach(vertex => distanceMap += vertex -> distance)

      distance += 1
    }

    distanceMap.map(kv => (kv._1, kv._2)).toMap
  }

  /** {@inheritdoc} */
  override def calculateScores(graph: Graph) = closenessScores(graph)

}

/** Uses breadth first search to calculate the centrality of each vertex. Keeps state. Not thread safe */
class BFSWithState(var graph: Graph) extends BFS {

  var distanceMap = mutable.Map.empty[Int, Map[Int, Int]]
  val fraudSet    = mutable.Set.empty[Int]
  var scores      = Seq.empty[Score]

  /**
   * Marks a node as fraudulent
   * @param v The vertex
   */
  def fraud(v: Int): Unit = {
    fraudSet += v
    propagateFraud(v)
  }

  /**
   * Propagates the effect of having a fraudulent node in the graph
   * @param v The vertex
   */
  private def propagateFraud(v: Int): Unit = {
    // Recalculates scores
    scores = scores.map { score =>
      if (fraudSet.contains(score.vertex)) Score(score.vertex, 0)  // Fraud node, nothing to do
      else distanceMap(v).get(score.vertex) match {
        // Connected node, recalculates the score
        case Some(distance) =>
          Score(score.vertex, score.score * (1f - math.pow(0.5, distance.toDouble).toFloat))
        // Non-connected node, does nothing
        case None => score
      }
    }.sortBy(_.score).reverse
  }

  def +(e: Edge): Unit = {
    graph = graph + e
    calculateScores()
  }

  /**
   * Calculates all the scores
   */
  def calculateScores() = {
    scores = Seq.empty[Score]
    distanceMap = mutable.Map.empty[Int, Map[Int, Int]]

    graph.adjacencyMap.keys.foreach(v => distanceMap(v) = distances(graph, v))
    scores = graph.adjacencyMap.keys.map(v => Score(v, closeness(graph, v))).toSeq
    fraudSet.foreach(v => propagateFraud(v))
  }

//  // These are not yet implemented
//  override def farness(graph: Graph, v: Int) =
//    throw new NotImplementedError("This method is only implemented in ancestor classes")
//  override def farnessScores(graph: Graph) =
//    throw new NotImplementedError("This method is only implemented in ancestor classes")
//  override def closeness(graph: Graph, v: Int) =
//    throw new NotImplementedError("This method is only implemented in ancestor classes")
//  override def closenessScores(graph: Graph) =
//    throw new NotImplementedError("This method is only implemented in ancestor classes")

}
