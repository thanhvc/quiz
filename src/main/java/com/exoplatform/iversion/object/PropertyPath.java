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
package com.exoplatform.iversion.object;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Created by The eXo Platform SAS Author : 
 * eXoPlatform exo@exoplatform.com 
 * Apr 5, 2014
 * 
 */
public abstract class PropertyPath implements Iterable<PropertyPath> {

  private static final String EMPTY_STRING = "";
  
  public static final Root ROOT = Root.ROOT;
  
  public abstract String toString();

  public abstract boolean equals(Object obj);

  public abstract int hashCode();

  public abstract String getName();

  private volatile List<PropertyPath> fullPath;
  
  abstract PropertyPath normalize(PropertyPath newParent);

  abstract List<PropertyPath> getFullPath();

  PropertyPath() {
  }

  public Property property(String name) {
    return new Property(this, name);
  }

  public Index index(long index) {
    return index(Long.toString(index));
  }

  public Index index(String index) {
    return new Index(this, index);
  }

  public Iterator<PropertyPath> iterator() {
    return path().iterator();
  }

  public List<PropertyPath> path() {
    return fullPath != null ? fullPath : (fullPath = getFullPath());
  }

  public boolean isRoot() {
    return false;
  }
  
  public PropertyPath toSchemaPath() {
    PropertyPath schemaPath = null;
    for(PropertyPath path : this) {
      schemaPath = path.normalize(schemaPath);
    }
    
    return schemaPath;
  }

  public boolean startsWith(PropertyPath other) {
    List<PropertyPath> thisPath = path();
    List<PropertyPath> otherPath = other.path();
    int otherSize = otherPath.size();
    return thisPath.size() >= otherSize
        && thisPath.get(otherSize - 1).equals(otherPath.get(otherSize - 1));
  }

  

  public static final class Root extends PropertyPath {

    private static final Root               ROOT      = new Root();

    private static final List<PropertyPath> FULL_PATH = ImmutableList.<PropertyPath> of(ROOT);

    private Root() {
    }

    List<PropertyPath> getFullPath() {
      return FULL_PATH;
    }

    @Override
    public boolean equals(Object obj) {
      return obj == this;
    }

    @Override
    public String toString() {
      return EMPTY_STRING;
    }

    @Override
    public int hashCode() {
      return 1;
    }

    public boolean isRoot() {
      return true;
    }

    @Override
    public String getName() {
      return EMPTY_STRING;
    }
    
    @Override
    PropertyPath normalize(PropertyPath newParent) {
      return ROOT;
    }

  }

  public static abstract class SubPath extends PropertyPath {

    public final PropertyPath parent;

    private SubPath(PropertyPath parent) {
      this.parent = checkNotNull(parent, "parent");
    }

    List<PropertyPath> getFullPath() {
      List<PropertyPath> parentPath = parent.getFullPath();
      ImmutableList.Builder<PropertyPath> pathBuilder = ImmutableList.builder();
      pathBuilder.addAll(parentPath);
      pathBuilder.add(this);
      return pathBuilder.build();
    }

  }

  public static final class Property extends SubPath {

    public final String name;

    private Property(PropertyPath parent, String name) {
      super(parent);
      checkArgument(!isNullOrEmpty(name), (Object) "name should not be null or empty");
      this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else if (obj instanceof Property) {
        Property other = (Property) obj;
        return this.name.equals(other.name) && parent.equals(other.parent);
      } else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return 31 * parent.hashCode() + name.hashCode();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder().append(parent.toString());
      if (!parent.isRoot()) {
        sb.append('.');
      }
      return sb.append(encode(name)).toString();
    }

    @Override
    public String getName() {
      return name;
    }
    
    @Override
    public Property normalize(PropertyPath newParent) {
      return parent.equals(newParent) ? this : new Property(newParent, name);
    }

  }

  public static final class Index extends SubPath {

    public final String index;

    private Index(PropertyPath parent, String index) {
      super(parent);
      this.index = checkNotNull(index, "index");
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else if (obj instanceof Index) {
        Index other = (Index) obj;
        return this.index.equals(other.index) && parent.equals(other.parent);
      } else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return 31 * parent.hashCode() + index.hashCode();
    }

    @Override
    public String toString() {
      return new StringBuilder().append(parent.toString())
                                .append('[')
                                .append(encode(index))
                                .append(']')
                                .toString();
    }

    @Override
    public String getName() {
      return index;
    }
    
    @Override
    PropertyPath normalize(PropertyPath newParent) {
      return parent.equals(newParent) && EMPTY_STRING.equals(index) ? this
                                                                   : new Index(newParent,
                                                                               EMPTY_STRING);
    }

  }

  static String encode(String str) {
    return str.replace("\\", "\\\\").replace(".", "\\.").replace("]", "\\]");
  }

}
