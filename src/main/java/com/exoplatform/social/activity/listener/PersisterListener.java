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
import com.exoplatform.social.activity.storage.ActivityStorage;
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.DataModel;
import com.exoplatform.social.activity.storage.cache.data.DataStatus;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 15, 2014  
 */
public class PersisterListener implements DataChangeListener<DataModel> {

  /** */
  final ActivityStorage storage;
  /** */
  final Map<String, ActivityData> activityCache;
  /** */
  final Map<ListActivitiesKey, ActivitiesListData> activitiesCache;
  /** */
  final SOCContext socContext;
  
  public PersisterListener(ActivityStorage storage, SOCContext socContext) {
    this.storage = storage;
    this.activityCache = socContext.getActivityCache();
    this.activitiesCache = socContext.getActivitiesCache();
    this.socContext = socContext;
  }

  @Override
  public void onAdd(DataModel target) {
    //System.out.println("Persister::onAdd");
    ActivityData data = activityCache.get(target.getHandle());
    if (data != null) {
      this.storage.saveActivity(data.build());
      data.setStatus(DataStatus.PERSISTENTED);
    }
  }

  @Override
  public void onRemove(DataModel target) {
    //System.out.println("Persister::onRemove");
    ActivityData data = activityCache.get(target.getHandle());
    this.storage.deleteActivity(data.build());
    activityCache.remove(target.getHandle());
  }

  @Override
  public void onUpdate(DataModel target) {
    //System.out.println("Persister::onUpdate");
    ActivityData data = activityCache.get(target.getHandle());
    if (data != null) {
      this.storage.update(data.build());
      data.setStatus(DataStatus.PERSISTENTED);
    }
  }
}
