package com.hex.util;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.hex.UIController;

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
  public HexBuildGenerator() {
  }
  
      
   public void generateBuildProperties(String lsPresentation,String lsBusiness,String lsPersistence,String outDirectory,String warFile,String templatesLocation) {
System.out.println("HexBuildGenerator ***********without deploy**************");
 String content = getBuildProperties(outDirectory ,lsPresentation,lsBusiness,lsPersistence,warFile,templatesLocation);
 //String fileName = "HexFrameBuild.properties";
   String fileName = HexBuildGenerator.class.getClassLoader().getResource("HexFrameBuild.properties").getFile();
 System.out.println(">>>>>>>"+HexBuildGenerator.class.getClassLoader().getResource("HexFrameBuild.properties").getFile());

   HexUtil.writeFile(content, fileName);
    runAnt();

  }

   public void generateBuildProperties(String lsPresentation,String lsBusiness,String lsPersistence,String outDirectory,String warFile,String deploy,String templatesLocation) {
	   System.out.println("HexBuildGenerator ***********with deploy**************");
	    String content = getBuildProperties(outDirectory ,lsPresentation,lsBusiness,lsPersistence,warFile,templatesLocation);
	    String fileName = "HexFrameBuild.properties";
	    
	    HexUtil.writeFile(content, fileName);
	    
	    
	    
	    runAntDeploy();

	  }
   
  private String getBuildProperties(String outputPath, String psPresentation,String lsBusiness,String lsPersistence,String warFile,String templatesLocation) {

    System.out.println("Inside generateBuildProperties " + outputPath);
    System.out.println("Inside generateBuildProperties " + psPresentation);
    System.out.println("Inside generateBuildProperties " + lsBusiness);
    System.out.println("Inside generateBuildProperties " + lsPersistence);
    System.out.println("Inside generateBuildProperties " + warFile);

    StringBuffer result = new StringBuffer();
    try {
    	DataInputStream dis = new DataInputStream(new FileInputStream(templatesLocation+
                "/templates/build/HexFrameBuild.template"));
    	File file = new File(templatesLocation+"/Buildlib/xwork-2.1.2.jar");
    	  /* InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\build\\HexFrameBuild.template");
  		 
   		DataInputStream dis = new DataInputStream(inputStream);   */
   	
   		//File file = new File(UIController.class.getClassLoader().getResource("\\templates\\Buildlib\\xwork-2.1.2.jar").getFile());
   		String BuildLib= file.getParent();
   		System.out.println("$$$$$$$$$$$$$$$$$$$"+file.getParent());
   	
      while (dis.available() > 0) {
        String line = dis.readLine();
        System.out.println("Line read " + line);

        int lineNo = line.indexOf("<OutDirectory>");

        if (line.indexOf("<OutDirectory>") >= 0) {
          outputPath = outputPath.replace('\\', '/');
          line = line.replaceAll("<OutDirectory>", outputPath);
        }

        if (line.indexOf("<TableName>") >= 0) {
          line = line.replaceAll("<TableName>", warFile);
        }

       if("IceFaces".equalsIgnoreCase(psPresentation)){
	        if (line.indexOf("<libdirectory>") >= 0) {
	              line = line.replaceAll("<libdirectory>", "icefaceslib");
	        }
        }
        else{
              if (line.indexOf("<libdirectory>") >= 0) {
            	  BuildLib = BuildLib.replace('\\', '/');
                  
            	  line = line.replaceAll("<libdirectory>", BuildLib);
              }
        }
        /*
        if("IceFaces".equalsIgnoreCase(psPresentation)){
	        if (line.indexOf("<packagelibdirectory>") >= 0) {
	              line = line.replaceAll("<packagelibdirectory>", "icefaceslib");
	        }
        }
        else if("Richfaces".equalsIgnoreCase(psPresentation)){
            if (line.indexOf("<packagelibdirectory>") >= 0) {
                  line = line.replaceAll("<packagelibdirectory>", "richfaceslib");
            }
        }
        else if("Primefaces".equalsIgnoreCase(psPresentation)){
            if (line.indexOf("<packagelibdirectory>") >= 0) {
                  line = line.replaceAll("<packagelibdirectory>", "Primefaceslib");
            }
        }
        else{
              if (line.indexOf("<packagelibdirectory>") >= 0) {
            	  line = line.replaceAll("<packagelibdirectory>", "lib");
              }
        }        
*/
        
        if (line.indexOf("<packagelibdirectory>") >= 0) {
            line = line.replaceAll("<packagelibdirectory>","lib");
      }
        if (line.indexOf("<packagelibdirectoryPresentation>") >= 0) {
            line = line.replaceAll("<packagelibdirectoryPresentation>",psPresentation+"lib");
      }
        if (line.indexOf("<packagelibdirectoryBussiness>") >= 0) {
            line = line.replaceAll("<packagelibdirectoryBussiness>",lsBusiness+"lib");
      }
        if (line.indexOf("<packagelibdirectoryPersistence>") >= 0) {
            line = line.replaceAll("<packagelibdirectoryPersistence>",lsPersistence+"lib");
      }
        result.append(line);
        result.append("\n");
      }
      dis.close();
    }
    catch (Exception exception) {

      exception.printStackTrace();
    }

    return result.toString();

  }
  
  public static void main(String[] args) {
	
	  System.out.println("Inside runAnt ");
	    Runtime runtime = Runtime.getRuntime();

	    File hexBuildfile = new File(HexBuildGenerator.class.getClassLoader().getResource("HexFrameBuild.xml").getFile());
	    try {
	    	System.out.println(hexBuildfile.getParent());
	    
	      Process p = runtime.exec(HexBuildGenerator.class.getResource("runant.cmd").getFile().toString()+" all "+hexBuildfile);
	      
	      InputStream errorStream = p.getErrorStream();
	      InputStream inputStream = p.getInputStream();
	      new HexBuildGenerator().readOutput(inputStream);
	      new HexBuildGenerator(). readOutput(errorStream);

	    }
	    catch (IOException exec) {
	      exec.printStackTrace();
	    }

	  
}

  private void runAnt() {
    System.out.println("Inside runAnt ");
    Runtime runtime = Runtime.getRuntime();
    File file = new File(HexBuildGenerator.class.getResource("runant.cmd").getFile());
    System.out.println("aaaaaaaaaaaaaaaaaaaaa"+file);
    File hexBuildfile = new File(HexBuildGenerator.class.getClassLoader().getResource("HexFrameBuild.xml").getFile());
    
    try {
      Process p = runtime.exec("sudo chmod 777 -R "+ HexBuildGenerator.class.getClassLoader().getResource(""));
      p.waitFor();
    	p = runtime.exec("sudo "+file.toString()+" all "+hexBuildfile );
      InputStream errorStream = p.getErrorStream();
      InputStream inputStream = p.getInputStream();
      readOutput(inputStream);
      readOutput(errorStream);

    }
    catch (IOException exec) {
      exec.printStackTrace();
    }

  }
  
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