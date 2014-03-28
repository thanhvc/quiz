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
package com.exoplatform.social;

import java.util.HashMap;
import java.util.Map;

import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.graph.UndirectedGraph;
import com.exoplatform.social.graph.Vertex;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 27, 2014  
 */
public class SOCContext {
  /** */
  final Map<String, ActivityData> activityCache;
  /** */
  final Map<ListActivitiesKey, ActivitiesListData> activitiesCache;
  
  /** */
  final UndirectedGraph activityCacheGraph;
  
  /** */
  final UndirectedGraph relationshipCacheGraph;
  
  public SOCContext() {
    this.activityCacheGraph = new UndirectedGraph(Vertex.MODEL);
    this.relationshipCacheGraph = new UndirectedGraph(Vertex.MODEL);
    this.activityCache = new HashMap<String, ActivityData>();
    this.activitiesCache = new HashMap<ListActivitiesKey, ActivitiesListData>();
  }
  
  /**
   * Returns the graph what provides relationship information 
   * between the activity and Identity's stream
   * 
   * For example: when UserA has relationship UserB, 
   * then activityA can show on their Feed stream both of them
   * 
   * @return the activity graph
   */
  public UndirectedGraph getActivityCacheGraph() {
    return activityCacheGraph;
  }
  
  /**
   * Returns the graph what provides connection information
   * 
   * @return the graph connection
   */
  public UndirectedGraph getRelationshipCacheGraph() {
    return relationshipCacheGraph;
  }

  public Map<String, ActivityData> getActivityCache() {
    return activityCache;
  }

  public Map<ListActivitiesKey, ActivitiesListData> getActivitiesCache() {
    return activitiesCache;
  }
  
  public void clear() {
    this.activitiesCache.clear();
    this.activityCache.clear();
    this.activityCacheGraph.clear();
    this.relationshipCacheGraph.clear();
  }
  
  

}
