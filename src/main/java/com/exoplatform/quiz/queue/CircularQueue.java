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
package com.exoplatform.quiz.queue;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform thanhvc@exoplatform.com
 * Feb 12, 2014
 */
public class CircularQueue<E> extends AbstractList<E> implements List<E>, Queue<E>, Serializable {

  private static final long serialVersionUID = 3993421269224511264L;

  private static final int  DEFAULT_CAPACITY = 100;

  private final int         initialCapacity;

  private volatile Object[] items;

  private int               mask;

  private int               first            = 0;

  private int               last             = 0;

  private boolean           full;

  private int               shrinkThreshold;

  /**
   * Construct a new, empty queue.
   */
  public CircularQueue() {
    this(DEFAULT_CAPACITY);
  }

  public CircularQueue(int initialCapacity) {
    int actualCapacity = normalizeCapacity(initialCapacity);
    this.items = new Object[actualCapacity];
    this.mask = actualCapacity - 1;
    this.initialCapacity = actualCapacity;
    this.shrinkThreshold = 0;
  }

  private static int normalizeCapacity(int initialCapacity) {
    int actualCapacity = 1;
    while (actualCapacity < initialCapacity) {
      actualCapacity <<= 1;
      if (actualCapacity < 0) {
        actualCapacity = 1 << 30;
        break;
      }
    }
    return actualCapacity;
  }

  /**
   * Returns the capacity of this queue.
   */
  public int capacity() {
    return this.items.length;
  }

  @Override
  public void clear() {
    if (!isEmpty()) {
      Arrays.fill(this.items, null);
      this.first = 0;
      this.last = 0;
      this.full = false;
      shrinkIfNeeded();
    }
  }

  @SuppressWarnings("unchecked")
  public E poll() {
    if (isEmpty()) {
      return null;
    }

    Object ret = this.items[this.first];
    this.items[this.first] = null;
    decreaseSize();

    if (this.first == this.last) {
      this.first = this.last = 0;
    }

    shrinkIfNeeded();
    return (E) ret;
  }

  public boolean offer(E item) {
    if (item == null) {
      throw new NullPointerException("item");
    }
    expandIfNeeded();
    this.items[this.last] = item;
    increaseSize();
    return true;
  }

  @SuppressWarnings("unchecked")
  public E peek() {
    if (isEmpty()) {
      return null;
    }

    return (E) this.items[this.first];
  }

  @SuppressWarnings("unchecked")
  @Override
  public E get(int idx) {
    checkIndex(idx);
    return (E) this.items[getRealIndex(idx)];
  }

  @Override
  public boolean isEmpty() {
    return (this.first == this.last) && !this.full;
  }

  @Override
  public int size() {
    if (this.full) {
      return capacity();
    }

    if (this.last >= this.first) {
      return this.last - this.first;
    } else {
      return this.last - this.first + capacity();
    }
  }

  @Override
  public String toString() {
    return "first=" + this.first + ", last=" + this.last + ", size=" + size() + ", mask = "
        + this.mask;
  }

  private void checkIndex(int idx) {
    if (idx < 0 || idx >= size()) {
      throw new IndexOutOfBoundsException(String.valueOf(idx));
    }
  }

  private int getRealIndex(int idx) {
    return (this.first + idx) & this.mask;
  }

  private void increaseSize() {
    this.last = (this.last + 1) & this.mask;
    this.full = this.first == this.last;
  }

  private void decreaseSize() {
    this.first = (this.first + 1) & this.mask;
    this.full = false;
  }

  private void expandIfNeeded() {
    if (this.full) {
      // expand queue
      final int oldLen = this.items.length;
      final int newLen = oldLen << 1;
      Object[] tmp = new Object[newLen];

      if (this.first < this.last) {
        System.arraycopy(this.items, this.first, tmp, 0, this.last - this.first);
      } else {
        System.arraycopy(this.items, this.first, tmp, 0, oldLen - this.first);
        System.arraycopy(this.items, 0, tmp, oldLen - this.first, this.last);
      }

      this.first = 0;
      this.last = oldLen;
      this.items = tmp;
      this.mask = tmp.length - 1;
      if (newLen >>> 3 > this.initialCapacity) {
        this.shrinkThreshold = newLen >>> 3;
      }
    }
  }

  private void shrinkIfNeeded() {
    int size = size();
    if (size <= this.shrinkThreshold) {
      // shrink queue
      final int oldLen = this.items.length;
      int newLen = normalizeCapacity(size);
      if (size == newLen) {
        newLen <<= 1;
      }

      if (newLen >= oldLen) {
        return;
      }

      if (newLen < this.initialCapacity) {
        if (oldLen == this.initialCapacity) {
          return;
        } else {
          newLen = this.initialCapacity;
        }
      }

      Object[] tmp = new Object[newLen];

      // Copy only when there's something to copy.
      if (size > 0) {
        if (this.first < this.last) {
          System.arraycopy(this.items, this.first, tmp, 0, this.last - this.first);
        } else {
          System.arraycopy(this.items, this.first, tmp, 0, oldLen - this.first);
          System.arraycopy(this.items, 0, tmp, oldLen - this.first, this.last);
        }
      }

      this.first = 0;
      this.last = size;
      this.items = tmp;
      this.mask = tmp.length - 1;
      this.shrinkThreshold = 0;
    }
  }

  @Override
  public boolean add(E o) {
    return offer(o);
  }

  @SuppressWarnings("unchecked")
  @Override
  public E set(int idx, E o) {
    checkIndex(idx);

    int realIdx = getRealIndex(idx);
    Object old = this.items[realIdx];
    this.items[realIdx] = o;
    return (E) old;
  }

  @Override
  public void add(int idx, E o) {
    if (idx == size()) {
      offer(o);
      return;
    }

    checkIndex(idx);
    expandIfNeeded();

    int realIdx = getRealIndex(idx);

    // Make a room for a new element.
    if (this.first < this.last) {
      System.arraycopy(this.items, realIdx, this.items, realIdx + 1, this.last - realIdx);
    } else {
      if (realIdx >= this.first) {
        System.arraycopy(this.items, 0, this.items, 1, this.last);
        this.items[0] = this.items[this.items.length - 1];
        System.arraycopy(this.items, realIdx, this.items, realIdx + 1, this.items.length - realIdx
            - 1);
      } else {
        System.arraycopy(this.items, realIdx, this.items, realIdx + 1, this.last - realIdx);
      }
    }

    this.items[realIdx] = o;
    increaseSize();
  }

  @SuppressWarnings("unchecked")
  @Override
  public E remove(int idx) {
    if (idx == 0) {
      return poll();
    }

    checkIndex(idx);

    int realIdx = getRealIndex(idx);
    Object removed = this.items[realIdx];

    // Remove a room for the removed element.
    if (this.first < this.last) {
      System.arraycopy(this.items, this.first, this.items, this.first + 1, realIdx - this.first);
    } else {
      if (realIdx >= this.first) {
        System.arraycopy(this.items, this.first, this.items, this.first + 1, realIdx - this.first);
      } else {
        System.arraycopy(this.items, 0, this.items, 1, realIdx);
        this.items[0] = this.items[this.items.length - 1];
        System.arraycopy(this.items, this.first, this.items, this.first + 1, this.items.length
            - this.first - 1);
      }
    }

    this.items[this.first] = null;
    decreaseSize();

    shrinkIfNeeded();
    return (E) removed;
  }

  public E remove() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return poll();
  }

  public E element() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return peek();
  }
}
