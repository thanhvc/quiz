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


import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 30, 2014  
 */
public class VersionGraph<K, V, M> extends VersionGraphBase<K, V, M> {

  /** */
  public final VersionNode<K, V, M> tip;
  
  
  VersionGraph(VersionGraph<K, V, M> parentGraph, Map<Long, VersionNode<K, V, M>> versionNodes, VersionNode<K, V, M> tip) {
    super(parentGraph, versionNodes);
    this.tip = tip;
  }
  
  public static <K, V, M> VersionGraph<K, V, M> init(Version<K, V, M> version) {
    Builder<K, V, M> builder = new Builder<K, V, M>();
    builder.add(version);
    return builder.build();
  }
  
  public static class Builder<K, V, M> extends VersionGraphBase<K, V, M> {

    private VersionNode<K, V, M> tip;
    
    public Builder() {
      this(null);
    }
    
    Builder(VersionGraph<K, V, M> parentGraph) {
      super(parentGraph, Maps.<Long, VersionNode<K, V, M>>newLinkedHashMap());
      this.tip = parentGraph.tip;
    }
    
    void add(Version<K, V, M> version) {
      checkRevision(tip, version);
      tip = new VersionNode<K, V, M>(version, revisionsToNodes(version.parentRevisons));
      
    }
    
    VersionGraph<K, V, M> build() {
      return new VersionGraph<K, V, M>(parentGraph, versionNodes, tip);
    }
    
    private static void checkRevision(VersionNode<?, ?, ?> tip, Version<?, ?, ?> version) {
      Preconditions.checkNotNull(version, "version");
      if (tip != null && version.revision <= tip.getRevision()) {
          throw new IllegalVersionOrderException(tip.getRevision(), version.revision);
      }
  }
    
  }

}
