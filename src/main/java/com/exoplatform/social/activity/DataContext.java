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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
public final class DataContext<M> {
  /** */
  private DataChangeQueue<M> changes;
  
  /**
   * Constructor the DataContext to update 
   * the data model what hold by caching
   * @param listener
   */
  public DataContext() {
  }
  
  public boolean hasChanges() {
    return changes != null && changes.size() > 0;
  }
  
  private void addChange(DataChange<M> change) {
    if (changes == null) {
      changes = new DataChangeQueue<M>();
    }
    //
    changes.addLast(change);
  }
  
  /**
   * Adds the new model
   * @param target
   */
  public void add(M target) {
    addChange(new DataChange.Add<M>(target));
  }
  
  /**
   * Moves the model
   * @param target
   */
  public void move(M target) {
    addChange(new DataChange.Move<M>(target));
  }
  
  /**
   * Removes the model
   * @param target
   */
  public void remove(M target) {
    addChange(new DataChange.Remove<M>(target));
  }
  
  /**
   * Updates the model
   * @param target
   */
  public void update(M target) {
    addChange(new DataChange.Update<M>(target));
  }
  
  public DataChangeQueue<M> getChanges() {
    return changes;
  }
  
  public List<DataChange<M>> peekChanges() {
    if (hasChanges()) {
      return changes;
    } else {
      return Collections.emptyList();
    }
  }

  public List<DataChange<M>> popChanges() {
    if (hasChanges()) {
      LinkedList<DataChange<M>> tmp = changes;
      changes = null;
      return tmp;
    } else {
      return Collections.emptyList();
    }
  }
}
