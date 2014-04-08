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
import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;
import com.exoplatform.social.activity.storage.cache.data.IdentityProvider;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.graph.Vertex;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 17, 2014  
 */
public class GraphCachedActivityStorageTest extends AbstractActivityTest {
  public void testAdd() throws Exception {
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
    
    Vertex<Object> v = socContext.getActivityCacheGraph().getVertex(activity.getId());
    assertNotNull(v);
    List<Vertex<Object>> adjacents = socContext.getActivityCacheGraph().getAdjacents(activity.getId());
    assertNotNull(adjacents);
    assertEquals(2, adjacents.size());
    
    ListActivitiesKey key = adjacents.get(0).unwrap(ListActivitiesKey.class);
    ActivitiesListData listData = socContext.getActivitiesCache().get(key);
    
    assertNotNull(listData);
    
    List<String> activityIds = listData.subList(0, 20);
    
    assertNotNull(activityIds);
    assertEquals(1, activityIds.size());
  }
  
  public void testAddMore() throws Exception {
    List<ExoSocialActivity> list = listOf(15, "mary", false, true);
    
    ExoSocialActivity activity = list.get(0);
    
    Vertex<Object> v = socContext.getActivityCacheGraph().getVertex(activity.getId());
    assertNotNull(v);
    List<Vertex<Object>> adjacents = socContext.getActivityCacheGraph().getAdjacents(activity.getId());
    assertNotNull(adjacents);
    assertEquals(2, adjacents.size());
    
    ListActivitiesKey key = adjacents.get(0).unwrap(ListActivitiesKey.class);
    ActivitiesListData listData = socContext.getActivitiesCache().get(key);
    
    assertNotNull(listData);
    
    List<String> activityIds = listData.subList(0, 20);
    
    assertNotNull(activityIds);
    assertEquals(15, activityIds.size());
    printOut(activityIds);
  }

  
  public void testGetFeed() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getFeed("mary", 0, 20);
    assertEquals(20, feed.size());
    dump(feed);
    
  }
  
  public void testGetFeedOffsetGreaterZero() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getFeed("mary", 20, 20);
    assertEquals(20, feed.size());
    dump(feed);
  }
  
  public void testGetConnection() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getConnections("mary", 0, 20);
    assertEquals(20, feed.size());
    dump(feed);
    
  }
  
  public void testGetConnectionOffsetGreaterZero() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getConnections("mary", 20, 20);
    assertEquals(20, feed.size());
    dump(feed);
  }
  
  public void testGetOwner() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getOwner("mary", 0, 20);
    assertEquals(20, feed.size());
    dump(feed);
    
  }
  
  public void testGetMySpacesOffsetGreaterZero() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getMySpaces("mary", 20, 20);
    assertEquals(20, feed.size());
    dump(feed);
  }
  
  public void testGetMySpaces() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getMySpaces("mary", 0, 20);
    assertEquals(20, feed.size());
    dump(feed);
    
  }
  
  public void testGetOwnerOffsetGreaterZero() throws Exception {
    listOf(50, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getOwner("mary", 20, 20);
    assertEquals(20, feed.size());
    dump(feed);
  }
  
  public void testAddMoreWithComment() throws Exception {
    List<ExoSocialActivity> list = listOf(15, "mary", false, true);
    
    ExoSocialActivity a = list.get(10);
    
    List<ExoSocialActivity> comments = listOf(4, "root", true, false);
    for(ExoSocialActivity c : comments) {
      cachedActivityStorage.saveComment(a, c);
    }
    
    //TODO make graph relationship between 'mary' and 'root'
    List<ExoSocialActivity> feed = cachedActivityStorage.getFeed("mary", 0, 20);
    assertEquals(15, feed.size());
    
    //validate whatshot
    assertEquals(a.getId(), feed.get(0).getId());
    dump(feed);
  }
  
  public void testAddMoreWithCommentComplexity() throws Exception {
    List<ExoSocialActivity> list = listOf(15, "mary", false, true);
    
    ExoSocialActivity a3 = list.get(10);
    
    List<ExoSocialActivity> comments = listOf(4, "root", true, false);
    cachedActivityStorage.saveComment(a3, comments.get(0));
    
    ExoSocialActivity a2 = list.get(8);
    cachedActivityStorage.saveComment(a2, comments.get(1));
    
    ExoSocialActivity a1 = list.get(6);
    cachedActivityStorage.saveComment(a1, comments.get(2));
    
    ExoSocialActivity a0 = list.get(14);
    cachedActivityStorage.saveComment(a0, comments.get(3));
    
    //TODO make graph relationship between 'mary' and 'root'
    List<ExoSocialActivity> feed = cachedActivityStorage.getFeed("mary", 0, 20);
    assertEquals(15, feed.size());
    
    //validate what's hot
    assertEquals(a0.getId(), feed.get(0).getId());
    assertEquals(a1.getId(), feed.get(1).getId());
    assertEquals(a2.getId(), feed.get(2).getId());
    assertEquals(a3.getId(), feed.get(3).getId());
    
    dump(feed);
    
    //ADD MORE ACTIVITY
    List<ExoSocialActivity> list1 = listOf(1, "mary", false, true);
    List<ExoSocialActivity> feed1 = cachedActivityStorage.getFeed("mary", 0, 20);
    
    assertEquals(16, feed1.size());
    assertEquals(list1.get(0).getId(), feed1.get(0).getId());
    assertEquals(a0.getId(), feed1.get(1).getId());
    assertEquals(a1.getId(), feed1.get(2).getId());
    assertEquals(a2.getId(), feed1.get(3).getId());
    assertEquals(a3.getId(), feed1.get(4).getId());
  }
  
  public void testRemoveActivity() throws Exception {
    List<ExoSocialActivity> list = listOf(15, "mary", false, true);
    
    List<ExoSocialActivity> feed = cachedActivityStorage.getFeed("mary", 0, 20);
    assertEquals(15, feed.size());
    
    //validate what's hot
    assertEquals(list.get(list.size() -1).getId(), feed.get(0).getId());
    ExoSocialActivity removedA = list.get(list.size() - 2);
    
    delete(removedA);
    feed = cachedActivityStorage.getFeed("mary", 0, 20);
    assertEquals(14, feed.size());
    dump(feed);
  }

}
