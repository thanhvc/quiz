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

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.exoplatform.social.activity.mock.MockPersister;
import com.exoplatform.social.activity.persister.PersisterTask;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 10, 2014  
 */
public class PersisterTimerTaskTest extends TestCase {
  
  public void testPersisterTimerTask() throws Exception {
    PersisterTask timerTask = PersisterTask.init().persister(new MockPersister()).wakeup(1000).timeUnit(TimeUnit.MILLISECONDS).build();
    
    timerTask.start();
    
    
    Thread.sleep(2010);
    
    timerTask.stop();
  }

  
}
