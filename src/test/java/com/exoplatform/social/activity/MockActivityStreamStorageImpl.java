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
package com.exoplatform.social.activity;

import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.ActivityStreamStorage;
import com.exoplatform.social.activity.storage.ref.ActivityRefKey;
import com.exoplatform.social.activity.storage.ref.RefFixedSizeAlgorithm;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 8, 2014  
 */
public class MockActivityStreamStorageImpl implements ActivityStreamStorage {
  
  final VersionChangeContext<ActivityRefKey> context;
  
  final PersistAlgorithm<ActivityRefKey> refFixedSizeAlgorithm;
  
  public MockActivityStreamStorageImpl() {
    this.context = new VersionChangeContext<ActivityRefKey>();
    this.refFixedSizeAlgorithm = new RefFixedSizeAlgorithm<ActivityRefKey>(this.context);
    
  }

  @Override
  public void savePoster(ExoSocialActivity activity) {
    //ActivityRefContext.initActivity(isUserOwner, activity).feedKey();
    //ActivityRefContext.initActivity(isUserOwner, activity).connectionsKey();
    
  }

  @Override
  public void save(ExoSocialActivity activity) {
    
  }

  @Override
  public void delete(String activityId) {
    
  }

  @Override
  public void like(String likerIdentityId, ExoSocialActivity activity) {
    
  }

  @Override
  public void unlike(String removedIdentityId, ExoSocialActivity activity) {
    
  }

  @Override
  public void connect(String senderIdentityId, String receiverIdentityId) {
    
  }

  @Override
  public void update(ExoSocialActivity activity, String[] mentioner) {
    
  }

  @Override
  public void updateCommenter(ExoSocialActivity activity) {
    
  }

}
