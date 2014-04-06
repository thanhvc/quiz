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
package com.exoplatform.iversion.object;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

public class PropertyTree {

    public static PropertyTree build(PropertyPath...paths) {
        return build(Arrays.asList(paths));
    }
    public static PropertyTree build(Collection<PropertyPath> paths) {
        Map<PropertyPath, PropertyTree> nodes = Maps.newHashMapWithExpectedSize(paths.size());
        for (PropertyPath path : paths) {
            PropertyTree parentTree = null;
            for (PropertyPath subpath : path) {
                PropertyTree childTree = nodes.get(subpath);
                if (childTree == null) {
                    childTree = new PropertyTree(subpath);
                    nodes.put(subpath, childTree);
                }
                if (parentTree != null) {
                    parentTree.children.put(subpath.getName(), childTree);
                } 
                parentTree = childTree;
            }
        }
        return nodes.get(PropertyPath.ROOT);
    }
    
    public final PropertyPath path;
    
    private Map<String, PropertyTree> children = Maps.newLinkedHashMap();
    
    private PropertyTree(PropertyPath path) {
        this.path = path;
    }
    
    public String getName() {
      return path.getName();
    }
    
    public Map<String, PropertyTree> getChildrenMap() {
        return unmodifiableMap(children);
    }
    
    public Collection<PropertyTree> getChildren() {
      return unmodifiableCollection(children.values());
  }
    
    public PropertyTree get(String childNode) {
        return children.get(childNode);
    }

}
