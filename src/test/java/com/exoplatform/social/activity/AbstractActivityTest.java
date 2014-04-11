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

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.mock.MockActivityStorageImpl;
import com.exoplatform.social.activity.mock.MockActivityStreamStorageImpl;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.CachedActivityStorage;
import com.exoplatform.social.activity.storage.cache.data.IdentityProvider;
import com.exoplatform.social.activity.storage.stream.StreamUpdater;
import com.exoplatform.utils.LogWatch;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 28, 2014  
 */
public abstract class AbstractActivityTest extends TestCase {
  /** */
  final static long DAY_MILISECONDS = 86400000; //24 x 60 x 60 x 1000
  /** */
  final static int PERSISTER_ACTIVITY_THRESHOLD = 12;
  /** */
  final static int PERSISTER_ACTIVITY_REF_THRESHOLD = 100;
  /** */
  protected SOCContext socContext;
  /** */
  protected CachedActivityStorage cachedActivityStorage;
  /** */
  protected MockActivityStorageImpl activityStorage;
  /** */
  protected MockActivityStreamStorageImpl streamStorage;
  /** */
  protected LogWatch watch = null;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    //
    watch = new LogWatch();
    //
    socContext = new SOCContext();
    //
    streamStorage = new MockActivityStreamStorageImpl(getPersistActivityRefThreshold(), socContext);
    //
    activityStorage = new MockActivityStorageImpl(streamStorage);
    //
    this.cachedActivityStorage = new CachedActivityStorage(getPersistActivityThreshold(), socContext, activityStorage);
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    socContext.clear();
    socContext = null;
    this.cachedActivityStorage = null;
    activityStorage = null;
    watch = null;
  }
  
  /**
   * returns persist activity threshold
   * @return
   */
  public int getPersistActivityThreshold() {
    return PERSISTER_ACTIVITY_THRESHOLD;
  }
  
  /**
   * returns persist activity ref threshold
   * @return
   */
  public int getPersistActivityRefThreshold() {
    return PERSISTER_ACTIVITY_REF_THRESHOLD;
  }
  
  public void clearAll() {
    this.socContext.clear();
    this.activityStorage.clear();
  }
  
  /**
   * Gets random identity
   * @param size
   * @return
   */
  public String getRandomIdentity(int size) {
    Random r = new Random();
    return "user" + r.nextInt(size);
  }
  
  /**
   * Populate the activities
   * if the given posterId is NULL, then it will make random posterId
   * 
   * Without the thread.sleep(...)
   *  
   * @param n number of activity will be populated
   * @param posterId the poster activity: if providing NULL, then it will make random posterId
   * @param isComment TRUE/ FALSE
   * @param isSave need to invoke storage saving
   * 
   * @return
   * @throws InterruptedException
   */
  protected List<ExoSocialActivity> listOfPerf(int n, String posterId, boolean isComment, boolean isSave) throws InterruptedException {
    List<ExoSocialActivity> list = new LinkedList<ExoSocialActivity>();
    ExoSocialActivity a = null;
    for (int i = 0; i < n; i++) {
      a = ActivityBuilder.getInstance()
                         .posterId(posterId == null ? getRandomIdentity(n) : posterId)
                         .title("title" + i)
                         .body("body" + i)
                         .titleId("titleId" + i)
                         .isComment(isComment)
                         .posterProviderId(IdentityProvider.USER.getName())
                         .take();
      if (isComment) {
        a.setCommenters(new String[] {"root", "john"});
      }
      
      if (isSave) {
        cachedActivityStorage.saveActivity(a);
      }
      
      
      list.add(a);
    }
    
    return list;
  }
  
  /**
   * Populate the activities
   * if the given posterId is NULL, then it will make random posterId
   *  
   * @param n number of activity will be populated
   * @param posterId the poster activity: if providing NULL, then it will make random posterId
   * @param isComment TRUE/ FALSE
   * @param isSave need to invoke storage saving
   * 
   * @return
   * @throws InterruptedException
   */
  protected List<ExoSocialActivity> listOf(int n, String posterId, boolean isComment, boolean isSave) throws InterruptedException {
    List<ExoSocialActivity> list = new LinkedList<ExoSocialActivity>();
    ExoSocialActivity a = null;
    for (int i = 0; i < n; i++) {
      a = ActivityBuilder.getInstance()
                         .posterId(posterId == null ? getRandomIdentity(n) : posterId)
                         .title("title" + i)
                         .body("body" + i)
                         .titleId("titleId" + i)
                         .isComment(isComment)
                         .posterProviderId(IdentityProvider.USER.getName())
                         .take();
      
      if (isComment) {
        a.setCommenters(new String[] {"root", "john"});
      }
      
      if (isSave) {
        cachedActivityStorage.saveActivity(a);
      }
      Thread.sleep(10);
      
      list.add(a);
    }
    
    return list;
  }
  
  public void delete(ExoSocialActivity activity) {
    cachedActivityStorage.deleteActivity(activity);
  }
  
  protected void printOut(List<String> ids) {
    for(int i= 0, length = ids.size(); i < length; i++) {
      System.out.println("id-" + i + ": " + ids.get(i));
    }
  }
  
  protected void dump(List<ExoSocialActivity> activities) {
    for(int i= 0, length = activities.size(); i < length; i++) {
      System.out.println(activities.get(i).toString());
    }
  }
  /**
   * Calculate remaining activity ref what stored in version changes context.
   * Excluding what persisted
   * 
   * @param n number of activity have been created
   * 
   * @return the number remaining changes
   */
  protected int numberOfRefKeys(int n) {
    int remain = (n % PERSISTER_ACTIVITY_THRESHOLD);
    //each user activity will have 2 activity ref, first for feed, second for owner
    //activity persister will be trigger when number of activity creating =  PERSISTER_ACTIVITY_THRESHOLD activity creating.
    //then PERSISTER_ACTIVITY_THRESHOLD x 2 = activity ref creating
    
    int totalExpected = 2 * (n == remain ? 0 : (n - remain));
    //if totalExpected = PERSISTER_ACTIVITY_REF_THRESHOLD = 100
    return totalExpected % PERSISTER_ACTIVITY_REF_THRESHOLD;
  }
  
  public static class ActivityBuilder {
    /** */
    final ExoSocialActivity activity;
    
    /** */
    public ActivityBuilder(ExoSocialActivity activity) {
      this.activity = activity;
    }
    
    public static ActivityBuilder getInstance() {
      return new ActivityBuilder(new ExoSocialActivity());
    }
    
    public static ActivityBuilder getInstance(ExoSocialActivity activity) {
      return new ActivityBuilder(activity);
    }
    
    public ActivityBuilder body(String body) {
      this.activity.setBody(body);
      return this;
    }
    
    public ActivityBuilder title(String title) {
      this.activity.setTitle(title);
      return this;
    }
    
    public ActivityBuilder titleId(String titleId) {
      this.activity.setTitleId(titleId);
      return this;
    }
    
    public ActivityBuilder posterId(String posterId) {
      this.activity.setPosterId(posterId);
      return this;
    }
    
    public ActivityBuilder isComment(boolean isComment) {
      this.activity.setComment(isComment);
      return this;
    }
    
    public ActivityBuilder posterProviderId(String provider) {
      this.activity.setPosterProviderId(provider);
      return this;
    }
    
    public ActivityBuilder commenters(String...commenters) {
      this.activity.setCommenters(commenters);
      return this;
    }
    
    public ActivityBuilder likers(String...likers) {
      this.activity.setLikers(likers);
      return this;
    }
    
    public ActivityBuilder mentioners(String...mentioners) {
      this.activity.setMentioners(mentioners);
      return this;
    }
    
    public ExoSocialActivity take() {
      return this.activity;
    }
  }

}
