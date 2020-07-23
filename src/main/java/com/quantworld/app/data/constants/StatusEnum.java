package com.quantworld.app.data.constants;

/**
 * @author: Shawn
 * @Date: 10/17/2019
 * @Description:
 */
public enum StatusEnum {

  NONE("NONE"),
  SUBMITTING("提交中"),      // active
  NOTTRADED("未成交"),       // active
  PARTTRADED("部分成交"),    // active
  CANCELLING("撤单中"),      // active
  ALLTRADED("全部成交"),
  PARTCANCELED("部分撤单"),   // active
  CANCELED("已撤销"),
  REJECTED("拒单"),
  CLOSED("订单关闭");
  String value;

  StatusEnum(String value) {
    this.value = value;
  }
}
