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
public abstract class ElementChange<E> {
  final E target;
  
  private ElementChange(E target) throws IllegalArgumentException {
    if (target == null) {
      throw new IllegalArgumentException("The target must not be null.");
    }
    
    this.target = target;
  }
  
  protected abstract void dispatch(GraphChangeListener<E> listener);
  
  public static final class Add<E> extends ElementChange<E> {
    
    public Add(E edge) {
      super(edge);
    }

    @Override
    protected void dispatch(GraphChangeListener<E> listener) {
      listener.onAdd(this.target);
    }
    
    @Override
    public String toString() {
      return "GraphChange.Add[target:" + target + "]";
    }
    
  }
  
  public static final class Remove<E> extends ElementChange<E> {

    public Remove(E edge) {
      super(edge);
    }

    @Override
    protected void dispatch(GraphChangeListener<E> listener) {
      listener.onRemove(this.target);
    }

    @Override
    public String toString() {
      return "GraphChange.Remove[target:" + target + "]";
    }

  }
  
  public static final class Update<E> extends ElementChange<E> {

    public Update(E edge) {
      super(edge);
    }

    @Override
    protected void dispatch(GraphChangeListener<E> listener) {
      listener.onUpdate(this.target);
    }

    @Override
    public String toString() {
      return "GraphChange.Update[target:" + target + "]";
    }
  }

}