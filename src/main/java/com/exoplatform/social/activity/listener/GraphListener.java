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
package com.exoplatform.social.activity.listener;

import com.exoplatform.social.SOCContext;
import com.exoplatform.social.activity.DataChangeListener;
import com.exoplatform.social.activity.model.ExoSocialActivity;
import com.exoplatform.social.activity.storage.cache.data.ActivityData;
import com.exoplatform.social.activity.storage.cache.data.StreamFixedSizeListener;
import com.exoplatform.social.activity.storage.stream.AStream;
import com.exoplatform.social.activity.storage.stream.AStream.UPDATER;
import com.exoplatform.social.activity.storage.stream.AStream.REMOVER;
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.social.graph.simple.SimpleUndirectGraph;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 11, 2014  
 */
public class GraphListener<M extends ExoSocialActivity> implements DataChangeListener<M>, StreamFixedSizeListener  {
  /** */
  final SimpleUndirectGraph graph;
  /** */
  final SOCContext socContext;

  public GraphListener(SOCContext socContext) {
    this.socContext = socContext;
    this.graph = socContext.getActivityCacheGraph();
  }

  @Override
  public void onAdd(M target) {
    ExoSocialActivity a = target;
    if (target.isComment()) {
      ActivityData parentData = this.socContext.getActivityCache().get(target.getParentId());
      if (parentData != null) {
        a = parentData.build();
      } else {
        return;
      }
    }
    
    //
    AStream.Builder builder = AStream.initActivity(a);
    UPDATER.init().context(socContext)
                   .feed(builder)
                   .connection(builder)
                   .owner(builder)
                   .myspaces(builder)
                   .space(builder).doExecute();
  }

  @Override
  public void onRemove(M target) {
    if (target.isComment()) return;
    //
    AStream.Builder builder = AStream.initActivity(target);
    REMOVER.init().context(socContext)
                   .feed(builder)
                   .connection(builder)
                   .owner(builder)
                   .myspaces(builder)
                   .space(builder).doExecute();
  }

  @Override
  public void onUpdate(M target) {
    AStream.Builder builder = AStream.initActivity(target);
    UPDATER.init().context(socContext)
                   .feed(builder)
                   .connection(builder)
                   .owner(builder)
                   .myspaces(builder)
                   .space(builder).doExecute();
  }

  @Override
  public void update(String inVertexId, String outVertexId) {
    Vertex<Object> inVertex = this.graph.getVertex(inVertexId);
    Vertex<Object> outVertex = this.graph.getVertex(outVertexId);
    
    if (inVertex != null && outVertex != null) {
      this.graph.removeEdge(inVertex, outVertex);
    }
  }

}