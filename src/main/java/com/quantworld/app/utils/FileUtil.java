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

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author YY
 * @version 1.0
 * @ClassName: FileUtil
 * @Description:
 * @date 2016年8月26日  上午10:26:17
 */
public class FileUtil {

  /**
   * 获取文件类型
   *
   * @param fileName
   * @return
   */
  public static String getFileExtName(String fileName) {
    if (fileName != null) {
      int i = fileName.lastIndexOf('.');
      if (i > -1) {
        return fileName.substring(i + 1).toLowerCase();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * 上传文件
   *
   * @param fileBytes
   * @param filePath
   * @param fileName
   * @throws Exception
   */
  public static void uploadFile(byte[] fileBytes, String filePath, String fileName) throws Exception {
    File targetFile = new File(filePath);
    if (!targetFile.exists()) {
      targetFile.mkdirs();
    }
    FileOutputStream out = new FileOutputStream(filePath + fileName);
    out.write(fileBytes);
    out.flush();
    out.close();
  }

}
