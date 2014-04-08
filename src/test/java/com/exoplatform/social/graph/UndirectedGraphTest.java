/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.exoplatform.social.graph;

import java.util.List;

import com.exoplatform.social.graph.Edge;
import com.exoplatform.social.graph.GraphContext.Scope;
import com.exoplatform.social.graph.Vertex;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 24, 2014  
 */
public class UndirectedGraphTest extends AbstractGraphTest {

  public void testAddVertex() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    graph.addEdge("mary-john", maryV, johnV);
    
    assertNotNull(graph.getVertex(maryV.handle));
    assertEquals(maryV.handle, graph.getVertex(maryV.handle).handle);
    assertEquals(1, graph.getAdjacentsSize("mary"));
    assertEquals(1, graph.getAdjacentsSize("john"));
    assertNotNull(graph.getEdge("mary-john"));
    
  }
  
  public void testAddMore() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    int numberOfConnections = 10;
    Vertex<Object> v_ith = null;
    Edge<Object, Vertex<Object>> e_ith = null;
    
    //
    for(int i = 0; i< numberOfConnections; i++) {
      v_ith = graph.addVertex("user" + i);
      e_ith = new Edge<Object, Vertex<Object>>("mary-user" + i, maryV, v_ith);
      graph.addEdge(e_ith);
    }
    
    assertEquals(numberOfConnections, graph.getAdjacents("mary").size());
    for(int i = 0; i< numberOfConnections; i++) {
      assertEquals(1, graph.getAdjacents("user" + i).size());
    }
    
    assertEquals(11, graph.getVertexSize());
    
  }
  
  public void testGetAdjacents() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    graph.addEdge(e1);
    
    Vertex<Object> demoV = graph.addVertex("demo");
    Edge<Object, Vertex<Object>> e2 = new Edge<Object, Vertex<Object>>("mary-demo", maryV, demoV);
    graph.addEdge(e2);
    
    assertEquals(2, graph.getAdjacents("mary").size());
    List<Vertex<Object>> vertices = graph.getAdjacents("mary");
    assertEquals("john", vertices.get(0).handle);
    assertEquals("demo", vertices.get(1).handle);
    
    assertEquals(1, graph.getAdjacents("john").size());
    assertEquals(1, graph.getAdjacents("demo").size());
    
    
  }
  
  public void testGetEdges() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    graph.addEdge(e1);
    
    Vertex<Object> demoV = graph.addVertex("demo");
    Edge<Object, Vertex<Object>> e2 = new Edge<Object, Vertex<Object>>("mary-demo", maryV, demoV);
    graph.addEdge(e2);
    
    assertEquals(2, graph.getAdjacents("mary").size());
    List<Edge<Object, Vertex<Object>>> edges = graph.getEdges("mary");
    assertEquals("mary-john", edges.get(0).label);
    assertEquals("mary-demo", edges.get(1).label);
    
    assertEquals(1, graph.getEdges("john").size());
    assertEquals(1, graph.getEdges("demo").size());
  }
  
  public void testRemoveEdge() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    graph.addEdge(e1);
    
    Vertex<Object> demoV = graph.addVertex("demo");
    Edge<Object, Vertex<Object>> e2 = new Edge<Object, Vertex<Object>>("mary-demo", maryV, demoV);
    graph.addEdge(e2);
    
    graph.removeEdge(maryV, johnV);
    
    assertEquals(1, graph.getEdges("mary").size());
    assertEquals(0, graph.getEdges("john").size());
    assertEquals(1, graph.getEdges("demo").size());
    
  }
  
  public void testRemoveVertex() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    graph.addEdge(e1);

    assertEquals(1, graph.getEdges("mary").size());
    assertEquals(1, graph.getEdges("john").size());
    
    graph.removeVertex(maryV.keyType, maryV.getHandle(), Scope.ALL);
    assertEquals(0, graph.getEdges("mary").size());
    assertEquals(0, graph.getEdges("john").size());
    graph.removeVertex(johnV.keyType, johnV.getHandle(), Scope.SINGLE);
    assertNull(graph.getVertex("john"));
  }
  
  public void testRemoVertex1() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    graph.addEdge(e1);
    
    Vertex<Object> demoV = graph.addVertex("demo");
    Edge<Object, Vertex<Object>> e2 = new Edge<Object, Vertex<Object>>("mary-demo", maryV, demoV);
    graph.addEdge(e2);
    
    graph.removeVertex(demoV.keyType, demoV.handle, Scope.ALL);
    
    assertEquals(1, graph.getEdges("mary").size());
    assertEquals(1, graph.getEdges("john").size());
    assertEquals(0, graph.getEdges("demo").size());
    
  }
  
}
