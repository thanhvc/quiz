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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.exoplatform.social.tree.TreeList;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Feb 22, 2014  
 */
public class TreeListTest extends TestCase {

  public static class LongTree extends TreeList<LongTree> {
    
    private final long value;
    
    public LongTree(long value) {
      this.value = value;
    }
  }
  
  private static LongTree tree(String name, long value, LongTree ...trees) {
    LongTree tree = new LongTree(value);
    if (trees != null) {
      for (LongTree child : trees) {
        tree.insertAt(null, child);
      }
    }
    
    return tree;
  }
  
  private void assertChildren(LongTree tree, Long ... expected) {
    List<Long> children = new ArrayList<Long>();
    for(Iterator<LongTree> iterator = tree.listIterator(); iterator.hasNext();) {
      LongTree next = iterator.next();
      children.add(next.value);
    }
    
    assertEquals(Arrays.asList(expected), children);
  }
  
  
  private void assertAllChildren(LongTree tree, Long ...expected) {
    List<Long> children = new ArrayList<Long>();
    
    for(LongTree current = tree.getFirst(); current != null; current = current.getNext()) {
      children.add(current.value);
    }
    
    assertEquals(Arrays.asList(expected), children);
  }
  
  private void assertAllChildren(LongTree tree) {
    assertAllChildren(tree, new Long[0]);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testInsert1() throws Exception {
    LongTree root = tree("root", 0);
    assertChildren(root);
    assertAllChildren(root);
    
    root = tree("", 0);
    root.insertAt(null, tree("a", 1));
    assertChildren(root, new Long(1));
    assertAllChildren(root, new Long(1));
    
    root = tree("", 0);
    root.insertAt(null, tree("a", 1));
    root.insertAt(null, tree("b", 2));
    assertChildren(root, new Long(1), new Long(2));
    assertAllChildren(root, 1L, 2L);
  }
  
  public void testMoveTree1() throws Exception {
    LongTree tree1 = tree("a", 1);
    LongTree tree2 = tree("b", 2);
    LongTree newTree = tree("root", 0, tree1, tree2);
    
    assertChildren(newTree, 1L, 2L);
    assertAllChildren(newTree, 1L, 2L);
  }
  
  public void testMoveTree2() throws Exception {
    LongTree a = tree("a", 1);
    LongTree root1 = tree("root", 0, a);
    assertChildren(root1, 1L);
    // in the case, the a tree's parent is available, it will be removed
    root1.insertAt(null, a);
    assertChildren(root1, 1L);
    
    root1.insertAt(0, a);
    assertChildren(root1, 1L);
  }
  
  public void testMoveTree3() throws Exception {
    LongTree a = tree("a", 1);
    LongTree root1 = tree("", 0, a);
    LongTree root2 = tree("", 0);
    
    root2.insertAt(0, a);
    assertAllChildren(root1);
    assertAllChildren(root2, 1L);
    assertSame(root2, a.getParent());
  }
  
  public void testReorderTree1() throws Exception {
    LongTree a = tree("a", 1);
    LongTree root1 = tree("root", 0, a);
    
    root1.insertAt(0, a);
    assertChildren(root1, 1L);
    assertAllChildren(root1, 1L);
    assertSame(root1, a.getParent());
  }
  
  public void testReorderTree2() throws Exception {
    LongTree a = tree("a", 1);
    LongTree root1 = tree("root", 0, a, tree("b", 2));
    
    root1.insertAt(2, a);
    assertAllChildren(root1, 2L, 1L);
    assertSame(root1, a.getParent());
    
    //
    root1.insertAt(0, a);
    assertAllChildren(root1, 1L, 2L);
    assertSame(root1, a.getParent());
  }
  
  public void testRemove() throws Exception {
    LongTree root = tree("root", 0, tree("a", 1), tree("b", 2), tree("c", 3));
    assertAllChildren(root, 1L, 2L, 3L);
    
    LongTree b = root.get(1);
    b.remove();
    assertNull(b.getParent());
    assertNull(b.getPrevious());
    assertNull(b.getNext());
    assertEquals(2, b.value);
    assertAllChildren(root, 1L, 3L);
  }
  
  public void testRemoveLast() throws Exception {
    LongTree root = tree("root", 0, tree("a", 1), tree("b", 2));
    assertAllChildren(root, 1L, 2L);
    
    LongTree b = root.get(1);
    assertEquals(2, b.value);
    b.remove();
    assertAllChildren(root, 1L);
    assertEquals(1, root.getLast().value);
  }
  
  public void testIteratorRemove() {
    LongTree root = tree("root", 0, tree("a", 1));
    Iterator<LongTree> it = root.listIterator();
    
    try {
      it.remove();
      fail();
    } catch(IllegalStateException e) {
    }
    
    LongTree a = it.next();
    it.remove();
    assertNull(a.getParent());
    assertFalse(it.hasNext());
    assertAllChildren(root);
  }
  
  public void testListIterator1() {
    LongTree a = tree("a", 1);
    LongTree root = tree("", 0, a);
    
    //
    ListIterator<LongTree> it = root.listIterator();
    assertTrue(it.hasNext());
    assertEquals(0, it.nextIndex());
    assertFalse(it.hasPrevious());
    assertEquals(-1, it.previousIndex());
    
    //next node
    assertSame(a, it.next());
    assertFalse(it.hasNext());
    assertEquals(1, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    
    //
    assertSame(a, it.previous());
    assertTrue(it.hasNext());
    assertEquals(0, it.nextIndex());
    assertFalse(it.hasPrevious());
    assertEquals(-1, it.previousIndex());
  }
  
  public void testListIterator2() {
    LongTree a = tree("a", 1);
    LongTree b = tree("b", 2);
    LongTree root = tree("root", 0, a, b);
    
    ListIterator<LongTree> it = root.listIterator();
    assertTrue(it.hasNext());
    assertEquals(0, it.nextIndex());
    assertFalse(it.hasPrevious());
    assertEquals(-1, it.previousIndex());
    assertSame(a, it.next());
    assertTrue(it.hasNext());
    assertEquals(1, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    
    assertSame(b, it.next());
    assertFalse(it.hasNext());
    assertEquals(2, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(1, it.previousIndex());
    
    it.remove();
    assertFalse(it.hasNext());
    assertEquals(1, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
  }
  
  public void testListIterator3() {
    LongTree a = tree("a", 1);
    LongTree b = tree("b", 2);
    LongTree c = tree("c", 3);
    LongTree root = tree("root", 0, a, b, c);
    
    //
    ListIterator<LongTree> it = root.listIterator();
    it.next();
    it.next();
    it.remove();
    assertTrue(it.hasNext());
    assertEquals(1, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertSame(c, it.next());
    
 // Remove middle
    root = tree("", 0, a = tree("a", 1), b = tree("b", 2), c = tree("c", 3));
    it = root.listIterator();
    it.next();
    it.next();
    it.next();
    it.previous();
    it.previous();
    it.remove();
    assertTrue(it.hasNext());
    assertEquals(1, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertSame(c, it.next());

    // Remove middle
    root = tree("", 0, a = tree("a", 1), b = tree("b", 2), c = tree("c", 3));
    it = root.listIterator();
    it.next();
    it.next();
    it.remove();
    assertTrue(it.hasNext());
    assertEquals(1, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertSame(a, it.previous());

    // Remove middle
    root = tree("", 0, a = tree("a", 1), b = tree("b", 2), c = tree("c", 3));
    it = root.listIterator();
    it.next();
    it.next();
    it.next();
    it.previous();
    it.previous();
    it.remove();
    assertTrue(it.hasNext());
    assertEquals(1, it.nextIndex());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertSame(a, it.previous());
    
  }
  
  @SuppressWarnings("unchecked")
  public void testListIteratorNavigation() {
    LongTree root = tree("",0, tree("1", 1), tree("2", 2), tree("3", 3), tree("4", 4), tree("5", 5));
    
    ListIterator<LongTree> it = root.listIterator();
    assertTrue(it.hasNext());
    assertTrue(!it.hasPrevious());
    assertEquals(-1, it.previousIndex());
    assertEquals(0, it.nextIndex());
    assertEquals(1, it.next().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertEquals(1, it.nextIndex());
    assertEquals(1, it.previous().value);
    assertTrue(it.hasNext());
    assertTrue(!it.hasPrevious());
    assertEquals(-1, it.previousIndex());
    assertEquals(0, it.nextIndex());
    assertEquals(1, it.next().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertEquals(1, it.nextIndex());
    assertEquals(2, it.next().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(1, it.previousIndex());
    assertEquals(2, it.nextIndex());
    assertEquals(2, it.previous().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertEquals(1, it.nextIndex());
    assertEquals(2, it.next().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(1, it.previousIndex());
    assertEquals(2, it.nextIndex());
    assertEquals(3, it.next().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(2, it.previousIndex());
    assertEquals(3, it.nextIndex());
    assertEquals(4, it.next().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(3, it.previousIndex());
    assertEquals(4, it.nextIndex());
    assertEquals(5, it.next().value);
    assertTrue(!it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(4, it.previousIndex());
    assertEquals(5, it.nextIndex());
    assertEquals(5, it.previous().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(3, it.previousIndex());
    assertEquals(4, it.nextIndex());
    assertEquals(4, it.previous().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(2, it.previousIndex());
    assertEquals(3, it.nextIndex());
    assertEquals(3, it.previous().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(1, it.previousIndex());
    assertEquals(2, it.nextIndex());
    assertEquals(2, it.previous().value);
    assertTrue(it.hasNext());
    assertTrue(it.hasPrevious());
    assertEquals(0, it.previousIndex());
    assertEquals(1, it.nextIndex());
    assertEquals(1, it.previous().value);
    assertTrue(it.hasNext());
    assertTrue(!it.hasPrevious());
    assertEquals(-1, it.previousIndex());
    assertEquals(0, it.nextIndex());
  }

  public void testListIteratorRemove() {
    LongTree root = tree("",
                            0,
                            tree("1", 1),
                            tree("2", 2),
                            tree("3", 3),
                            tree("4", 4),
                            tree("5", 5));
    ListIterator<LongTree> it = root.listIterator();
    try {
      it.remove();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
    assertEquals(1, it.next().value);
    assertEquals(2, it.next().value);
    assertAllChildren(root, 1L, 2L, 3L, 4L, 5L);
    it.remove();
    assertAllChildren(root, 1L, 3L, 4L, 5L);
    assertEquals(3, it.next().value);
    assertEquals(3, it.previous().value);
    assertEquals(1, it.previous().value);
    it.remove();
    assertAllChildren(root, 3L, 4L, 5L);
    assertTrue(!it.hasPrevious());
    assertEquals(3, it.next().value);
    it.remove();
    assertAllChildren(root, 4L, 5L);
    try {
      it.remove();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
    assertEquals(4, it.next().value);
    assertEquals(5, it.next().value);
    it.remove();
    assertAllChildren(root, 4L);
    assertEquals(4, it.previous().value);
    it.remove();
    assertAllChildren(root);
  }

  public void testListIteratorAdd() {
    LongTree root = tree("", 0);
    ListIterator<LongTree> it = root.listIterator();
    it.add(tree("a", 1));
    assertEquals(0, it.previousIndex());
    assertEquals(1, it.nextIndex());
    assertAllChildren(root, 1L);
    it.add(tree("c", 3));
    assertEquals(1, it.previousIndex());
    assertEquals(2, it.nextIndex());
    assertAllChildren(root, 1L, 3L);
    it.add(tree("e", 5));
    assertEquals(2, it.previousIndex());
    assertEquals(3, it.nextIndex());
    assertAllChildren(root, 1L, 3L, 5L);
    assertEquals(5, it.previous().value);
    assertEquals(1, it.previousIndex());
    assertEquals(2, it.nextIndex());
    it.add(tree("d", 4));
    assertEquals(2, it.previousIndex());
    assertEquals(3, it.nextIndex());
    assertAllChildren(root, 1L, 3L, 4L, 5L);
    assertEquals(4, it.previous().value);
    assertEquals(1, it.previousIndex());
    assertEquals(2, it.nextIndex());
    assertEquals(3, it.previous().value);
    assertEquals(0, it.previousIndex());
    assertEquals(1, it.nextIndex());
    it.add(tree("b", 2));
    assertEquals(1, it.previousIndex());
    assertEquals(2, it.nextIndex());
    assertAllChildren(root, 1L, 2L, 3L, 4L, 5L);
  }

  public void testListIteratorMove() {
    LongTree root = tree("", 0, tree("a", 1), tree("b", 2), tree("c", 3));
    ListIterator<LongTree> it = root.listIterator();
    it.add(root.get(2));
    assertAllChildren(root, 3L, 1L, 2L);
  }

  public void testInsertFirstThrowsNPE() {
    LongTree a = tree("a", 0);
    try {
      a.insertFirst(null);
      fail();
    } catch (NullPointerException ignore) {
    }
  }

  public void testInsertLastThrowsNPE() {
    LongTree a = tree("a", 0);
    try {
      a.insertLast(null);
      fail();
    } catch (NullPointerException ignore) {
    }
  }

  public void testInsertBeforeThrowsNPE() {
    LongTree a = tree("a", 0);
    try {
      a.insertOnLeft(null);
      fail();
    } catch (NullPointerException ignore) {
    }
  }

  public void testInsertBeforeThrowsISE() {
    LongTree a = tree("a", 0);
    LongTree b = tree("b", 1);
    try {
      a.insertOnLeft(b);
      fail();
    } catch (IllegalStateException ignore) {
    }
  }

  public void testInsertAfterThrowsNPE() {
    LongTree a = tree("a", 0);
    try {
      a.insertOnRight(null);
      fail();
    } catch (NullPointerException ignore) {
    }
  }

  public void testInsertAfterThrowsISE() {
    LongTree a = tree("a", 0);
    LongTree b = tree("b", 1);
    try {
      a.insertOnRight(b);
      fail();
    } catch (IllegalStateException ignore) {
    }
  }

  public void testRemoveThrowsISE() {
    LongTree a = tree("a", 0);
    try {
      a.remove();
      fail();
    } catch (IllegalStateException ignore) {
    }
  }
  
}
