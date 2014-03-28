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
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.CachedActivityStorage;
import com.exoplatform.social.activity.storage.cache.data.IdentityProvider;
import com.exoplatform.utils.LogWatch;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 28, 2014  
 */
public abstract class AbstractActivityTest extends TestCase {
  
  /** */
  final static int PERSISTER_THRESHOLD = 12;
  
  /** */
  protected SOCContext socContext;
  /** */
  protected CachedActivityStorage cachedActivityStorage;
  /** */
  protected MockActivityStorageImpl activityStorage;
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
    activityStorage = new MockActivityStorageImpl();
    //
    this.cachedActivityStorage = new CachedActivityStorage(getPersistThreshold(), socContext, activityStorage);
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    this.cachedActivityStorage = null;
    activityStorage = null;
    socContext = null;
    watch = null;
  }
  
  /**
   * returns persist threshold
   * @return
   */
  public int getPersistThreshold() {
    return PERSISTER_THRESHOLD;
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
   * Build list of activity data
   * @param n
   * @param posterId
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
      
      if (isSave) {
        cachedActivityStorage.saveActivity(a);
      }
      
      
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
