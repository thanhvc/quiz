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
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.DataChangeListener;
import com.exoplatform.social.activity.DataChangeQueue;
import com.exoplatform.social.activity.DataContext;
import com.exoplatform.social.activity.listener.CachedListener;
import com.exoplatform.social.activity.listener.GraphListener;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.persister.Persister;
import com.exoplatform.social.activity.persister.PersisterTask;
import com.exoplatform.social.activity.storage.ActivityStorage;
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.DataModel;
import com.exoplatform.social.activity.storage.cache.data.DataStatus;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.activity.storage.impl.ActivityStorageImpl.PersisterListener;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
public class CachedActivityStorage implements ActivityStorage, Persister {
  /** */
  static final long INTERVAL_ACTIVITY_PERSIST_THRESHOLD = 2000; //2s = 1000 x 2
  /** */
  static final String MENTION_CHAR = "@";
  /** */
  final DataContext<DataModel> context;
  /** */
  final DataChangeListener<ExoSocialActivity> cachedListener;
  /** */
  final DataChangeListener<DataModel> jcrPersisterListener;
  /** */
  final GraphListener<ExoSocialActivity> graphListener;
  /** */
  final PersisterTask timerTask;
  /** */
  final ActivityStorage storage;
  /** */
  final SOCContext socContext;
  /** */
  final Map<String, ActivityData> activityCache;
  /** */
  final Map<ListActivitiesKey, ActivitiesListData> activitiesCache;
  
  public CachedActivityStorage(int persisterThreshold, SOCContext socContext, ActivityStorage storage) {
    this.storage = storage;
    this.socContext = socContext;
    this.context = new  DataContext<DataModel>();
    this.cachedListener = new CachedListener<ExoSocialActivity>(context, socContext);
    this.jcrPersisterListener = new PersisterListener(this.storage, socContext);
    this.graphListener = new GraphListener<ExoSocialActivity>(socContext);
    this.activityCache = socContext.getActivityCache();
    this.activitiesCache = socContext.getActivitiesCache();
    timerTask = PersisterTask.init()
                             .persister(this)
                             .wakeup(INTERVAL_ACTIVITY_PERSIST_THRESHOLD)
                             .timeUnit(TimeUnit.MILLISECONDS)
                             .maxFixedSize(persisterThreshold)
                             .build();
    timerTask.start();
  }
  
  @Override
  public void commit(boolean forceCommit) {
    persistFixedSize(forceCommit);
  }
  
  private void persistFixedSize(boolean forcePersist) {
    if (timerTask.shoudldPersist(context.getChangesSize()) || forcePersist) {
      this.socContext.getSession().startSession();
      DataChangeQueue<DataModel> changes = context.popChanges();
      if (changes != null) {
        changes.broadcast(this.jcrPersisterListener);
      }
      context.popChanges();
      this.socContext.getSession().stopSession();
    }
  }

  @Override
  public void saveActivity(ExoSocialActivity activity) {
    cachedListener.onAdd(activity);
    graphListener.onAdd(activity);
    commit(false);
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
    commit(false);
  }

  @Override
  public void update(ExoSocialActivity activity) {
    cachedListener.onUpdate(activity);
    commit(false);
  }
  
  @Override
  public void deleteActivity(ExoSocialActivity activity) {
    cachedListener.onRemove(activity);
    graphListener.onRemove(activity);
    commit(false);
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
    
    commit(false);
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
    
    ListActivitiesKey key =  ListActivitiesKey.init(remoteId).key(StreamType.FEED);
    
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
    
    ListActivitiesKey key =  ListActivitiesKey.init(remoteId).key(StreamType.CONNECTION);
    
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

    ListActivitiesKey key =  ListActivitiesKey.init(remoteId).key(StreamType.MY_SPACES);

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
    
    ListActivitiesKey key =  ListActivitiesKey.init(remoteId).key(StreamType.SPACE_STREAM);
    
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

    ListActivitiesKey key =  ListActivitiesKey.init(remoteId).key(StreamType.OWNER);

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
}
