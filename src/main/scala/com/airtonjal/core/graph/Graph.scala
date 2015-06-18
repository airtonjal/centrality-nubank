package com.airtonjal.core.graph

import scalaz.Scalaz._

case class Edge(v1: Int, v2: Int)

/**
 * A graph data structure
 * @param edgeMap Graph edges. Given a vertex identifier, returns a set of adjacent vertexes identifiers
 */
class Graph(edgeMap: Map[Int, Set[Int]]) {

  /**
   * Adds a new edge
   * @param edge The new edge
   * @return A new Graph containing the edge. The same graph if the Edge already existed
   */
  def +(edge: Edge) : Graph = {
    require(edge.v1 != edge.v2, "Edge vertexes are the same")
  }

  def isAdjacent(v1: Int, v2: Int) = edgeMap.contains(v1) && edgeMap(v1).contains(v2)

}

/**
 * Creates a graph given its set of edges
 */
object GraphBuilder {

  def createGraph(edges : Seq[Edge]): Graph = {
    require(edges.size > 0, "Empty edges")

    // Using scalaz Semigroup operation: http://stackoverflow.com/questions/1262741/scala-how-to-merge-a-collection-of-maps
    val edgeMap = edges.map(e => Map(e.v1 -> Set(e.v2), e.v2 -> Set(e.v1))).reduceLeft(_ |+| _)//.flatten//.toMap

//    edgeMap.foreach(println(_))

    new Graph(edgeMap)
  }


}
