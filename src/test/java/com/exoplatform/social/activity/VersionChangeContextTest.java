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

import java.util.List;

import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.stream.AStream;
import com.exoplatform.social.activity.storage.stream.AStream.Builder;
import com.exoplatform.social.activity.storage.stream.ActivityRefContext;
import com.exoplatform.social.activity.storage.stream.ActivityRefKey;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 10, 2014  
 */
public class VersionChangeContextTest extends AbstractActivityTest {
  
  private VersionChangeContext<ActivityRefKey> versionContext;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    versionContext = new VersionChangeContext<ActivityRefKey>();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    this.versionContext = null;
  }

  public void testAddChangesAndUpdate() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);

    for(ExoSocialActivity a : made) {
      Builder builder = AStream.initActivity(a);
      versionContext.add(builder.feedRefKey());
      versionContext.add(builder.ownerRefKey());
    }
    
    assertEquals(10, this.versionContext.getChangesSize());
  }
  
  public void testUpdateIncrease() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);

    for(ExoSocialActivity a : made) {
      Builder builder = AStream.initActivity(a);
      versionContext.add(builder.feedRefKey());
      versionContext.add(builder.ownerRefKey());
    }
    
    ExoSocialActivity a3 = made.get(3);
    
    Long increaseLastUpdated = System.currentTimeMillis() + 5000;
    a3.setLastUpdated(increaseLastUpdated);
    
    Builder builder = AStream.initActivity(a3);
    versionContext.add(builder.feedRefKey());
    versionContext.add(builder.ownerRefKey());
    
    assertEquals(10, this.versionContext.getChangesSize());
    Long revision = this.versionContext.getRevision(builder.feedRefKey());
    assertEquals(increaseLastUpdated, revision);
    revision = this.versionContext.getRevision(builder.ownerRefKey());
    assertEquals(increaseLastUpdated, revision);
    
  }
  
  public void testUpdateDecrease() throws Exception {
    List<ExoSocialActivity> made =  listOf(5, "mary", false, false);

    for(ExoSocialActivity a : made) {
      Builder builder = AStream.initActivity(a);
      versionContext.add(builder.feedRefKey());
      versionContext.add(builder.ownerRefKey());
    }
    
    ExoSocialActivity a1 = made.get(1);
    
    Long decreaseLastUpdated = a1.getLastUpdated() - 5000;
    a1.setLastUpdated(decreaseLastUpdated);
    
    Builder builder = AStream.initActivity(a1);
    versionContext.add(builder.feedRefKey());
    versionContext.add(builder.ownerRefKey());
    
    assertEquals(10, this.versionContext.getChangesSize());
    Long revision = this.versionContext.getRevision(builder.feedRefKey());
    assertTrue(decreaseLastUpdated < revision);
    revision = this.versionContext.getRevision(builder.ownerRefKey());
    assertTrue(decreaseLastUpdated < revision);
    
  }
  
  public void testAddChangesMore() throws Exception {
    
  }
  
  public void testRemoveChanges() throws Exception {
    
  }
}
