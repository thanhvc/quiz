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
package com.exoplatform.social.iversion.object;

import static com.exoplatform.iversion.object.PropertyPath.ROOT;
import junit.framework.TestCase;

import com.exoplatform.iversion.object.PropertyPath;

public class PropertyPathTest extends TestCase {

  public void testPathEquals() {
    assertEquals(children_0(), children_0());
    assertNotSame(children_0(), children_0_name());
    assertNotSame(_0, _1);
  }
  
  public void testSchemaPath() {
    assertEquals("children[].name", children_0_name().toSchemaPath().toString());
    assertEquals(_0.toSchemaPath(), _1.toSchemaPath());
  }

  public void testHashCode() {
    assertEquals(children_0().hashCode(), children_0().hashCode());
    assertTrue(children_0().hashCode() != children_0_name().hashCode());
  }

  public void testToString() {
    assertEquals(_0.toString(), "[0]");
    assertEquals(children_0().toString(), "children[0]");
    assertEquals(children_0_name().toString(), "children[0].name");
  }

  public void tesNestedIndexes() {
    assertEquals(_1_0.toString(), "[1][0]");
  }

  public void testPeculiarIndex() {
    assertEquals(ROOT.index("index containing \\[\\]..").toString(),
                 "[index containing \\\\[\\\\\\]\\.\\.]");
  }

  public void testPeculiarProperty() {
    assertEquals(ROOT.property("property containing \\[\\].").toString(),
                 "property containing \\\\[\\\\\\]\\.");
  }

  public static PropertyPath _0   = ROOT.index("0");

  public static PropertyPath _1   = ROOT.index("1");

  public static PropertyPath _1_0 = _1.index("0");

  public static PropertyPath children() {
    return ROOT.property("children");
  }

  public static PropertyPath children_0() {
    return children().index("0");
  }

  public static PropertyPath children_0_name() {
    return children_0().property("name");
  }

}