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

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.exoplatform.social.graph.Edge;
import com.exoplatform.social.graph.Vertex;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 24, 2014  
 */
public class GraphPerfTest extends AbstractGraphTest {
  
  private void prerformance(int length) throws Exception {
    watch.start();
    Random r = new Random();
    Vertex v_ith = null;
    Edge<Vertex> e_ith = null;
    
    //
    for(int i = 0; i< length; i++) {
      String user = "inUser" + r.nextInt(length);
      Vertex userV = graph.addVertex(user);
      v_ith = graph.addVertex("user" + i);
      e_ith = new Edge<Vertex>(user + "_user" + i, userV, v_ith);
      graph.addEdge(e_ith);
    }
    
    watch.stop();
    for(int i = 0; i< length; i++) {
      assertEquals(1, graph.getAdjacents("user" + i).size());
    }
    watch.stop();
    System.out.println("Process:: vertices=" + graph.getVertexSize() + "| edges: " + graph.getEdgeSize() + "| elapsed=" + watch.toString(watch.elapsedTime(), TimeUnit.MILLISECONDS));
  }
  
  public void testPerformance() throws Exception {
    prerformance(1000);
    graph.clear();
    prerformance(5000);
    graph.clear();
    prerformance(10000);
    graph.clear();
    prerformance(20000);
    graph.clear();
    prerformance(30000);
    graph.clear();
    prerformance(40000);
    graph.clear();
    prerformance(50000);
    graph.clear();
    prerformance(100000);
    graph.clear();
    prerformance(200000);
    graph.clear();
    prerformance(300000);
    graph.clear();
    prerformance(400000);
    graph.clear();
    prerformance(500000);
    graph.clear();
    prerformance(600000);
    graph.clear();
    prerformance(700000);
    graph.clear();
    prerformance(800000);
    graph.clear();
    prerformance(900000);
    graph.clear();
    prerformance(1000000);
    
    Thread.sleep(5000);
  }
}
