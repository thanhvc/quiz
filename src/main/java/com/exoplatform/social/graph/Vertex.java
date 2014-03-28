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
 * Mar 22, 2014  
 */
public class Vertex implements Element {
  
  public static final VertexModel<Vertex, Edge<Vertex>> MODEL = new VertexModel<Vertex, Edge<Vertex>>() {
    public VertexContext<Vertex, Edge<Vertex>> getContext(Vertex vertex) {
        return vertex.context;
    }

    public Vertex create(VertexContext<Vertex, Edge<Vertex>> context) {
        return new Vertex(context);
    }
  };

  /** */
  final Object handle;
  
  /** */
  final Class<?> keyType;
  
  /** */
  final ElementType type;
  
  /** */
  final VertexContext<Vertex, Edge<Vertex>> context;
  
  public Vertex(VertexContext<Vertex, Edge<Vertex>> context) {
    this.handle = context.getHandle();
    this.keyType = this.handle.getClass();
    this.type = ElementType.VERTEX;
    this.context = context;
  }
  
  @Override
  public Object getHandle() {
    return this.handle;
  }
  
  public <T> T unwrap(Class<T> type) {
    if (type.equals(this.keyType)) {
      return type.cast(this.handle);
    } else {
      throw new IllegalArgumentException("The provided type is incorrect." + type.toString());
    }
  }

  @Override
  public ElementType getType() {
    return this.type;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Vertex)) {
      return false;
    }

    Vertex that = (Vertex) o;

    if (handle != null ? !handle.equals(that.handle) : that.handle != null) {
      return false;
    }

    return true;
  }
  
  @Override
  public String toString() {
    return "Vertex[handle = " + this.handle + "]";
  }
  
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (handle != null ? handle.hashCode() : 0);
    return result;
  }

  @Override
  public Class<?> getHandleType() {
    // TODO Auto-generated method stub
    return null;
  }
}
