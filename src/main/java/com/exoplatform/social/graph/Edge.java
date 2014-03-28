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

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 23, 2014  
 */
public class Edge<V extends Element> implements Element {
  
  /** */
  final Object handle;
  
  /** */
  final Class<?> keyType;
  
  /** */
  final String label;
  
  /** */
  final ElementType type;
  
  /** */
  final V inVertex;
  
  /** */
  final V outVertex;
  
  public Edge(String label, V inVertex, V outVertex) {
    this.handle = label;
    this.keyType = this.handle.getClass();
    this.label = label;
    this.inVertex = inVertex;
    this.outVertex = outVertex;
    this.type = ElementType.EDGE;
  }
  
  public Edge(Object handle, String label, V inVertex, V outVertex) {
    this.handle = handle;
    this.keyType = this.handle.getClass();
    this.label = label;
    this.inVertex = inVertex;
    this.outVertex = outVertex;
    this.type = ElementType.EDGE;
  }

  @Override
  public Object getHandle() {
    return this.handle;
  }
  
  @Override
  public Class<?> getHandleType() {
    return null;
  }
  
  /**
   * Gets the label of the edge
   * @return
   */
  public String getLabel() {
    return this.label;
  }

  @Override
  public ElementType getType() {
    return this.type;
  }
  
  /**
   * Gets the vertex what connected by given the vertex
   * and connected by this edge
   * 
   * @param inVertex the given vertex
   * @return
   */
  public V getOutVertex(Object name) {
    return name.equals(this.inVertex.getHandle()) ? this.outVertex : this.inVertex;
  }
  
  /**
   * Returns the empty edge
   * @param handle
   * @return
   */
  public static Edge<Element> EMPTY(String handle) {
    return new Edge<Element>(handle, null, null);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Edge)) {
      return false;
    }

    Edge<?> that = (Edge<?>) o;

    if (label != null ? !label.equals(that.label) : that.label != null) {
      return false;
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (label != null ? label.hashCode() : 0);
    return result;
  }
  
  @Override
  public String toString() {
    return "Edge[label= " + this.label + ", inVertex =" + this.inVertex.getHandle() + ", outVertex =" + this.outVertex.getHandle() +"]";
  }

  

}
