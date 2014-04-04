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

import java.util.Collections;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 30, 2014  
 */
public abstract class VersionGraph<K, V, M,
                  T extends Version<K, V, M>,
                  G extends VersionGraph<K, V, M, T, G>> 
       extends VersionGraphBase<K, V, M,T, G> {

  /** */
  public final VersionNode<K, V, M, T> tip;
  
  
  protected VersionGraph(Builder<K, V, M, T, G> builder) {
    super(builder.parentGraph, Collections.unmodifiableMap(builder.versionNodes));
    this.tip = builder.tip;
  }
  
  public abstract G commit(T version);
  
  public abstract G commit(Iterable<T> versions);
  
  public final Merge<K, V> merge(Set<Long> revisions) {
    return new Merge<K, V>(Iterables.transform(revisions, this));
  }

  protected static <K, 
                    V, 
                    M, 
                    T extends Version<K, V, M>, 
                    G extends VersionGraph<K, V, M, T, G>> 
            G build(Builder<K, V, M, T, G> builder) {
    return builder.build();
  }
  
  protected static <K, V, M, T extends Version<K, V, M>,
                             G extends VersionGraph<K, V, M, T, G>> 
                      G build(Builder<K, V, M, T, G> builder, T version) {
    builder.add(version);
    return builder.build();
  }
  
  protected static <K, 
                    V, 
                    M, 
                    T extends Version<K, V, M>,
                    G extends VersionGraph<K, V, M, T, G>> 
            G build(Builder<K, V, M, T, G> builder, Iterable<T> versions) {
    
    for(T version : versions) {
      builder.add(version);
    }
    
    return builder.build();
  }
  
  
  
  public static abstract class Builder<K, V, M,
                 T extends Version<K, V, M>,
                 G extends VersionGraph<K, V, M, T, G>> 
         extends VersionGraphBase<K, V, M, T, G> {

    private VersionNode<K, V, M, T> tip;
    
    protected Builder() {
      this(null);
    }
    
    protected Builder(G parentGraph) {
      super(parentGraph, Maps.<Long, VersionNode<K, V, M, T>>newLinkedHashMap());
      if (parentGraph != null) {
        this.tip = parentGraph.tip;
      }
      
    }
    
    void add(T version) {
      Preconditions.checkNotNull(version, "version");
      tip = new VersionNode<K, V, M, T>(tip, version, revisionsToNodes(version.parentRevisons));
      versionNodes.put(version.revision, tip);
    }
    
    protected abstract G build();
    
  }

}
