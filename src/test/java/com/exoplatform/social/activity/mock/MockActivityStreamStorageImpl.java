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
package com.exoplatform.social.activity.mock;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.persister.Persister;
import com.exoplatform.social.activity.persister.PersisterTask;
import com.exoplatform.social.activity.storage.ActivityStreamStorage;
import com.exoplatform.social.activity.storage.stream.AStream;
import com.exoplatform.social.activity.storage.stream.AStream.Builder;
import com.exoplatform.social.activity.storage.stream.AStreamVersion;
import com.exoplatform.social.activity.storage.stream.ActivityRefKey;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 8, 2014  
 */
public class MockActivityStreamStorageImpl implements ActivityStreamStorage, Persister {
  
  /** */
  static final long INTERVAL_ACTIVITY_PERSIST_THRESHOLD = 5000; //5m = 1000 x 5
  /** */
  final SOCContext socContext;
  /** */
  final AStreamVersion streamUpdater;
  /** */
  final PersisterTask timerTask;
  /** */
  final static int persisterThreshold = 500;
  
  public MockActivityStreamStorageImpl(SOCContext socContext) {
    this(persisterThreshold, socContext);
  }
  
  public MockActivityStreamStorageImpl(int maxPersisterThreshold, SOCContext socContext) {
    this.socContext = socContext;
    this.streamUpdater = socContext.getStreamUpdater();
    timerTask = PersisterTask.init()
                             .persister(this)
                             .wakeup(INTERVAL_ACTIVITY_PERSIST_THRESHOLD)
                             .timeUnit(TimeUnit.MILLISECONDS)
                             .maxFixedSize(maxPersisterThreshold)
                             .build();
    timerTask.start();
  }

  @Override
  public void savePoster(ExoSocialActivity activity) {
    Builder builder = AStream.initActivity(activity);
    streamUpdater.owner(builder);
    commit(false);
  }

  @Override
  public void save(ExoSocialActivity activity) {
    String[] commenters = activity.getCommenters();
    
    if (commenters != null) {
      for(String commenterId : commenters) {
        Builder builder = AStream.initActivity(commenterId, activity);
        streamUpdater.owner(builder);
      }
    }
    
    commit(false);
  }

  @Override
  public void delete(String activityId) {
    ExoSocialActivity activity = socContext.getActivityCache().get(activityId).build();
    Builder builder = AStream.initActivity(activity);
    streamUpdater.remove(builder);
  }

  @Override
  public void like(String likerIdentityId, ExoSocialActivity activity) {
    
  }

  @Override
  public void unlike(String removedIdentityId, ExoSocialActivity activity) {
    
  }

  @Override
  public void connect(String senderIdentityId, String receiverIdentityId) {
    
  }

  @Override
  public void update(ExoSocialActivity activity, String[] mentioner) {
    //nothing need
  }

  @Override
  public void updateCommenter(ExoSocialActivity activity) {
    String[] commenters = activity.getCommenters();
    if (commenters != null) {
      for(String commenterId : commenters) {
        Builder builder = AStream.initActivity(commenterId, activity);
        streamUpdater.owner(builder);
      }
    }
    
    //TODO
    //gets activity reference and update
    //after we can remove the update
    commit(false);
  }
  
  @Override
  public void commit(boolean forceCommit) {
    persistFixedSize(forceCommit);
  }

  private void persistFixedSize(boolean forcePersist) {
    if (timerTask.shoudldPersist(streamUpdater.getChangesSize()) || forcePersist) {
      Set<ActivityRefKey> keys = streamUpdater.popChanges();
      for (ActivityRefKey key : keys) {
        System.out.println("persit:: " + key.toString());
      }
    }
  }

}
