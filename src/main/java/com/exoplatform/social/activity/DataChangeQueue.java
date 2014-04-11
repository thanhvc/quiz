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

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
@SuppressWarnings("serial")
public class DataChangeQueue<M> extends LinkedList<DataChange<M>> implements DataChangeListener<M> {

  public DataChangeQueue() {

  }

  public DataChangeQueue(Collection<? extends DataChange<M>> c) {
    super(c);
  }
  /**
   * 1. Implements PersisterListener works with JCR
   * 2. Implements CachingListener works with eXo Caching
   * 
   * @param listener
   */
  public void broadcast(DataChangeListener<M> listener) {
    for(DataChange<M> change : this) {
      change.dispatch(listener);
    }
  }
  
  @Override
  public void onAdd(M target) {
    add(new DataChange.Add<M>(target));
    
  }

  @Override
  public void onRemove(M target) {
    add(new DataChange.Remove<M>(target));
  }

  @Override
  public void onUpdate(M target) {
    add(new DataChange.Update<M>(target));
  }

  @Override
  public void onMove(M target) {
    
  }

}
