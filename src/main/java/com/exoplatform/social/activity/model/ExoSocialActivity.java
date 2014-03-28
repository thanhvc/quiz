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
package com.exoplatform.social.activity.model;

import java.io.Serializable;
import java.util.Map;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 12, 2014  
 */
@SuppressWarnings("serial")
public class ExoSocialActivity implements Serializable {
  /** */
  String id;
  /** */
  String parentId;
  /** */
  String titleId;
  /** */
  String title;
  /** */
  String body;
  /** */
  String posterId;
  /** */
  String posterProviderId;
  /** */
  String[] mentioners;
  /** */
  String[] commenters;
  /** */
  String[] likers;
  /** */
  long lastUpdated;
  /** */
  boolean isComment = false;
  
  Map<String, String> templateParams;
  
  public ExoSocialActivity() {
    this.id = Long.toString(System.currentTimeMillis());
    this.lastUpdated = System.currentTimeMillis();
  }

  public void setId(String id) {
    this.id = id;
  }

  public String[] getCommenters() {
    return commenters == null ? new String[0] : commenters;
  }

  public void setCommenters(String[] commenters) {
    this.commenters = commenters;
  }



  public String getPosterId() {
    return posterId;
  }



  public void setPosterId(String posterId) {
    this.posterId = posterId;
  }
  
  public String getPosterProviderId() {
    return posterProviderId;
  }

  public void setPosterProviderId(String posterProviderId) {
    this.posterProviderId = posterProviderId;
  }

  public String[] getMentioners() {
    return mentioners;
  }



  public void setMentioners(String[] mentioners) {
    this.mentioners = mentioners;
  }



  public String[] getLikers() {
    return likers;
  }

  public void setLikers(String[] likers) {
    this.likers = likers;
  }

  public String getId() {
    return id;
  }

  public String getTitleId() {
    return titleId;
  }

  public void setTitleId(String titleId) {
    this.titleId = titleId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Map<String, String> getTemplateParams() {
    return templateParams;
  }

  public void setTemplateParams(Map<String, String> templateParams) {
    this.templateParams = templateParams;
  }

  public boolean isComment() {
    return isComment;
  }

  public void setComment(boolean isComment) {
    this.isComment = isComment;
  }
  
  public long getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
  
  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return "ExoSocialActivity[id:" + this.id + ", title: " + this.title +" ]";
  }
  
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExoSocialActivity)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    ExoSocialActivity that = (ExoSocialActivity) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }
  
  
 
}
