package com.quantworld.app.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

abstract class BaseData implements Serializable {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  String gatewayName;

  public String getGatewayName() {
    return gatewayName;
  }

  public void setGatewayName(String gatewayName) {
    this.gatewayName = gatewayName;
  }

  public Object deepClone() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Object copyObject = null;
    try {
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(this);
      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bis);
      copyObject = ois.readObject();
    } catch (Exception e) {
      logger.error("Can not clone current object", e);
    }
    return copyObject;
  }
}
