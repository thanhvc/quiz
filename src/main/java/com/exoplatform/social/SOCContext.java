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

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import com.exoplatform.social.activity.VersionChangeContext;
import com.exoplatform.social.activity.operator.Persister;
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.activity.storage.ref.ActivityRefKey;
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.social.graph.simple.SimpleUndirectGraph;

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
  final VersionChangeContext<ActivityRefKey> versionContext;
  
  /** */
  final SimpleUndirectGraph activityCacheGraph;
  
  /** */
  final SimpleUndirectGraph relationshipCacheGraph;
  
  /** */
  private static ConcurrentHashMap<Object, SoftReference<Persister>> persisters;
  /** */
  private static ScheduledExecutorService scheduledExecutor;
  
  @SuppressWarnings("unchecked")
  public SOCContext() {
    this.versionContext = new VersionChangeContext<ActivityRefKey>();
    this.activityCacheGraph = new SimpleUndirectGraph(Vertex.MODEL);
    this.relationshipCacheGraph = new SimpleUndirectGraph(Vertex.MODEL);
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
  public SimpleUndirectGraph getActivityCacheGraph() {
    return activityCacheGraph;
  }
  
  /**
   * Returns the graph what provides connection information
   * 
   * @return the graph connection
   */
  public SimpleUndirectGraph getRelationshipCacheGraph() {
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
    this.versionContext.clearChanges();
  }
  
  public VersionChangeContext<ActivityRefKey> getVersionContext() {
    return versionContext;
  }
  
  public class ActivityTask implements Runnable {
    public void run() {
      try {
      } catch (Throwable t) {
      }
    }
  }
  

}
