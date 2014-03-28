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
package com.exoplatform.lab;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 11, 2014  
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class TypeReference<T> {

  private final Type type;

  private volatile Constructor<?> constructor;

  protected TypeReference() {
    Type superclass = getClass().getGenericSuperclass();
    if (superclass instanceof Class) {
      throw new RuntimeException("Missing type parameter.");
    }
    this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
  }

  public Type getType() {
    return this.type;
  }

  public boolean equals(Object o) {
    return (o instanceof TypeReference) ? ((TypeReference) o).type.equals(this.type) : false;
  }

  public int hashCode() {
    return this.type.hashCode();
  }
}
