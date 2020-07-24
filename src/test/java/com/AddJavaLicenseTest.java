package com;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.Test;

/**
 * License复制工具
 *
 * @author jianggujin
 *
 */
public class AddJavaLicenseTest implements FileFilter {
  /**
   * 读取License文件
   *
   * @param in
   * @param charset
   * @return
   * @throws IOException
   */
  public String readLicenseHeader(InputStream in, String charset) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
    StringBuilder builder = new StringBuilder("/*\r\n");
    String line = null;
    while ((line = reader.readLine()) != null) {
      builder.append(" * ");
      builder.append(line);
      builder.append("\r\n");
    }
    builder.append(" */");
    return builder.toString();
  }

  /**
   * 处理license头部信息
   *
   * @param root
   * @param in
   * @param charset
   * @throws IOException
   */
  public void processLicenseHeader(File root, InputStream in, String charset) throws IOException {
    System.out.println("开始读取并格式化license...");
    String headerBody = readLicenseHeader(in, charset);
    System.out.println(headerBody);
    System.out.println("读取并格式化license完成...");
    if (root.isDirectory() || root.getName().endsWith(".java")) {
      System.out.println("开始处理：" + root.getAbsolutePath());
      processLicenseHeader(root, charset, headerBody);
    }
  }

  private void processLicenseHeader(File root, String charset, String headerBody) throws IOException {
    if (root.isFile()) {
      System.out.println("开始读取并处理：" + root.getAbsolutePath());
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(root), charset));
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, charset));
      writer.write(headerBody);
      writer.write("\r\n");
      String line = null;
      boolean body = false;
      while ((line = reader.readLine()) != null) {
        if (body || line.startsWith("package ") || line.startsWith("import ")) {
          body = true;
          writer.write(line);
          writer.write("\r\n");
        }
      }
      reader.close();
      writer.close();
      FileOutputStream out = new FileOutputStream(root);
      stream.writeTo(out);
      out.flush();
      out.close();
      System.out.println("读取并处理[" + root.getAbsolutePath() + "]完成");
    } else {
      File[] list = root.listFiles(this);
      if (list != null)
        for (File file : list) {
          processLicenseHeader(file, charset, headerBody);
        }
    }
  }

  @Override
  public boolean accept(File file) {
    return file.isDirectory() || file.getName().endsWith(".java");
  }

  @Test
  public void test() throws IOException {
    processLicenseHeader(new File("src/main/java"), new FileInputStream("src/main/resources/license.txt"), "UTF-8");
  }
}
