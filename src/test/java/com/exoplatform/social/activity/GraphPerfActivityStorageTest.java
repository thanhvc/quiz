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

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.exoplatform.social.activity.model.ExoSocialActivity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 28, 2014  
 */
public class GraphPerfActivityStorageTest extends AbstractActivityTest {
  /** */
  final static int PERSISTER_THRESHOLD = 1000;
  
  @Override
  public int getPersistActivityThreshold() {
    return PERSISTER_THRESHOLD;
  }

  private void prerformance(int length) throws Exception {
    watch.start();
    List<ExoSocialActivity> list = listOfPerf(length, null, false, true);
    assertEquals(length, list.size());
    watch.stop();
    System.out.println("Process:: activities:" + length + "| vertices=" + socContext.getActivityCacheGraph().getVertexSize() + "| edges: " + socContext.getActivityCacheGraph().getEdgeSize() + " | elapsed=" + watch.toString(watch.elapsedTime(), TimeUnit.MILLISECONDS));
  }
  
  public void testPerformance() throws Exception {
    prerformance(1000);
    this.clearAll();
    prerformance(5000);
    this.clearAll();
    prerformance(10000);
    this.clearAll();
    prerformance(20000);
    this.clearAll();
    prerformance(30000);
    this.clearAll();
    prerformance(40000);
    this.clearAll();
    prerformance(50000);
    this.clearAll();
    prerformance(100000);
    this.clearAll();
    prerformance(200000);
    this.clearAll();
    prerformance(300000);
    this.clearAll();
    prerformance(400000);
    this.clearAll();
    prerformance(500000);
    this.clearAll();
    prerformance(600000);
    this.clearAll();
    prerformance(700000);
    this.clearAll();
    prerformance(800000);
    this.clearAll();
    prerformance(900000);
    this.clearAll();
    prerformance(1000000);
  }
}
