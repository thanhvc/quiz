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
package com.exoplatform.social.graph;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 22, 2014  
 */
public interface GraphChangeListener<E> {
  /**
   * A edge was added.
   *
   * @param target the added node
   * @param parent the parent node
   * @param previous the optional previous node
   */
  void onAdd(E target);

  
  /**
   * A edge was removed.
   *
   * @param target the removed node
   * @param parent the parent node
   */
  void onRemove(E target);

  /**
   * A node was updated.
   *
   * @param target the updated node
   * @param state the new state
   */
  void onUpdate(E target);

  /**
   * A base implementation that can be subclassed.
   *
   * @param <N> the node generic type
   */
  class Base<E> implements GraphChangeListener<E> {

    @Override
    public void onAdd(E target) {
    }

    @Override
    public void onRemove(E target) {
    }

    @Override
    public void onUpdate(E target) {
    }
  }
}