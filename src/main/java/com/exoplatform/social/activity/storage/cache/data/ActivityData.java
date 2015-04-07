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
package com.exoplatform.social.activity.storage.cache.data;

import java.util.Map;

import com.exoplatform.social.activity.model.ExoSocialActivity;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 13, 2014  
 */
public class ActivityData {
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
  Map<String, String> templateParams;
  /** */
  DataStatus status;
  /** */
  boolean isComment = false;
  
  
  public ActivityData(ExoSocialActivity target, DataStatus status) {
    this.id = target.getId();
    this.title = target.getTitle();
    this.titleId = target.getTitleId();
    this.body = target.getBody();
    this.status = status;
    this.commenters = target.getCommenters();
    this.mentioners = target.getMentioners();
    this.likers = target.getLikers();
    this.lastUpdated = target.getLastUpdated();
    this.posterId = target.getPosterId();
    this.posterProviderId = target.getPosterProviderId();
  }
  
  public DataModel buildModel() {
    return new DataModel(id, parentId);
  }
  
  public ExoSocialActivity build() {
    ExoSocialActivity activity = new ExoSocialActivity();
    activity.setId(this.id);
    activity.setTitle(this.title);
    activity.setTitleId(this.titleId);
    activity.setBody(this.body);
    activity.setCommenters(this.commenters);
    activity.setMentioners(this.mentioners);
    activity.setLikers(this.likers);
    activity.setLastUpdated(this.lastUpdated);
    activity.setPosterId(this.posterId);
    activity.setPosterProviderId(this.posterProviderId);
    return activity;
  }

  public DataStatus getStatus() {
    return status;
  }



  public void setStatus(DataStatus status) {
    this.status = status;
  }



  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public String getParentId() {
    return parentId;
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

  public String getPosterId() {
    return posterId;
  }

  public void setPosterId(String posterId) {
    this.posterId = posterId;
  }

  public String[] getMentioners() {
    return mentioners;
  }

  public void setMentioners(String[] mentioners) {
    this.mentioners = mentioners;
  }

  public String[] getCommenters() {
    return commenters;
  }

  public void setCommenters(String[] commenters) {
    this.commenters = commenters;
  }

  public String[] getLikers() {
    return likers;
  }

  public void setLikers(String[] likers) {
    this.likers = likers;
  }
  
  public long getLastUpdated() {
    return lastUpdated;
  }

  @Override
  public String toString() {
    return "ActivityData[id = " + this.id +", status = " + this.status.toString() + "]";
  }
}
