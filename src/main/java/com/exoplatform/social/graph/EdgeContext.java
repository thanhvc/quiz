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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 22, 2014  
 */
class EdgeContext<H, V extends Vertex<H>, E extends Edge<H, V>> {
  //TODO improve this E extends Edge<H,V>
  /** the mapping vertex's handle and list of edges's handle */
  final Map<Object, SoftReference<List<Object>>> vertexEdgeMap;
  
  /**  the mapping edge's label and edge*/
  final Map<Object, SoftReference<Edge<H, V>>> incidences;
  
  public EdgeContext() {
    vertexEdgeMap = new Hashtable<Object, SoftReference<List<Object>>>();
    incidences = new Hashtable<Object, SoftReference<Edge<H, V>>>();
  }
  
  /**
   * wrap the object into soft reference.
   * @param value
   * @return
   */
  private <T> SoftReference<T> softReference(T value) {
    return new SoftReference<T>(value);
  }
  
  /**
   * Add the edge associated with the vertex's handle.
   * 1. inVertex, pus in the map what its handle associated with all of edges
   * 2. outVertex, pus in the map what its handle associated with all of edges
   * 
   * @param handle the edge's label
   * @param edge the edge
   */
  public void add(Edge<H, V> edge) {
    if (this.incidences.containsKey(edge.getHandle())) {
      throw new IllegalArgumentException("The edge " + edge.getLabel() + " is already existing.");
    }
    
    //process IN
    SoftReference<List<Object>> softInSet = vertexEdgeMap.get(edge.inVertex.getHandle());
    List<Object> inSet = softInSet != null ? softInSet.get() : new ArrayList<Object>();
    if (softInSet == null) {
      this.vertexEdgeMap.put(edge.inVertex.getHandle(), softReference(inSet));  
    }
      
    //
    inSet.add(edge.handle);

    //process OUT
    SoftReference<List<Object>> softOutSet = vertexEdgeMap.get(edge.outVertex.getHandle());
    
    List<Object> outSet = softOutSet != null ? softOutSet.get() : new ArrayList<Object>();
    if (softOutSet == null) {
      this.vertexEdgeMap.put(edge.outVertex.getHandle(), softReference(outSet));
    }
    
    //
    outSet.add(edge.handle);
    
    //
    this.incidences.put(edge.getHandle(), softReference(edge));
  }
  
  /**
   * Removes the edge by the its label
   * 
   * @param name the  vertex's handle or edge's label
   */
  public void remove(String label) {
    Edge<H, V> v = this.incidences.get(label).get();
    if (v == null) return;
    
    //IN
    List<Object> inSet = vertexEdgeMap.get(v.inVertex.getHandle()).get();
    if (inSet != null) {
      inSet.remove(v.handle);
    }
    
    //OUT
    List<Object> outSet = vertexEdgeMap.get(v.outVertex.getHandle()).get();
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
    List<Object> in = vertexEdgeMap.get(inHandle).get();
    List<Object> out = vertexEdgeMap.get(outHandle).get();
    
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
    SoftReference<List<Object>> softEdgeHandles = this.vertexEdgeMap.get(name);
    //
    if (softEdgeHandles == null || softEdgeHandles.get() == null)
      return Collections.emptyList();
    
    List<Object> edgeHandles = softEdgeHandles.get();

    List<V> vertices = new ArrayList<V>(edgeHandles.size());
    for (Object handle : edgeHandles) {
      vertices.add(this.incidences.get(handle).get().getOutVertex(name));
    }

    return Collections.unmodifiableList(vertices);
  }
  /**
   * Gets the edges by the vertex's name
   * 
   * @param name the vertex's name
   * @return
   */
  public List<Edge<H, V>> getEdges(Object name) {
    
    SoftReference<List<Object>> softEdgeHandles = this.vertexEdgeMap.get(name);
    //
    if (softEdgeHandles == null || softEdgeHandles.get() == null) return Collections.emptyList();
    
    List<Object> edgeHandles = softEdgeHandles.get();
    //
    List<Edge<H, V>> edges = new ArrayList<Edge<H, V>>(edgeHandles.size());
    for(Object handle : edgeHandles) {
      edges.add(this.incidences.get(handle).get());
    }
    
    return Collections.unmodifiableList(edges);
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
  public Edge<H, V> get(Object label) {
    return this.incidences.get(label).get();
  }
  
  /**
   * Clear all of elements
   */
  public void clear() {
    this.incidences.clear();
    this.vertexEdgeMap.clear();
  }
}
