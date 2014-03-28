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
  /** */
  final LinkedList<String> activityDataKeys;
  
  /**
   * Constructors the empty instance.
   */
  public ActivitiesListData() {
    activityDataKeys = new LinkedList<String>();
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
   * @param id
   */
  public void insertFirst(String id) {
    //System.out.println("BEGIN::insert at the top: " + id);
    int position = this.activityDataKeys.indexOf(id);
    if (position > 0) {
      this.activityDataKeys.remove(position);
      this.activityDataKeys.offerFirst(id);
    } else if (position == -1) {
      this.activityDataKeys.offerFirst(id);
    }
    
    //System.out.println("END::insert at the top: " + id);
  }
  
  /**
   * Inserts the id on the top of this list
   * @param id
   */
  public void insertFirst(Collection<String> ids) {
    for(String id : ids) {
      insertFirst(id);
    }
  }
  
  /**
   * Inserts the activityId on the bottom of the list
   * @param id
   */
  public void insertLast(String id) {
    int position = this.activityDataKeys.indexOf(id);
    if (position < activityDataKeys.size() - 1) {
      this.activityDataKeys.remove(position);
    }
    this.activityDataKeys.offerLast(id);
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
  

}
