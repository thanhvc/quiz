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

import java.util.ArrayList;
import java.util.List;

import com.exoplatform.social.activity.AbstractActivityTest;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.activity.storage.stream.ActivityRefContext;
import com.exoplatform.social.activity.storage.stream.ActivityRefKey;
import com.exoplatform.social.activity.storage.stream.ActivityRefContext.PostType;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 10, 2014  
 */
public class ActivityRefContextTest extends AbstractActivityTest {
  
  public void testBuildActivity() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);
    List<ActivityRefContext> keys = new ArrayList<ActivityRefContext>(made.size());
    
    for(ExoSocialActivity a : made) {
      keys.add(ActivityRefContext.initActivity(a).build());
    }
    
    assertEquals(5, keys.size());
    
    for(ActivityRefContext c : keys) {
      assertEquals(PostType.ACTIVITY, c.type);
      assertNull(c.comment);
      assertNotNull(c.activity);
    }
  }
  
  public void testBuildComment() throws Exception {
    List<ExoSocialActivity> activity =  listOf(1, "mary", false, false);
    
    List<ExoSocialActivity> comments =  listOf(5, "mary", true, false);
    List<ActivityRefContext> keys = new ArrayList<ActivityRefContext>(comments.size());
    
    for(ExoSocialActivity comment : comments) {
      keys.add(ActivityRefContext.initComment(activity.get(0), comment).build());
    }
    
    assertEquals(5, keys.size());
    
    for(ActivityRefContext c : keys) {
      assertEquals(PostType.COMMENT, c.type);
      assertNotNull(c.comment);
      assertNotNull(c.activity);
    }
  }
  
  public void testBuildFeedKeys() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);
    List<ActivityRefKey> keys = new ArrayList<ActivityRefKey>(made.size());
    
    for(ExoSocialActivity a : made) {
      keys.add(ActivityRefContext.initActivity(a).feedKey());
    }
    
    for(ActivityRefKey key : keys) {
      assertEquals(StreamType.FEED, key.type);
      assertNotNull(key.activityId);
      assertNotNull(key.identityId);
    }
  }
  
  public void testBuildConnectionsKeys() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);
    List<ActivityRefKey> keys = new ArrayList<ActivityRefKey>(made.size());
    
    for(ExoSocialActivity a : made) {
      keys.add(ActivityRefContext.initActivity(a).connectionsKey());
    }
    
    for(ActivityRefKey key : keys) {
      assertEquals(StreamType.CONNECTION, key.type);
      assertNotNull(key.activityId);
      assertNotNull(key.identityId);
    }
  }
  
  public void testBuildMySpacesKeys() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);
    List<ActivityRefKey> keys = new ArrayList<ActivityRefKey>(made.size());
    
    for(ExoSocialActivity a : made) {
      keys.add(ActivityRefContext.initActivity(a).mySpacesKey());
    }
    
    for(ActivityRefKey key : keys) {
      assertEquals(StreamType.MY_SPACES, key.type);
      assertNotNull(key.activityId);
      assertNotNull(key.identityId);
    }
  }
  
  public void testBuildOwnerKeys() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);
    List<ActivityRefKey> keys = new ArrayList<ActivityRefKey>(made.size());
    
    for(ExoSocialActivity a : made) {
      keys.add(ActivityRefContext.initActivity(a).ownerKey());
    }
    
    for(ActivityRefKey key : keys) {
      assertEquals(StreamType.OWNER, key.type);
      assertNotNull(key.activityId);
      assertNotNull(key.identityId);
    }
  }
  
}