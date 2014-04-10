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
package com.exoplatform.social.activity.storage.ref;

import com.exoplatform.social.activity.Version;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.activity.storage.ref.ActivityRefContext.Builder;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 8, 2014  
 */
public final class ActivityRefKey extends Version {
  final String identityId;
  final StreamType type;
  final String activityId;
  
  public ActivityRefKey(Builder builder, StreamType type) {
    super(builder.activity.getLastUpdated());
    this.identityId = builder.identityId;
    this.type = type;
    this.activityId = builder.activity.getId();
  }
  
  @Override
  public String toString() {
    return super.toString();
  }
  
  @Override
  public int hashCode() {
    return super.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
  
  
  

}
