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
import java.util.Arrays;
import java.util.List;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Mar
 * 11, 2014
 */
public class GenericTypeTest {

  public static void main(String[] args) {
    ArrayList<String> list = new ArrayList<String>();
    list.add("Just me");

    Favorites f = new Favorites();
    f.setFavorite(new TypeReference<ArrayList<String>>() {}, list);

    //
    f.setFavorite(new TypeReference<String>() {}, "Hello");
    
    //
    f.setFavorite(new TypeReference<Object>() {}, "Hello");

    //
    List<String> newList = f.getFavorite(new TypeReference<ArrayList<String>>() {});
    System.out.println(newList);

    //
    String str = f.getFavorite(new TypeReference<String>() {});
    System.out.println(str);
    
    //
    String[] arrays = new String[]{"2","3","4"};
    f.setFavorite(new TypeReference<String[]>() {}, arrays);
    String[] newArrays = f.getFavorite(new TypeReference<String[]>(){});
    System.out.println(Arrays.asList(newArrays));
    
    Object value = f.getFavorite(new TypeReference<Object>() {});
    System.out.println(value);
  }

}
