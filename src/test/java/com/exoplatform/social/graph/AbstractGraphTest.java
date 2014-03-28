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

import junit.framework.TestCase;

import com.exoplatform.social.graph.EdgeContext;
import com.exoplatform.social.graph.UndirectedGraph;
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.utils.LogWatch;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 24, 2014  
 */
public abstract class AbstractGraphTest extends TestCase {
  /** */
  protected UndirectedGraph graph = null;
  
  /** */
  protected EdgeContext<Vertex> edgeContext = null;
  
  /** */
  protected LogWatch watch = null;
  
  @Override
  protected void setUp() throws Exception {
    graph = new UndirectedGraph(Vertex.MODEL);
    edgeContext = new EdgeContext<Vertex>();
    watch = new LogWatch();
  }
  
  @Override
  protected void tearDown() throws Exception {
    edgeContext = null;
    graph = null;
    watch = null;
  }

}
