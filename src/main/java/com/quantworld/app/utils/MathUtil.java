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
package com.quantworld.app.utils;

/**
 * @author: Shawn
 * @Date: 12/25/2019
 * @Description: Some util method for calculating
 */
public class MathUtil {

  /**
   * Get floor number according the precision
   *
   * @param number
   * @param precision
   * @return
   */
  public static float getFloorNumberWithPrecision(float number, int precision) {
    return (float) (Math.floor(number * ((int) Math.pow(10, precision))) / (((int) Math.pow(10, precision)) * 1.0f));
  }

  public static float getCeilNumberWithPrecision(float number, int precision) {
    return (float) (Math.ceil(number * ((int) Math.pow(10, precision))) / (((int) Math.pow(10, precision)) * 1.0f));
  }

  /**
   * @param number: 价格
   * @param precision： 精度
   * @return： 向上合并后的价格
   */
  public static float getCeilByLastNumber(float number, int precision) {
    // 精度小于0，直接返回， 不做精度计算
    if (precision < 0) {
      return number;
    }
    // 计算小数点后有几位
    int numberLengthAfterPoint = 0;
    // 临时变量
    float tempNum = 0;
    if (number > 0) {
      // 这一步的目的是为了方式0.0015这种数， 会出现1.5E-3这种科学计数法
      tempNum = number + 1;
    } else {
      // 同上
      tempNum = number - 1;
    }
    // 获取number的字符串表达
    String numStr = tempNum + "";
    // 以小数点分割，第二个数组元素即小数点后的数字
    String[] numStrArray = numStr.split("\\.");
    // 得到小数点后数字的长度
    numberLengthAfterPoint = numStrArray[1].length();
    if (numberLengthAfterPoint >= precision) {
      // 向上取数
      return getCeilNumberWithPrecision(number, numberLengthAfterPoint - precision + 1);
    } else {
      return number;
    }
  }

  /**
   *
   * @param number: 价格
   * @param precision： 精度
   * @return： 向下合并后的价格
   */
  public static float getFloorByLastNumber(float number, int precision) {
    if (precision < 0) {
      return number;
    }
    int numberLengthAfterPoint = 0;
    float tempNum = 0;
    if (number > 0) {
      tempNum = number + 1;
    } else {
      tempNum = number - 1;
    }
    String numStr = tempNum + "";
    String[] numStrArray = numStr.split("\\.");
    numberLengthAfterPoint = numStrArray[1].length();
    if (numberLengthAfterPoint >= precision) {
      return getFloorNumberWithPrecision(number, numberLengthAfterPoint - precision + 1);
    } else {
      return number;
    }
  }
}
