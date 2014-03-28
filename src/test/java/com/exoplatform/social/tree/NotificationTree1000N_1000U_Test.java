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

import com.exoplatform.social.tree.SocialNode;



/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Feb 26, 2014  
 */
public class NotificationTree1000N_1000U_Test extends AbstractNotificationTreeTest {
  /** User Number of NotificationInfo per Plugin */
  final static int NUMBER_OF_NOTIFICATION = 1000;
  
  public void testBuildNotificationTree() throws Exception {
    SocialNode root = tree("0", "root");
    build(root, NUMBER_OF_NOTIFICATION, "ActivityCommentPlugin");
    assertEquals(NUMBER_OF_NOTIFICATION, root.getNodeCount());
    assertEquals(NUMBER_OF_NOTIFICATION, root.getNodeSize());
  }
  
}
