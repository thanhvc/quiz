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
package com.exoplatform.social.activity;

import junit.framework.TestCase;

import com.exoplatform.social.activity.storage.cache.data.ActivitiesListData;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 8, 2014  
 */
public class ActivityListDataTest extends TestCase {

  public void testInsertFirst() throws Exception {
    ActivitiesListData data = new ActivitiesListData("mylist", 10);
    
    for(int i = 0; i< 100; i++) {
      data.insertFirst("activity_" + i, null);
    }
    
    assertEquals(10, data.size());
    
    
  }
}
