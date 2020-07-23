package com.quantworld.app.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "asset")
public class Asset extends Entitys implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @Column(nullable = false, unique = true, length = 50)
  private String userName;

  @Column(nullable = false, length = 50)
  private String exChangeName;

  @Column(nullable = true, length = 200)
  private String accessKey;

  @Column(nullable = true, length = 200)
  private String secretKey;

  @Column(nullable = true, length = 50)
  private String assetPassword;

  @Column(nullable = false, insertable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;

  @Column(nullable = false, insertable = false, updatable = false)
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

  public String getExChangeName() {
    return exChangeName;
  }

  public void setExChangeName(String exChangeName) {
    this.exChangeName = exChangeName;
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

  public String getAssetPassword() {
    return assetPassword;
  }

  public void setAssetPassword(String assetPassword) {
    this.assetPassword = assetPassword;
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
}
