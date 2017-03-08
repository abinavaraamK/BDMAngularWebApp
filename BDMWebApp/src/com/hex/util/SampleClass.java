package com.hex.util;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.io.InputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SampleClass {

  private String empid;

  public SampleClass() {
    System.out.println("Emp Name " + empid);
  }

  public static void main(String[] args) {

    Integer integer = null;
    String str = "12";
    integer = Integer.valueOf(str);

    System.out.println("Integer " + integer);

    SampleClass sampleClass1 = new SampleClass();

    StringBuffer buffer = new StringBuffer();
    buffer.append("abc");
    buffer.append("\\");
    System.out.println("Str " + buffer.toString());
    //sampleClass1.runAnt();


  }

  private void runAnt() {
    String empName;
    int k;
    System.out.println("Inside runAnt ");
    Runtime runtime = Runtime.getRuntime();

    try {

      //Process p = runtime.exec("D:\\HexFrame\\src\\runant.cmd");
      Process p = runtime.exec("D:\\BaseDataManager\\BDM-With updated templates Run Time\\runant.cmd");
      InputStream errorStream = p.getErrorStream();
      InputStream inputStream = p.getInputStream();
      readOutput(inputStream);
      readOutput(errorStream);

    }
    catch (IOException exec) {
      exec.printStackTrace();
    }

  }

  private void readOutput(InputStream inputStream) {
  BufferedReader reader = new BufferedReader(new InputStreamReader(
      inputStream));
  String line = null;

  try {
    while ( (line = reader.readLine()) != null) {
      System.out.println("Output " + line);
    }
  }
  catch (Exception ex) {
    ex.printStackTrace();
  }
}


//  private void readOutput(InputStream inputStream) {
//    DataInputStream buf = new DataInputStream(inputStream);
//
//    try {
//      while (buf.available() > 0) {
//        String str = buf.readLine();
//        System.out.println("Output " + str);
//      }
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }
//  }

}