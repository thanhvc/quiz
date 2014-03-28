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
package com.exoplatform.social.activity.storage;

import java.util.List;

import com.exoplatform.social.activity.model.ExoSocialActivity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
public interface ActivityStorage {

  void saveActivity(ExoSocialActivity activity);
  
  void saveComment(ExoSocialActivity activity, ExoSocialActivity comment);
  
  void update(ExoSocialActivity activity);
  
  void deleteActivity(ExoSocialActivity activity);
  
  void deleteComment(String activityId, String commentId);
  
  ExoSocialActivity getActivity(String activityId);
  
  List<ExoSocialActivity> getFeed(String remoteId, int offset, int limit);
  
  List<ExoSocialActivity> getConnections(String remoteId, int offset, int limit);
  
  List<ExoSocialActivity> getMySpaces(String remoteId, int offset, int limit);
  
  List<ExoSocialActivity> getSpace(String remoteId, int offset, int limit);
  
  List<ExoSocialActivity> getOwner(String remoteId, int offset, int limit);
  
  int getNumberOfFeed(String remoteId);
  
  int getNumberOfConnections(String remoteId);
  
  int getNumberOfMySpaces(String remoteId);
  
  int getNumberOfSpace(String remoteId);
  
  int getNumberOfOwner(String remoteId);
}
