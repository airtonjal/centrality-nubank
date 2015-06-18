package com.airtonjal.core.graph

import scalaz.Scalaz._

case class Edge(v1: Int, v2: Int)

/**
 * A graph data structure
 * @param edges Graph edges. Given a vertex identifier, returns a set of adjacent vertexes identifiers
 */
class Graph(val edges: Map[Int, Set[Int]]) {

  /**
   * Adds a new edge
   * @param edge The new edge
   * @return A new Graph containing the edge. The same graph if the Edge already existed
   */
  def +(edge: Edge) : Graph = {
    require(edge.v1 != edge.v2, "Edge vertexes are the same")

    new Graph(
      edges ++
        Map(edge.v1 -> (edges.getOrElse(edge.v1, Set.empty[Int]) + edge.v2)) ++
        Map(edge.v2 -> (edges.getOrElse(edge.v2, Set.empty[Int]) + edge.v1))
    )
  }

  def isAdjacent(v1: Int, v2: Int) = edges.contains(v1) && edges(v1).contains(v2)

}

/**
 * Creates a graph given its set of edges
 */
object GraphBuilder {

  def createGraph(edges : Seq[Edge]): Graph = {
    require(edges.size > 0, "Empty edges")

    // Using scalaz Semigroup operation: http://stackoverflow.com/questions/1262741/scala-how-to-merge-a-collection-of-maps
    val edgeMap = edges.map(e => Map(e.v1 -> Set(e.v2), e.v2 -> Set(e.v1))).reduceLeft(_ |+| _)

    new Graph(edgeMap)
  }


}
