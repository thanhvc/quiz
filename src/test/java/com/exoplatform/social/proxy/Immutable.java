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
package com.exoplatform.social.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 6, 2014  
 */
public class Immutable implements InvocationHandler {

  private final Class<?> targetClass;
  private final Map<String, Object> fields;
  
  public Immutable(Class<?> targetClass, Map<String, Object> immutableFields) {
    this.targetClass = targetClass;
    this.fields = immutableFields;
  }
  
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if(method.getName().equals("toString")) {
      return fields.toString();
    }
    
    if (method.getName().equals("hashCode")) {
      return fields.hashCode();
    }
    
    String fieldName = method.getName();
    
    if (method.getReturnType().equals(targetClass)) {
      if (!fields.containsKey(fieldName)) {
        fields.put(fieldName, args[0]);
        return proxy;
      } else {
        Map<String, Object> newFields = new HashMap<String, Object>();
        newFields.put(fieldName, args[0]);
        return ImmutableBuilder.of(new Immutable(targetClass, newFields));
      }
      
    } else {
      return fields.get(fieldName);
    }
  }
  
  public Class<?> getTargetClass() {
    return targetClass;
  }

}
