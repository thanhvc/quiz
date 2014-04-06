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

import java.lang.reflect.Proxy;
import java.util.HashMap;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 6, 2014  
 */
public class ImmutableBuilder {

  @SuppressWarnings("unchecked")
  static <T> T of(Immutable immutable) {
    Class<?> targetClass = immutable.getTargetClass();
    //1. loader - the class loader to define the proxy class
    //2. the list of interfaces for the proxy class to implement
    //3. the invocation handler to dispatch method invocations to
    return (T) Proxy.newProxyInstance(targetClass.getClassLoader(),
                                      new Class<?>[]{targetClass}, immutable);
    
  }
  
  /**
   * Return instance of proxy class for specified interface class
   * and dispatch method invocation to the specified invocation handler
   * @param aClass
   * @return
   */
  public static <T> T of(Class<T> aClass) {
    return of(new Immutable(aClass, new HashMap<String, Object>()));
  }
}
