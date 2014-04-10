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

import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.data.StreamType;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 8, 2014  
 */
public class ActivityRefContext {
  
  public enum PostType {
    ACTIVITY,
    COMMENT
  }
  
  final boolean isUserOwner;
  final String identityId;
  final ExoSocialActivity activity;
  final ExoSocialActivity comment;
  final PostType type;
  
  public static Builder initActivity(boolean isUserOwner, ExoSocialActivity activity) {
    return new Builder(isUserOwner, activity);
  }
  
  public static Builder initComment(boolean isUserOwner, ExoSocialActivity activity, ExoSocialActivity comment) {
    return new Builder(isUserOwner, activity, comment);
  }
  
  public ActivityRefContext(Builder builder) {
    this.isUserOwner = builder.isUserOwner;
    this.identityId = builder.identityId;
    this.activity = builder.activity;
    this.comment = builder.comment;
    this.type = builder.type;
  }
  
  public static class Builder {
    public boolean isUserOwner;
    public String identityId;
    public ExoSocialActivity activity;
    public ExoSocialActivity comment;
    public PostType type;
    
    public Builder(boolean isUserOwner, ExoSocialActivity activity, ExoSocialActivity comment) {
      this.isUserOwner = isUserOwner;
      this.type = PostType.COMMENT;
      this.activity = activity;
      this.comment = comment;
      this.identityId = comment.getPosterId();
    }
    
    public Builder(boolean isUserOwner, ExoSocialActivity activity) {
      this.isUserOwner = isUserOwner;
      this.type = PostType.ACTIVITY;
      this.activity = activity;
      this.comment = null;
      this.identityId = activity.getPosterId();
    }
    
    public ActivityRefContext build() {
      return new ActivityRefContext(this);
    }
    
    /**
     * Build the feed stream key and context
     * @return
     */
    public ActivityRefKey feedKey() {
      return new ActivityRefKey(this, StreamType.FEED);
    }
    
    /**
     * Build the connection stream key and context
     * @return
     */
    public ActivityRefKey connectionsKey() {
      return this.isUserOwner ? new ActivityRefKey(this, StreamType.CONNECTION) : null;
    }
    
    /**
     * Build the owner stream key and context
     * @return
     */
    public ActivityRefKey ownerKey() {
      return this.isUserOwner ? new ActivityRefKey(this, StreamType.OWNER) : null;
    }
    
    
    /**
     * Build the my spaces stream key and context
     * @return
     */
    public ActivityRefKey mySpacesKey() {
      return !this.isUserOwner ? new ActivityRefKey(this, StreamType.MY_SPACES) : null;
    }
  }

}
