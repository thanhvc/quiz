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

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform thanhvc@exoplatform.com
 * Feb 22, 2014
 */
public class TreeList<T extends TreeList<T>> {

  private T   parent;

  private T   right;

  private T   left;

  private T   head;

  private T   tail;

  private int size;

  public TreeList() {
    this.right = null;
    this.left = null;
    this.head = null;
    this.tail = null;
    this.size = 0;
  }

  public final T getFirst() {
    return head;
  }

  public final T getLast() {
    return tail;
  }

  public final T getPrevious() {
    return left;
  }

  public final T getNext() {
    return right;
  }

  public final T getParent() {
    return parent;
  }
  
  /**
   * Returns the size.
   *
   * @return the size
   */
  public final int getSize() {
      return size;
  }

  /**
   * Inserts the specified tree in the last position
   * 
   * @param tree the tree will insert
   * @throws NullPointerException
   */
  public final void insertLast(T tree) throws NullPointerException {
    if (tree == null) {
      throw new NullPointerException("Tree must not be null.");
    }

    if (tail == null) {
      insertFirst(tree);
    } else {
      tail.insertOnRight(tree);
    }
  }

  /**
   * Inserts the specified tree in first position
   * 
   * @param tree the tree will insert
   * @throws NullPointerException
   */
  public final void insertFirst(T tree) throws NullPointerException {
    if (tree == null) {
      throw new NullPointerException("Tree must not be null.");
    }

    if (head == null) {
      beforeInsert(tree);
      if (tree.parent != null) {
        // if inserts specified tree is existing parent
        // the parent will be removed.
        tree.remove();
      }

      head = tail = tree;

      tree.parent = (T) this;
      size++;
      afterInsert(tree);
    } else {
      head.insertOnLeft(tree);
    }
  }

  /**
   * Insert the specified tree on the after of this
   * 
   * @param tree the tree insert after
   */
  public final void insertOnRight(T tree) throws NullPointerException, IllegalStateException {
    if (tree == null) {
      throw new NullPointerException("Tree must not be null.");
    }

    if (parent == null) {
      throw new IllegalStateException();
    }

    if (this != tree) {
      //callback
      parent.beforeInsert(tree);
      
      if (tree.parent != null) {
        tree.remove();
      }

      tree.left = (T) this;
      tree.right = right;
      if (right == null) {
        parent.tail = tree;
      } else {
        right.left = tree;
      }

      this.right = tree;
      tree.parent = parent;
      tree.parent.size++;
      //callback
      parent.afterInsert(tree);

    }
  }

  /**
   * Insert the specified tree on the before of this
   * 
   * @param tree the tree insert on the right
   */
  public final void insertOnLeft(T tree) throws NullPointerException, IllegalStateException {
    if (tree == null) {
      throw new NullPointerException("Tree must not be null.");
    }

    if (parent == null) {
      throw new IllegalStateException();
    }

    if (this != tree) {
      if (tree.parent != null) {
        tree.remove();
      }

      tree.right = (T) this;
      tree.left = left;
      if (left == null) {
        parent.head = tree;
      } else {
        left.right = tree;
      }

      left = tree;
      tree.parent = parent;
      parent.size++;
    }
  }

  public final T get(int index) throws IndexOutOfBoundsException {
    if (index < 0) {
      throw new IndexOutOfBoundsException("No negative index allowed");
    }

    //
    T current = head;
    while (true) {
      if (current == null) {
        throw new IndexOutOfBoundsException("index " + index + " is greater than the children size");
      }
      if (index == 0) {
        break;
      } else {
        current = current.right;
        index--;
      }
    }
    return current;
  }
  
  
  
  /**
   * Removes all of the elements from this list.
   */
  public void clear() {
    for (T t = getFirst(); t != null; t = t.getNext()) {
      t.remove();
    }
  }


  /**
   * Removes this tree from its parent
   * 
   * @throws IllegalStateException
   */
  public final void remove() throws IllegalStateException {
    if (parent == null) {
      throw new IllegalStateException();
    }

    parent.beforeRemove((T) this);

    if (left == null) {
      parent.head = right;
    } else {
      left.right = right;
    }
    if (right == null) {
      parent.tail = left;
    } else {
      right.left = left;
    }
    T _parent = parent;
    parent = null;
    left = null;
    right = null;
    _parent.size--;
    
    size--;

    _parent.afterRemove((T) this);

  }

  /**
   * @param index
   * @param tree
   * @throws NullPointerException
   * @throws IllegalStateException
   */
  public final void insertAt(Integer index, T tree) throws NullPointerException,
                                                   IllegalStateException,
                                                   IndexOutOfBoundsException {
    if (tree == null) {
      throw new NullPointerException("No null tree accepted");
    }
    if (index != null && index < 0) {
      throw new IndexOutOfBoundsException("No negative index permitted");
    }

    //
    if (index != null) {
      T a = head;
      if (index == 0) {
        insertFirst(tree);
      } else {
        while (index > 0) {
          if (a == null) {
            throw new IndexOutOfBoundsException();
          }
          index--;
          a = a.right;
        }

        //
        if (a == null) {
          insertLast(tree);
        } else if (a != tree) {
          a.insertOnLeft(tree);
        }
      }
    } else {
      T a = tail;
      if (a == null) {
        insertFirst(tree);
      } else if (a != tree) {
        a.insertOnRight(tree);
      }
    }
  }

  public final ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      T   right   = head;

      T   current = null;

      T   left    = null;

      int index   = 0;

      public boolean hasNext() {
        return right != null;
      }

      public T next() {
        if (right != null) {
          current = right;

          //
          left = right;
          right = right.right;
          index++;
          return current;
        } else {
          throw new NoSuchElementException();
        }
      }

      public boolean hasPrevious() {
        return left != null;
      }

      public T previous() {
        if (left != null) {
          current = left;

          //
          right = left;
          left = left.left;
          index--;
          return current;
        } else {
          throw new NoSuchElementException();
        }
      }

      public int nextIndex() {
        return index;
      }

      public int previousIndex() {
        return index - 1;
      }

      public void remove() {
        if (current == null) {
          throw new IllegalStateException("no element to remove");
        }
        if (current == left) {
          index--;
        }
        right = current.right;
        left = current.left;
        current.remove();
        current = null;
      }

      public void set(T tree) {
        throw new UnsupportedOperationException();
      }

      public void add(T tree) {
        if (left == null) {
          insertFirst(tree);
        } else {
          left.insertOnRight(tree);
        }
        index++;
        left = tree;
        right = tree.right;
      }
    };

  }

  /**
   * Callback when insertion raised
   * @param tree
   */
  public void afterInsert(T tree) {
  }
  /**
   * Callback when insertion raised
   * @param tree
   */
  public void beforeInsert(T tree) {
  }
  /**
   * Callback when removing raised
   * @param tree
   */
  public void beforeRemove(T tree) {
    
  }
  
  /**
   * Callback when removing raised
   * @param tree
   */
  public void afterRemove(T tree) {
    
  }

}
