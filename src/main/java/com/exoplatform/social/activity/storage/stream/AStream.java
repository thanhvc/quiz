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
package com.exoplatform.social.activity.storage.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.IdentityProvider;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.activity.storage.cache.data.StreamFixedSizeListener;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.activity.storage.stream.ActivityRefContext.PostType;
import com.exoplatform.social.graph.GraphContext.Scope;
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.social.graph.simple.SimpleUndirectGraph;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 12, 2014  
 */
public abstract class AStream implements StreamFixedSizeListener {
  
  protected SOCContext socContext; 
  
  final List<ActivityRefKey> keys;
  //TODO
  //AStream.UPDATER.context(SOCContext).input(target)
  //             .feed(builder.key(StreamType.FEED))
  //             .connection(builder.key(StreamType.CONNECTION))
  //             .owner(builder.key(StreamType.OWNER)
  //            .spaces(builder.key(StreamType.MY_SPACES));
  
  public AStream() {
    keys = new ArrayList<ActivityRefKey>(5);
  }
  
  public AStream context(SOCContext socContext) {
    this.socContext = socContext;
    return this;
  }
  
  public AStream feed(Builder builder) {
    this.keys.add(builder.feedRefKey());
    return this;
  }
  
  public AStream owner(Builder builder) {
    if (builder.isUserOwner) {
      this.keys.add(builder.ownerRefKey());
    }
    
    return this;
  }
  
  public AStream connection(Builder builder) {
    if (builder.isUserConnection) {
      this.keys.add(builder.connectionsRefKey());
    }
    
    return this;
  }
  
  public AStream myspaces(Builder builder) {
    if (!builder.isUserOwner) {
      this.keys.add(builder.mySpacesRefKey());
    }
    
    return this;
  }
  
  public AStream space(Builder builder) {
    if (!builder.isUserOwner) {
      this.keys.add(builder.spaceRefKey());
    }
    
    return this;
  }
  
  @Override
  public void update(String inVertexId, String outVertexId) {
    SimpleUndirectGraph graph = this.socContext.getActivityCacheGraph();
    Vertex<Object> inVertex = graph.getVertex(inVertexId);
    Vertex<Object> outVertex = graph.getVertex(outVertexId);
    
    if (inVertex != null && outVertex != null) {
      graph.removeEdge(inVertex, outVertex);
    }
  }
  
  public abstract void doExecute();
  
  public static class UPDATER extends AStream {
    
    public static AStream init() {
      return new UPDATER();
    }
    @Override
    public void doExecute() {
      Map<ListActivitiesKey, ActivitiesListData> activitiesCache = this.socContext.getActivitiesCache();
      SimpleUndirectGraph graph = this.socContext.getActivityCacheGraph();
      
      //
      for(ActivityRefKey refKey : keys) {
        ListActivitiesKey cacheKey = refKey.listActivitiesKey();
        ActivitiesListData data = activitiesCache.get(cacheKey);
        
        if (data == null) {
          data = new ActivitiesListData(cacheKey.label());
          activitiesCache.put(cacheKey, data);
          
          Vertex<Object> inVertex = graph.addVertex(refKey.activityId);
          Vertex<Object> outVertex = graph.addVertex(cacheKey);
          graph.addEdge(cacheKey.label(), inVertex, outVertex);
        }
        
        data.insertFirst(refKey.activityId, this);
      }
    }
    
  };
  
  public static class REMOVER extends AStream {
    
    public static AStream init() {
      return new REMOVER();
    }
    
    
    @Override
    public void doExecute() {
      Map<ListActivitiesKey, ActivitiesListData> activitiesCache = this.socContext.getActivitiesCache();
      SimpleUndirectGraph graph = this.socContext.getActivityCacheGraph();
      
      //
      for(ActivityRefKey refKey : keys) {
        ListActivitiesKey cacheKey = refKey.listActivitiesKey();
        ActivitiesListData data = activitiesCache.get(cacheKey);
        
          if (data != null) {
            //this.activityGraph.removeEdge(key.label())
            //.removeVertex(String.class, target.getId())
            //.removeVertex(ListActivitiesKey.class, key)
            graph.removeEdge(cacheKey.label());
            graph.removeVertex(String.class, refKey.activityId, Scope.ALL);
            graph.removeVertex(ListActivitiesKey.class, cacheKey.label(), Scope.SINGLE);
            data.remove(refKey.activityId);
        }
        
      }
    }
    
  };
  
  public static Builder initActivity(ExoSocialActivity activity) {
    return new Builder(activity);
  }
  
  public static Builder initActivity(String identityId, ExoSocialActivity activity) {
    return new Builder(identityId, activity);
  }
  
  public static Builder initComment(ExoSocialActivity activity, ExoSocialActivity comment) {
    return new Builder(activity, comment);
  }
  
  public static Builder initComment(String identityId, ExoSocialActivity activity, ExoSocialActivity comment) {
    return new Builder(identityId, activity, comment);
  }

  public static class Builder {
    public boolean isUserOwner;
    public boolean isUserConnection;
    public String identityId;
    public ExoSocialActivity activity;
    public ExoSocialActivity comment;
    public PostType type;
    public SOCContext socContext;
    
    public Builder(ExoSocialActivity activity, ExoSocialActivity comment) {
     this(activity.getPosterId(), activity, comment);
    }
    
    public Builder(String identityId, ExoSocialActivity activity, ExoSocialActivity comment) {
      this.isUserOwner = IdentityProvider.USER.name().equalsIgnoreCase(activity.getPosterProviderId());
      this.type = comment != null ? PostType.COMMENT : PostType.ACTIVITY;
      this.activity = activity;
      this.comment = comment;
      this.identityId = identityId;
      this.isUserConnection = this.identityId != this.activity.getPosterId();
      
    }
    
    public Builder(ExoSocialActivity activity) {
      this(activity, null);
    }
    
    public Builder(String identityId, ExoSocialActivity activity) {
      this(identityId, activity, null);
    }
    
    public ActivityRefContext buildContext() {
      return new ActivityRefContext(this);
    }
    
    /**
     * Build the feed stream key and context
     * @return
     */
    public ActivityRefKey feedRefKey() {
      return new ActivityRefKey(this, StreamType.FEED);
    }
    
    /**
     * Build the connection stream key and context
     * @return
     */
    public ActivityRefKey connectionsRefKey() {
      return this.isUserOwner ? new ActivityRefKey(this, StreamType.CONNECTION) : null;
    }
    
    /**
     * Build the owner stream key and context
     * @return
     */
    public ActivityRefKey ownerRefKey() {
      return this.isUserOwner ? new ActivityRefKey(this, StreamType.OWNER) : null;
    }
    /**
     * Build the my spaces stream key and context
     *
     * @return
     */
    public ActivityRefKey mySpacesRefKey() {
      //TODO return NULL Option
      return !this.isUserOwner ? new ActivityRefKey(this, StreamType.MY_SPACES) : null;
    }
    
    public ActivityRefKey spaceRefKey() {
      //TODO return NULL Option
      return !this.isUserOwner ? new ActivityRefKey(this, StreamType.SPACE_STREAM) : null;
    }
    
    public ListActivitiesKey feedCacheKey() {
      return this.isUserOwner ? ListActivitiesKey.init(identityId).key(StreamType.FEED) : null;
    }
    
    public ListActivitiesKey connectionCacheKey() {
      return this.isUserOwner ? ListActivitiesKey.init(identityId).key(StreamType.CONNECTION) : null;
    }
    
    public ListActivitiesKey ownerCacheKey() {
      return this.isUserOwner ? ListActivitiesKey.init(identityId).key(StreamType.OWNER) : null;
    }
    
    public ListActivitiesKey myspacesCacheKey() {
      return this.isUserOwner ? ListActivitiesKey.init(identityId).key(StreamType.MY_SPACES) : null;
    }
    
    public ListActivitiesKey spaceCacheKey() {
      return this.isUserOwner ? null : ListActivitiesKey.init(identityId).key(StreamType.SPACE_STREAM);
    }
  }
}
