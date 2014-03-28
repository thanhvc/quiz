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

import java.util.List;
import java.util.Vector;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Feb 12, 2014  
 */

/**
 * Rotating queue of fixed size.
 * 
 */
public class RotatingQueue<T> {

  public RotatingQueue(int capacity) {
    size = capacity;
    queue = new Vector<T>(capacity);
    mostRecentItem = capacity - 1;
  }

  private T lastInsertedElement;

  /**
   * Inserts an element to the head of the queue, pushing all other elements one
   * position forward.
   * 
   * @param element
   */
  public synchronized void addFirst(T element) {
    // Get index
    mostRecentItem = advancePointer(mostRecentItem);

    // Check if list already has an element
    if (queue.size() == mostRecentItem) {
      queue.add(element);
    } else {
      queue.set(mostRecentItem, element);
    }
    lastInsertedElement = element;
  }
  
  public synchronized void addLast(T element) {
    // Get index
    mostRecentItem = advancePointer(mostRecentItem);

    // Check if list already has an element
    if (queue.size() == mostRecentItem) {
      queue.add(element);
    } else {
      queue.set(mostRecentItem, element);
    }
    lastInsertedElement = element;
  }

  public T getElement(int index) {
    // Normalize index to size of queue
    index = index % size;

    // Translate wanted index to queue index
    int queueIndex = mostRecentItem - index;
    // If negative, add size
    if (queueIndex < 0) {
      queueIndex += size;
    }

    // Check if element already exists in queue
    if (queueIndex < queue.size()) {
      return queue.get(queueIndex);
    } else {
      return null;
    }
  }

  public int size() {
    return size;
  }

  public synchronized void setSize(int size) {
    this.size = size;
  }

  private int advancePointer(int oldPointer) {
    int pointer = oldPointer + 1;
    if (pointer < size) {
      return pointer;
    } else {
      return 0;
    }
  }

  public List<T> toList() {
    return queue.subList(0, queue.size());
  }

  public T getLastInstertedElement() {
    if (queue.size() < 1) {
      return null;
    }
    return queue.get(mostRecentItem);
  }

  // /
  // INSTANCE VARIABLES
  // /
  private Vector<T> queue;

  private int       mostRecentItem;

  private int       size;

  public static void main(String[] args) {
    RotatingQueue<Integer> qu = new RotatingQueue<Integer>(10);

    qu.addFirst(1);
    qu.addFirst(2);
    qu.addFirst(3);
    qu.addFirst(4);
    qu.addFirst(5);
    
    List<Integer> l = qu.toList();
    
    for(Integer i : l) {
      System.out.println(" i = " + i);
    }
  }
}
