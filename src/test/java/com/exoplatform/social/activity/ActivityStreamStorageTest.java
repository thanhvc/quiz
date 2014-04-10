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
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.DataStatus;
import com.exoplatform.social.activity.storage.cache.data.IdentityProvider;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 10, 2014  
 */
public class ActivityStreamStorageTest extends AbstractActivityTest {
  
  
  public void testSaveActivity() throws Exception {
    ExoSocialActivity activity = ActivityBuilder.getInstance()
                                                .posterId("mary")
                                                .title("title")
                                                .body("body")
                                                .titleId("titleId")
                                                .commenters("")
                                                .posterProviderId(IdentityProvider.USER.getName())
                                                .isComment(false)
                                                .take();
    cachedActivityStorage.saveActivity(activity);
    assertEquals(1, socContext.getActivityCache().size());
    ActivityData data = socContext.getActivityCache().get(activity.getId());
    assertNotNull(data);
    assertEquals(data.getStatus(), DataStatus.TRANSIENT);
    
    //don't enough number of activity to active Activity Persister
    assertEquals(0, this.socContext.getVersionContext().getChangesSize());
  }
  
  public void testGetFeed() throws Exception {
    
    listOf(50, "mary", false, true);

    List<ExoSocialActivity> feed = cachedActivityStorage.getFeed("mary", 0, 20);
    assertEquals(20, feed.size());
    
    //make sure get from caching
    feed = cachedActivityStorage.getFeed("mary", 0, 20);
    assertEquals(20, feed.size());
    
    assertEquals(numberOfRefKeys(50), this.socContext.getVersionContext().getChangesSize());
  }
  
  public void testGetMySpaces() throws Exception {
    
    listOf(60, "john", false, true);

    List<ExoSocialActivity> myspaces = cachedActivityStorage.getMySpaces("john", 0, 20);
    assertEquals(20, myspaces.size());
    
    //make sure get from caching
    myspaces = cachedActivityStorage.getMySpaces("john", 0, 20);
    assertEquals(20, myspaces.size());
    
    assertEquals(numberOfRefKeys(60), this.socContext.getVersionContext().getChangesSize());
  }
  
  public void testGetOwner() throws Exception {
    
    listOf(80, "demo", false, true);

    List<ExoSocialActivity> owner = cachedActivityStorage.getOwner("demo", 0, 20);
    assertEquals(20, owner.size());
    
    //make sure get from caching
    owner = cachedActivityStorage.getOwner("demo", 0, 20);
    assertEquals(20, owner.size());
    
    assertEquals(numberOfRefKeys(80), this.socContext.getVersionContext().getChangesSize());
  }
  
  public void testGetConnections() throws Exception {

    listOf(70, "bob", false, true);

    List<ExoSocialActivity> connections = cachedActivityStorage.getConnections("bob", 0, 20);
    assertEquals(20, connections.size());

    // make sure get from caching
    connections = cachedActivityStorage.getConnections("bob", 0, 20);
    assertEquals(20, connections.size());
    
    assertEquals(numberOfRefKeys(70), this.socContext.getVersionContext().getChangesSize());
  }
  
  public void testSaveActivities10() throws Exception {
    
    List<ExoSocialActivity> list = listOf(10, "mary", false, true);
    
    assertEquals(10, socContext.getActivityCache().size());
    ActivityData data0 = socContext.getActivityCache().get(list.get(0).getId());
    assertNotNull(data0);
    assertEquals(data0.getStatus(), DataStatus.TRANSIENT);
    
    assertEquals(numberOfRefKeys(10), this.socContext.getVersionContext().getChangesSize());
  }
  
  public void testSaveActivities15() throws Exception {
    
    List<ExoSocialActivity> list = listOf(15, "mary", false, true);
    
    assertEquals(15, socContext.getActivityCache().size());
    ActivityData data0 = socContext.getActivityCache().get(list.get(0).getId());
    assertNotNull(data0);
    assertEquals(data0.getStatus(), DataStatus.PERSISTENTED);
    
    ActivityData data11 = socContext.getActivityCache().get(list.get(11).getId());
    assertNotNull(data11);
    assertEquals(data11.getStatus(), DataStatus.PERSISTENTED);
    
    ActivityData data12 = socContext.getActivityCache().get(list.get(12).getId());
    assertNotNull(data12);
    assertEquals(data12.getStatus(), DataStatus.TRANSIENT);
    
    //14 > PERSISTER_THRESHOLD
    ActivityData data14 = socContext.getActivityCache().get(list.get(14).getId());
    assertNotNull(data14);
    assertEquals(data14.getStatus(), DataStatus.TRANSIENT);
    
    assertEquals(numberOfRefKeys(15), this.socContext.getVersionContext().getChangesSize());
    
  }
  
  public void testUpdateActivities10() throws Exception {
    
    assertEquals(0, this.socContext.getVersionContext().getChangesSize());
    
    List<ExoSocialActivity> list = listOf(10, "mary", false, true);
    
    ActivityData data0 = socContext.getActivityCache().get(list.get(0).getId());
    ExoSocialActivity a = data0.build();
    a = ActivityBuilder.getInstance(a).title("updated by mary").take();
    cachedActivityStorage.update(a);
    
    ActivityData dataEnd = socContext.getActivityCache().get(list.get(list.size() - 1).getId());
    a = dataEnd.build();
    a = ActivityBuilder.getInstance(a).title("updated by root").take();
    cachedActivityStorage.update(a);
    
    data0 = socContext.getActivityCache().get(list.get(0).getId());
    assertNotNull(data0);
    assertEquals(data0.getStatus(), DataStatus.PERSISTENTED);
    assertEquals("updated by mary", data0.getTitle());
    
    int numberOfUpdate = 4; //(2 updated x 2 refs)
    assertEquals(numberOfRefKeys(14) - numberOfUpdate, this.socContext.getVersionContext().getChangesSize());
    
  }
  
  public void testSaveComment4() throws Exception {
    
    List<ExoSocialActivity> list = listOf(1, "mary", false, false);
    for(ExoSocialActivity a : list) {
      cachedActivityStorage.saveActivity(a);
    }
    
    ExoSocialActivity a = list.get(0);
    
    List<ExoSocialActivity> comments = listOf(4, "root", true, false);
    for(ExoSocialActivity c : comments) {
      cachedActivityStorage.saveComment(a, c);
    }
    
    ActivityData data0 = socContext.getActivityCache().get(list.get(0).getId());
    assertEquals(data0.getStatus(), DataStatus.TRANSIENT);
    assertTrue(data0.getLastUpdated() > 0);
    
    ActivityData comment0 = socContext.getActivityCache().get(list.get(0).getId());
    assertEquals(comment0.getStatus(), DataStatus.TRANSIENT);
  }
  
  public void testSaveComment10() throws Exception {
    
    List<ExoSocialActivity> list = listOf(2, "mary", false, false);
    for(ExoSocialActivity a : list) {
      cachedActivityStorage.saveActivity(a);
    }
    
    ExoSocialActivity a = list.get(0);
    
    List<ExoSocialActivity> comments = listOf(10, "root", true, false);
    for(ExoSocialActivity c : comments) {
      cachedActivityStorage.saveComment(a, c);
    }
    
    ActivityData data0 = socContext.getActivityCache().get(list.get(0).getId());
    assertEquals(data0.getStatus(), DataStatus.PERSISTENTED);
    assertTrue(data0.getLastUpdated() > 0);
    assertEquals("root@10", data0.getCommenters()[0]);
    
    ActivityData comment0 = socContext.getActivityCache().get(list.get(0).getId());
    assertEquals(comment0.getStatus(), DataStatus.PERSISTENTED);
  }
  
  public void testDeleteComment10() throws Exception {
    List<ExoSocialActivity> list = listOf(2, "mary", false, false);
    for(ExoSocialActivity a : list) {
      cachedActivityStorage.saveActivity(a);
    }
    
    ExoSocialActivity a = list.get(0);
    
    List<ExoSocialActivity> comments = listOf(10, "root", true, false);
    for(ExoSocialActivity c : comments) {
      cachedActivityStorage.saveComment(a, c);
    }
    
    ActivityData activityData = socContext.getActivityCache().get(a.getId());
    assertEquals(activityData.getStatus(), DataStatus.PERSISTENTED);
    assertEquals("root@10", activityData.getCommenters()[0]);
    
    ActivityData commentEnd = socContext.getActivityCache().get(comments.get(comments.size()-1).getId());
    cachedActivityStorage.deleteComment(a.getId(), commentEnd.getId());
    
    ActivityData data0 = socContext.getActivityCache().get(a.getId());
    
    assertEquals("root@9", data0.getCommenters()[0]);
    
  }

}
