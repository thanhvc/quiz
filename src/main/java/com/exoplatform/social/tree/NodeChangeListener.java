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
package com.exoplatform.social.tree;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 4, 2014  
 */
public interface NodeChangeListener<N> {

  /**
   * A node was added.
   *
   * @param target the added node
   * @param parent the parent node
   * @param previous the optional previous node
   */
  void onAdd(N target, N parent, N previous);

  /**
   * A node was created.
   *
   * @param target the created node
   * @param parent the parent node
   * @param previous the optional previous node
   * @param name the name of the created node
   */
  void onCreate(N target, N parent, N previous, String name);

  /**
   * A node was removed.
   *
   * @param target the removed node
   * @param parent the parent node
   */
  void onRemove(N target, N parent);

  /**
   * A node was destroyed.
   *
   * @param target the destroyed node
   * @param parent the parent node
   */
  void onDestroy(N target, N parent);

  /**
   * A node was renamed.
   *
   * @param target the renamed node
   * @param parent the parent node
   * @param name the new node name
   */
  void onRename(N target, N parent, String name);

  /**
   * A node was updated.
   *
   * @param target the updated node
   * @param state the new state
   */
  void onUpdate(N target, NodeState state);

  /**
   * A node was moved.
   *
   * @param target the moved node.
   * @param from the previous parent
   * @param to the new parent
   * @param previous the optional previous node
   */
  void onMove(N target, N from, N to, N previous);

  /**
   * A base implementation that can be subclassed.
   *
   * @param <N> the node generic type
   */
  class Base<N> implements NodeChangeListener<N> {

      public void onAdd(N target, N parent, N previous) {
      }

      public void onCreate(N target, N parent, N previous, String name) {
      }

      public void onRemove(N target, N parent) {
      }

      public void onDestroy(N target, N parent) {
      }

      public void onRename(N target, N parent, String name)  {
      }

      public void onUpdate(N target, NodeState state) {
      }

      public void onMove(N target, N from, N to, N previous) {
      }
  }
}