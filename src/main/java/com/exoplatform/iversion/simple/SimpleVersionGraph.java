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
package com.exoplatform.iversion.simple;

import com.exoplatform.iversion.VersionGraph;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 4, 2014  
 */
public class SimpleVersionGraph extends VersionGraph<String, String, String, SimpleVersion, SimpleVersionGraph> {

  SimpleVersionGraph(Builder builder) {
    super(builder);
  }
  
  public static SimpleVersionGraph init() {
    return build(new Builder());
  }
  
  public static SimpleVersionGraph init(SimpleVersion version) {
    return build(new Builder(), version);
  }
  
  public static SimpleVersionGraph init(Iterable<SimpleVersion> versions) {
    return build(new Builder(), versions);
  }
  
  @Override
  public SimpleVersionGraph commit(Iterable<SimpleVersion> versions) {
    return build(new Builder(this), versions);
  }
  
  @Override
  public SimpleVersionGraph commit(SimpleVersion version) {
    return build(new Builder(this), version);
  }
  
  public static class Builder extends VersionGraph.Builder<String, String, String, SimpleVersion, SimpleVersionGraph> {

    protected Builder() {
      super(null);
    }
    
    protected Builder(SimpleVersionGraph parentGraph) {
      super(parentGraph);
    }
    
    @Override
    protected SimpleVersionGraph build() {
      return new SimpleVersionGraph(this);
    }
    
  }
}
