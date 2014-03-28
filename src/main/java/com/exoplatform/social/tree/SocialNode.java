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

import java.util.Collection;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 4, 2014  
 */
public class SocialNode {

  public final static NodeModel<SocialNode> SELF_MODEL = new NodeModel<SocialNode>() {

    @Override
    public NodeContext<SocialNode> getContext(SocialNode node) {
      return node.context;
    }

    @Override
    public SocialNode create(NodeContext<SocialNode> context) {
      return new SocialNode(context);
    }
    
  };
  
  /**
   * The context of node
   */
  final NodeContext<SocialNode> context;

  /**
   * The constructor gives the specified context
   * @param context
   */
  public SocialNode(NodeContext<SocialNode> context) {
    this.context = context;
  }
  /**
   * Gets handle of node
   * @return
   */
  public String getName() {
    return context.getName();
  }
  
  /**
   * Gets handle of node
   * @return
   */
  public String getHandle() {
    return context.getHandle();
  }
  
  /**
   * Gets node state
   * @return
   */
  public NodeState getState() {
    return context.getState();
  }
  
  /**
   * Gets parent of node
   * @return
   */
  public SocialNode getParent() {
    return context.getParentNode();
  }
  
  /**
   * Gets children
   * @return
   */
  public Collection<SocialNode> getChildren() {
    return context.getNodes();
  }
  
  /**
   * Gets the node by Name
   * @param childName
   * @return
   */
  public SocialNode getChild(String childName) {
    return this.context.getNode(childName);
  }
  
  /**
   * Gets node by index
   * @param childIndex
   * @return
   */
  public SocialNode getChild(int childIndex) {
    return context.getNode(childIndex);
  }
  
  /**
   * Adds new child for this.
   * @param childName node's uuid
   */
  public SocialNode addChild(String childName) {
    return this.context.add(null, childName).node;
  }
  
  /**
   * Adds new child at specified index
   * Keeps changes in BlockingQueueChanges
   * @param index
   * @param child
   */
  public void addChild(int index, SocialNode child) {
    this.context.add(index, child.context);
  }
  
  /**
   * Adds new child node
   * @param child
   */
  public void addChild(SocialNode child) {
    this.context.add(null, child.context);
  }
  
  /**
   * Adds child node by name at given index
   * @param index
   * @param childName
   * @return
   */
  public SocialNode addChild(int index, String childName) {
    return this.context.add(index, childName).node;
  }
  
  /**
   * Removes child node by name
   * @param childName given the name
   * @return
   */
  public boolean removeChild(String childName) {
    return context.removeNode(childName);
  }
  /**
   * Gets node size exclude hidden node (implement later)
   * @return
   */
  public int getNodeCount() {
    return this.context.getNodeCount();
  }
  
  /**
   * Gets number of nodes
   * @return
   */
  public int getNodeSize() {
    return this.context.getSize();
  }
  
  public void update(String key, Object value) {
    
    this.context.state.put(key, value);
  }
  
  /**
  public Iterator<NodeChange<NotificationNode>> update(ActivityManager service, Scope scope) {
    NodeChangeQueue<NotificationNode> queue = new NodeChangeQueue<NotificationNode>();
    service.updateNode(context, scope, new NodeContextChangeAdapter<Node>(queue));
    return queue.iterator();
  }

  public Iterator<NodeChange<NotificationNode>> rebase(ActivityManager service, Scope scope) throws NavigationServiceException {
    NodeChangeQueue<NotificationNode> queue = new NodeChangeQueue<NotificationNode>();
    service.rebaseNode(context, scope, new NodeContextChangeAdapter<NotificationNode>(queue));
    return queue.iterator();
  }

  public Iterator<NodeChange<NotificationNode>> save(NavigationService service) throws NavigationServiceException {
    NodeChangeQueue<NotificationNode> queue = new NodeChangeQueue<NotificationNode>();
    service.saveNode(context, new NodeContextChangeAdapter<NotificationNode>(queue));
    return queue.iterator();
  }
  */
  
}
