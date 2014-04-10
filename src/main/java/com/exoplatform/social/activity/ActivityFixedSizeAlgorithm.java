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


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
public class ActivityFixedSizeAlgorithm<M> implements PersistAlgorithm<M> {
  /** */
  final DataContext<M> context;
  
  /** */
  int persisterThreshold = 100;
  
  public ActivityFixedSizeAlgorithm(DataContext<M> context) {
    this.context = context;
  }
  
  public ActivityFixedSizeAlgorithm(DataContext<M> context, int persisterThreshold) {
    this(context);
    
    if (persisterThreshold > 0) {
      this.persisterThreshold = persisterThreshold;
    }
    
  }
  
  @Override
  public boolean shoudldPersist() {
    return context.getChanges().size() >= persisterThreshold;
  }

}
