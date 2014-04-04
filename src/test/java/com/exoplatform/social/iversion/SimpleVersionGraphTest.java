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
package com.exoplatform.social.iversion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.exoplatform.iversion.Merge;
import com.exoplatform.iversion.simple.SimpleVersion;
import com.exoplatform.iversion.simple.SimpleVersionGraph;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 4, 2014  
 */
public class SimpleVersionGraphTest extends TestCase {
  
  public static List<VersionExpectation> EXPECTATIONS = Arrays.asList(
                          when(version(1l)
                              .properties(mapOf(
                                          "firstName", "John",
                                          "lastName", "Doe")))
                              .expectProperties(mapOf(
                                          "firstName", "John",
                                          "lastName", "Doe")),


                          when(version(2l)
                                  .parents(setOf(1l))
                                  .properties(mapOf(
                                              "status", "Single")))
                                  .expectProperties(mapOf(
                                              "firstName", "John", // 1
                                              "lastName", "Doe", // 1
                                              "status", "Single")), // 2


                          when(version(3l)
                                  .parents(setOf(1l))
                                  .properties(mapOf(
                                              "mood", "Lonely")))
                                  .mergeRevisions(setOf(3l, 2l))
                                  .expectProperties(mapOf(
                                              "firstName", "John", // 1
                                              "lastName", "Doe", // 1
                                              "status", "Single", // 2
                                              "mood", "Lonely")), // 3


                          when(version(4l)
                                  .parents(setOf(3l))
                                  .properties(mapOf(
                                              "lastName", "Foe",
                                              "status", "Just married",
                                              "mood", "Ecstatic",
                                              "married", "2013-10-12")))
                                  .mergeRevisions(setOf(4l))
                                  .expectProperties(mapOf(
                                              "firstName", "John", // 1
                                              "lastName", "Foe", // 4
                                              "status", "Just married", // 4
                                              "mood", "Ecstatic", // 4
                                              "married", "2013-10-12")), // 4

                          then("Merge with ancestor")
                                  .mergeRevisions(setOf(3l, 4l))
                                  .expectRevisions(setOf(4l))
                                  .expectProperties(mapOf(
                                          "firstName", "John", // 1
                                          "lastName", "Doe", // 1
                                          "status", "Just married", // 4 - "Single" from version 2 is not merged here!
                                          "mood", "Lonely", // 3
                                          "married", "2013-10-12")) // 4
                                  .expectConflicts(multimapOf(
                                          "lastName", "Foe", // 4
                                          "mood", "Ecstatic" // 4
                                          )),


                          when(version(5l)
                                  .parents(setOf(2l))
                                  .properties(mapOf(
                                              "status", "Just married")))
                                  .mergeRevisions(setOf(4l, 5l))
                                  .expectProperties(mapOf(
                                              "firstName", "John",
                                              "lastName", "Foe",
                                              "status", "Just married", // 4 and 5 - not conflicting!
                                              "mood", "Ecstatic", // 4
                                              "married", "2013-10-12")), // 4

                          then("Conflicting merge")
                                  .mergeRevisions(setOf(5l, 4l))
                                  .expectProperties(mapOf(
                                          "firstName", "John", // 1
                                          "lastName", "Doe", // 1
                                          "status", "Just married", // 4 and 5 - not conflicting!
                                          "mood", "Ecstatic", // 4
                                          "married", "2013-10-12")) // 4.
                                  .expectConflicts(multimapOf(
                                          "lastName", "Foe" // 4
                                          )),


                          when(version(6l)
                                  .parents(setOf(5l, 4l))
                                  .properties(mapOf(
                                              "status", "Married",
                                              "mood", null,
                                              "married", null)))
                                  .expectProperties(mapOf(
                                              "firstName", "John",
                                              "lastName", "Foe",
                                              "status", "Married")) // 4 and 5 - not conflicting!
                          );
  
  public void testUpdates() throws Exception {
    SimpleVersionGraph versionGraph = SimpleVersionGraph.init();
    
    for(VersionExpectation expectation : EXPECTATIONS) {
      if (expectation.version != null) {
        versionGraph = versionGraph.commit(expectation.version);
      }
      
      assertExpectations(versionGraph, expectation);
    }
  }
  
  private void assertExpectations(SimpleVersionGraph versionGraph, VersionExpectation expectation) {
    //merge list revisions
    Merge<String, String> merge = versionGraph.merge(expectation.mergeRevisions);
    
    assertEquals(title("revisions", versionGraph, expectation), 
               merge.revisions, 
               expectation.expectedRevisions);
       
    assertEquals(title("properties", versionGraph, expectation), 
               merge.getProperties(), 
               expectation.expectedProperties);
       
    assertEquals(title("conflicts", versionGraph, expectation), 
               Multimaps.transformValues(merge.conflicts, merge.getVersionPropertyValue), 
               expectation.expectedConflicts);
  }
  
  private static String title(String assertLabel, SimpleVersionGraph graph, VersionExpectation expectation) {
    return assertLabel + " of #" + graph.tip.getRevision() + (expectation.title != null ? ": " + expectation.title : "");
  }
  
  public static class VersionExpectation {
    public final String title;
    public final SimpleVersion version;
    public Set<Long> mergeRevisions = ImmutableSet.of();
    public Map<String, String> expectedProperties;
    public Multimap<String, String> expectedConflicts = ImmutableMultimap.of();
    public Set<Long> expectedRevisions;
    
    public VersionExpectation(String title) {
      this(null, title);
    }
    
    public VersionExpectation(SimpleVersion version) {
      this(version, null);
    }
    
    public VersionExpectation(SimpleVersion version, String title) {
      this.version = version;
      this.title = title;
      if (version != null) {
        this.mergeRevisions = ImmutableSet.of(version.revision); 
      }
      
      this.expectedRevisions = mergeRevisions;
    }
    
    public VersionExpectation mergeRevisions(Set<Long> mergeRevisions) {
      this.mergeRevisions = mergeRevisions;
      this.expectedRevisions = mergeRevisions;
      return this;
    }
    
    public VersionExpectation expectProperties(Map<String, String> expectedProperties) {
      this.expectedProperties = expectedProperties;
      return this;
    }
    
    public VersionExpectation expectRevisions(Set<Long> expectedRevisions) {
      this.expectedRevisions = expectedRevisions;
      return this;
    }
    
    public VersionExpectation expectConflicts(Multimap<String, String> expectedConflicts) {
      this.expectedConflicts = expectedConflicts;
      return this;
    }
  }
  
  public static SimpleVersion.Builder version(long rev) {
    return new SimpleVersion.Builder(rev);
  }
  
  public static VersionExpectation when(SimpleVersion.Builder builder) {
    return new VersionExpectation(builder.build());
  }
  
  public static VersionExpectation then(String title) {
    return new VersionExpectation(title);
  }
  
  public static <T> Set<T> setOf(T...revs) {
    return ImmutableSet.copyOf(revs);
  }
  
  public static Map<String, String> mapOf(String ...entries) {
    Map<String, String> map = Maps.newLinkedHashMap();
    for(int i=0, len=entries.length; i+1 < len; i+=2) {
      map.put(entries[i], entries[i+1]);
    }
    
    return Collections.unmodifiableMap(map);
    
  }
  
  public static Multimap<String, String> multimapOf(String ...entries) {
    Multimap<String, String> map = ArrayListMultimap.create();
    for(int i=0, len = entries.length; i+1<len; i+=2) {
      map.put(entries[i], entries[i+1]);
    }
    
    return map;
  }

}
