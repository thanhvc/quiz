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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 24, 2014  
 */
public class InterSectionList {

  /**
   * @param args
   */
  public static void main(String[] args) {
    List<String> l1 = new ArrayList<String>(10);
    l1.add("1");
    l1.add("2");
    l1.add("5");
    List<String> l2 = new ArrayList<String>(10);
    l2.add("3");
    l2.add("4");
    l2.add("5");
    l2.add("6");
    
    l1.retainAll(l2);
    
    for(String s : l1) {
      System.out.println(s);
    }

  }

}
