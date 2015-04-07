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

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 6, 2014  
 */
public class ProxyClassTest extends TestCase {

  /**
   * @param args
   */
  public void test() throws Exception {
   IdentityEntity identity = ImmutableBuilder.of(IdentityEntity.class)
                                              .setId("identityId1")
                                              .setFirstName("mary")
                                              .setLastName("kelly");
    
    Person mark = ImmutableBuilder.of(Person.class)
                                  .setId("id1")
                                  .setName("mark")
                                  .setIdentity(identity);
    /** if the data is existing in Map, create new instance of John*/
    Person john = mark.setName("john").setAge(24);
    /** if the data is existing in Map, create new instance of mary*/
    Person mary = john.setName("mary");
    
    assertEquals("mark", mark.getName());
    assertEquals("id1", mark.getId());
    assertEquals("john", john.getName());
    assertEquals("mary", mary.getName());
    
    //JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl();
    //JsonValue jsonValue = jsonGenerator.createJsonObjectFromMap(mark.values());
    //System.out.println(jsonValue.toString());

  }

}
