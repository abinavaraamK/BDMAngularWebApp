package com.hex.vo;

import java.util.HashMap;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TemplateVO {
  public TemplateVO() {
  }
  private String templateDirectory;
  private String templateFileName;
  private String outputFileName;
  private String outputDirectory;

  private HashMap dynamicContent;


  public void setTemplateDirectory(String templateDirectory) {
   this.templateDirectory = templateDirectory;
 }

 public String getTemplateDirectory() {
   return templateDirectory;
 }


 public void setOutputDirectory(String directory) {
   this.outputDirectory = directory;
 }

 public String getOutputDirectory() {
   return outputDirectory;
 }

 public void setTemplateFileName(String templateFileName) {
  this.templateFileName = templateFileName;
}

public String getTemplateFileName() {
  return templateFileName;
}

public void setOutputFileName(String outputFileName) {
  this.outputFileName = outputFileName;
}

public String getOutputFileName() {
  return outputFileName;
}

public void setDynamicContent(HashMap dynamicContent) {
  this.dynamicContent = dynamicContent;
}

public HashMap getDynamicContent() {
  return dynamicContent;
}

public String toString(){
System.out.println("##########@@@@@@@@@@@@@@@@@@@@@@@@@@@@@!!!!!!!!!!!!!!!!^^^^^^^^^^^^^^^^^^"+this.getOutputDirectory()+":"+this.getOutputFileName()+":"+this.templateDirectory+":"+this.templateFileName+":"+this.getDynamicContent()+"$$$");
  return this.getOutputDirectory()+":"+this.getOutputFileName()+":"+this.templateDirectory+":"+this.templateFileName+":"+this.getDynamicContent()+"$$$";
  
}



}