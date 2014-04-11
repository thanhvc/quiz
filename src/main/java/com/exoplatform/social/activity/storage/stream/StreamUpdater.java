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
package com.exoplatform.social.activity.storage.stream;

import java.util.Set;

import com.exoplatform.social.activity.VersionChangeContext;
import com.exoplatform.social.activity.storage.stream.ActivityRefContext.Builder;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 11, 2014  
 */
public class StreamUpdater {

  /** */
  private final VersionChangeContext<ActivityRefKey> versionContext;
  
  public StreamUpdater() {
    versionContext = new VersionChangeContext<ActivityRefKey>();
  }
  
  public void clearAll() {
    versionContext.clearChanges();
  }
  
  public VersionChangeContext<ActivityRefKey> getVersionContext() {
    return versionContext;
  }
  
  public void owner(Builder builder) {
    if (builder.isUserOwner) {
      versionContext.add(builder.feedKey(), builder.ownerKey());
    } else {
      versionContext.add(builder.feedKey(), builder.mySpacesKey());
    }
  }
  
  public void connecter(Builder builder) {
    if (builder.isUserOwner) {
      versionContext.add(builder.feedKey(), builder.connectionsKey());
    }
  }
  
  public void remove(Builder builder) {
    if (builder.isUserOwner) {
      versionContext.remove(builder.feedKey(), builder.ownerKey(), builder.connectionsKey());
    } else {
      versionContext.remove(builder.feedKey(), builder.mySpacesKey());
    }
  }
  
  public int getChangesSize() {
    return versionContext.getChangesSize();
  }
  
  
  public Set<ActivityRefKey> popChanges() {
    return versionContext.popChanges();
  }
  
}
