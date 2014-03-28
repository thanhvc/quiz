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

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Feb 12, 2014  
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayListTest {
  public static String testList(List list) {
    StringBuffer result = new StringBuffer(1024);
    list.add("ABCD");
    list.add("EFGH");
    list.add("IJKL");
    list.addAll(list);
    result.append(list);
    result.append(list.contains("IJKL"));
    result.append(list.containsAll(new ArrayList()
      {{add("ABCD");add("EFGH");}}));
    result.append(list.equals(new ArrayList(list)));
    for (int i=0; i<6; i++)
      result.append(list.get(i));
    result.append(list.indexOf("EFGH"));
    result.append(list.isEmpty());
    result.append(list.lastIndexOf("EFGH"));
    for (int i=0; i<3; i++) result.append(list.remove(3));
    for (int i=0; i<3; i++) result.append(list.remove(0));
    for (int i=0; i<6; i++) list.add(Integer.toString(i));
    for (int i=0; i<6; i++) result.append(list.get(i));
    Object[] els = list.toArray();
    for (int i=0; i<els.length; i++) result.append(els[i]);
    String[] strs = (String[])list.toArray(new String[0]);
    for (int i=0; i<strs.length; i++) result.append(strs[i]);
    for (int i=0; i<32; i++) {
      list.add(Integer.toHexString(i));
      result.append(list.remove(0));
    }
    result.append(list);
    return result.toString();
  }
  public static void testPerformance(List list, int length) {
    Object job = new Object();
    int iterations = 0;
    for (int j=0; j<length; j++) list.add(job);
    long time = -System.currentTimeMillis();
    while(time + System.currentTimeMillis() < 2000) {
      iterations++;
      for (int j=0; j<100; j++) {
        list.remove(0);
        list.add(job);
      }
    }
    time += System.currentTimeMillis();
    System.out.println(list.getClass() + " managed " +
      iterations + " iterations in " + time + "ms");
  }
  public static void testCorrectness() {
    String al = testList(new ArrayList(6));
    String cal = testList(new CircularArrayList(6));
    if (al.equals(cal)) System.out.println("Correctness Passed");
    else {
      System.out.println("Expected:");
      System.out.println(al);
      System.out.println("But got:");
      System.out.println(cal);
    }
  }
  public static void testPerformance(int length) {
    System.out.println("Performance with queue length = " + length);
    testPerformance(new ArrayList(), length);
    testPerformance(new LinkedList(), length);
    testPerformance(new CircularArrayList(), length);
  }
  public static void main(String[] args) {
    testCorrectness();
    testPerformance(1);
    testPerformance(10);
    testPerformance(100);
    testPerformance(1000);
    testPerformance(10000);
    testPerformance(100000);
  }
}