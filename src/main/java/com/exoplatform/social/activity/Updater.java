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

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.IdentityProvider;
import com.exoplatform.social.activity.storage.cache.data.ListActivitiesKey;
import com.exoplatform.social.activity.storage.cache.data.StreamType;
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.social.graph.simple.SimpleUndirectGraph;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 8, 2014  
 */
public class Updater<M extends ExoSocialActivity, G extends SimpleUndirectGraph> extends ActivityOperator<G, M> {
  /** */
  final OperatorContext addContext;
  
  public Updater(SOCContext socContext) {
    this.addContext = new OperatorContext.UPDATE(socContext);
  }

  @Override
  public void execute(G graph, M model) {
    if (model.isComment()) {
      //1. Finds activityId vertex 
      //2. Gets the adjacent of activityId vertex
      //3. Gets from caching
      //4. Remove, and put the top
      ActivityData parentData = addContext.activityCache.get(model.getParentId());
      if(parentData != null) {
        updateAllStream(graph, parentData.build());
      }
      
    } else {
      graph.addVertex(model.getId());
      //#1. Update stream of poster
      //#2. Gets relationship of poster from relationshipCacheGraph
      //#3. Build Key of AcrivityStream and find in activity caching if it represent in eXo Caching or not
      //#4. If YES, 
      updateAllStream(graph, model);
    }
  }
  
  /**
   * 
   * @param target
   * @return
   */
  private boolean updateAllStream(G graph, ExoSocialActivity target) {
    boolean success = false; 
    
    boolean isSpaceActivity = target.getPosterProviderId().equalsIgnoreCase(IdentityProvider.SPACE.getName());
    success |= updateStream(graph, target.getPosterId(), true, isSpaceActivity, target);
    List<Vertex<Object>> vertices = addContext.relationshipGraph.getAdjacents(target.getPosterId());
    
    //add new activity for poster's stream, FEED, CONNECTION, MY SPACES, AND MY ACTIVITY
    for(Vertex<Object> v : vertices) {
      success |= updateStream(graph, v.unwrap(String.class), false, isSpaceActivity, target);
    }
    
    
    return success;
  }
  
  private boolean updateStream(G graph, String identityId, boolean isPoster, boolean isSpaceActivity, ExoSocialActivity target) {
    boolean success = false;
    ListActivitiesKey key = new ListActivitiesKey(identityId, StreamType.FEED);
    success |= addContext.update(graph, isPoster, key, target);
    //my connection
    if (!isPoster) {
      key = new ListActivitiesKey(identityId, StreamType.CONNECTION);
      success |= addContext.update(graph, isPoster, key, target);
    }
    
    //my space
    if (isSpaceActivity) {
      key = new ListActivitiesKey(identityId, StreamType.MY_SPACES);
      success |= addContext.update(graph, isPoster, key, target);
    }
    
    key = new ListActivitiesKey(identityId, StreamType.OWNER);
    success |= addContext.update(graph, isPoster, key, target);
    
    return success;
  }

  
  
}
