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

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 30, 2014  
 */
public class Version<K, V, M> {
  
  private class ToVersionProperties implements Function<V, VersionProperty<V>> {

    @Override
    public VersionProperty<V> apply(V input) {
        return new VersionProperty<V>(revision, input);
    }
    
  }

  /** */
  public final long revision;
  
  /** */
  public final String branch; 
  
  /** */
  public final Set<Long> parentRevisons;
  
  /** */
  public final Map<K, V> properties;
  
  /** */
  public final M metadata;
  
  public Version(Builder<K, V, M> builder) {
    this.revision = builder.revision;
    this.branch = builder.branch;
    this.parentRevisons = builder.parentRevisions;
    this.properties = unmodifiableMap(newLinkedHashMap(builder.properties));
    this.metadata = builder.metadata;
  }

  public Map<K, VersionProperty<V>> getVersionProperties() {
    return Maps.transformValues(properties, toVersionProperties());
  }
  
  private Function<? super V, VersionProperty<V>> toVersionProperties() {
    return new ToVersionProperties();
  }
  
  @Override
  public String toString() {
    return "Version[revision = " + revision + "]";
  }
  
  public static class Builder<K, V, M> {
    /** */
    private static Set<Long> EMPTY_PARENTS = ImmutableSet.of();
    
    /** */
    public final long revision;
    
    /** */
    public String branch;
    
    /** */
    public Set<Long> parentRevisions = EMPTY_PARENTS;
    
    /** */
    public Map<K, V> properties = ImmutableMap.of();
    
    public M metadata;
    
    public Builder(long revision) {
      this.revision = revision;
    }
    /**
     * Sets branch value
     * @param branch
     * @return
     */
    public Builder<K, V, M> branch(String branch) {
      this.branch = branch;
      return this;
    }
    
    public Builder<K, V, M> parents(Set<Long> parentRevisions) {
      this.parentRevisions = parentRevisions;
      return this;
    }
    
    public Builder<K, V, M> properties(Map<K, V> properties) {
      this.properties = properties;
      return this;
    }
    
    public Builder<K, V, M> metadata(M metadata) {
      this.metadata = metadata;
      return this;
    }
  }
}
