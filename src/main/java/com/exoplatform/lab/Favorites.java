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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 11, 2014  
 */
class Favorites {
  private Map<TypeReference<?>, Object> favorites = new HashMap<TypeReference<?>, Object>();

  public <T> void setFavorite(TypeReference<T> typeRef, T thing) {
    favorites.put(typeRef, thing);
  }

  @SuppressWarnings("unchecked")
  public <T> T getFavorite(TypeReference<T> typeRef) {
    return (T) favorites.get(typeRef);
  }

}