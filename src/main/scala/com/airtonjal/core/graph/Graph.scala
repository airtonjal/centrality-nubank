package com.airtonjal.core.graph

import scalaz.Scalaz._

case class Edge(v1: Int, v2: Int) {
  def equals(e: Edge) = (e.v1 == v1 && e.v2 == v2) || (e.v1 == v2 && e.v2 == v1)
}

/**
 * A immutable graph data structure
 * @param adjacencyMap Graph adjacency map. Given a vertex identifier, returns a set of adjacent vertexes identifiers
 */
class Graph(val adjacencyMap: Map[Int, Set[Int]]) {

  /**
   * Adds a new edge
   * @param edge The new edge
   * @return A new Graph containing the edge. The same graph if the Edge already existed
   */
  def +(edge: Edge): Graph = {
    require(edge.v1 != edge.v2, "Edge vertexes are the same")

    new Graph(
      adjacencyMap ++
        Map(edge.v1 -> (adjacencyMap.getOrElse(edge.v1, Set.empty[Int]) + edge.v2)) ++
        Map(edge.v2 -> (adjacencyMap.getOrElse(edge.v2, Set.empty[Int]) + edge.v1))
    )
  }

  def adjacent(v1: Int, v2: Int) = adjacencyMap.contains(v1) && adjacencyMap(v1).contains(v2)

  override def toString = adjacencyMap.mkString("\n")
}

object GraphBuilder {

  def createGraph(edges : Seq[Edge]): Graph = {
    require(edges.size > 0, "Empty edges")

    // Using scalaz Semigroup operation: http://stackoverflow.com/questions/1262741/scala-how-to-merge-a-collection-of-maps
    val edgeMap = edges.map(e => Map(e.v1 -> Set(e.v2), e.v2 -> Set(e.v1))).reduceLeft(_ |+| _)

    new Graph(edgeMap)
  }


}
