package com.hex.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
public class HexStruts2Generator {

    private String lsPackageDir;
    private String lsPackage;
    
    public HexStruts2Generator() {
    }

    private String initCap(String ps) {
        ps = ps.substring(0, 1).toUpperCase() + ps.substring(1).toLowerCase();
        return ps;
    }

    public String getErrorJSPContent() {
        StringBuffer buffer = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\Struts2\\Error_Struts2.template"));
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
        
        Struts2JSPGenerator jspGen = new Struts2JSPGenerator();
        

        for (int i = 0; i < list.size(); i++) {
            HashMap poMap = (HashMap) list.get(i);
            ArrayList tableList = (ArrayList) poMap.get("LIST");
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
        
    }

     

    public  void generateWebXML(String outDirectory) {

        System.out.println(" Inside generateWebXML ");
        StringBuffer buffer = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\Struts2\\web_Struts2.template"));
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
            String psPageTitle) throws IOException {
        lsPackageDir = psPackageDir;
        lsPackage = psPackage;

        //generateFormBean(columns, psDistinationDir);
        generateAction(columns, psDistinationDir);
    //generateResourceBundle(columns, psObjectName, psDistinationDir, psPageTitle);
   // generateResourceBundle(list,psDistinationDir);
    //generateConfig(psObjectName, psDistinationDir);
    }

    private void generateStrutsConfigFile(ArrayList list, String outDirectory) {

        StringBuffer result = new StringBuffer();
        DataInputStream dis = null;
        FileOutputStream fos = null;

        try {
            result = new StringBuffer();
           /* StringBuffer formBeans = new StringBuffer();*/
            StringBuffer actionMappings = new StringBuffer();
            String psDistinationDir = "";
            for (int i = 0; i < list.size(); i++) {
                HashMap poMap = (HashMap) list.get(i);
                String psObjectName = (String) poMap.get("TABLE");
                psDistinationDir = (String) poMap.get("DIRECTORY");
                psObjectName = HexUtil.initCap(psObjectName);
                String actionPackage = lsPackage + ".action.";
                String lsActionBean = psObjectName + "Action";
 
                actionMappings.append("    <action name=\"home\" \n");
                actionMappings.append("            method=\"home\" >\n");

                actionMappings.append("            <result type=\"redirect\">/jsp/home.jsp</result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                
                actionMappings.append("    <action name=\"list\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"list\" >\n");

                actionMappings.append("            <result name=\"success\">/jsp/" + psObjectName+"List.jsp </result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                actionMappings.append("    <action name=\"add\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"addnew\" >\n");

                actionMappings.append("            <result name=\"add\">/jsp/" + psObjectName+"Add.jsp </result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                actionMappings.append("    <action name=\"create\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"create\" >\n");

                actionMappings.append("            <result name=\"success\">/jsp/" + psObjectName+"List.jsp </result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + psObjectName+"Add.jsp </result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                actionMappings.append("    <action name=\"search\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"search\" >\n");

                actionMappings.append("            <result name=\"success\">/jsp/" + psObjectName+"List.jsp </result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                actionMappings.append("    <action name=\"edit\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"edit\" >\n");

                actionMappings.append("            <result name=\"edit\">/jsp/" + psObjectName+"Edit.jsp </result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                actionMappings.append("    <action name=\"update\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"update\" >\n");

                actionMappings.append("            <result name=\"success\">/jsp/" + psObjectName+"List.jsp</result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                actionMappings.append("    <action name=\"delete\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"delete\" >\n");

                actionMappings.append("            <result name=\"list\">/jsp/" + psObjectName+"List.jsp</result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
                
                actionMappings.append("    <action name=\"deleteall\" class=\"" + actionPackage + lsActionBean +"\"\n");
                actionMappings.append("            method=\"deleteAll\" >\n");

                actionMappings.append("            <result name=\"list\">/jsp/" + psObjectName+"List.jsp</result>\n");
                actionMappings.append("            <result name=\"failure\">/jsp/" + getErrorJSP(psObjectName)+"</result>\n");
                
                actionMappings.append("    </action>\n\n");
               
            }
            dis = new DataInputStream(new FileInputStream(
                    "templates\\Struts2\\Struts_xml.template"));
            result = new StringBuffer();
            while (dis.available() > 0) {
                String line = dis.readLine();
               
                line = HexUtil.replaceTags(line, "<action-mapping>",
                        actionMappings.toString());
                result.append(line);
                result.append("\n");
            }
            dis.close();
            
            String strutsConfigDirectory = outDirectory + "\\WEB-INF\\"
					+ "\\classes\\";
            HexUtil.makeDirectory(strutsConfigDirectory);
            HexUtil.writeFile(result.toString(),
                    strutsConfigDirectory + "\\struts.xml");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    

    private void generateAction(ArrayList tableList, String outDirectory) throws IOException {
        TableVO tableVO = null;
        String className = "";
        String poContent = "";
        String poContent1 = "";
        StringBuffer result = new StringBuffer();
        
        for (int i = 0; i < tableList.size(); i++) {
            tableVO = (TableVO) tableList.get(i);
      
            className = HexUtil.initCap(tableVO.getTableName());
            
            String lsSelectColumnsActionMethods = getSelectColumnsActionMethod(
                    tableList, className);
            String lsStaticSelectColumnsActionMethods = getStaticSelectColumnsActionMethod(
                    tableList, className);
            String lsCheckBoxContent = getCheckBoxContent(tableList, className);
            poContent = getActionObject(className, tableList,
                    lsSelectColumnsActionMethods,lsStaticSelectColumnsActionMethods, lsCheckBoxContent, tableList, lsCheckBoxContent);
            
            
        }
        outDirectory = outDirectory + "\\src\\" + lsPackageDir + "\\action";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + className + "Action.java";
        DataInputStream dis = new DataInputStream(new FileInputStream(
                "templates\\Struts2\\BaseAction.template"));
        result = new StringBuffer();
        while (dis.available() > 0) {
            String line = dis.readLine();
            
            line = HexUtil.replaceTags(line, "<Package>", lsPackage);
            result.append(line);
            result.append("\n");
        }
        poContent1=result.toString();
        dis.close();
        String outputFile1 = outDirectory + "\\" + "BaseAction.java";
        HexUtil.writeFile(poContent, outputFile);
        HexUtil.writeFile(poContent1, outputFile1);
    }
       
    private String getCheckBoxContent(ArrayList poTableList,
            String psClassName) {

        StringBuffer buffer = new StringBuffer();
        String lsFormBeanName = HexUtil.initCap(psClassName);
     
        String lsFormBean = getVOParamInstance(psClassName);
        for (int index = 0; index < poTableList.size(); index++) {
            TableVO tableVO = (TableVO) poTableList.get(index);
            if (HexFrameConstants.CHECK_BOX.equalsIgnoreCase(tableVO.getControlType())) {
                String lsGetMethodName = "is" + HexUtil.initCap(tableVO.getColumnName().toLowerCase());
                String lsSetMethodName = "set" + HexUtil.initCap(tableVO.getColumnName().toLowerCase());
               
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
            buffer.append("\t\t\t       return(\"failure\");\n");
            buffer.append("\t\t\t}");
        }
        return buffer.toString();
    }

    
    private String getStaticSelectColumnsActionMethod(ArrayList poTableList,
            String psClassName) {

        psClassName = psClassName + "BusinessDelegate";
        StringBuffer buffer = new StringBuffer();
        boolean blnObjectCreated = false;
        for (int index = 0; index < poTableList.size(); index++) {
            TableVO tableVO = (TableVO) poTableList.get(index);
            String lsTargetTable2 = tableVO.getTargetTable2();
            if (lsTargetTable2 != null && lsTargetTable2.length() > 0) {
                lsTargetTable2 = lsTargetTable2.toLowerCase();
                String lsMethodName = HexUtil.initCap(lsTargetTable2);
                 
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
            buffer.append("\t\t\t       return(\"failure\");\n");
            buffer.append("\t\t\t}");
        }
        return buffer.toString();
    }
    
    
    private String getActionObject(String className, ArrayList tableList,
            String psSelectColumnsMethods,String psStaticSelectColumnsMethods, String psCheckBoxContent,ArrayList columns,String psDistinationDir) {
        StringBuffer result = new StringBuffer();
        String lsVariables = "";
        String lsResetVariable = "";
        String lsMethods = "";
        String tableName = "";
        DataInputStream dis = null;
        FileOutputStream fos = null;
       
        TableVO tableVO = (TableVO) tableList.get(0);
        String lsPk = HexUtil.initCap(tableVO.getColumnName());

       String voTransfromationCode = getVOTramsformationCode(className, tableList);
       String formTransfromationCode = getFormTramsformationCode(className,
                tableList);
      
       
       for (int i = 0; i < columns.size(); i++) {
    	   tableVO = (TableVO) columns.get(i);
           tableName = tableVO.getTableName().toUpperCase();
           String prop = tableVO.getColumnName().toLowerCase();
         	lsVariables = lsVariables + "\t" + "private String " + prop + ";\n";
           lsResetVariable = lsResetVariable + "\t" + "\t" + prop + " = \"\" ;\n";
           lsMethods = lsMethods + "\t" + "public void set" + HexUtil.initCap(prop) +
                   "(String newValue) { \n";
           lsMethods = lsMethods + "\t" + "\t" + prop + " = newValue; \n";
           lsMethods = lsMethods + "\t" + "}\n";
           lsMethods = lsMethods + "\t" + "public String get" + HexUtil.initCap(prop) + "() { \n";
           lsMethods = lsMethods + "\t" + "\t" + "return " + prop + ";\n ";
           lsMethods = lsMethods + "\t" + "}\n";
           
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
                    "templates\\Struts2\\Action2.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<PrivateVariables>", lsVariables);
                line = HexUtil.replaceTags(line, "<SetGetMethods>", lsMethods);
                line = HexUtil.replaceTags(line, "<VariablesReset>", lsResetVariable);
                line = HexUtil.replaceTags(line, "<ValueObject>", getVOObjectInstance(className));
                line = HexUtil.replaceTags(line, "<ValueObjectParam>", getVOParamInstance(className));
                line = HexUtil.replaceTags(line, "<POObject>", getPOObjectInstance(className));
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                line = HexUtil.replaceTags(line, "<packageName>", HexUtil.initSmall(className));
                line = HexUtil.replaceTags(line, "<VOTransformationCode>", voTransfromationCode);
                line = HexUtil.replaceTags(line, "<PrimaryKey>", lsPk);
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>", psSelectColumnsMethods);
                line = HexUtil.replaceTags(line, "<StaticSelectColumnsMethods>", psStaticSelectColumnsMethods);
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
                System.out.println(" **************  Table Name in ResourceBundle *************************" + psObjectName);
                lsResourceProperties = psObjectName.toUpperCase() + ".PAGE_TITLE=" +
                        psObjectName + "\n";
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
                    "templates\\resource\\resource-properties.template"));
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

   private String getVOTramsformationCode(String className, ArrayList tableList) {
      
        Method object = null;
        ArrayList methods = getGettersAndSetters(tableList);
        StringBuffer transformationCode = new StringBuffer();
        String voName = getVOObjectInstance(className);
        String formName = getPOObjectInstance(className);

        for (int count = 0; count < methods.size(); count++) {

            object = (Method) methods.get(count);
            String dataType = object.getReturnType();

            if (dataType.equals("Integer")) {

                transformationCode.append("\t\t\t" + voName + ".set" +
                        object.getProperty());
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
                        "\t\t\tDateFormat dateFormat = new SimpleDateFormat(\""+object.getDateFormat()+"\");");

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
        String formName = getVOParamInstance(className);

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
            	
                transformationCode.append("\t\tif(" + voName + ".get" +
                        object.getProperty() +
                        "() != null){\n");
                transformationCode.append(
                        "\t\t\tDateFormat dateFormat = new SimpleDateFormat(\""+object.getDateFormat()+"\");");

                
                transformationCode.append("(dateFormat.format(");
                transformationCode.append(voName + ".get" +
                        object.getProperty() + "()));" +
                        "\n");

                transformationCode.append("\t\t}\n");
            } else {
               
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
