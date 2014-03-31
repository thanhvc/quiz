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
package com.exoplatform.iversion;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 30, 2014  
 */
public class VersionNode<K, V, M> {
  /** */
  private final Version<K, V, M> version;
  
  /** */
  private final Set<VersionNode<K, V, M>> parents;
  
  /** */
  private volatile SoftReference<Map<K, VersionProperty<V>>> softProperties;
  
  /** */
  private volatile SoftReference<Set<Long>> softRevisions;

  public VersionNode(Version<K, V, M> version, Set<VersionNode<K, V, M>> parents) {
    Preconditions.checkNotNull(version, "version");
    Preconditions.checkNotNull(parents, "parents");
    
    this.version = version;
    this.parents = parents;
    this.softProperties = softReference(null);
    this.softRevisions = softReference(null);
  }
  
  /**
   * wrap the object into soft reference.
   * @param value
   * @return
   */
  private <T> SoftReference<T> softReference(T value) {
    return new SoftReference<T>(value);
  }
  
  /**
   * Returns all of properties merge from its properties and parent's properties
   * if t0...tn belongs T set, make sure that if the same key, 
   * just takes its property when t0 < ti
   * 
   * that mean that only get the latest Version Property base on its revision.
   *  
   * @return
   */
  public Map<K, VersionProperty<V>> getProperties() {
    Map<K, VersionProperty<V>> properties = softProperties.get();
    
    if (properties == null) {
      properties = mergeProperties();
      softProperties = softReference(properties);
    }
    
    return properties;
  }
  
  /**
   * Gets the properties of its parents and itself.
   * Builds the map key, and Version Property, 
   * and the map is also immutable object.
   * 
   * @return
   */
  public Map<K, VersionProperty<V>> mergeProperties() {
    Map<K, VersionProperty<V>> properties = Maps.newLinkedHashMap();
    
    for(VersionNode<K, V, M> parent : parents) {
      for(Map.Entry<K, VersionProperty<V>> entry : parent.getProperties().entrySet()) {
        K key = entry.getKey();
        //TODO: this code processes only get latest property value
        VersionProperty<V> nextValue = entry.getValue();
        VersionProperty<V> prevValue = properties.get(key);
        
        if (prevValue == null) {
          properties.put(key, nextValue);
        } else if (prevValue.revision < nextValue.revision) {
          properties.put(key, nextValue);
        }
      }
    }
    
    properties.putAll(version.getVersionProperties());
    
    return Collections.unmodifiableMap(properties);
  }
  
  
  public Set<Long> getRevisions() {
    Set<Long> revisions = softRevisions.get();
    
    if (revisions == null) {
      ImmutableSet.Builder<Long> builder = ImmutableSet.builder();
      collectRevisions(builder);
      revisions = builder.build();
      softRevisions = softReference(revisions);
    }
    
    return revisions;
  }
  
  private void collectRevisions(ImmutableSet.Builder<Long> revisions) {
    revisions.add(getRevision());
    
    //
    for(VersionNode<K, V, M> parent : parents) {
      revisions.addAll(parent.getRevisions());
    }
  }
  
  public long getRevision() {
    return version.revision;
  }
}
