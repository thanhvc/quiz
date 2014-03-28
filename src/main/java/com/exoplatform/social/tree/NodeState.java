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
package com.exoplatform.social.tree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Feb 25, 2014  
 */
@SuppressWarnings("serial")
public final class NodeState implements Serializable {
  public static final NodeState INITIAL = new NodeState.Builder().build();
  
  public static class Builder {
    /** contains the latest version of state changes*/
    long version;
    
    /** jcr uuid of node */
    String id;
    
    public Builder() {
      this.version = 0;
      this.id = "";
    }
    
    public NodeState build() {
      return new NodeState(id, version);
    }
  };
  
  /** contains the latest version of state changes*/
  private long version;
  
  /** contains the state changes consist of field name and value*/
  //TODO improve it with typesafe
  private Map<String, Object> stateChanges; 
  
  /** jcr uuid of node */
  private final String id;
  
  public NodeState(String id, long version) {
    this.id = id;
    this.version = version;
    this.stateChanges = new HashMap<String, Object>();
  }
  
  
  /**
   * Gets id of node
   * @return
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Gets version of node
   * @return
   */
  public long getVersion() {
    return this.version;
  }
  
  /**
   * Sets the version of node as milliseconds
   * @param version
   */
  public void setVersion(long version) {
    this.version = version;
  }
  
  /**
   * Sets current milliseconds as current version
   */
  public void setCurrentVersion() {
    this.version = System.currentTimeMillis();
  }
  
  /**
   * Sets the state what has been changes
   * @param key the fieldName of state
   * @param value the changed the value.
   */
  public void put(String key, Object value) {
    stateChanges.put(key, value);
  }
  
  /**
   * Sets the state what has been changes and set latest milliseconds for version
   * @param key the fieldName of state
   * @param value the changed the value.
   */
  public void putWithVersion(String key, Object value) {
    put(key, value);
    setCurrentVersion();
  }
  
  /**
   * Gets Iterator of value
   * @return
   */
  public Iterator<Object> getValueIterator() {
    return stateChanges.values().iterator();
  }
  
  /**
   * Gets Iterator of keys
   * @return
   */
  public Iterator<String> getKeyIterator() {
    return stateChanges.keySet().iterator();
  }
  
  /**
   * Gets size of state changes
   * @return
   */
  public int getSize() {
    return stateChanges.size();
  }
  
  @Override
  public String toString() {
    return "NodeState{id =[" + id + "], version [" + version + "]}";
  }
}
