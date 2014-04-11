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

import com.exoplatform.social.activity.Version;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.activity.storage.stream.ActivityRefContext.Builder;

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
    return "ActivityRefKey[identityId="+this.identityId+", activityId = " + this.activityId + ", revision = " + revision +"]";
  }
  
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ActivityRefKey)) {
      return false;
    }

    ActivityRefKey that = (ActivityRefKey) o;

    if (identityId != null ? !identityId.equals(that.identityId) : that.identityId != null) {
      return false;
    }
    
    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }
    
    if (activityId != null ? !activityId.equals(that.activityId) : that.activityId != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (identityId != null ? identityId.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (activityId != null ? activityId.hashCode() : 0);
    return result;
  }

}
