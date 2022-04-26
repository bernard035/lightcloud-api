package com.rollking.lightcloud.utils;

import java.util.Random;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-12
 * */
public class StrUtils {
  /**
   * @param length 长度
   * @return 生成的随机字符串
   */
  public static String randStr(int length) {
    String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      builder.append(base.charAt(number));
    }
    return builder.toString();
  }

  /**
   * @param url 需要切分的URL字符串
   * @return 返回URL以‘/'分隔的结果
   */
  public static String[] urlSplit(String url) {
    return url.split(String.valueOf('/'));
  }

  /**
   * @param url 需要处理的URL
   * @return 文件名和文件后缀
   */
  public static String urlLastName(String url) {
    return url.substring(url.lastIndexOf('/') + 1);
  }

  /**
   * @param fileName 文件名或URL
   * @return 文件类型
   */
  public static String getFileType(String fileName) {
    String tail = fileName.substring(fileName.lastIndexOf('.') + 1);
    switch (tail) {
      case "jpg":
      case "png":
      case "gif":
        return "photo";
      case "mp3":
      case "flac":
      case "ape":
        return "music";
      case "mp4":
      case "avi":
      case "mpg":
        return "video";
      case "txt":
      case "md":
      case "html":
        return "text";
      case "doc":
      case "docx":
        return "doc";
      case "xls":
      case "xlsx":
        return "xls";
      case "ppt":
      case "pptx":
        return "ppt";
      default:
        return "file";
    }
  }

  /**
   * @param gloPath 全局路径
   * @return 用户路径
   */
  public static String gloPath2userPath(String gloPath) {
    return gloPath.substring(idxId(gloPath));
  }

  /**
   * @param path 全局路径
   * @return 用户域在串中的下标
   */
  public static int idxId(String path) {
    String[] pieces = urlSplit(path);
    int idx = -1;
    for (String s : pieces) {
      idx += s.length() + 1;
      if (s.matches("[0-9]+")) break;
    }
    assert idx != -1; // 如果 -1 说明传入路径没有数字域
    return idx;
  }
}
