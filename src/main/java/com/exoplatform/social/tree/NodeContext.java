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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Feb 22, 2014  
 */
public final class NodeContext<N> extends TreeList<NodeContext<N>> {
  
  /** The owner tree. */
  final TreeContext<N> tree;
 
  /**
   * 1. uuid of node also key of caching
   * 2. uuid generates by IdGenertor and it will be replaced after push jcr
   * 
   */
  final String handle;
  
  /** the node name*/
  String name;
  
  /** the node is wrapped by node context*/
  final N node;
  
  /** A data snapshot */
  final NodeData data;
  
  /** the new state if any */
  NodeState state;
  
  NodeContext(TreeContext<N> tree, NodeData data) {
    this.handle = data.id;
    this.name = data.name;
    this.tree = tree;
    this.node = tree.model.create(this);
    this.data = data;
    this.state = null;
  }
  
  NodeContext(TreeContext<N> tree, String handle, String name) {
    this.handle = handle;
    this.name = name;
    this.tree = tree;
    this.node = tree.model.create(this);
    this.data = null;
    this.state = null;
  }
  
  NodeContext(NodeModel<N> model, String handle, String name) {
    this.handle = handle;
    this.name = name;
    this.tree = new TreeContext<N>(model, this);
    this.node = tree.model.create(this);
    this.data = null;
    this.state = null;
  }
  
  
  /**
   * Returns the context id or null if the context is not associated with a persistent navigation node.
   *
   * @return the id
   */
  public String getId() {
    return data != null ? data.id : null;
  }
  /**
   * Returns the handle of node.
   * @return
   */
  public String getHandle() {
    return this.handle;
  }
  
  /**
   * Returns the node state
   * @return
   */
  public NodeState getState() {
    return this.state;
  }
  
  /**
   * Apply the filter logic on this node only. This method will not modify the state of this node.
   *
   * @param filter the filter to apply
   * @return true if the filter accepts this node
   */
  public boolean accept(NodeFilter filter) {
      return filter.accept(getState());
  }
  /**
   * 
   * @return
   */
  public String getName() {
    return name != null ? name : data.name;
}
  
  /**
   * Returns the relative depth of this node with respect to the ancestor argument.
   * 
   * @param ancestor the ancestor
   * @return the depth
   * @throws IllegalArgumentException
   * @throws NullPointerException
   */
  public int getDepth(NodeContext<N> ancestor) throws IllegalArgumentException, NullPointerException {
    if (ancestor == null) {
      throw new NullPointerException();
    }
    
    int depth = 0;
    
    for(NodeContext<N> current = this; current != null; current= current.getParent()) {
      if (current == ancestor) {
        return depth;
      } else {
        depth++;
      }
    }
    throw new IllegalArgumentException("Context " + ancestor + "is not an ancestor of " + this);
  }
  
  public NodeContext<N> get(String name) throws NullPointerException {
    if (name == null) {
      throw new NullPointerException();
    }
    
    for(NodeContext<N> node = getFirst(); node != null; node = node.getNext()) {
      if (node.name.equals(name)) {
        return node;
      }
    }
    
    return null;
  }
  
  /**
   * Inserts the NodeData into the last position
   * @param data
   * @return
   */
  public NodeContext<N> insertLast(NodeData data) {
    if (data == null) {
      throw new NullPointerException("NodeData must not be null.");
    }
    
    //
    NodeContext<N> context = new NodeContext<N>(tree, data);
    insertLast(context);
    return context;
  }
  
  /**
   * Implements NodeCount exclude hidden node
   * @return
   */
  public int getNodeCount() {
    return getSize();
  }
  
  /**
   * Gets the parent of this
   * @return
   */
  public N getParentNode() {
    NodeContext<N> parent = getParent();
    return parent != null ? parent.node : null;
  }
  
  public N getNode(String name) throws NullPointerException {
    return get(name).node;
  }
  
  public N getNode(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("Index " + index + " cannot be negative");
    }

    NodeContext<N> context = getFirst();
    while (context != null && index-- > 0) {
      context = context.getNext();
    }

    if (context == null) {
      throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    } else {
      return context.node;
    }
  }
  
  public Iterator<N> iterator() {
    return new Iterator<N>() {
      NodeContext<N> next = getFirst();
      
      @Override
      public boolean hasNext() {
        return next != null;
      }

      @Override
      public N next() {
        if (next != null) {
          NodeContext<N> tmp = next;
          do {
            next = next.getNext();
          } while (next != null);
          return tmp.node;
        } else {
          throw new NoSuchElementException();
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
      
    };
  }
  
  private Collection<N> nodes;
  /**
   * Gets nodes collection
   * @return
   */
  public Collection<N> getNodes() {
    if (nodes == null) {
      nodes = new AbstractCollection<N>() {
        public Iterator<N> iterator() {
          return NodeContext.this.iterator();
        }

        public int size() {
          return getNodeCount();
        }
      };
    }

    return null;
  }
  
  /**
   * Remove the node by specified name
   * @param name
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   * @throws IllegalStateException
   */
  public boolean removeNode(String name) throws NullPointerException, IllegalArgumentException, IllegalStateException {
    NodeContext<N> node = get(name);
    if(node == null) {
      throw new IllegalArgumentException("Can remove non existent " + name + " child.");
    }
    
    return node.removeNode();
  }
  
  /**
   * Removes the node
   * @return
   * @throws IllegalStateException
   */
  public boolean removeNode() throws IllegalStateException {
    try {
      this.remove();
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
  
  public void add(Integer index, NodeContext<N> child) throws NullPointerException {
    //
    NodeContext<N> previous;
    if (index == null) {
      NodeContext<N> before = getLast();
      while (before != null) {
        before = before.getPrevious();
      }
      previous = before;
      
    } else if (index < 0) {
      throw new IndexOutOfBoundsException("No negative index accepted");
    } else if (index == 0) {
      previous = null;
    } else {
      NodeContext<N> before = getFirst();
      if (before == null) {
        throw new IndexOutOfBoundsException("Index " + index + " is greater than 0");
      }
      for (int count = index; count > 1; count--) {
        before = before.getNext();
        if (before == null) {
          throw new IndexOutOfBoundsException("Index " + index
              + " is greater than the number of children " + (index - count));
        }
      }
      previous = before;
    }
    
    //TODO
    //if (previousParent != null) {
    //  tree.addChange(new NodeChange.Moved<NodeContext<N>>(previousParent, this, previous, child));
    //} else {
    //  tree.addChange(new NodeChange.Created<NodeContext<N>>(this, previous, child, child.name));
    //}
    if (previous != null) {
      previous.insertOnRight(child);
    } else {
      this.insertAt(0, child);
    }
  }
  
  public NodeContext<N> add(Integer index, String name) throws NullPointerException, IndexOutOfBoundsException, IllegalStateException {
    if (name == null) {
      throw new NullPointerException("No null name accepted");
    }
    
    NodeContext<N> nodeContext = new NodeContext<N>(tree, name, name);
    add(index, nodeContext);
    return nodeContext;
  }
  
  @Override
  public String toString() {
      return toString(1, new StringBuilder()).toString();
  }

  public StringBuilder toString(int depth, StringBuilder sb) {
      if (sb == null) {
          throw new NullPointerException();
      }
      if (depth < 0) {
          throw new IllegalArgumentException("Depth cannot be negative " + depth);
      }
      sb.append("NodeContext[id=").append(getId()).append(",name=").append(getName());
      if (depth > 0) {
          sb.append(",children={");
          for (NodeContext<N> current = getFirst(); current != null; current = current.getNext()) {
              if (current.getPrevious() != null) {
                  sb.append(',');
              }
              current.toString(depth - 1, sb);
          }
          sb.append("}");
      } else {
          sb.append("]");
      }
      return sb;
  } 
  
}
