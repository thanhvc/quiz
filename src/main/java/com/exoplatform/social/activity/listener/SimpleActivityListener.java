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
import com.exoplatform.social.graph.Vertex;
import com.exoplatform.social.graph.simple.SimpleUndirectGraph;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 14, 2014  
 */
public class SimpleActivityListener<M extends ExoSocialActivity> extends AbstractActivityListener<M> {

  /** */
  final SOCContext socContext;
  /** */
  final DataChangeListener<ExoSocialActivity> cachedListener;
  /** */
  final GraphListener<ExoSocialActivity> graphListener;
  /** */
  final SimpleUndirectGraph graph;
  
  public SimpleActivityListener(SOCContext socContext) {
    this.socContext = socContext;
    this.graph = socContext.getActivityCacheGraph();
    this.cachedListener = new CachedListener<ExoSocialActivity>(socContext.getDataContext(), socContext);
    this.graphListener = new GraphListener<ExoSocialActivity>(socContext);
  }
  
  @Override
  public void onAddActivity(M activity) {
    cachedListener.onAdd(activity);
    graphListener.onAdd(activity);
  }

  @Override
  public void onAddComment(M activity, M comment) {
    cachedListener.onUpdate(activity);
    cachedListener.onAdd(comment);
    graphListener.onUpdate(activity);
  }

  @Override
  public void onUpdateActivity(M activity) {
    cachedListener.onUpdate(activity);
  }

  @Override
  public void onRemoveActivity(M activity) {
    cachedListener.onRemove(activity);
    graphListener.onRemove(activity);
  }

  @Override
  public void onRemoveComment(M activity, M comment) {
    cachedListener.onUpdate(activity);
    cachedListener.onRemove(comment);
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
