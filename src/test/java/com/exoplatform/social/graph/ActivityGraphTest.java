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

import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.graph.Vertex;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 26, 2014  
 */
public class ActivityGraphTest extends AbstractGraphTest {

  
  public void testAddVertex() throws Exception {
    ListActivitiesKey maryK = new ListActivitiesKey("mary", StreamType.FEED);
    ListActivitiesKey johnK = new ListActivitiesKey("john", StreamType.FEED);
    
    //using ListActivitiesKey as key
    Vertex maryV = graph.addVertex(maryK);
    Vertex johnV = graph.addVertex(johnK);
    
    graph.addEdge("mary-john", maryV, johnV);
    
    //using String object
    Vertex demoV = graph.addVertex("demo");
    graph.addEdge("mary-demo", maryV, demoV);
    
    assertNotNull(graph.getVertex(maryV.handle));
    assertEquals(maryK, graph.getVertex(maryV.handle).handle);
    assertEquals(2, graph.getAdjacentsSize(maryK));
    assertEquals(1, graph.getAdjacentsSize(johnK));
    assertNotNull(graph.getEdge("mary-john"));
    
    //assert String.class
    assertEquals(demoV.handle, graph.getVertex(demoV.handle).handle);
    
  }
}
