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
package com.exoplatform.social.activity.operator;

import java.util.Map;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.DataFixedSizeListener;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.graph.GraphContext.Scope;
import com.exoplatform.social.graph.Operator;
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.social.graph.simple.SimpleUndirectGraph;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 8, 2014  
 */
public abstract class ActivityOperator<G extends SimpleUndirectGraph, M extends ExoSocialActivity> implements Operator<G, M> {
  
   public static abstract class OperatorContext {
    /** */
    final Map<String, ActivityData> activityCache;
    /** */
    final Map<ListActivitiesKey, ActivitiesListData> activitiesCache;
    /** */
    final SimpleUndirectGraph relationshipGraph;
    
    OperatorContext(SOCContext socContext) {
      this.activityCache = socContext.getActivityCache();
      this.activitiesCache = socContext.getActivitiesCache();
      this.relationshipGraph = socContext.getRelationshipCacheGraph();
    }
    
    /**
     * Updates the graph when adds new activity.
     *  
     * @param graph the updating graph
     * @param isPoster if Poster provided TRUE Otherwise FALSE
     * @param key the vertex key
     * @param target the added Activity
     * @return success returns TRUE Otherwise FALSE
     */
    abstract boolean update(SimpleUndirectGraph graph, boolean isPoster, ListActivitiesKey key, ExoSocialActivity target);
    
    /**
     * Updates the graph when activity has been removed
     * @param graph
     * @param key
     * @param target
     * @return
     */
    abstract boolean remove(SimpleUndirectGraph graph, ListActivitiesKey key, ExoSocialActivity target);
    
    public static class UPDATE extends OperatorContext implements DataFixedSizeListener {

      /** */
      final SimpleUndirectGraph graph;
      
      UPDATE(SOCContext socContext) {
        super(socContext);
        this.graph = socContext.getActivityCacheGraph();
      }
      
      @Override
      public boolean update(SimpleUndirectGraph graph,
                         boolean isPoster,
                         ListActivitiesKey key,
                         ExoSocialActivity target) {
        return processGraph(graph, isPoster, key, target);
      }
      
      private boolean processGraph(SimpleUndirectGraph graph, boolean isPoster, ListActivitiesKey key, ExoSocialActivity target) {
        ActivitiesListData data = activitiesCache.get(key);
        if (!isPoster && data == null)
          return false;

        if (data == null) {
          data = new ActivitiesListData(key.label());
          activitiesCache.put(key, data);
          
          Vertex<Object> inVertex = graph.getVertex(target.getId());
          Vertex<Object> outVertex = graph.addVertex(key);
          graph.addEdge(key.label(), inVertex, outVertex);
        }
        
        // TODO check edge is existing or not
        // add edge and inVertex and outVertex
        data.insertFirst(target.getId(), this);
        
        return true;
      }

      @Override
      public boolean remove(SimpleUndirectGraph graph, ListActivitiesKey key, ExoSocialActivity target) {
        throw new UnsupportedOperationException("Unsupported to invoke remove() method.");
      }
      
      @Override
      public void update(String inVertexId, String outVertexId) {
        Vertex<Object> inVertex = this.graph.getVertex(inVertexId);
        Vertex<Object> outVertex = this.graph.getVertex(outVertexId);
        
        if (inVertex != null && outVertex != null) {
          this.graph.removeEdge(inVertex, outVertex);
        }
      }
    };
    

    public static class REMOVE extends OperatorContext {

      REMOVE(SOCContext socContext) {
        super(socContext);
      }
      
      @Override
      public boolean update(SimpleUndirectGraph graph,
                         boolean isPoster,
                         ListActivitiesKey key,
                         ExoSocialActivity target) {
        throw new UnsupportedOperationException("Unsupported to invoke add() method.");
      }
      
      @Override
      public boolean remove(SimpleUndirectGraph graph, ListActivitiesKey key, ExoSocialActivity target) {
        return removeGraph(graph, key, target);
      }
      
      /**
       * 
       * @param key
       * @param target
       * @return
       */
      private boolean removeGraph(SimpleUndirectGraph graph, ListActivitiesKey key, ExoSocialActivity target) {
        ActivitiesListData data = activitiesCache.get(key);
        if (data != null) {
          //TODO implement remove as sequence commands 
          //this.activityGraph.removeEdge(key.label()).removeVertex(String.class, target.getId()).removeVertex(ListActivitiesKey.class, key)
          graph.removeEdge(key.label());
          graph.removeVertex(String.class, target.getId(), Scope.ALL);
          graph.removeVertex(ListActivitiesKey.class, key, Scope.SINGLE);
          data.remove(target.getId());
          return true;
        }
        
        return false;
      }
    };
  }

}
