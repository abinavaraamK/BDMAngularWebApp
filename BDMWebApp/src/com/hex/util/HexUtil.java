package com.hex.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import com.hex.vo.TableVO;

/*
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author not attributable
 * @version 1.8
 */

public class HexUtil {
      public HexUtil() {
      }

      public static void writeFile(String content, String fileName) {   
            try {
                  FileOutputStream out = new FileOutputStream(fileName);
                  out.write(content.getBytes("ISO8859_1"));
                  out.flush();
                  out.close();
            }
            catch (IOException exception) {
                  exception.printStackTrace();
            }
      }

      public void writeFile(String content, String directory, String fileName) {
            try {
                  File woFile = new File(directory);

                  if (!woFile.isDirectory()) {
                        woFile.mkdir();
                  }

                  fileName = directory + "\\" + fileName;
                  FileOutputStream out = new FileOutputStream(fileName);

                  out.write(content.getBytes("ISO8859_1"));
                  out.flush();
                  out.close();
            }
            catch (IOException exception) {
                  exception.printStackTrace();
            }
      }

      public static void makeDirectory(String outDirectory) {

            File file = new File(outDirectory);
            if (file.isDirectory() == false) {
                  file.mkdirs();
            }

      }

      public static String initCap(String ps) {
            ps = ps.substring(0, 1).toUpperCase() + ps.substring(1).toLowerCase();
            return ps;
      }

      public static String initSmall(String name) {
            name = name.substring(0, 1).toLowerCase() + name.substring(1).toLowerCase();
            return name;
      }

      public static String replaceTags(String psSource, String psTag, String psVar) {
            if (psSource.indexOf(psTag) >= 0) {
                  psSource = psSource.replaceAll(psTag, psVar);
            }
            return psSource;
      }

      public static String getDataType(TableVO tableVO) {
            String lsDataType = tableVO.getDataType();
            if (lsDataType.equalsIgnoreCase("NUMBER") ||
                lsDataType.equalsIgnoreCase("NUMERIC")) {
                  if (tableVO.getDataScale() == null ||
                      tableVO.getDataScale().intValue() == 0) {
                        return "Integer";
                  }
                  else {
                        return "Double";
                  }
            }
            if (lsDataType.equalsIgnoreCase("INT") ||
                lsDataType.equalsIgnoreCase("INTEGER") ||
                lsDataType.equalsIgnoreCase("LONG")) {
                  return "Integer";
            }
            if (lsDataType.equalsIgnoreCase("FLOAT") ||
                lsDataType.equalsIgnoreCase("DOUBLE")) {
                  return "Double";
            }

            if (lsDataType.equalsIgnoreCase("VARCHAR2") ||
                lsDataType.equalsIgnoreCase("VARCHAR") ||
                lsDataType.equalsIgnoreCase("CHAR") ||
                lsDataType.equalsIgnoreCase("NCHAR") ||
                lsDataType.equalsIgnoreCase("NVARCHAR2") ||
                lsDataType.equalsIgnoreCase("STRING")) {
                  return "String";
            }
            if (lsDataType.equalsIgnoreCase("DATE") ||
                lsDataType.equalsIgnoreCase("DATETIME")) {
                  return "Date";
            }
            return "String";
      }

      public static String getJavaDataType(TableVO tableVO) {
            String lsDataType = tableVO.getDataType();
            if (lsDataType.equalsIgnoreCase("NUMBER")) {

                  if (tableVO.getDataScale() == null ||
                      tableVO.getDataScale().intValue() == 0) {
                        return "java.lang.Integer";
                  }
                  else {
                        return "java.lang.Double";
                  }
            }
            if (lsDataType.equalsIgnoreCase("INT UNSIGNED") ||lsDataType.equalsIgnoreCase("INT") ||
                lsDataType.equalsIgnoreCase("INTEGER") ||
                lsDataType.equalsIgnoreCase("LONG")) {
                  return "java.lang.Integer";
            }
            if (lsDataType.equalsIgnoreCase("FLOAT") ||
                lsDataType.equalsIgnoreCase("DOUBLE")) {
                  return "java.lang.Double";
            }
            if (lsDataType.equalsIgnoreCase("VARCHAR2") ||
                lsDataType.equalsIgnoreCase("VARCHAR") ||
                lsDataType.equalsIgnoreCase("CHAR") ||
                lsDataType.equalsIgnoreCase("NCHAR") ||
                lsDataType.equalsIgnoreCase("NVARCHAR2") ||
                lsDataType.equalsIgnoreCase("STRING")) {
                  return "java.lang.String";
            }
            if (lsDataType.equalsIgnoreCase("DATE") ||
                lsDataType.equalsIgnoreCase("DATETIME")) {
                  return "java.util.Date";
            }
            return "java.lang.String";
      }

}