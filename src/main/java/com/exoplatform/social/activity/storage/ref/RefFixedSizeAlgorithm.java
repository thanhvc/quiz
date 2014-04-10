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
package com.exoplatform.social.activity.storage.ref;

import com.exoplatform.social.activity.DataContext;
import com.exoplatform.social.activity.PersistAlgorithm;
import com.exoplatform.social.activity.Version;
import com.exoplatform.social.activity.VersionChangeContext;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 9, 2014  
 */
public class RefFixedSizeAlgorithm<T extends Version> implements PersistAlgorithm<T> {
  /** */
  final VersionChangeContext<T> context;
  
  /** */
  final static int persisterThreshold = 100;
  
  final int maxThreshold;
  
  public RefFixedSizeAlgorithm(VersionChangeContext<T> context) {
    this(context, persisterThreshold);
  }
  
  public RefFixedSizeAlgorithm(VersionChangeContext<T> context, int maxThreshold) {
    this.context = context;
    this.maxThreshold = maxThreshold;
    
  }
  
  @Override
  public boolean shoudldPersist() {
    return context.getChanges().size() >= maxThreshold;
  }

}
