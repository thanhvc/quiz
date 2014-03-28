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



/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 18, 2014  
 */
public class ListActivitiesKey {
  
  /**  */
  private String remoteId;
  
  /** */
  private StreamType type;
  
  public ListActivitiesKey(String remoteId, StreamType type) {
    this.remoteId = remoteId;
    this.type = type;
  }
  
  /**
   * builds label of vertex in graph
   * 
   * @return
   */
  public String label() {
    return remoteId + "_" + this.type.name();
  }
  
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ListActivitiesKey)) {
      return false;
    }
    
    ListActivitiesKey that = (ListActivitiesKey) o;

    if (remoteId != null ? !remoteId.equals(that.remoteId) : that.remoteId != null) {
      return false;
    }
    
    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (remoteId != null ? remoteId.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }
  
  @Override
  public String toString() {
    return "ListActivitiesKey[remoteId: " + this.remoteId + ",type: " + this.type.toString() + " ]";
  }

}
