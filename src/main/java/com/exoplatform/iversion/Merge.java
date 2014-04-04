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

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Maps.filterValues;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 3, 2014  
 */
public class Merge<K, V> {

  public final Map<K, VersionProperty<V>> mergedProperties;

  public final Multimap<K, VersionProperty<V>> conflicts;

  public final Set<Long> revisions;
  
  public final Function<VersionProperty<V>, V> getVersionPropertyValue = new Function<VersionProperty<V>, V>() {

      @Override
      public V apply(VersionProperty<V> input) {
          return input != null ? input.value : null;
      }
      
  };

  public <M, T extends Version<K, V, M>> Merge(Iterable<VersionNode<K, V, M, T>> versions) {
      checkNotNull(versions, "versions");
      Iterator<VersionNode<K, V, M, T>> iter = versions.iterator();

      // No versions
      if (!iter.hasNext()) {
          mergedProperties = ImmutableMap.of();
          revisions = ImmutableSet.of();
          conflicts = ImmutableMultimap.of();
      } else {
          VersionNode<K, V, M, T> versionNode = next(iter);

          // One version
          if (!iter.hasNext()) {
              mergedProperties = versionNode.getProperties();
              revisions = ImmutableSet.of(versionNode.getRevision());
              conflicts = ImmutableMultimap.of();
          } 
          // More than one version -> merge!
          else {
              Map<K, VersionProperty<V>> mergedProperties = Maps.newLinkedHashMap(versionNode.getProperties());
              Set<Long> heads = Sets.newHashSet(versionNode.getRevision());
              ImmutableMultimap.Builder<K, VersionProperty<V>> conflicts = ImmutableMultimap.builder();

              Set<Long> mergedRevisions = Sets.newHashSet(versionNode.getRevisions());
              do {
                  versionNode = next(iter);

                  // Version already merged?
                  if (!mergedRevisions.contains(versionNode.getRevision())) {
                      for (Map.Entry<K, VersionProperty<V>> entry : versionNode.getProperties().entrySet()) {
                          K key = entry.getKey();
                          VersionProperty<V> nextValue = entry.getValue();

                          // nextValue derives from common ancestor?
                          if (!mergedRevisions.contains(nextValue.revision)) {
                              VersionProperty<V> previousValue = mergedProperties.get(key);

                              // New value
                              if (previousValue == null) {
                                  mergedProperties.put(key, nextValue);
                              }
                              // Conflicting value?
                              else if (!equal(previousValue.value, nextValue.value)) {
                                  conflicts.put(key, nextValue);
                              }
                          }
                      }
                      mergedRevisions.addAll(versionNode.getRevisions());

                      heads.removeAll(versionNode.getRevisions());
                      heads.add(versionNode.getRevision());
                  }
              } while (iter.hasNext());

              this.mergedProperties = unmodifiableMap(mergedProperties);
              this.revisions = unmodifiableSet(heads);
              this.conflicts = conflicts.build();
          }
      }

  }

  public Map<K, V> getProperties() {
      return filterValues(Maps.transformValues(mergedProperties, getVersionPropertyValue), notNull());
  }
  
  private <M, T extends Version<K, V, M>> VersionNode<K, V, M, T> next(Iterator<VersionNode<K, V, M, T>> iter) {
      VersionNode<K, V, M, T> versionNode = iter.next();
      checkNotNull(versionNode, "versions should not contain nulls");
      return versionNode;
  }

}
