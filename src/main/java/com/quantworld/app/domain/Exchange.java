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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "exchange")
public class Exchange extends Entitys implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @Column(nullable = false, length = 50)
  private String userName;

  @Column(nullable = false, length = 50)
  private String type;

  @Column(nullable = false, length = 50)
  private String exchange;

  @Column(nullable = false, length = 100)
  private String accessKey;

  @Column(nullable = false, length = 100)
  private String secretKey;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModifyTime;

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


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getLastModifyTime() {
    return lastModifyTime;
  }

  public void setLastModifyTime(Date lastModifyTime) {
    this.lastModifyTime = lastModifyTime;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public String toString() {
    return "Exchange{" +
        "id='" + id + '\'' +
        ", userName='" + userName + '\'' +
        ", type='" + type + '\'' +
        ", exchange='" + exchange + '\'' +
        ", accessKey='" + accessKey + '\'' +
        ", secretKey='" + secretKey + '\'' +
        ", createTime=" + createTime +
        ", lastModifyTime=" + lastModifyTime +
        '}';
  }
}
