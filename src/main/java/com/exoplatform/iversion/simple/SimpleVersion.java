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

import java.util.Map;
import java.util.Set;

import com.exoplatform.iversion.Version;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 4, 2014  
 */
public class SimpleVersion extends Version<String, String, String>{

  public SimpleVersion(Builder builder) {
    super(builder);
  }
  
  public String getComment() {
    return metadata;
  }
  
  public static class Builder extends Version.Builder<String, String, String> {

    public Builder(long revision) {
      super(revision);
    }
    
    @Override
    public Builder branch(String branch) {
      super.branch(branch);
      return this;
    }
    
    @Override
    public Builder parents(Set<Long> parentRevisions) {
      super.parents(parentRevisions);
      return this;
    }
    
    @Override
    public Builder properties(Map<String, String> properties) {
      super.properties(properties);
      return this;
    }
    
    @Override
    public Builder metadata(String metadata) {
      super.metadata(metadata);
      return this;
    }
    
    public Builder comment(String comment) {
      super.metadata(comment);
      return this;
    }
    
    public SimpleVersion build() {
      return new SimpleVersion(this);
    }
  }
}
