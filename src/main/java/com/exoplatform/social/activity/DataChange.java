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
package com.exoplatform.social.activity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
public abstract class DataChange<M> {
  final M target;
  
  private DataChange(M target) throws IllegalArgumentException {
    if (target == null) {
      throw new IllegalArgumentException("The target must not be null.");
    }
    
    this.target = target;
  }
  
  protected abstract void dispatch(DataChangeListener<M> listener);
  
  public static final class Add<M> extends DataChange<M> {
    
    public Add(M model) {
      super(model);
    }

    @Override
    protected void dispatch(DataChangeListener<M> listener) {
      listener.onAdd(this.target);
    }
    
    @Override
    public String toString() {
      return "DataChange.Add[target:" + target + "]";
    }
    
  }
  
  public static final class Remove<M> extends DataChange<M> {

    public Remove(M model) {
      super(model);
    }

    @Override
    protected void dispatch(DataChangeListener<M> listener) {
      listener.onRemove(this.target);
    }

    @Override
    public String toString() {
      return "DataChange.Remove[target:" + target + "]";
    }

  }
  
  public static final class Move<M> extends DataChange<M> {

    public Move(M model) {
      super(model);
    }

    @Override
    protected void dispatch(DataChangeListener<M> listener) {
      listener.onMove(this.target);
    }

    @Override
    public String toString() {
      return "DataChange.Move[target:" + target + "]";
    }
  }
  
  public static final class Update<M> extends DataChange<M> {

    public Update(M model) {
      super(model);
    }

    @Override
    protected void dispatch(DataChangeListener<M> listener) {
      listener.onMove(this.target);
    }

    @Override
    public String toString() {
      return "DataChange.Update[target:" + target + "]";
    }
  }

}
