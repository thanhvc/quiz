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

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 30, 2014  
 */
public abstract class VersionGraphBase<K, V, M,
                                        T extends Version<K, V, M>,
                                        G extends VersionGraph<K, V, M, T, G>> 
                implements Function<Long, VersionNode<K, V, M, T>>{
  /** */
  public final VersionGraph<K, V, M, T, G> parentGraph;
  
  /** */
  public final Map<Long, VersionNode<K, V, M, T>> versionNodes;
  
  private final RevisionToVersionNode revisionToVersionNode = new RevisionToVersionNode();
  
  private class RevisionToVersionNode implements Function<Long, VersionNode<K, V, M, T>> {

    @Override
    public VersionNode<K, V, M, T> apply(Long input) {
      return getVersionNode(input);
    }
  }
  
  VersionGraphBase(VersionGraph<K, V, M, T, G> parentGraph, Map<Long, VersionNode<K, V, M, T>> versionNodes) {
    this.parentGraph = parentGraph;
    this.versionNodes = Preconditions.checkNotNull(versionNodes, "versionNodes");
  }
  
  Set<VersionNode<K, V, M, T>> revisionsToNodes(Iterable<Long> revisions) {
    return ImmutableSet.copyOf(Iterables.transform(revisions, revisionToVersionNode));
  }
  
  public VersionNode<K, V, M, T> getVersionNode(long revision) {
    VersionNode<K, V, M, T> node = versionNodes.get(revision);
    if (node == null) {
      if(parentGraph != null) {
        return parentGraph.getVersionNode(revision);
      }
      
      throw new VersionNotFoundException(revision);
    }
    
    return node;
  }
  
  @Override
  public VersionNode<K, V, M, T> apply(Long input) {
    return input != null ? getVersionNode(input) : null;
  }

}
