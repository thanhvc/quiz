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
package com.exoplatform.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 3, 2014  
 */
public class ListTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    List<String> list1 = new ArrayList<String>();
    list1.add("1");
    list1.add("2");
    list1.add("3");
    List<String> list2 = new ArrayList<String>();

    list2.add("3");
    list2.add("4");
    list2.add("5");
    //keep the same
    //list1.retainAll(list2);
    list1.addAll(list2);
    
    Collection<String> union = new TreeSet<String>(list1);
    union.addAll(list2);
    
    System.out.println("List1 lenght = " + list1.size());
    System.out.println("union lenght = " + union.size());
  }

}
