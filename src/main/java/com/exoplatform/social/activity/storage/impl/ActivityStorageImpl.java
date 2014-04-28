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
package com.exoplatform.social.activity.storage.impl;

import java.util.List;
import java.util.Map;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.DataChangeListener;
import com.exoplatform.social.activity.model.ExoSocialActivity;
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
 * Mar 12, 2014  
 */
public class ActivityStorageImpl implements ActivityStorage {

  @Override
  public void saveActivity(ExoSocialActivity activity) {
    
  }

  @Override
  public void saveComment(ExoSocialActivity activity, ExoSocialActivity comment) {
    
  }

  @Override
  public void update(ExoSocialActivity activity) {
    
  }

  @Override
  public void deleteActivity(ExoSocialActivity activity) {
    
  }

  @Override
  public void deleteComment(String activityId, String commentId) {
    
  }
  
  @Override
  public List<ExoSocialActivity> getFeed(String remoteId, int offset, int limit) {
    return null;
  }

  @Override
  public List<ExoSocialActivity> getConnections(String remoteId, int offset, int limit) {
    return null;
  }

  @Override
  public List<ExoSocialActivity> getMySpaces(String remoteId, int offset, int limit) {
    return null;
  }

  @Override
  public List<ExoSocialActivity> getSpace(String remoteId, int offset, int limit) {
    return null;
  }

  @Override
  public List<ExoSocialActivity> getOwner(String remoteId, int offset, int limit) {
    return null;
  }
  
  @Override
  public ExoSocialActivity getActivity(String activityId) {
    return null;
  }

  @Override
  public int getNumberOfFeed(String remoteId) {
    return 0;
  }

  @Override
  public int getNumberOfConnections(String remoteId) {
    return 0;
  }

  @Override
  public int getNumberOfMySpaces(String remoteId) {
    return 0;
  }

  @Override
  public int getNumberOfSpace(String remoteId) {
    return 0;
  }

  @Override
  public int getNumberOfOwner(String remoteId) {
    return 0;
  }
}
