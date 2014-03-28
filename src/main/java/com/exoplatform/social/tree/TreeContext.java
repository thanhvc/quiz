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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 4, 2014  
 */
class TreeContext<N> {
  
  /** . */
  private NodeChangeQueue<NodeContext<N>> changes;
  
  /** . */
  final NodeModel<N> model;
  
  /** . */
  int sequence;
  
  /** . */
  final NodeContext<N> root;
  
  public TreeContext(NodeModel<N> model, NodeContext<N> root) {
    this.sequence = 0;
    this.root = root;
    this.model = model;
  }
  
  public NodeChangeQueue<NodeContext<N>> getChanges() {
    return changes;
  }
  
  void addChange(NodeChange<NodeContext<N>> change) {
    if (changes == null) {
        changes = new NodeChangeQueue<NodeContext<N>>();
    }

    //
    if (change.target.tree != this) {
        // Normally should be done for all arguments depending on the change type
        throw new AssertionError("Ensure we are not mixing badly things");
    }

    // Perform state modification here
    if (change instanceof NodeChange.Renamed<?>) {
        NodeChange.Renamed<NodeContext<N>> renamed = (NodeChange.Renamed<NodeContext<N>>) change;
        renamed.target.name = renamed.name;
    } else if (change instanceof NodeChange.Created<?>) {
        NodeChange.Created<NodeContext<N>> added = (NodeChange.Created<NodeContext<N>>) change;
        if (added.previous != null) {
            added.previous.insertOnRight(added.target);
        } else {
            added.parent.insertAt(0, added.target);
        }
    } else if (change instanceof NodeChange.Moved<?>) {
        NodeChange.Moved<NodeContext<N>> moved = (NodeChange.Moved<NodeContext<N>>) change;
        if (moved.previous != null) {
            moved.previous.insertOnRight(moved.target);
        } else {
            moved.to.insertAt(0, moved.target);
        }
    } else if (change instanceof NodeChange.Destroyed<?>) {
        NodeChange.Destroyed<NodeContext<N>> removed = (NodeChange.Destroyed<NodeContext<N>>) change;
        removed.target.remove();
    } else if (change instanceof NodeChange.Updated<?>) {
        NodeChange.Updated<NodeContext<N>> updated = (NodeChange.Updated<NodeContext<N>>) change;
        updated.target.state = updated.state;
    }

    //
    changes.addLast(change);
}

  boolean hasChanges() {
    return changes != null && changes.size() > 0;
  }

  List<NodeChange<NodeContext<N>>> peekChanges() {
    if (hasChanges()) {
      return changes;
    } else {
      return Collections.emptyList();
    }
  }

  List<NodeChange<NodeContext<N>>> popChanges() {
    if (hasChanges()) {
      LinkedList<NodeChange<NodeContext<N>>> tmp = changes;
      changes = null;
      return tmp;
    } else {
      return Collections.emptyList();
    }
  }

  NodeContext<N> create(String handle, String name, NodeState state) {
    return new NodeContext<N>(this, handle, name);
  }


}
