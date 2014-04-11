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
package com.exoplatform.social.activity.listener;

import java.util.Map;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.DataChangeListener;
import com.exoplatform.social.activity.DataContext;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.DataModel;
import com.exoplatform.social.activity.storage.cache.data.DataStatus;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 11, 2014  
 */
public class CachedListener<M extends ExoSocialActivity> implements DataChangeListener<M> {
  
  final DataContext<DataModel> context;
  /** */
  final Map<String, ActivityData> activityCache;
  /** */
  final Map<ListActivitiesKey, ActivitiesListData> activitiesCache;
  /** */
  final SOCContext socContext;
  
  public CachedListener(SOCContext socContext) {
    this.socContext = socContext;
    this.context = socContext.getDataContext();
    this.activityCache = socContext.getActivityCache();
    this.activitiesCache = socContext.getActivitiesCache();
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
 
}