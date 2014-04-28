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

import com.exoplatform.social.activity.DataChangeListener;
import com.exoplatform.social.activity.model.ExoSocialActivity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 14, 2014  
 */
public class StreamConnectionListener <M extends ExoSocialActivity> implements DataChangeListener<M>, StreamFixedSizeListener {

  @Override
  public void update(String inVertexId, String outVertexId) {
    
  }

  @Override
  public void onAdd(M target) {
    
  }

  @Override
  public void onRemove(M target) {
    
  }

  @Override
  public void onUpdate(M target) {
    // TODO Auto-generated method stub
    
  }

}
