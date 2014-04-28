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

import java.util.LinkedList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by The eXo Platform SAS 
 * Author : eXoPlatform exo@exoplatform.com 
 * Apr 28, 2014
 */
public class CategoryIndexTest {

  public static class Category {
    Long displayIndex = new Long(0);

    final Long lastModifed;

    final Long actualIndex;

    final String name;

    public static Builder init(long index) {
      return new Builder(index);
    }

    public static Builder init(long index, Category tail) {
      return new Builder(index, tail);
    }

    public Category(Builder builder) {
      this.lastModifed = builder.lastModified();
      this.actualIndex = builder.actualIndex;
      this.name = builder.name;
    }

    public Long getDisplayIndex() {
      return displayIndex;
    }

    public void setDisplayIndex(long displayIndex) {
      this.displayIndex = new Long(displayIndex);
    }

    public Long getLastModifed() {
      return lastModifed;
    }

    public Long getActualIndex() {
      return actualIndex;
    }

    public static class Builder {
      public Long actualIndex;

      public String name;

      public Builder(long actualIndex) {
        this(actualIndex, null);
      }

      public Builder(long actualIndex, Category tail) {
        if (tail == null) {
          this.actualIndex = new Long(actualIndex);
        } else {
          this.actualIndex = new Long(tail.getActualIndex() + 1);
        }
      }

      public Builder name(String name) {
        this.name = name;
        return this;
      }

      public Long lastModified() {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          System.out.println(e.getMessage());
        }
        return System.currentTimeMillis();
      }

      public Category build() {
        return new Category(this);
      }

    }
  }

  public static void main(String[] args) {
    TreeMap<Long, TreeMap<Long, Category>> holder = new TreeMap<Long, TreeMap<Long, Category>>();

    Category tail = null;
    // 1l
    Category c = Category.init(1l).name("aaaa1").build();
    append(holder, c);
    tail = c;

    // insert 10l
    c = Category.init(10l, null).name("aaaa2").build();
    append(holder, c);
    tail = c;

    LinkedList<Category> got = transform(holder);
    print(got);

    // insert 4l
    c = Category.init(4l, tail).name("aaaa3").build();
    append(holder, c);
    tail = c;

    // insert 2l
    c = Category.init(2l, null).name("aaaa2.1").build();
    append(holder, c);
    tail = c;

    got = transform(holder);
    print(got);
  }

  private static void print(LinkedList<Category> data) {
    System.out.println("########################################");
    for (Category c : data) {
      System.out.println("" + c.displayIndex + " name : " + c.name + " actual = "
          + c.actualIndex.longValue());
    }
  }

  private static void append(TreeMap<Long, TreeMap<Long, Category>> holder, Category c) {
    TreeMap<Long, Category> entry = holder.get(c.getActualIndex());
    if (entry == null) {
      entry = new TreeMap<Long, Category>();
    }
    //
    entry.put(c.lastModifed, c);
    holder.put(c.getActualIndex(), entry);

  }

  private static LinkedList<Category> transform(TreeMap<Long, TreeMap<Long, Category>> transformHolder) {
    LinkedList<Category> result = new LinkedList<Category>();
    long index = 1;
    for (Map.Entry<Long, TreeMap<Long, Category>> e : transformHolder.entrySet()) {
      TreeMap<Long, Category> sub = e.getValue();

      NavigableMap<Long, Category> nav = sub.descendingMap();

      for (Map.Entry<Long, Category> subE : nav.entrySet()) {
        Category c = subE.getValue();
        c.setDisplayIndex(index);
        result.add(c);
        index++;
      }
    }

    return result;

  }

}
