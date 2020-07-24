/*
 * Copyright 2019-2020 Shawn Peng
 * Email: shawnpeng@yeah.net
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quantworld.app.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User extends Entitys implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;

  @Column(nullable = false, unique = true, length = 50)
  private String userName;

  @Column(nullable = false, length = 50)
  private String passWord;

  @Column(nullable = false, unique = true, length = 50)
  private String email;

  @Column(nullable = false)
  private Long createTime;

  @Column(nullable = false)
  private Long lastModifyTime;

  @Column(nullable = true, length = 50)
  private String profilePicture;

  @Column(nullable = true,length = 65535,columnDefinition="Text")
  private String introduction;

  @Column(nullable = true, length = 50)
  private String validataCode;

  @Column(nullable = true, length = 50)
  private String backgroundPicture;

  public User() {
    super();
  }

  public User(String email, String passWord, String userName) {
    super();
    this.email = email;
    this.passWord = passWord;
    this.userName = userName;
  }

  @Id
  @GenericGenerator(name = "idGenerator", strategy = "uuid")
  @GeneratedValue(generator = "idGenerator")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public String getValidataCode() {
    return validataCode;
  }

  public void setValidataCode(String validataCode) {
    this.validataCode = validataCode;
  }

  public String getBackgroundPicture() {
    return backgroundPicture;
  }

  public void setBackgroundPicture(String backgroundPicture) {
    this.backgroundPicture = backgroundPicture;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getLastModifyTime() {
    return lastModifyTime;
  }

  public void setLastModifyTime(Long lastModifyTime) {
    this.lastModifyTime = lastModifyTime;
  }
}
