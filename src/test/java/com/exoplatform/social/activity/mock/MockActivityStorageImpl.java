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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.ActivityStorage;
import com.exoplatform.social.activity.storage.ActivityStreamStorage;

/**
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 25, 2014  
 */
public class MockActivityStorageImpl implements ActivityStorage {
  
//  Calendar calendar1 = Calendar.getInstance();
//  Calendar calendar2 = Calendar.getInstance();
//  calendar1.set(<your earlier date>);
//  calendar2.set(<your current date>);
//  long milliseconds1 = calendar1.getTimeInMillis();
//  long milliseconds2 = calendar2.getTimeInMillis();
//  long diff = milliseconds2 - milliseconds1;
//  long diffSeconds = diff / 1000;
//  long diffMinutes = diff / (60 * 1000);
//  long diffHours = diff / (60 * 60 * 1000);
//  long diffDays = diff / (24 * 60 * 60 * 1000);
//  System.out.println("\nThe Date Different Example");
//  System.out.println("Time in milliseconds: " + diff
//+ " milliseconds.");
//  System.out.println("Time in seconds: " + diffSeconds
//+ " seconds.");
//  System.out.println("Time in minutes: " + diffMinutes 
//+ " minutes.");
//  System.out.println("Time in hours: " + diffHours 
//+ " hours.");
//  System.out.println("Time in days: " + diffDays 
//+ " days.");
//}
  /** */
  final Map<String, LinkedList<ExoSocialActivity>> posterActivites;
  
  /** */
  final LinkedList<ExoSocialActivity> activites;
  
  /** */
  final Map<String, LinkedList<ExoSocialActivity>> activityIdComments;
  
  /** */
  final ActivityStreamStorage streamStorage;
  
  /** */
  public MockActivityStorageImpl(ActivityStreamStorage streamStorage) {
    this.streamStorage = streamStorage;
    activites = new LinkedList<ExoSocialActivity>();
    posterActivites = new HashMap<String, LinkedList<ExoSocialActivity>>();
    activityIdComments = new HashMap<String, LinkedList<ExoSocialActivity>>();
  }

  @Override
  public void saveActivity(ExoSocialActivity activity) {
    LinkedList<ExoSocialActivity> my = posterActivites.get(activity.getPosterId());
    if (my == null) {
      my = new LinkedList<ExoSocialActivity>();
      posterActivites.put(activity.getPosterId(), my);
    }
    
    my.addFirst(activity);
    
    activites.addFirst(activity);
    
    streamStorage.savePoster(activity);
    streamStorage.save(activity);
    
  }

  @Override
  public void saveComment(ExoSocialActivity activity, ExoSocialActivity comment) {
    LinkedList<ExoSocialActivity> myComments;
    if (!activityIdComments.containsKey(activity.getId())) {
      myComments = new LinkedList<ExoSocialActivity>();
      activityIdComments.put(activity.getId(), myComments);
    } else {
      myComments = activityIdComments.get(activity.getId());
    }
    myComments.addLast(comment);

    //what's hot!
    LinkedList<ExoSocialActivity> whatsHot = posterActivites.get(activity.getPosterId());
    
    if (whatsHot != null) {
      whatsHot.remove(activity);
      whatsHot.addFirst(activity);
    }
    
    streamStorage.updateCommenter(activity);
    streamStorage.update(activity, new String[]{});
  }

  @Override
  public void update(ExoSocialActivity activity) {
    List<ExoSocialActivity> my = posterActivites.get(activity.getPosterId());
    if (my == null) {
      return;
    }
    
    my.remove(activity);
    my.add(activity);
  }

  @Override
  public void deleteActivity(ExoSocialActivity activity) {
    List<ExoSocialActivity> my = posterActivites.get(activity.getPosterId());
    if (my == null) {
      return;
    }
    
    my.remove(activity);
    
    streamStorage.delete(activity.getId());
  }

  @Override
  public void deleteComment(String activityId, String commentId) {
    List<ExoSocialActivity> myComments;
    if (activityIdComments.containsKey(activityId)) {
      myComments = activityIdComments.get(activityId);
      int found = 0;
      for(ExoSocialActivity c : myComments) {
        if (c.equals(commentId)) {
          break;
        }
        found++;
      }
      
      myComments.remove(found);
    }
    
    
  }
  
  private List<ExoSocialActivity> getSubList(String remoteId, int offset, int limit) {
    List<ExoSocialActivity> all = posterActivites.get(remoteId);
    
    //
    if (all == null) {
      return Collections.emptyList();
    }
    
    //
    if (offset < 0 || limit < 0) {
      return Collections.emptyList();
    }
    
    //
    int to = Math.min(all.size(), offset + limit);
    return all.subList(offset, to);
  }
  
  private int getNumberOfStream(String remoteId) {
    List<ExoSocialActivity> all = posterActivites.get(remoteId);
    
    return all != null ? all.size() : 0;
  }
  
  @Override
  public ExoSocialActivity getActivity(String activityId) {
    return activites.get(activites.indexOf(activityId));
  }

  @Override
  public List<ExoSocialActivity> getFeed(String remoteId, int offset, int limit) {
    return getSubList(remoteId, offset, limit);
  }

  @Override
  public List<ExoSocialActivity> getConnections(String remoteId, int offset, int limit) {
    return getSubList(remoteId, offset, limit);
  }

  @Override
  public List<ExoSocialActivity> getMySpaces(String remoteId, int offset, int limit) {
    return getSubList(remoteId, offset, limit);
  }

  @Override
  public List<ExoSocialActivity> getSpace(String remoteId, int offset, int limit) {
    return getSubList(remoteId, offset, limit);
  }

  @Override
  public List<ExoSocialActivity> getOwner(String remoteId, int offset, int limit) {
    return getSubList(remoteId, offset, limit);
  }

  @Override
  public int getNumberOfFeed(String remoteId) {
    return getNumberOfStream(remoteId);
  }

  @Override
  public int getNumberOfConnections(String remoteId) {
    return getNumberOfStream(remoteId);
  }

  @Override
  public int getNumberOfMySpaces(String remoteId) {
    return getNumberOfStream(remoteId);
  }

  @Override
  public int getNumberOfSpace(String remoteId) {
    return getNumberOfStream(remoteId);
  }

  @Override
  public int getNumberOfOwner(String remoteId) {
    return getNumberOfStream(remoteId);
  }
  
  public void clear() {
    this.activites.clear();
    this.posterActivites.clear();
    this.activityIdComments.clear();
  }

  

}
