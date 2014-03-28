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
public interface NodeModel<N> {
  
  NodeModel<NodeContext<?>> SELF_MODEL = new NodeModel<NodeContext<?>>() {

    @Override
    public NodeContext<NodeContext<?>> getContext(NodeContext<?> node) {
      throw new UnsupportedOperationException();
    }

    @Override
    public NodeContext<?> create(NodeContext<NodeContext<?>> context) {
      return context;
    }
    
  };
  
  /**
   * Returns the context of node what wrapped by node
   * @return
   */
  NodeContext<N> getContext(N node);
  
  /**
   * Creates a node wrapping a context
   * @return
   */
  public N create(NodeContext<N> context);
  
  

}
