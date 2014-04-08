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
package com.exoplatform.social.activity.storage.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.DataChangeListener;
import com.exoplatform.social.activity.DataChangeQueue;
import com.exoplatform.social.activity.DataContext;
import com.exoplatform.social.activity.DataFixedSizeListener;
import com.exoplatform.social.activity.FixedSizeAlgorithm;
import com.exoplatform.social.activity.PersistAlgorithm;
import com.exoplatform.social.activity.Remover;
import com.exoplatform.social.activity.Updater;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.ActivityStorage;
import com.exoplatform.social.activity.storage.ActivityStreamStorage;
import com.exoplatform.social.activity.storage.SOCSession;
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.DataModel;
import com.exoplatform.social.activity.storage.cache.data.DataStatus;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.activity.storage.impl.ActivityStorageImpl.PersisterListener;
import com.exoplatform.social.graph.Operator;
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.social.graph.simple.SimpleUndirectGraph;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
public class CachedActivityStorage implements ActivityStorage {
  /** */
  static final String MENTION_CHAR = "@";
  /** */
  final DataContext<DataModel> context;
  /** */
  final DataChangeListener<ExoSocialActivity> cachedListener;
  /** */
  final DataChangeListener<DataModel> persisterListener;
  /** */
  final GraphListener<ExoSocialActivity> graphListener;
  /** */
  final PersistAlgorithm<DataModel> fixedSizeAlgorithm;
  /** */
  final ActivityStreamStorage streamStorage;
  /** */
  final ActivityStorage storage;
  /** */
  final SOCSession session;
  /** */
  final SOCContext socContext;
  /** */
  final Map<String, ActivityData> activityCache;
  /** */
  final Map<ListActivitiesKey, ActivitiesListData> activitiesCache;
  
  public CachedActivityStorage(int persisterThreshold, SOCContext socContext, ActivityStorage storage) {
    this.storage = storage;
    this.streamStorage = new CachedActivityStreamStorage();
    this.session = new SOCSession();
    this.socContext = socContext;
    this.context = new  DataContext<DataModel>();
    this.fixedSizeAlgorithm = new FixedSizeAlgorithm<DataModel>(context, persisterThreshold);
    this.cachedListener = new CachedListener<ExoSocialActivity>(context, socContext, fixedSizeAlgorithm);
    this.persisterListener = new PersisterListener(this.storage, this.session, socContext);
    this.graphListener = new GraphListener<ExoSocialActivity>(socContext);
    this.activityCache = socContext.getActivityCache();
    this.activitiesCache = socContext.getActivitiesCache();
  }
  
  private void persist() {
    if (fixedSizeAlgorithm.shoudldPersist()) {
      session.startSession();
      DataChangeQueue<DataModel> changes = context.getChanges();
      changes.broacast(this.persisterListener);
      context.popChanges();
      session.stopSession();
    }
  }

  @Override
  public void saveActivity(ExoSocialActivity activity) {
    cachedListener.onAdd(activity);
    graphListener.onAdd(activity);
    persist();
  }

  @Override
  public void saveComment(ExoSocialActivity activity, ExoSocialActivity comment) {
    List<String> commenters = new ArrayList<String>();
    activity.setCommenters(processCommenters(activity.getCommenters(), comment.getPosterId(), commenters, true));
    activity.setLastUpdated(System.currentTimeMillis());
    comment.setParentId(activity.getId());
    //
    cachedListener.onUpdate(activity);
    cachedListener.onAdd(comment);
    graphListener.onAdd(comment);
    persist();
  }

  @Override
  public void update(ExoSocialActivity activity) {
    cachedListener.onUpdate(activity);
    persist();
  }
  
  @Override
  public void deleteActivity(ExoSocialActivity activity) {
    cachedListener.onRemove(activity);
    graphListener.onRemove(activity);
    persist();
  }

  @Override
  public void deleteComment(String activityId, String commentId) {
    ActivityData data = activityCache.get(activityId);
    ExoSocialActivity activity = data.build();
    ActivityData commentData = activityCache.get(commentId);
    List<String> commenters = new ArrayList<String>();
    data.setCommenters(processCommenters(activity.getCommenters(), commentData.getPosterId(), commenters, false));
    
    
    activityCache.put(data.getId(), data);
    cachedListener.onUpdate(activityCache.get(activityId).build());
    cachedListener.onRemove(activityCache.get(commentId).build());
    
    persist();
  }
  
  private String[] processCommenters(String[] commenters, String commenter, List<String> addedOrRemovedIds, boolean isAdded) {
    if (commenter == null || commenter.length() == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    
    String newCommenter = commenter + MENTION_CHAR; 
    commenters = isAdded ? add(commenters, newCommenter, addedOrRemovedIds) : remove(commenters, newCommenter, addedOrRemovedIds);
    
    return commenters;
  }

  private String[] add(String[] mentionerIds, String mentionStr, List<String> addedOrRemovedIds) {
    if (ArrayUtils.toString(mentionerIds).indexOf(mentionStr) == -1) { // the first mention
      addedOrRemovedIds.add(mentionStr.replace(MENTION_CHAR, ""));
      return (String[]) ArrayUtils.add(mentionerIds, mentionStr + 1);
    }
    
    String storedId = null;
    for (String mentionerId : mentionerIds) {
      if (mentionerId.indexOf(mentionStr) != -1) {
        mentionerIds = (String[]) ArrayUtils.removeElement(mentionerIds, mentionerId);
        storedId = mentionStr + (Integer.parseInt(mentionerId.split(MENTION_CHAR)[1]) + 1);
        break;
      }
    }
    

    addedOrRemovedIds.add(mentionStr.replace(MENTION_CHAR, ""));
    mentionerIds = (String[]) ArrayUtils.add(mentionerIds, storedId);
    return mentionerIds;
  }

  private String[] remove(String[] mentionerIds, String mentionStr, List<String> addedOrRemovedIds) {
    for (String mentionerId : mentionerIds) {
      if (mentionerId.indexOf(mentionStr) != -1) {
        int numStored = Integer.parseInt(mentionerId.split(MENTION_CHAR)[1]) - 1;
        
        if (numStored == 0) {
          addedOrRemovedIds.add(mentionStr.replace(MENTION_CHAR, ""));
          return (String[]) ArrayUtils.removeElement(mentionerIds, mentionerId);
        }

        mentionerIds = (String[]) ArrayUtils.removeElement(mentionerIds, mentionerId);
        mentionerIds = (String[]) ArrayUtils.add(mentionerIds, mentionStr + numStored);
        addedOrRemovedIds.add(mentionStr.replace(MENTION_CHAR, ""));
        break;
      }
    }
    return mentionerIds;
  }
  
  @Override
  public ExoSocialActivity getActivity(String activityId) {
    return this.activityCache.containsKey(activityId) ? this.activityCache.get(activityId).build()
                                                     : this.storage.getActivity(activityId);
  }
  
  @Override
  public List<ExoSocialActivity> getFeed(String remoteId, int offset, int limit) {
    List<ExoSocialActivity> got = null;
    
    ListActivitiesKey key =  new ListActivitiesKey(remoteId, StreamType.FEED);
    
    if (this.activitiesCache.containsKey(key)) {
      
      //
      ActivitiesListData data = this.activitiesCache.get(key);
      
      //
      if (data != null) {
        got = buildActivities(data, offset, limit);
      } 
      
    } else {
      got = this.storage.getFeed(remoteId, offset, limit);
      //
      ActivitiesListData data = buildIds(key, got);
      //
      this.activitiesCache.put(key, data);
    }
    return got;
  }
  
  /**
   * Build the activity list from the caches Ids.
   *
   * @param data ids
   * @return activities
   */
  private List<ExoSocialActivity> buildActivities(ActivitiesListData data, int offset, int limit) {

    List<ExoSocialActivity> activities = new ArrayList<ExoSocialActivity>();
    List<String> ids = data.subList(offset, limit);
    for (String id : ids) {
      ExoSocialActivity a = getActivity(id);
      activities.add(a);
    }
    return activities;

  }
  
  /**
   * Build the ids from the activity list.
   *
   * @return remoteId
   * @param activities activities
   * 
   */
  private ActivitiesListData buildIds(ListActivitiesKey key, List<ExoSocialActivity> activities) {

    ActivitiesListData data = this.activitiesCache.get(key);
    if (data == null) {
      data = new ActivitiesListData(key.label());
    }
    
    //
    for(int i = activities.size()-1; i >= 0; i--) {
      ExoSocialActivity a = activities.get(i);
      if (!data.contains(a.getId())) {
        data.insertFirst(a.getId(), this.graphListener);
      }
      //
      if (!this.activityCache.containsKey(a.getId())) {
        this.activityCache.put(a.getId(), new ActivityData(a, DataStatus.NONE));
      }
    }
    
    return data;

  }
  

  @Override
  public List<ExoSocialActivity> getConnections(String remoteId, int offset, int limit) {
    
    List<ExoSocialActivity> got = null;
    
    ListActivitiesKey key =  new ListActivitiesKey(remoteId, StreamType.CONNECTION);
    
    if (this.activitiesCache.containsKey(key)) {
      
      //
      ActivitiesListData data = this.activitiesCache.get(key);
      
      //
      if (data != null) {
        got = buildActivities(data, offset, limit);
      } 
      
    } else {
      got = this.storage.getConnections(remoteId, offset, limit);
      //
      ActivitiesListData data = buildIds(key, got);
      //
      this.activitiesCache.put(key, data);
    }
    return got;
  }

  @Override
  public List<ExoSocialActivity> getMySpaces(String remoteId, int offset, int limit) {
    List<ExoSocialActivity> got = null;

    ListActivitiesKey key = new ListActivitiesKey(remoteId, StreamType.MY_SPACES);

    if (this.activitiesCache.containsKey(key)) {

      //
      ActivitiesListData data = this.activitiesCache.get(key);

      //
      if (data != null) {
        got = buildActivities(data, offset, limit);
      }

    } else {
      got = this.storage.getMySpaces(remoteId, offset, limit);
      //
      ActivitiesListData data = buildIds(key, got);
      //
      this.activitiesCache.put(key, data);
    }
    return got;
  }

  @Override
  public List<ExoSocialActivity> getSpace(String remoteId, int offset, int limit) {
    List<ExoSocialActivity> got = null;
    
    ListActivitiesKey key =  new ListActivitiesKey(remoteId, StreamType.SPACE_STREAM);
    
    if (this.activitiesCache.containsKey(key)) {
      
      //
      ActivitiesListData data = this.activitiesCache.get(key);
      
      //
      if (data != null) {
        got = buildActivities(data, offset, limit);
      } 
      
    } else {
      got = this.storage.getSpace(remoteId, offset, limit);
      //
      ActivitiesListData data = buildIds(key, got);
      //
      this.activitiesCache.put(key, data);
    }
    return got;
  }

  @Override
  public List<ExoSocialActivity> getOwner(String remoteId, int offset, int limit) {
    List<ExoSocialActivity> got = null;

    ListActivitiesKey key = new ListActivitiesKey(remoteId, StreamType.OWNER);

    if (this.activitiesCache.containsKey(key)) {

      //
      ActivitiesListData data = this.activitiesCache.get(key);

      //
      if (data != null) {
        got = buildActivities(data, offset, limit);
      }

    } else {
      got = this.storage.getOwner(remoteId, offset, limit);
      //
      ActivitiesListData data = buildIds(key, got);
      //
      this.activitiesCache.put(key, data);
    }
    return got;
  }

  @Override
  public int getNumberOfFeed(String remoteId) {
    return this.storage.getNumberOfFeed(remoteId);
  }

  @Override
  public int getNumberOfConnections(String remoteId) {
    return this.storage.getNumberOfConnections(remoteId);
  }

  @Override
  public int getNumberOfMySpaces(String remoteId) {
    return this.storage.getNumberOfMySpaces(remoteId);
  }

  @Override
  public int getNumberOfSpace(String remoteId) {
    return this.storage.getNumberOfSpace(remoteId);
  }

  @Override
  public int getNumberOfOwner(String remoteId) {
    return this.storage.getNumberOfOwner(remoteId);
  }
  
  
  static class CachedListener<M extends ExoSocialActivity> implements DataChangeListener<M> {
    
    final DataContext<DataModel> context;
    /** */
    final PersistAlgorithm<DataModel> fixedSizeAlgorithm;
    /** */
    final Map<String, ActivityData> activityCache;
    /** */
    final Map<ListActivitiesKey, ActivitiesListData> activitiesCache;
    /** */
    final SOCContext socContext;
    
    public CachedListener(DataContext<DataModel> context, SOCContext socContext,
                          PersistAlgorithm<DataModel> fixedSizeAlgorithm) {
      this.context = context;
      this.fixedSizeAlgorithm = fixedSizeAlgorithm;
      this.activityCache = socContext.getActivityCache();
      this.activitiesCache = socContext.getActivitiesCache();
      this.socContext = socContext;
    }

    @Override
    public void onAdd(ExoSocialActivity target) {
      ActivityData data = new ActivityData(target, DataStatus.TRANSIENT);
      activityCache.put(data.getId(), data);
      
      //
      context.add(data.buildModel());
    }

    @Override
    public void onRemove(ExoSocialActivity target) {
      ActivityData data = activityCache.get(target.getId());
      if (data != null && data.getStatus().equals(DataStatus.REMOVED)) {
        return;
      }
      
      data.setStatus(DataStatus.REMOVED);
      // activityCache.put(data.getId(), data);
      // TODO find all of activity's comments, and update DataStatus.REMOVED
      context.remove(data.buildModel());
    }

    @Override
    public void onUpdate(ExoSocialActivity target) {
      ActivityData data = activityCache.get(target.getId());
      if (data != null && data.getStatus().equals(DataStatus.REMOVED)) {
        return;
      }
      //TODO handle better concurrency updating
      ActivityData updated = new ActivityData(target, data.getStatus());
      activityCache.put(data.getId(), updated);
      
      context.update(data.buildModel());
    }

    @Override
    public void onMove(ExoSocialActivity target) {
      
    }
  }
  
  static class GraphListener<M extends ExoSocialActivity> implements DataChangeListener<M>, DataFixedSizeListener  {

    /** */
    final Operator<SimpleUndirectGraph, M> remover;

    /** */
    final Operator<SimpleUndirectGraph, M> updater;

    /** */
    final SimpleUndirectGraph graph;

    public GraphListener(SOCContext socContext) {
      remover = new Remover<M, SimpleUndirectGraph>(socContext);
      updater = new Updater<M, SimpleUndirectGraph>(socContext);
      this.graph = socContext.getActivityCacheGraph();
    }

    @Override
    public void onAdd(M target) {
      updater.execute(this.graph, target);
    }

    @Override
    public void onRemove(M target) {
      remover.execute(graph, target);
    }

    @Override
    public void onUpdate(M target) {

    }

    @Override
    public void onMove(M target) {

    }
    
    @Override
    public void update(String inVertexId, String outVertexId) {
      Vertex<Object> inVertex = this.graph.getVertex(inVertexId);
      Vertex<Object> outVertex = this.graph.getVertex(outVertexId);
      
      if (inVertex != null && outVertex != null) {
        this.graph.removeEdge(inVertex, outVertex);
      }
    }

  }
}
