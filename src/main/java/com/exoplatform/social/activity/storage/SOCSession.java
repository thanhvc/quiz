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
package com.exoplatform.social.activity.storage;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
public class SOCSession {
  /** */
  final static SOCSession socSession = new SOCSession();
  
  public static SOCSession getInstance() {
    return socSession;
  }
  boolean started = false;

  public void startSession() {
    if (started) {
      System.out.println("session started....");
    } else {
      started = true;
      System.out.println("start new session....");
    }
  }
  
  public void stopSession() {
    if (started) {
      started = false;
      System.out.println("stop session.");
    } else {
      System.out.println("start new session.");
    }
  }
  
  public void logout() {
    started = false;
    //System.out.println("session releasing.");
  }
}
