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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 22, 2014  
 */
class EdgeContext<V extends Element> {
  
  /** the mapping vertex's handle and list of edges's handle */
  final Map<Object, List<Object>> vertexEdgeMap;
  
  /**  the mapping edge's label and edge*/
  final Map<Object, Edge<V>> incidences;
  
  public EdgeContext() {
    vertexEdgeMap = new HashMap<Object, List<Object>>();
    incidences = new HashMap<Object, Edge<V>>();
  }
  
  /**
   * Add the edge associated with the vertex's handle.
   * 
   * @param handle the edge's label
   * @param edge the edge
   */
  public void add(Edge<V> edge) {
    if (this.incidences.containsKey(edge.getHandle())) {
      throw new IllegalArgumentException("The edge " + edge.getLabel() + " is already existing.");
    }
    
    //process IN
    List<Object> inSet = vertexEdgeMap.get(edge.inVertex.getHandle());
    if (inSet == null) {
      inSet = new ArrayList<Object>();
      this.vertexEdgeMap.put(edge.inVertex.getHandle(), inSet);  
    }
    //
    inSet.add(edge.handle);

    //process OUT
    List<Object> outSet = vertexEdgeMap.get(edge.outVertex.getHandle());
    if (outSet == null) {
      outSet = new ArrayList<Object>();
      this.vertexEdgeMap.put(edge.outVertex.getHandle(), outSet);
    }
    //
    outSet.add(edge.handle);
    
    //
    this.incidences.put(edge.getHandle(), edge);
  }
  
  /**
   * Removes the edge by the its label
   * 
   * @param name the  vertex's handle or edge's label
   */
  public void remove(String label) {
    Edge<V> v = this.incidences.get(label);
    if (v == null) return;
    
    //IN
    List<Object> inSet = vertexEdgeMap.get(v.inVertex.getHandle());
    if (inSet != null) {
      inSet.remove(v.handle);
    }
    
    //OUT
    List<Object> outSet = vertexEdgeMap.get(v.outVertex.getHandle());
    if (outSet != null) {
      outSet.remove(v.handle);
    }
    
    //removes in incidences
    this.incidences.remove(label);
  }
  
  /**
   * Removes the edge by the its label
   * 
   * @param name the  vertex's handle or edge's label
   */
  public void remove(Object inHandle, Object outHandle) {
    List<Object> in = vertexEdgeMap.get(inHandle);
    List<Object> out = vertexEdgeMap.get(outHandle);
    
    //
    List<Object> newIn = new ArrayList<Object>(in);
    List<Object> newOut = new ArrayList<Object>(out);
    
    //intersection two lists
    if (newIn.retainAll(newOut)) {
      if (newIn.size() > 0) {
        Object edgeLabel = newIn.get(0);
        this.incidences.remove(edgeLabel);
        //
        in.remove(edgeLabel);
        out.remove(edgeLabel);
      }
    }
  }
  
  /**
   * Gets the adjacent by the vertex's name
   * @param name the edge's label, in vertex's handle or out vertex's handle
   * @return
   */
  public List<V> getAdjacents(Object name) {
    List<Object> edgeHandles = this.vertexEdgeMap.get(name);
    //
    if (edgeHandles == null) return Collections.emptyList();
    
    //
    List<V> vertices = new ArrayList<V>(edgeHandles.size());  
    for(Object handle : edgeHandles) {
      vertices.add(this.incidences.get(handle).getOutVertex(name));
    }
    
    return vertices;
  }
  /**
   * Gets the edges by the vertex's name
   * 
   * @param name the vertex's name
   * @return
   */
  public List<Edge<V>> getEdges(Object name) {
    List<Object> edgeHandles = this.vertexEdgeMap.get(name);
    //
    if (edgeHandles == null) return Collections.emptyList();
    
    //
    List<Edge<V>> edges = new ArrayList<Edge<V>>(edgeHandles.size());
    for(Object handle : edgeHandles) {
      edges.add(this.incidences.get(handle));
    }
    
    return edges;
  }
  
  /**
   * Gets number of edges in the graph
   * @return
   */
  public int getEdgeSize() {
    return incidences.size();
  }
  
  /**
   * Gets the edge by the edge's label
   * @param label
   * @return
   */
  public Edge<V> get(Object label) {
    return this.incidences.get(label);
  }
  
  /**
   * Clear all of elements
   */
  public void clear() {
    this.incidences.clear();
    this.vertexEdgeMap.clear();
  }
}
