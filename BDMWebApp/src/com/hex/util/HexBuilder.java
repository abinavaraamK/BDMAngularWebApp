package com.hex.util;
import java.util.*;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 */

public class HexBuilder {
  public HexBuilder() {
  }
  public void build(HashMap poMap)
  {
    Properties loProp = new Properties();
    try {
      FileInputStream fis = new FileInputStream("D:/HexFrame/HexBuilder.properties");
      loProp.load(fis);
      fis.close();

      buildConfig(poMap, loProp);

    }
    catch( Exception exp ) {
      exp.printStackTrace();
    }
  }

  private void buildConfig( HashMap poMap , Properties poProp)
  {
    StringBuffer loConfigfile = new StringBuffer();
    String lsProjectName = (String)poMap.get("PROJECTNAME");
    String lsProjectPath = (String)poMap.get("PROJECTPATH");
    String lsUserName = (String)poMap.get("USERNAME");
    String lsPassword = (String)poMap.get("PASSWORD");
    String lsVssServerPath = (String)poMap.get("VSSSERVERPATH");
    String lsVssProjectPath = (String)poMap.get("VSSPROJECTPATH");
    String lsBuildScriptPath = (String)poMap.get("BUILDSCRIPTPATH");
    String lsBootStrapper = (String)poMap.get("BOOTSTRAPPER");
    String lsQuietPeriod = (String)poMap.get("QUIETPERIOD");
    String lsInterval = (String)poMap.get("SCHEDULEINTERVAL");

    try {
      String lsCruiseControlPath = (String)poProp.get("CruiseControlPath");
      String lsStatusFileName = (String)poProp.get("StatusFileName");
      String lsSMTPMailHost = (String)poProp.get("SMTPMailHost");
      String lsSuccessEmailAddress = (String)poProp.get("SuccessEmailAddress");
      String lsFailureEmailAddress = (String)poProp.get("FailureEmailAddress");
      String lsReturnEmailAddress = (String)poProp.get("ReturnEmailAddress");
      String lsBuildResultURL = (String)poProp.get("BuildResultURL");
      DataInputStream dis = new DataInputStream(new FileInputStream(
          "templates\\Config.template"));
      while (dis.available() > 0) {
        String lsLine = dis.readLine();
        lsLine = replaceTags(lsLine,"<ProjectName>",lsProjectName);
        lsLine = replaceTags(lsLine,"<LocalProjectPath>",lsProjectPath+"\\"+lsProjectName);
        lsLine = replaceTags(lsLine,"<UserName>",lsUserName);
        lsLine = replaceTags(lsLine,"<Password>",lsPassword);
        lsLine = replaceTags(lsLine,"<VSSPath>",lsVssServerPath);
        lsLine = replaceTags(lsLine,"<VSSProjectPath>",lsVssProjectPath);
        lsLine = replaceTags(lsLine,"<AntPath>",lsBuildScriptPath);
        lsLine = replaceTags(lsLine,"<BootStraber>",lsBootStrapper);
        lsLine = replaceTags(lsLine,"<QuietPeriod>",lsQuietPeriod);
        lsLine = replaceTags(lsLine,"<Interval>",lsInterval);
        lsLine = replaceTags(lsLine,"<LogFilePath>",(lsCruiseControlPath+"\\main\\logs\\"+lsStatusFileName));
        lsLine = replaceTags(lsLine,"<SMTPMailHost>",lsSMTPMailHost);
        lsLine = replaceTags(lsLine,"<SuccessEmailAddress>",lsSuccessEmailAddress);
        lsLine = replaceTags(lsLine,"<FailureEmailAddress>",lsFailureEmailAddress);
        lsLine = replaceTags(lsLine,"<ReturnEmailAddress>",lsReturnEmailAddress);
        lsLine = replaceTags(lsLine,"<BuildResultURL>",lsBuildResultURL);
        loConfigfile.append(lsLine);
        loConfigfile.append("\n");
      }
      dis.close();

      dis = new DataInputStream(new FileInputStream(
          lsCruiseControlPath+"\\main\\bin\\"+"Config.xml"));
      loConfigfile.append("</cruisecontrol>");
      StringBuffer loConfigXMLfile = new StringBuffer();
      while (dis.available() > 0) {
        String lsLine = dis.readLine();
        lsLine = replaceTags(lsLine,"</cruisecontrol>",loConfigfile.toString());
        loConfigXMLfile.append(lsLine);
        loConfigXMLfile.append("\n");
      }
      dis.close();
      DataOutputStream dos = new DataOutputStream(new FileOutputStream(
          lsCruiseControlPath+"\\main\\bin\\"+"Config.xml"));
      dos.writeBytes(loConfigXMLfile.toString());
      dos.close();
    }
    catch (Exception exp) {
      exp.printStackTrace();
    }
  }

  private String replaceTags( String psSource, String psTag, String psVar )
  {
    if (psSource.indexOf(psTag) >= 0) {
      psSource = psSource.replaceAll(psTag, psVar);
    }
    return psSource;
  }
}