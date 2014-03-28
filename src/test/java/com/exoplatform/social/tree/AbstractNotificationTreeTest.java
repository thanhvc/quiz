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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.exoplatform.social.tree.NodeContext;
import com.exoplatform.social.tree.NodeData;
import com.exoplatform.social.tree.NodeState;
import com.exoplatform.social.tree.SocialNode;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Feb 25, 2014  
 */
public abstract class AbstractNotificationTreeTest extends TestCase {
  /** User Number of Notification System */
  final static int NUMBER_OF_USER = 1000;
  
  /** User Number of Plugin on System */
  final static int NUMBER_OF_PLUGIN = 100;
  
  //
  /** User Prefix */
  final static String USER_PREFIX = "user";
  final static String[] users = new String[NUMBER_OF_USER];
  static  {
    for(int i = 0; i < NUMBER_OF_USER; i++) {
      users[i] = USER_PREFIX + i;
    }
  }
  
  //
  /** Plugin Prefix */
  final static String PLUGIN_PREFIX = "PLUGIN_";
  final static String[] PLUGINS = new String[NUMBER_OF_PLUGIN];
  static  {
    for(int i = 0; i < NUMBER_OF_PLUGIN; i++) {
      PLUGINS[i] = PLUGIN_PREFIX + i;
    }
  }
  
  protected static SocialNode tree(String id, String name) {
    NodeContext<SocialNode> context = new NodeContext<SocialNode>(SocialNode.SELF_MODEL, id, name);
    return context.node;
  }
  
  
  protected static void build(SocialNode root, int number, String pluginId) {
    Random userRandom = new Random(NUMBER_OF_USER);
    Random activityRandom = new Random(100);
//    for(int i=0; i<number; i++) {
//      NodeData data = new NodeData("" + i, pluginId, "" + i, NodeState.INITIAL);
//      data.state.sendToDaily = Arrays.asList(sentTo(10, 50));
//      //20% - notification info has sendToWeekly is empty
//      if ((i%5) == 0) {
//        data.state.sendToWeekly = new ArrayList<String>();
//      } else {
//        data.state.sendToWeekly = Arrays.asList(sentTo(30, 90));
//      }
//      
//      data.state.param("activityId", "activityId" + activityRandom.nextInt());
//      data.state.param("posterId", users[userRandom.nextInt()]);
//      
//      NodeContext<SocialNode> context = new NodeContext<SocialNode>(root.context.tree, data);
//      root.addChild(context.node);
//    }
    
  }
  
  private static String[] sentTo(int from, int to) {
    Random rgen = new Random();
    for (int i = 0; i < users.length; i++) {
      int randomPosition = rgen.nextInt(users.length);
      String temp = users[i];
      users[i] = users[randomPosition];
      users[randomPosition] = temp;
    }
    return Arrays.copyOfRange(users, from, to);
  }
  
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
}
