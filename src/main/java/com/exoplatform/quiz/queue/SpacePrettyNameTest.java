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
package com.exoplatform.quiz.queue;

import java.io.StringWriter;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * May 7, 2014  
 */
public class SpacePrettyNameTest {
  
  public static void main(String[] args) throws Exception {
    String test = convertSpecialCharacters("õspace1");
    System.out.println(test);
  }
  
  public static String convertSpecialCharacters(String text) {
    if (text == null) return null;
    final char SPECIAL_CHARACTERS = 'õ';
    final char LATIN_CHARACTERS = 'i';
    
    StringWriter writer = new StringWriter(text.length());
    for (int i = 0; i < text.length(); i++) {
      char currentCharacter = text.charAt(i);
      if (currentCharacter == SPECIAL_CHARACTERS) {
        writer.write(LATIN_CHARACTERS);
      } else {
        writer.write(currentCharacter);
      }
    }
    
    return writer.toString();
  }

}
