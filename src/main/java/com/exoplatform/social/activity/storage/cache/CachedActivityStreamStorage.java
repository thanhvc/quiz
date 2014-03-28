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

import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.ActivityStreamStorage;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 13, 2014  
 */
public class CachedActivityStreamStorage implements ActivityStreamStorage {

  @Override
  public void savePoster(ExoSocialActivity activity) {
    
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
    // TODO Auto-generated method stub
    
  }

  @Override
  public void connect(String senderIdentityId, String receiverIdentityId) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void update(ExoSocialActivity activity, String[] mentioner) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updateCommenter(ExoSocialActivity activity) {
    // TODO Auto-generated method stub
    
  }

}
