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
  private String identityId;
  
  /** */
  private StreamType type;
  
  public static Builder init(String identityId) {
    return new Builder(identityId);
  }
  
  private ListActivitiesKey(Builder builder, StreamType type) {
    this.identityId = builder.identityId;
    this.type = type;
  }
  
  /**
   * builds label of vertex in graph
   * 
   * @return
   */
  public String label() {
    return identityId + "_" + this.type.name();
  }
  
  public static class Builder {
    public final String identityId;
    public Builder(String identityId) {
      this.identityId = identityId;
    }

    public ListActivitiesKey key(StreamType type) {
      return new ListActivitiesKey(this, type);
    }
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

    if (identityId != null ? !identityId.equals(that.identityId) : that.identityId != null) {
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
    result = 31 * result + (identityId != null ? identityId.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }
  
  @Override
  public String toString() {
    return "ListActivitiesKey[remoteId: " + this.identityId + ",type: " + this.type.toString() + " ]";
  }

}
