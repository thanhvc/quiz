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
public interface DataChangeListener<M> {
  /**
   * A model is added
   * @param target the added model
   */
  void onAdd(M target);
  
  /**
   * A model is removed
   * @param target
   */
  void onRemove(M target);
  
  /**
   * A model is updated
   * @param target
   */
  void onUpdate(M target);
  
  /**
   * A model is moved
   * @param target
   */
  void onMove(M target);
  
  class Base<M> implements DataChangeListener<M> {

    @Override
    public void onAdd(M target) {
      
    }

    @Override
    public void onRemove(M target) {
      
    }

    @Override
    public void onUpdate(M target) {
      
    }
    
    @Override
    public void onMove(M target) {
      
    }
    
  }
  

}
