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
import com.exoplatform.social.graph.Vertex;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 24, 2014  
 */
public class EdgeContextTest extends AbstractGraphTest {
  /** */
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testRemoveEdge() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    edgeContext.add(e);
    
    assertEquals(1, edgeContext.getAdjacents("mary").size());
    assertEquals(1, edgeContext.getAdjacents("john").size());
    
    edgeContext.remove("mary-john");
    
    assertEquals(0, edgeContext.getAdjacents("mary").size());
    assertEquals(0, edgeContext.getAdjacents("john").size());
  }
  
  public void testAddVertex() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    edgeContext.add(e);
    
    assertEquals(1, edgeContext.getAdjacents("mary").size());
    assertEquals(1, edgeContext.getAdjacents("john").size());
  }
  
  public void testAddExisting() {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    
    edgeContext.add(e);
    
    try {
      Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
      edgeContext.add(e1);
      fail("The edge " + e1.getLabel() + " is already existing.");
    } catch (IllegalArgumentException ex) {
      
    }
    
    assertEquals(1, edgeContext.getAdjacents("mary").size());
    assertEquals(1, edgeContext.getAdjacents("john").size());
    
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
      edgeContext.add(e_ith);
    }
    
    assertEquals(numberOfConnections, edgeContext.getAdjacents("mary").size());
    for(int i = 0; i< numberOfConnections; i++) {
      assertEquals(1, edgeContext.getAdjacents("user" + i).size());
    }
    
  }
  
  public void testGetAdjacents() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    edgeContext.add(e1);
    
    Vertex<Object> demoV = graph.addVertex("demo");
    Edge<Object, Vertex<Object>> e2 = new Edge<Object,Vertex<Object>>("mary-demo", maryV, demoV);
    edgeContext.add(e2);
    
    assertEquals(2, edgeContext.getAdjacents("mary").size());
    List<Vertex<Object>> vertices = edgeContext.getAdjacents("mary");
    assertEquals("john", vertices.get(0).handle);
    assertEquals("demo", vertices.get(1).handle);
    
    assertEquals(1, edgeContext.getAdjacents("john").size());
    assertEquals(1, edgeContext.getAdjacents("demo").size());
    
    
  }
  
  public void testGetEdges() throws Exception {
    Vertex<Object> maryV = graph.addVertex("mary");
    Vertex<Object> johnV = graph.addVertex("john");
    Edge<Object, Vertex<Object>> e1 = new Edge<Object, Vertex<Object>>("mary-john", maryV, johnV);
    edgeContext.add(e1);
    
    Vertex<Object> demoV = graph.addVertex("demo");
    Edge<Object, Vertex<Object>> e2 = new Edge<Object, Vertex<Object>>("mary-demo", maryV, demoV);
    edgeContext.add(e2);
    
    assertEquals(2, edgeContext.getAdjacents("mary").size());
    List<Edge<Object, Vertex<Object>>> edges = edgeContext.getEdges("mary");
    assertEquals("mary-john", edges.get(0).label);
    assertEquals("mary-demo", edges.get(1).label);
    
    assertEquals(1, edgeContext.getEdges("john").size());
    assertEquals(1, edgeContext.getEdges("demo").size());
  }
  
  
}
