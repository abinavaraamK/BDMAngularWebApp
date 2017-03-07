package com.hex.util;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.hex.vo.TableVO;
import com.hex.vo.Method;
import java.util.HashMap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class HexStrutsGenerator {

    private String lsPackageDir;
    private String lsPackage;
    
    public HexStrutsGenerator() {
    }

    private String initCap(String ps) {
        ps = ps.substring(0, 1).toUpperCase() + ps.substring(1).toLowerCase();
        return ps;
    }

    public String getErrorJSPContent() {
        StringBuffer buffer = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "D:\\BaseDataManager\\templates\\struts\\Error.template"));
            while (dis.available() > 0) {
                String s = dis.readLine();
                buffer.append(s);
                buffer.append("\n");
            }
            dis.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return buffer.toString();
    }

    public void generatePresentationTierFiles(ArrayList list) throws Exception {

        String outDirectory = "";
        String warFile="";
		String logger = "";
		ArrayList tableList=null;
        StrutsJSPGenerator jspGen = new StrutsJSPGenerator();
        

        for (int i = 0; i < list.size(); i++) {
            HashMap poMap = (HashMap) list.get(i);
            tableList = (ArrayList) poMap.get("LIST");
            outDirectory = (String) poMap.get("DIRECTORY");
            String table = (String) poMap.get("TABLE");
            String lsPageTitle = (String) poMap.get("PAGE_TITLE");
            String lsPackage = (String) poMap.get("PACKAGE");
            String lsPresentation = (String) poMap.get("PRESENTATION");
            String lsBusiness = (String) poMap.get("BUSINESS");
            String lsPersistence = (String) poMap.get("PERSISTENCE");
            String lsPackageDir = (String) poMap.get("PACKAGE_DIR");
            warFile = (String) poMap.get("WAR_FILE");
            
            String lsPageSource[] = jspGen.getListPageSource(tableList, lsPageTitle);

            if (lsPageSource == null) {
                throw new Exception("Primary Key not found for this table!");
            }
            String jspDirectory = outDirectory + "\\jsp";
            HexUtil.makeDirectory(jspDirectory);

            String outputFile;
            outputFile = jspDirectory + "\\" + initCap(table) + "List.jsp";
            HexUtil.writeFile(lsPageSource[1], outputFile);

            //outputFile = jspDirectory + "\\" + "index.jsp";
            //HexUtil.writeFile(lsPageSource[0], outputFile);

            outputFile = jspDirectory + "\\" + initCap(table) + "Add.jsp";
            HexUtil.writeFile(lsPageSource[2], outputFile);
            outputFile = jspDirectory + "\\" + initCap(table) + "Edit.jsp";
            HexUtil.writeFile(lsPageSource[3], outputFile);
            String errorContent = getErrorJSPContent();
            outputFile = jspDirectory + "\\" + getErrorJSP(initCap(table));
            HexUtil.writeFile(errorContent, outputFile);
            generateStrutsFiles(tableList, table, outDirectory, lsPackage,
                    lsPackageDir, lsPageTitle);
        }
        System.out.println("Out directory in Struts *****************" + outDirectory);
        generateWebXML(outDirectory);
        generateStrutsConfigFile(list, outDirectory);
        generateResourceBundle(list, outDirectory);
        jspGen.generateIndexPage(list,warFile,outDirectory);        
        generateLogger(tableList, outDirectory, logger);
    }

     

    public  void generateWebXML(String outDirectory) {

        System.out.println(" Inside generateWebXML ");
        StringBuffer buffer = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "D:\\BaseDataManager\\templates\\struts\\web.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                buffer.append(line);
                buffer.append("\n");
            }
            dis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String outD = outDirectory + "\\WEB-INF\\";
        HexUtil.makeDirectory(outD);
        HexUtil.writeFile(buffer.toString(),
                outD + "\\web.xml");

    }

    public void generateStrutsFiles(ArrayList columns, String psObjectName,
            String psDistinationDir, String psPackage,
            String psPackageDir,
            String psPageTitle) {
        lsPackageDir = psPackageDir;
        lsPackage = psPackage;

        generateFormBean(columns, psDistinationDir);
        generateAction(columns, psDistinationDir);
    //generateResourceBundle(columns, psObjectName, psDistinationDir, psPageTitle);
    //generateResourceBundle(list,psDistinationDir);
    //generateConfig(psObjectName, psDistinationDir);
    }

    private void generateStrutsConfigFile(ArrayList list, String outDirectory) {

        StringBuffer result = new StringBuffer();
        DataInputStream dis = null;
        FileOutputStream fos = null;

        try {
            result = new StringBuffer();
            StringBuffer formBeans = new StringBuffer();
            StringBuffer actionMappings = new StringBuffer();
            String psDistinationDir = "";
            for (int i = 0; i < list.size(); i++) {
                HashMap poMap = (HashMap) list.get(i);
                String psObjectName = (String) poMap.get("TABLE");
                psDistinationDir = (String) poMap.get("DIRECTORY");
                String lsFormInstance = psObjectName.toLowerCase() + "Form";
                psObjectName = HexUtil.initCap(psObjectName);
                String formPackage = lsPackage + ".form.";
                String actionPackage = lsPackage + ".action.";
                String lsFormBean = psObjectName + "Form";
                String lsActionBean = psObjectName + "Action";

                //Generation of Struts-config.xml

                formBeans.append("        <form-bean name=\"" + lsFormInstance +
                        "\" type=\"" + formPackage + lsFormBean + "\"/>\n\n");

                actionMappings.append("    <action path=\"/" + psObjectName.toLowerCase() +
                        "\" type=\"" + actionPackage + lsActionBean +
                        "\"\n");
                actionMappings.append("            scope=\"session\" validate=\"true\"\n");
                actionMappings.append("            parameter=\"dispatch\" ");
                actionMappings.append("name=\"" + lsFormInstance + "\">\n");

                actionMappings.append("            <forward name=\"list\" path=\"/jsp/" +
                        psObjectName + "List.jsp\" redirect=\"false\"/>\n");
                actionMappings.append("            <forward name=\"add\" path=\"/jsp/" +
                        psObjectName + "Add.jsp\" redirect=\"false\"/>\n");
                actionMappings.append("            <forward name=\"edit\" path=\"/jsp/" +
                        psObjectName + "Edit.jsp\" redirect=\"false\"/>\n");
                actionMappings.append("            <forward name=\"success\" path=\"" +
                        "/jsp/" + psObjectName + ".jsp" +
                        "\" redirect=\"true\"/>\n");
                actionMappings.append(
                        "            <forward name=\"failure\" path=\"/jsp/" +
                        getErrorJSP(psObjectName) + "\" redirect=\"false\"/>\n");
                actionMappings.append("    </action>\n\n");
            }
            dis = new DataInputStream(new FileInputStream(
                    "D:\\BaseDataManager\\templates\\struts\\Struts-config.template"));
            result = new StringBuffer();
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<form-bean-mappings>",
                        formBeans.toString());
                line = HexUtil.replaceTags(line, "<action-path-mappings>",
                        actionMappings.toString());
                result.append(line);
                result.append("\n");
            }
            dis.close();
            //String strutsConfigDirectory = psDistinationDir + "\\WEB-INF\\";
            String strutsConfigDirectory = outDirectory + "\\WEB-INF\\";
            HexUtil.makeDirectory(strutsConfigDirectory);
            HexUtil.writeFile(result.toString(),
                    strutsConfigDirectory + "\\struts-config.xml");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void generateFormBean(ArrayList columns, String psDistinationDir) {
    	System.out.println("Getting into form bean generation===============");
    	TableVO tableVO=null;
        String lsVariables = "";
        String lsResetVariable = "";
        String lsMethods = "";
        String tableName = "";
        DataInputStream dis = null;
        FileOutputStream fos = null;
        String property = "";
        String dataType = "";
        
        for (int i = 0; i < columns.size(); i++) {
        	 
            tableVO = (TableVO) columns.get(i);
            property = tableVO.getColumnName().toLowerCase();
            
            dataType = HexUtil.getDataType(tableVO);
            tableName = tableVO.getTableName().toUpperCase();
            String prop = tableVO.getColumnName().toLowerCase();
          	lsResetVariable = lsResetVariable + "\t" + "\t" + prop + " = \"\" ;\n";
                 
          	
          	
          	
            if ((tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX1)) || (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX2)))
            {
            	lsVariables = lsVariables + "\t" + "private String " + prop + ";\n";
                        
            	/*lsVariables = lsVariables + "\t" + "private " + dataType + " " + property +" = \"\" ;\n";*/
               
         lsMethods = lsMethods + "\t" + "public void set" + HexUtil.initCap(prop) + "(String newValue) { \n";
         lsMethods = lsMethods + "\t" + "\t" + prop + " = newValue; \n";
         lsMethods = lsMethods + "\t" + "}\n";
         lsMethods = lsMethods + "\t" + "public String get" + HexUtil.initCap(prop) + "() { \n";
         lsMethods = lsMethods + "\t" + "\t" + "return " + prop + ";\n ";
         lsMethods = lsMethods + "\t" + "}\n";
            
            }
           else if ((tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.TEXT_BOX1)) || (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.TEXT_BOX2)))
            {
            	
					
						/*lsVariables = lsVariables + "\t" + "private " + dataType + " " + property + "=\"" + InetAddress.getLocalHost().getHostName()+ "\"" +
						        ";\n";*/
						lsVariables = lsVariables + "\t" + "private " + dataType + " " + property + "=\"" + "null"+ "\"" +
				        ";\n";
						lsMethods = lsMethods + "\t" + "public void set" + HexUtil.initCap(prop) +
			               "(String newValue) { \n";
			       lsMethods = lsMethods + "\t" + "\t" + prop + " = newValue; \n";
			       lsMethods = lsMethods + "\t" + "}\n";
			       lsMethods = lsMethods + "\t" + "public String get" + HexUtil.initCap(prop) + "() { \n";
			       lsMethods = lsMethods + "\t" + "\t" + "return " + prop + ";\n ";
			       lsMethods = lsMethods + "\t" + "}\n";
					
						
			
            }
           else{
        	   lsVariables = lsVariables + "\t" + "private String " + prop + ";\n";
        	   lsMethods = lsMethods + "\t" + "public void set" + HexUtil.initCap(prop) +
               "(String newValue) { \n";
       lsMethods = lsMethods + "\t" + "\t" + prop + " = newValue; \n";
       lsMethods = lsMethods + "\t" + "}\n";
       lsMethods = lsMethods + "\t" + "public String get" + HexUtil.initCap(prop) + "() { \n";
       lsMethods = lsMethods + "\t" + "\t" + "return " + prop + ";\n ";
       lsMethods = lsMethods + "\t" + "}\n";
           }
            
            
            
            
            
            if (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.PASSWORD)) {

                String proppassword = tableVO.getColumnName().toLowerCase() + "password";
                lsVariables = lsVariables + "\t" + "private String " + proppassword + ";\n";
                lsResetVariable = lsResetVariable + "\t" + "\t" + proppassword + " = \"\" ;\n";
                lsMethods = lsMethods + "\t" + "public void set" + HexUtil.initCap(proppassword) +
                        "(String newValue) { \n";
                lsMethods = lsMethods + "\t" + "\t" + proppassword + " = newValue; \n";
                lsMethods = lsMethods + "\t" + "}\n";
                lsMethods = lsMethods + "\t" + "public String get" + HexUtil.initCap(proppassword) + "() { \n";
                lsMethods = lsMethods + "\t" + "\t" + "return " + proppassword + ";\n ";
                lsMethods = lsMethods + "\t" + "}\n";
            }
        }

        
        
        
        try {
            dis = new DataInputStream(new FileInputStream(
                    "D:\\BaseDataManager\\templates\\struts\\FormBean.template"));
            StringBuffer result = new StringBuffer();

            /*psObjectName = HexUtil.initCap(psObjectName);
            String lsFormBean = psObjectName + "Form";*/

            tableName = HexUtil.initCap(tableName);
            String lsFormBean = tableName + "Form";

            while (dis.available() > 0) {
                String line = dis.readLine();
                
                line = HexUtil.replaceTags(line, "<ClassName>", lsFormBean);
                
                line = HexUtil.replaceTags(line, "<PrivateVariables>", lsVariables);
                line = HexUtil.replaceTags(line, "<SetGetMethods>", lsMethods);
                line = HexUtil.replaceTags(line, "<VariablesReset>", lsResetVariable);
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                result.append(line);
                result.append("\n");
            }
            dis.close();

           
            String outDirectory = psDistinationDir + "\\src\\" + lsPackageDir + "\\form";
            HexUtil.makeDirectory(outDirectory);
            String outputFile = outDirectory + "\\" + lsFormBean + ".java";
            HexUtil.writeFile(result.toString(), outputFile);

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
/*    String conditionaddstruts="";
    String conditionupdatestruts="";*/
    String tableNameLC="";
    private void generateAction(ArrayList tableList, String outDirectory) {
        TableVO tableVO = null;
        String className = "";
        String poContent = "";
        
        
        String property = "";
        for (int i = 0; i < tableList.size(); i++) {
            tableVO = (TableVO) tableList.get(i);
            //tableVO = (TableVO) tableList.get(0);
            tableNameLC = tableVO.getTableName().toLowerCase();
            property = tableVO.getColumnName().toLowerCase();
            /*if(property.equalsIgnoreCase("createddate")){
          		conditionaddstruts="String date=new Date().toString();\nDate formatter=null;\ntry{"+tableNameLC+".setCreatedby(InetAddress.getLocalHost().getHostName());"+tableNameLC+".setModifiedby(InetAddress.getLocalHost().getHostName());formatter = new SimpleDateFormat(\"EEE MMM dd HH:mm:ss Z yyyy\", Locale.ENGLISH).parse(date);	}catch(Exception e){e.printStackTrace();}\n String fdate = new SimpleDateFormat(\"MM-dd-yyyy\").format(formatter);"+tableNameLC+".setCreateddate(fdate);"+tableNameLC+".setModifieddate(fdate);";
          		conditionupdatestruts="String date=new Date().toString();\nDate formatter=null;\ntry{"+tableNameLC+".setModifiedby(InetAddress.getLocalHost().getHostName());formatter = new SimpleDateFormat(\"EEE MMM dd HH:mm:ss Z yyyy\", Locale.ENGLISH).parse(date);}catch(Exception e){e.printStackTrace();}String fdate = new SimpleDateFormat(\"MM-dd-yyyy\").format(formatter); "+tableNameLC+".setModifieddate(fdate);";
          	         	
          	}*/
            className = HexUtil.initCap(tableVO.getTableName());
            String lsSelectColumnsActionMethods = getSelectColumnsActionMethod(
                    tableList, className);
            String lsCheckBoxContent = getCheckBoxContent(tableList, className);
            poContent = getActionObject(className, tableList,
                    lsSelectColumnsActionMethods, lsCheckBoxContent);
        }
        outDirectory = outDirectory + "\\src\\" + lsPackageDir + "\\action";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + className + "Action.java";
        HexUtil.writeFile(poContent, outputFile);
    }

   
    private String getActionObject(String className, ArrayList tableList,
            String psSelectColumnsMethods, String psCheckBoxContent) {
        StringBuffer result = new StringBuffer();

        TableVO tableVO = (TableVO) tableList.get(0);
        String lsPk = HexUtil.initCap(tableVO.getColumnName());

        String voTransfromationCode = getVOTramsformationCode(className, tableList);
        String formTransfromationCode = getFormTramsformationCode(className,
                tableList);

        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "D:\\BaseDataManager\\templates\\struts\\Action.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<tableNameLC>", tableNameLC);
               /* line=HexUtil.replaceTags(line, "<conditionaddstruts>", conditionaddstruts);
                line=HexUtil.replaceTags(line, "<conditionupdatestruts>", conditionupdatestruts);*/
                line = HexUtil.replaceTags(line, "<ValueObject>", getVOObjectInstance(className));
                line = HexUtil.replaceTags(line, "<ValueObjectParam>", getVOParamInstance(className));
                line = HexUtil.replaceTags(line, "<POObject>", getPOObjectInstance(className));
                line = HexUtil.replaceTags(line, "<FormObject>", getFormObjectInstance(className));
                line = HexUtil.replaceTags(line, "<FormObjectParam>", getFormParamInstance(className));
                line = HexUtil.replaceTags(line, "<packageName>", HexUtil.initSmall(className));
                line = HexUtil.replaceTags(line, "<VOTransformationCode>", voTransfromationCode);
                line = HexUtil.replaceTags(line, "<FormTransformationCode>", formTransfromationCode);
                line = HexUtil.replaceTags(line, "<PrimaryKey>", lsPk);
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>", psSelectColumnsMethods);
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                line = HexUtil.replaceTags(line, "<CheckBoxContent>", psCheckBoxContent);
                result.append(line);
                result.append("\n");
            }
            dis.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result.toString();
    }

    private String getCheckBoxContent(ArrayList poTableList,
            String psClassName) {

        StringBuffer buffer = new StringBuffer();
        String lsFormBeanName = HexUtil.initCap(psClassName);
        String lsFormBean = "wo" + lsFormBeanName + "Form";
        for (int index = 0; index < poTableList.size(); index++) {
            TableVO tableVO = (TableVO) poTableList.get(index);
            if (HexFrameConstants.CHECK_BOX.equalsIgnoreCase(tableVO.getControlType())) {
                String lsGetMethodName = "get" + HexUtil.initCap(tableVO.getColumnName().toLowerCase());
                String lsSetMethodName = "set" + HexUtil.initCap(tableVO.getColumnName().toLowerCase());
                buffer.append("if(" + lsFormBean + "." + lsGetMethodName + "().equals(\"\")){\n");
                buffer.append("\t" + lsFormBean + "." + lsSetMethodName + "(\"N\");\n");
                buffer.append("}\n");
            }
        }
        return buffer.toString();
    }

    private String getSelectColumnsActionMethod(ArrayList poTableList,
            String psClassName) {

        psClassName = psClassName + "BusinessDelegate";
        StringBuffer buffer = new StringBuffer();
        boolean blnObjectCreated = false;
        for (int index = 0; index < poTableList.size(); index++) {
            TableVO tableVO = (TableVO) poTableList.get(index);
            String lsTargetTable = tableVO.getTargetTable();
            if (lsTargetTable != null && lsTargetTable.length() > 0) {
                lsTargetTable = lsTargetTable.toLowerCase();
                String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
                String lsValueColumn = tableVO.getValueColumn().toLowerCase();
                String lsMethodName = HexUtil.initCap(lsTargetTable) +
                        HexUtil.initCap(lsKeyColumn) +
                        HexUtil.initCap(lsValueColumn);
                if (!blnObjectCreated) {
                    buffer.append("\t\ttry {\n");
                    buffer.append("\t\t\t\t" + psClassName + " lo" + psClassName +
                            " = new " +
                            psClassName + "();\n");
                }
                buffer.append("\t\t\t\tList lo" + lsMethodName + " = lo" + psClassName +
                        ".getSelect" + lsMethodName + "();\n");
                buffer.append("\t\t\t\trequest.setAttribute(\"" + lsMethodName +
                        "\",lo" +
                        lsMethodName + ");\n");
                blnObjectCreated = true;
            }
        }
        if (blnObjectCreated) {
            buffer.append("\t\t\t} catch (HexApplicationException exception) {\n");
            buffer.append("\t\t\t       request.setAttribute(\"Exception\",exception);\n");
            buffer.append("\t\t\t       return mapping.findForward(\"failure\");\n");
            buffer.append("\t\t\t}");
        }
        return buffer.toString();
    }

    
    private void generateResourceBundle(ArrayList list, String psDistinationDir) {

        System.out.println(" ************** Enter into ResourceBundle *************************");
        String lsResourceProperties = "";
        StringBuffer resouceBundle = new StringBuffer();
        DataInputStream dis = null;
        StringBuffer result = new StringBuffer();
        String exceptionProperties = "";
        try {

            for (int i = 0; i < list.size(); i++) {
                HashMap poMap = (HashMap) list.get(i);
                String psObjectName = (String) poMap.get("TABLE");
                String page_title=(String) poMap.get("PAGE_TITLE");
                System.out.println(" **************  Table Name in ResourceBundle *************************" + psObjectName);
                lsResourceProperties = psObjectName.toUpperCase() + ".PAGE_TITLE=" +
                page_title + "\n";
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".ADD_PAGE_TITLE=" + psObjectName + " - Add\n");
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".EDIT_PAGE_TITLE=" + psObjectName +
                        " - Edit\n");

                ArrayList tableVOList = (ArrayList) poMap.get("LIST");
                for (int count = 0; count < tableVOList.size(); count++) {

                    TableVO tableVO = (TableVO) tableVOList.get(count);
                    String prop = tableVO.getColumnName().toLowerCase();
                    lsResourceProperties = lsResourceProperties +
                            (psObjectName.toUpperCase() + "." + prop.toUpperCase() + "=" +
                            tableVO.getLabelName() + "\n");
                }

                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".BUTTON.ADD=Add\n");
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".BUTTON.UPDATE=Update\n");
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".BUTTON.LIST=List\n");
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".BUTTON.DELETE=Delete\n");
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".BUTTON.SAVE=Save\n");
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".BUTTON.GO=Go\n");
                lsResourceProperties = lsResourceProperties +
                        (psObjectName.toUpperCase() + ".BUTTON.CLEAR=Clear\n");
                resouceBundle.append(lsResourceProperties);
            }

            exceptionProperties = exceptionProperties +
                    ("ERROR.DATANOTAVAILABLE=The value you have entered is not available in the Database.\n");
            exceptionProperties = exceptionProperties +
                    ("DUPLICATE_KEY_EXCEPTION=Insert or Update failed, Violation of an integrity constraint\n");
            exceptionProperties = exceptionProperties +
                    ("DATA_INTEGRITY_EXCEPTION=Insert or Update failed, Violation of an integrity constraint\n");
            exceptionProperties = exceptionProperties +
                    ("RECORD_NOT_FOUND_EXCEPTION=Expected data is not available in the Database\n");
            exceptionProperties = exceptionProperties +
                    ("GENERAL_DB_EXCEPTION=General Database error.\n");
          

            resouceBundle.append(exceptionProperties);
            dis = new DataInputStream(new FileInputStream(
                    "D:\\BaseDataManager\\templates\\resource\\resource-properties.template"));
            result = new StringBuffer();
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<resource-properties>",
                        resouceBundle.toString());
                result.append(line);
                result.append("\n");
            }
            dis.close();
            String classesDirectory = psDistinationDir + "\\WEB-INF\\" + "\\classes\\";
            HexUtil.makeDirectory(classesDirectory);

            HexUtil.writeFile(resouceBundle.toString(), classesDirectory + "\\ApplicationResources.properties");
            HexUtil.writeFile(resouceBundle.toString(), classesDirectory + "\\ApplicationResources_en.properties");

        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }
    
/*Method for logger*/
    private void generateLogger(ArrayList columns, String psDistinationDir,
			String logger) {
		TableVO tableVO = new TableVO();

		System.out.println(" ************** Enter into Logger *************************");

		StringBuffer resouceBundle = new StringBuffer();
		DataInputStream dis = null;
		StringBuffer result = new StringBuffer();
		String logProperties = "";

		try {
			ResourceBundle resourceBundle = new PropertyResourceBundle(
					new FileInputStream("D:\\rcp workspace\\BaseDataManager4.0\\src\\BaseDataManager.properties"));
			String logfile = resourceBundle.getString("logger.file");
			System.out.println(logfile);
			logProperties = logProperties + ("# Log levels\n");
			logProperties = logProperties
					+ ("# Uncomment the following line to enable full loggin for every class\n");
			logProperties = logProperties
					+ ("log4j.rootLogger=info, stdout, R\n");
			logProperties = logProperties
					+ ("log4j.logger.gr.xfrag=info, stdout, R\n");
			logProperties = logProperties
					+ ("# Console appender configuration\n");
			logProperties = logProperties
					+ ("log4j.appender.stdout=org.apache.log4j.ConsoleAppender\n");
			logProperties = logProperties
					+ ("# Pattern to output the caller's file name and line number.\n");
			logProperties = logProperties
					+ ("log4j.appender.stdout.layout=org.apache.log4j.PatternLayout\n");
			logProperties = logProperties
					+ ("log4j.appender.stdout.layout.ConversionPattern=%5p [%t] (%F:%L) - %m%n\n");
			logProperties = logProperties + ("# Rolling File Appender\n");
			logProperties = logProperties
					+ ("log4j.appender.R=org.apache.log4j.RollingFileAppender\n");
			logProperties = logProperties
					+ ("# Path and file name to store the log file.\n");
			logProperties = logProperties
					+ ("log4j.appender.R.File=" + logfile + "\n");
			logProperties = logProperties
					+ ("log4j.appender.R.MaxFileSize=500KB\n");
			logProperties = logProperties + ("# Keep one backup file\n");
			logProperties = logProperties
					+ ("log4j.appender.R.MaxBackupIndex=1\n");
			logProperties = logProperties
					+ ("# Rolling File Appender layout\n");
			logProperties = logProperties
					+ ("log4j.appender.R.layout=org.apache.log4j.PatternLayout\n");
			logProperties = logProperties
					+ ("log4j.appender.R.layout.ConversionPattern=%d - %c - %p - %m%n\n");
			System.out.println("Logger detail" + logger);   

			resouceBundle.append(logProperties);
			dis = new DataInputStream(new FileInputStream(
					"D:\\BaseDataManager\\templates\\log\\log-properties.template"));
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<log-properties>",
						resouceBundle.toString());
				result.append(line);
				result.append("\n");
			}
			dis.close();

			String classesDirectory = psDistinationDir + "\\WEB-INF\\"
					+ "\\classes\\";
			HexUtil.writeFile(result.toString(), classesDirectory+"\\log4j.properties");
			
			

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}
    
    
    private String getVOTramsformationCode(String className, ArrayList tableList) {
        //System.out.println("Inside getVOTramsformationCode");
        Method object = null;
        ArrayList methods = getGettersAndSetters(tableList);
        StringBuffer transformationCode = new StringBuffer();
        String voName = getVOObjectInstance(className);
        String formName = getFormObjectInstance(className);

        for (int count = 0; count < methods.size(); count++) {

            object = (Method) methods.get(count);
            String dataType = object.getReturnType();

            //System.out.println("Return Data Type " + dataType);

            if (dataType.equals("Integer")) {

                // Checks for not null and transfroms from formbean to ValueObject
                transformationCode.append("\t\tif(!" + formName + ".get" +
                        object.getProperty() +
                        "().trim().equals(\"\")){\n");

                transformationCode.append("\t\t\t" + voName + ".set" +
                        object.getProperty());

                //System.out.println("Handling Data Type 1" + dataType);
                transformationCode.append("(Integer.valueOf(");
                transformationCode.append(formName + ".get" +
                        object.getProperty() + "().trim()));" + "\n");

                transformationCode.append("\t\t}\n");
            } else if (dataType.equals("Double")) {

                transformationCode.append("\t\tif(!" + formName + ".get" +
                        object.getProperty() +
                        "().trim().equals(\"\")){\n");

                transformationCode.append("\t\t\t" + voName + ".set" +
                        object.getProperty());

                transformationCode.append("(Double.valueOf(");
                transformationCode.append(formName + ".get" +
                        object.getProperty() + "().trim()));" + "\n");

                transformationCode.append("\t\t}\n");

            } else if (dataType.equals("Date")) {

                transformationCode.append("\t\ttry {\n");
                transformationCode.append("\t\tif(!" + formName + ".get" +
                        object.getProperty() +
                        "().trim().equals(\"\")){\n");
                transformationCode.append(
                        "\t\t\tDateFormat dateFormat = new SimpleDateFormat(\"MM-dd-yyyy\");");

                transformationCode.append("\t\t\t" + voName + ".set" +
                        object.getProperty());

                transformationCode.append("(dateFormat.parse(");
                transformationCode.append(formName + ".get" +
                        object.getProperty() + "().trim()));" + "\n");

                transformationCode.append("\t\t}\n");
                transformationCode.append(
                        "\t\t} catch ( java.text.ParseException pExp) {\n");
                transformationCode.append("\t\t\tpExp.printStackTrace();\n");
                transformationCode.append("\t\t}\n");

            } else {
                //System.out.println("Handling Data Type 3" + dataType);
                transformationCode.append("\t\t" + voName + ".set" +
                        object.getProperty());
                transformationCode.append("(" + formName + ".get" +
                        object.getProperty() + "().trim());" + "\n");
            }
        }
        return transformationCode.toString();
    }

    private String getFormTramsformationCode(String className,
            ArrayList tableList) {
        //System.out.println("Inside getVOTramsformationCode");
        Method object = null;
        ArrayList methods = getGettersAndSetters(tableList);
        StringBuffer transformationCode = new StringBuffer();
        String voName = getVOParamInstance(className);
        String formName = getFormParamInstance(className);

        for (int count = 0; count < methods.size(); count++) {

            object = (Method) methods.get(count);
            String dataType = object.getReturnType();

            //System.out.println("Return Data Type " + dataType);

            if (dataType.equals("Integer")) {

                // Checks for not null and transfroms from formbean to ValueObject
                transformationCode.append("\t\tif(" + voName + ".get" +
                        object.getProperty() +
                        "() != null){\n");

                transformationCode.append("\t\t\t" + formName + ".set" +
                        object.getProperty());

                //System.out.println("Handling Data Type 1" + dataType);
                transformationCode.append("(String.valueOf(");
                transformationCode.append(voName + ".get" +
                        object.getProperty() + "().intValue()));" +
                        "\n");

                transformationCode.append("\t\t}\n");
            } else if (dataType.equals("Double")) {

                transformationCode.append("\t\tif(" + voName + ".get" +
                        object.getProperty() +
                        "() != null){\n");

                transformationCode.append("\t\t\t" + formName + ".set" +
                        object.getProperty());

                transformationCode.append("(String.valueOf(");
                transformationCode.append(voName + ".get" +
                        object.getProperty() + "().doubleValue()));" +
                        "\n");

                transformationCode.append("\t\t}\n");

            } else if (dataType.equals("Date")) {
            	//TableVO tablevo= new TableVO();
                // Checks for not null and transfroms from formbean to ValueObject
                transformationCode.append("\t\tif(" + voName + ".get" +
                        object.getProperty() +
                        "() != null){\n");
                transformationCode.append(
                "\t\t\tDateFormat dateFormat = new SimpleDateFormat(\"MM-dd-yyyy\");");

                transformationCode.append("\t\t\t" + formName + ".set" +
                        object.getProperty());
                //System.out.println("Handling Data Type 1" + dataType);
                transformationCode.append("(dateFormat.format(");
                transformationCode.append(voName + ".get" +
                        object.getProperty() + "()));" +
                        "\n");

                transformationCode.append("\t\t}\n");
            } else {
                //System.out.println("Handling Data Type 3" + dataType);
                transformationCode.append("\t\t" + formName + ".set" +
                        object.getProperty());
                transformationCode.append("(" + voName + ".get" +
                        object.getProperty() + "());" + "\n");
            }
        }
        return transformationCode.toString();
    }

    private ArrayList getGettersAndSetters(ArrayList tableList) {
        TableVO tableVO = null;
        String property = "";
        ArrayList methods = new ArrayList();
        Method method = new Method();

        for (int count = 0; count < tableList.size(); count++) {
            method = new Method();
            tableVO = (TableVO) tableList.get(count);
            property = tableVO.getColumnName().toLowerCase();
            method.setProperty(HexUtil.initCap(property));
            method.setReturnType(HexUtil.getDataType(tableVO));
            method.setDateFormat(tableVO.getDateFormat());
            methods.add(method);
        }
        return methods;
    }

    public String getDataType(TableVO tableVO) {
        String lsDataType = tableVO.getDataType();
        if (lsDataType.equalsIgnoreCase("NUMBER") ||
                lsDataType.equalsIgnoreCase("NUMERIC")) {
            if (tableVO.getDataScale() == null ||
                    tableVO.getDataScale().intValue() == 0) {
                return "Integer";
            } else {
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

    private String getFormObjectInstance(String className) {
        return "wo" + className + "Form";
    }

    private String getFormParamInstance(String className) {
        return className.toLowerCase() + "Form";
    }

    private String getPOObjectInstance(String className) {
        return "wo" + className + "PO";
    }

    private String getVOObjectInstance(String className) {
        return "wo" + className + "VO";
    }

    private String getVOParamInstance(String className) {
        return className.toLowerCase() + "VO";
    }

    private String getErrorJSP(String jspName) {
        return "Error_" + jspName + ".jsp";
    }
}
