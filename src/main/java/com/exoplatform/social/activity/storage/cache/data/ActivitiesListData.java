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
package com.exoplatform.social.activity.storage.cache.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 13, 2014  
 */
@SuppressWarnings("serial")
public class ActivitiesListData implements Serializable {
  
  final static int FIXED_SIZE = 100;
  
  final int maxSize;
  /** */
  final LinkedList<String> activityDataKeys;
  /* **/
  final String myKey;
  
  public ActivitiesListData(String key) {
    this(key, FIXED_SIZE);
  }
  
  /**
   * Constructors the empty instance.
   */
  public ActivitiesListData(String key, int fixedSize) {
    activityDataKeys = new LinkedList<String>();
    this.maxSize = fixedSize;
    this.myKey = key;
  }
  
  /**
   * Keys list contains the given activityId or not
   * 
   * @param activityId the activityId
   * @return TRUE/FALSE
   */
  public boolean contains(String activityId) {
    return activityDataKeys.contains(activityId);
  }
  
  /**
   * Inserts the id on the top of this list
   * 
   * @param id the activity id
   * @listener handles the case when is over fixed size
   */
  public void insertFirst(String id, StreamFixedSizeListener listener) {
    //System.out.println("BEGIN::insert at the top: " + id);
    int position = this.activityDataKeys.indexOf(id);
    if (position > 0) {
      this.activityDataKeys.remove(position);
      this.activityDataKeys.offerFirst(id);
    } else if (position == -1) {
      this.activityDataKeys.offerFirst(id);
    }
    
    maintainFixedSize(listener);
    //System.out.println("END::insert at the top: " + id);
  }
  
  /**
   * Handles the case when the activity keys size over fixed size
   * 
   * @param listener the listener to handle
   */
  private void maintainFixedSize(StreamFixedSizeListener listener) {
    if (activityDataKeys.size() > maxSize) {
      String outV = activityDataKeys.removeLast();
      if (listener != null) {
        listener.update(outV, myKey);
      }
    }
  }
  /**
   * Determine can add more activity id on this
   * makes sure it is not over fixed size
   * 
   * @return TRUE can add more Otherwise FALSE
   */
  public boolean canAddMore() {
    return size() < this.maxSize;
  }
  
  /**
   * Inserts the id on the top of this list
   * @param id
   */
  public void insertFirst(Collection<String> ids) {
    for(String id : ids) {
      insertFirst(id, null);
    }
  }
  
  /**
   * Inserts the activityId on the bottom of the list
   * @param id
   */
  public void insertLast(String id) {
    if (canAddMore()) {
      int position = this.activityDataKeys.indexOf(id);
      if (position < activityDataKeys.size() - 1) {
        this.activityDataKeys.remove(position);
      }
      this.activityDataKeys.offerLast(id);
    }
    
  }
  
  /**
   * Inserts the activityIds on the bottom of the list
   * @param id
   */
  public void insertLast(Collection<String> ids) {
    for(String id : ids) {
      insertLast(id);
    }
  }
  /**
   * Returns the sub list from this list extract between the specified offset and limit
   * 
   * @param offset the offset value
   * @param limit the limit value
   * @return sub list of the activityDataKeys list
   */
  public List<String> subList(int offset, int limit) {
    int to = Math.min(this.activityDataKeys.size(), offset + limit);
    return this.activityDataKeys.subList(offset, to);
  }
  
  /**
   * Removes the id
   */
  public void remove(String id) {
    this.activityDataKeys.remove(id);
  }
  
  /**
   * Returns size of the activities id
   * @return
   */
  public int size() {
    return activityDataKeys.size();
  }
  

}
