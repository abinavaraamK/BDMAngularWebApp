package com.hex.util;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HexBuildGenerator {

  private String isClassName = "HexBuildGenerator";
  String finalWarLoc = "";
  public HexBuildGenerator() {
  }
  
      
    public void generateBuildProperties(String lsPresentation,
      String lsBusiness, String lsPersistence, String outDirectory,
      String warFile, String templatesLocation, String baseLocation) {
    System.out
        .println("HexBuildGenerator ***********without deploy**************");
    String content = getBuildProperties(outDirectory, lsPresentation,
        lsBusiness, lsPersistence, warFile, templatesLocation);
    // String fileName = "HexFrameBuild.properties";
    String fileName = HexBuildGenerator.class.getClassLoader()
        .getResource("HexFrameBuild.properties").getFile();
    System.out.println(">>>>>>>"
        + HexBuildGenerator.class.getClassLoader()
            .getResource("HexFrameBuild.properties").getFile());

    HexUtil.writeFile(content, fileName);
    readPropertyFile(baseLocation);
    runAnt(baseLocation);

  }
  public String testFiles(File[] files){
    String finalWarLoc = "";
    if (files.length > 0) {
      for (File file : files) {
        if (file.isFile()) {
          System.out.println(file.getAbsolutePath());
          if(file.getAbsolutePath().contains(".war")){
            finalWarLoc = file.getAbsolutePath().toString();
            System.out.println(finalWarLoc+" war file location");
          }
        } else {
          testFiles(file.listFiles());
        }
      }
    } else {
      System.out.println("no files in the folder");
    }
    return finalWarLoc;
  }

  private void readPropertyFile(String baseLocation) {
    Properties prop = new Properties();
    InputStream input = null;

    File[] files = new File(baseLocation+"NewWebAppArchive").listFiles();
    testFiles(files);
    try {

      input = new FileInputStream(baseLocation+"WEB-INF/classes/HexFrameBuild.properties");

      // load a properties file
      prop.load(input);

            // get the property value and print it out
      System.out.println(prop.getProperty("PROJECT.BASE.DIR"));
      System.out.println(prop.getProperty("PROJECT.BLUWMIX.WAR"));
      System.out.println(prop.getProperty("PROJECT.WAR"));
      System.out.println(prop.getProperty("PROJECT.LIB"));
      System.out.println(prop.getProperty("PROJECT.LIB.DEST"));
      System.out.println(prop.getProperty("DEVELOP.SRC.JAVA.DIR"));
      System.out.println(prop.getProperty("PROJECT.BUILD"));

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  private String getBuildProperties(String outputPath, String psPresentation,
      String lsBusiness, String lsPersistence, String warFile,
      String templatesLocation) {

    System.out.println("Inside generateBuildProperties " + outputPath);
    System.out.println("Inside generateBuildProperties " + outputPath.replace('\\', '/'));
    System.out.println("Inside generateBuildProperties " + outputPath.replace("//", "/"));
    System.out.println("checking for location");
    System.out.println("Inside generateBuildProperties " + psPresentation);
    System.out.println("Inside generateBuildProperties " + lsBusiness);
    System.out.println("Inside generateBuildProperties " + lsPersistence);
    System.out.println("Inside generateBuildProperties " + warFile);

    StringBuffer result = new StringBuffer();
    try {
      DataInputStream dis = new DataInputStream(new FileInputStream(
          templatesLocation
              + "/templates/build/HexFrameBuild.template"));
      File file = new File(templatesLocation
          + "/Buildlib/xwork-2.1.2.jar");
      /*
       * InputStream inputStream= UIController.class.getResourceAsStream(
       * "\\templates\\templates\\build\\HexFrameBuild.template");
       * 
       * DataInputStream dis = new DataInputStream(inputStream);
       */

      // File file = new
      // File(UIController.class.getClassLoader().getResource("\\templates\\Buildlib\\xwork-2.1.2.jar").getFile());
      String BuildLib = file.getParent();
      System.out.println("$$$$$$$$$$$$$$$$$$$" + file.getParent());

      while (dis.available() > 0) {
        String line = dis.readLine();
        System.out.println("Line read " + line);

        int lineNo = line.indexOf("<OutDirectory>");

        if (line.indexOf("<OutDirectory>") >= 0) {
          outputPath = outputPath.replace('\\', '/');
          outputPath = outputPath.replace("//", "/");
          line = line.replaceAll("<OutDirectory>", outputPath);
        }

        if (line.indexOf("<blueMixOutDir>") >= 0) {
          outputPath = outputPath.replace('\\', '/');
          line = line.replaceAll("<blueMixOutDir>", outputPath.replace("/"+warFile, ""));
        }
        if (line.indexOf("<TableName>") >= 0) {
          line = line.replaceAll("<TableName>", warFile);
        }

        if ("IceFaces".equalsIgnoreCase(psPresentation)) {
          if (line.indexOf("<libdirectory>") >= 0) {
            line = line.replaceAll("<libdirectory>", "icefaceslib");
          }
        } else {
          if (line.indexOf("<libdirectory>") >= 0) {
            BuildLib = BuildLib.replace('\\', '/');

            line = line.replaceAll("<libdirectory>", BuildLib);
          }
        }
        /*
         * if("IceFaces".equalsIgnoreCase(psPresentation)){ if
         * (line.indexOf("<packagelibdirectory>") >= 0) { line =
         * line.replaceAll("<packagelibdirectory>", "icefaceslib"); } }
         * else if("Richfaces".equalsIgnoreCase(psPresentation)){ if
         * (line.indexOf("<packagelibdirectory>") >= 0) { line =
         * line.replaceAll("<packagelibdirectory>", "richfaceslib"); } }
         * else if("Primefaces".equalsIgnoreCase(psPresentation)){ if
         * (line.indexOf("<packagelibdirectory>") >= 0) { line =
         * line.replaceAll("<packagelibdirectory>", "Primefaceslib"); }
         * } else{ if (line.indexOf("<packagelibdirectory>") >= 0) {
         * line = line.replaceAll("<packagelibdirectory>", "lib"); } }
         */

        if (line.indexOf("<packagelibdirectory>") >= 0) {
          line = line.replaceAll("<packagelibdirectory>", "lib");
        }
        if (line.indexOf("<packagelibdirectoryPresentation>") >= 0) {
          line = line.replaceAll("<packagelibdirectoryPresentation>",
              psPresentation + "lib");
        }
        if (line.indexOf("<packagelibdirectoryBussiness>") >= 0) {
          line = line.replaceAll("<packagelibdirectoryBussiness>",
              lsBusiness + "lib");
        }
        if (line.indexOf("<packagelibdirectoryPersistence>") >= 0) {
          line = line.replaceAll("<packagelibdirectoryPersistence>",
              lsPersistence + "lib");
        }
        result.append(line);
        result.append("\n");
      }
      dis.close();
    } catch (Exception exception) {

      exception.printStackTrace();
    }

    return result.toString();

  }
  
  public static void main(String[] args) {
	  
  }

  private void runAnt(String baseLocation) {

    System.out.println("Inside runAnt ");
    System.out.println("HexBuildGenerator.class.getClassLoader().getResource " + HexBuildGenerator.class.getClassLoader().getResource(""));
    //File hexBuildfile = new File(HexBuildGenerator.class.getClassLoader().getResource("HexFrameBuild.xml").getFile());
    System.out.println("baseLocation "+baseLocation);
    String runAntPath = (baseLocation +"WEB-INF/classes/com/hex/util/runant.sh");
    System.out.println("runAntPath " + runAntPath);
    String hexBuildfile = (baseLocation+"WEB-INF/classes/HexFrameBuild.xml");
    System.out.println("hexBuildfile " + hexBuildfile);
    /*String cmd1 = "chmod +x " + runAntPath;
    System.out.println("cmd for running runant " + cmd1);
    String cmd2 = "chmod +x " + hexBuildfile;
    System.out.println("cmd for running runant " + cmd2);*/
    
    File buildFile = new File(hexBuildfile);
    Project project = new Project();
    project.setUserProperty("ant.file",buildFile.getAbsolutePath());
    DefaultLogger consoleLogger = new DefaultLogger();
    consoleLogger.setErrorPrintStream(System.err);
    consoleLogger.setOutputPrintStream(System.out);
    consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
    project.addBuildListener(consoleLogger);
    project.init();
    ProjectHelper helper = ProjectHelper.getProjectHelper();
    project.addReference("ant.projectHelper",helper);
    helper.parse(project, buildFile);
    project.executeTarget(project.getDefaultTarget());  
  }


/*
    System.out.println("Inside runAnt ");
    Runtime runtime = Runtime.getRuntime();
    File file = new File(HexBuildGenerator.class.getResource("runant.cmd").getFile());
    System.out.println("aaaaaaaaaaaaaaaaaaaaa"+file);
    File hexBuildfile = new File(HexBuildGenerator.class.getClassLoader().getResource("HexFrameBuild.xml").getFile());
    
    try {

    	Process p = runtime.exec(file.toString()+" all "+hexBuildfile );
      InputStream errorStream = p.getErrorStream();
      InputStream inputStream = p.getInputStream();
      readOutput(inputStream);
      readOutput(errorStream);

    }
    catch (IOException exec) {
      exec.printStackTrace();
    }

  }
*/

  private void runAntDeploy() {
	    System.out.println("Inside runAntDeploy ");
	    Runtime runtime = Runtime.getRuntime();

	    try {

	      Process p = runtime.exec("runant.cmd deploy");
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

}