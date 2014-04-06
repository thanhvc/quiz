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
package com.exoplatform.social.iversion.object;

import junit.framework.TestCase;

import org.junit.Test;

import com.exoplatform.iversion.object.PropertyTree;
import static com.exoplatform.iversion.object.PropertyPath.ROOT;
import static com.exoplatform.social.iversion.object.PropertyPathTest.*;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 6, 2014  
 */
public class PropertyTreeTest extends TestCase {

  public void tesSinglePath() {
      PropertyTree tree = PropertyTree.build(children_0_name());
      assertPropertyTree(tree, 1);
      
      tree = assertPropertyTree(tree, "children", 1);
      tree = assertPropertyTree(tree, "0", 1);
      tree = assertPropertyTree(tree, "name", 0);
  }
  
  private PropertyTree assertPropertyTree(PropertyTree tree, String node, int expectedChildren) {
      tree = tree.get(node);
      assertPropertyTree(tree, expectedChildren);
      return tree;
  }
  
  private void assertPropertyTree(PropertyTree tree, int expectedChildren) {
      assertNotNull(tree);
      assertEquals(tree.getChildrenMap().size(), expectedChildren);
  }
  
  public void testNoPaths() {
      PropertyTree tree = PropertyTree.build();
      assertNull(tree);
      
  }

  public void testPaths() {
      PropertyTree root = PropertyTree.build(
              children_0_name(),
              children_0(),
              ROOT.property("name"), 
              children().index(123));
      
      assertPropertyTree(root, 2);
      
      assertPropertyTree(root, "name", 0);
      
      PropertyTree children = assertPropertyTree(root, "children", 2);

      PropertyTree tree = assertPropertyTree(children, "0", 1);
      tree = assertPropertyTree(tree, "name", 0);
      
      assertPropertyTree(children, "123", 0);
  }
  
}
