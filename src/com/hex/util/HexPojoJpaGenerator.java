package com.hex.util;

import java.util.ArrayList;
import com.hex.vo.TableVO;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import com.hex.vo.Method;
import com.hex.dao.TableDAO;
import com.hex.vo.DataBaseVO;
import java.util.Hashtable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class HexPojoJpaGenerator {

    private String lsPackage = "";
    private String lsPackageDir = "";
    private String lsDatabase = "";
    public HexPojoJpaGenerator() {

    }

    //public void generateJpaFiles(HashMap poMap) {
    public void generateJpaFiles(ArrayList list) {

        System.out.println("**********Enter into HexPojoJpaGenerator method*********");
        String outDirectory = "";
        for (int i = 0; i < list.size(); i++) {
            HashMap poMap = (HashMap) list.get(i);

            lsPackage = (String) poMap.get("PACKAGE");
            lsPackageDir = (String) poMap.get("PACKAGE_DIR");
            outDirectory = (String) poMap.get("DIRECTORY");
            lsDatabase = (String) poMap.get("DATABASE");
            ArrayList tableList = (ArrayList) poMap.get("LIST");
            generateDAO(tableList, outDirectory);
        }
         
            generateHexTemplates(list, outDirectory);                     
            generateMappingXML(list, outDirectory);
            generatePersistenceXML(list, outDirectory);
            generateClassLoaderXML(list, outDirectory);
        }

      private  void generateDAO(ArrayList tableList, String outDirectory) {
        TableVO tableVO = null;
        String tableName = "";

        String lsSelectColumnsMethods = getSelectColumnsDAOMethod(tableList);
        tableVO = (TableVO) tableList.get(0);
        tableName = HexUtil.initCap(tableVO.getTableName());
        String daoContent = getDataAccessObject(tableName, tableName,
                lsSelectColumnsMethods);

        outDirectory = outDirectory + "\\src\\" + lsPackageDir + "\\dao";
        System.out.println("outDirectory in  pojo dao"+outDirectory);
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + tableName + "DaoImpl.java";
        HexUtil.writeFile(daoContent, outputFile);

        lsSelectColumnsMethods = getSelectColumnsDAOInterfaceMethod(tableList);
        daoContent = getDataAccessInterfaceObject(tableName, tableName, lsSelectColumnsMethods);
        outputFile = outDirectory + "\\" + tableName + "Dao.java";
        HexUtil.writeFile(daoContent, outputFile);
    }

    private String getDataAccessObject(String className, String valueObject,
            String psSelectColumnsMethods) {
        TableVO tableVO = null;
        StringBuffer result = new StringBuffer();
        TableDAO tableDAO = TableDAO.getInstance();
        ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(className);
        String pkMethod = "";
        if (pkDetails.size() == 1) {
            tableVO = (TableVO) pkDetails.get(0);
            pkMethod = HexUtil.initCap(tableVO.getColumnName());
        }
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\pojo\\jpa\\DaoImpl.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<packageName>", HexUtil.initSmall(className));
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<ValueObject>", valueObject);
                line = HexUtil.replaceTags(line, "<PrimaryKey>", pkMethod);
                line = HexUtil.replaceTags(line, "<ValueObjectInstance>", getVOObjectInstance(valueObject));
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>", psSelectColumnsMethods);
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                result.append(line);
                result.append("\n");
            }
            dis.close();
        } catch (Exception exception) {

            exception.printStackTrace();
        }
System.out.println("result.toString"+result.toString());
        return result.toString();
    }

    private String getSelectColumnsDAOMethod(ArrayList poTableList) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < poTableList.size(); index++) {
            TableVO tableVO = (TableVO) poTableList.get(index);
            String lsTargetTable = tableVO.getTargetTable();
            if (lsTargetTable != null && lsTargetTable.length() > 0) {
                lsTargetTable = lsTargetTable.toLowerCase();
                String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
                String lsValueColumn = tableVO.getValueColumn().toLowerCase();
                String lsMethodName = "getSelect" + HexUtil.initCap(lsTargetTable) +
                        HexUtil.initCap(lsKeyColumn) +
                        HexUtil.initCap(lsValueColumn);
                /*String lsSelectQry = "select " +lsKeyColumn + "," +lsValueColumn +
                " from " + lsTargetTable +" as " + lsTargetTable;*/
                String lsSelectQry = "";
                if("oracle".equalsIgnoreCase(lsDatabase)) {
	                lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
	                " from " + lsTargetTable;
                }
                else {
                	lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
	                " from " + lsTargetTable + " as " + lsTargetTable;
                }                   
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@"+lsSelectQry);
                buffer.append("\tpublic java.util.List " + lsMethodName + "() throws HexApplicationException {\n");
                buffer.append("\t\ttry {\n");
                buffer.append("\t\t\t return new HexJpaTemplate().getRecords(\"" + lsSelectQry + "\");\n");
                buffer.append("\t\t}catch(Exception exception) {\n");
                buffer.append("\t\t\t    throw new HexApplicationException( exception );\n");
                buffer.append("\t\t}\n");
                buffer.append("\t}\n\n");
            }
            else{
            	System.out.println("@@@@@@@@@@@@@Empty");
            }
        }
        return buffer.toString();
    }

    private String getDataAccessInterfaceObject(String className, String valueObject,
            String psSelectColumnsMethods) {
        TableVO tableVO = null;
        StringBuffer result = new StringBuffer();
        TableDAO tableDAO = TableDAO.getInstance();
        ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(className);
        String pkMethod = "";

        if (pkDetails.size() == 1) {
            tableVO = (TableVO) pkDetails.get(0);
            pkMethod = HexUtil.initCap(tableVO.getColumnName());
        }

        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\pojo\\jpa\\DaoInterface.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<ValueObject>", valueObject);
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>", psSelectColumnsMethods);
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                result.append(line);
                result.append("\n");
            }
            dis.close();
        } catch (Exception exception) {

            exception.printStackTrace();
        }
        return result.toString();
    }

    private String getSelectColumnsDAOInterfaceMethod(ArrayList poTableList) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < poTableList.size(); index++) {
            TableVO tableVO = (TableVO) poTableList.get(index);
            String lsTargetTable = tableVO.getTargetTable();
            if (lsTargetTable != null && lsTargetTable.length() > 0) {
                lsTargetTable = lsTargetTable.toLowerCase();
                String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
                String lsValueColumn = tableVO.getValueColumn().toLowerCase();
                String lsMethodName = "getSelect" + HexUtil.initCap(lsTargetTable) +
                        HexUtil.initCap(lsKeyColumn) +
                        HexUtil.initCap(lsValueColumn);
                buffer.append("\tpublic java.util.List " + lsMethodName + "() throws HexApplicationException; \n");
                buffer.append("\t\n\n");
            }
        }
        return buffer.toString();
    }

    private void generateContextXML(ArrayList tableList, String outDirectory) {

        TableVO tableVO = null;
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        String className = "";
        //tableVO = (TableVO) tableList.get(0);

        //className = HexUtil.initCap(tableVO.getTableName());
        //String xmlContent = getAppContextXML(className, dbVO);
        String xmlContent = getAppContextXML(tableList, dbVO);

        outDirectory = outDirectory + "\\WEB-INF\\classes\\";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" +
                "applicationContext.xml";
        HexUtil.writeFile(xmlContent, outputFile);
    }

   public String getAppContextXML(ArrayList list, DataBaseVO dbVO) {
        StringBuffer result = new StringBuffer();
        String psObjectName = "";
        DataInputStream dis = null;

        try {

            StringBuffer beanId = new StringBuffer();
            StringBuffer beanEntity = new StringBuffer();


            for (int i = 0; i < list.size(); i++) {
                HashMap poMap = (HashMap) list.get(i);
                psObjectName = (String) poMap.get("TABLE");               

                beanId.append("    <bean id =\"" + HexUtil.initCap(psObjectName) + "\" class=\"" + "org.springframework.transaction.interceptor.TransactionProxyFactoryBean" + "\">\n");
                beanId.append("       <property name =\"" + "transactionManager" +"\"  ref =\"" + "transactionManager" + "\"/>\n");                
                beanId.append("       <property name =\"" + "target" + "\">\n");
                beanId.append("       <bean class=\"" + lsPackage + ".service." + HexUtil.initCap(psObjectName) + "Impl" + "\">\n");
                beanId.append("         <property name =\"" + psObjectName.toLowerCase() + "Dao" +"\" ref =\"" + HexUtil.initCap(psObjectName) + "Dao"+ "\"/>\n");                
                beanId.append("       </bean>\n");
                beanId.append("       </property>\n");
                beanId.append("       <property name =\"" + "transactionAttributes" + "\">\n");
                beanId.append("       <props>\n");
                beanId.append("       <prop key =\"" + "*" + "\">\n");
                beanId.append("         PROPAGATION_REQUIRED, -ApplicationException");
                beanId.append("       </prop>\n");
                beanId.append("       </props>\n");
                beanId.append("       </property>\n");
                beanId.append("   </bean>\n");
               
                beanEntity.append("    <bean id =\"" + HexUtil.initCap(psObjectName) + "Dao" + "\" class=\"" + lsPackage + ".dao." + HexUtil.initCap(psObjectName) + "DaoImpl" + "\">\n");
                beanEntity.append("       <property name =\"" + "entityManagerFactory" + "\">\n");
                beanEntity.append("       <ref bean =\"" + "entityManagerFactory" + "\"/>\n");
                beanEntity.append("       </property>\n");
                beanEntity.append("   </bean>\n");
            }


            dis = new DataInputStream(new FileInputStream(
                    "templates\\jpa\\applicationContext.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<DataSource>", psObjectName);

                line = HexUtil.replaceTags(line, "<LowerClassName>",
                        psObjectName.toLowerCase());
                line = HexUtil.replaceTags(line, "<Driver>", dbVO.driverName);

                if (line.indexOf("<url>") >= 0) {
                    String url = dbVO.getUrl();
                    if (dbVO.getPortNo() != null &&
                            dbVO.getPortNo().trim().length() > 0) {
                        url = url + ":" + dbVO.getPortNo();
                    }
                    if (dbVO.getSchema() != null &&
                            dbVO.getSchema().trim().length() > 0) {
                        url = url + ":" + dbVO.getSchema();
                    }
                    line = line.replaceAll("<url>", url);
                }
                line = HexUtil.replaceTags(line, "<username>", dbVO.getUserName());
                line = HexUtil.replaceTags(line, "<password>", dbVO.getPassword());                
                line = HexUtil.replaceTags(line, "<bean-Dao-mappings>", beanId.toString());
                line = HexUtil.replaceTags(line, "<bean-Entity-mappings>", beanEntity.toString());               
                result.append(line);
                result.append("\n");
            }
            dis.close();
        } catch (Exception exception) {

            exception.printStackTrace();
        }

        return result.toString();

    }

    private void generateMappingXML(ArrayList tableList, String outDirectory) {

        String className = "";
        TableVO tableVO = null;
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        //tableVO = (TableVO) tableList.get(0);
        
        //className = HexUtil.initCap(tableVO.getTableName());
        String xmlContent = getMappingXML(dbVO, tableList);
        //String xmlContent = getMappingXML(className, dbVO, tableList);

        outDirectory = outDirectory + "\\WEB-INF\\classes\\META-INF"; 
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + "orm.xml";
        HexUtil.writeFile(xmlContent, outputFile);
    }

    //public String getMappingXML(String className, DataBaseVO dbVO, ArrayList tableList) {
    
    public String getMappingXML(DataBaseVO dbVO, ArrayList tableList) {
                
        StringBuffer result = new StringBuffer();
        TableDAO tableDAO = TableDAO.getInstance();
        String className = "";
        DataInputStream dis = null;
        String pkFields = "";
        String columnProp = "";
              
        try {
            StringBuffer columnProperties = new StringBuffer();
            for (int i = 0; i < tableList.size(); i++) {
                HashMap poMap = (HashMap) tableList.get(i);
                className = (String) poMap.get("TABLE");
                ArrayList list = (ArrayList) poMap.get("LIST");                
                ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(className);               
                pkFields = getPrimaryKeyFields(pkDetails);               
                // columnProp = getColumnProperties(pkDetails, tableList);
                columnProp = getColumnProperties(pkDetails, list);
                
                //String buffer added 
                columnProperties.append("<entity class =\"" + lsPackage +".vo."+ HexUtil.initCap(className) +"\" >\n");                
                columnProperties.append("<table name =\"" +  className.toUpperCase() +"\" />\n");   
                columnProperties.append("<attributes>\n");
                columnProperties.append(pkFields);
                columnProperties.append(columnProp);                         
                columnProperties.append("<transient name =\""+ "toBeDeleted" +"\" />\n");   
                columnProperties.append("</attributes>\n");
                columnProperties.append("</entity>\n");                    
            }

            dis = new DataInputStream(new FileInputStream(
                    "templates\\jpa\\orm.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                //line = HexUtil.replaceTags(line, "<ValueObject>", className);
                //line = HexUtil.replaceTags(line, "<TableName>", className.toUpperCase());
               // line = HexUtil.replaceTags(line, "<Primary-Key-Fields>", pkFields);
                line = HexUtil.replaceTags(line, "<Column-Properties>", columnProperties.toString());
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                result.append(line);
                result.append("\n");
            }
             dis.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result.toString();
    }


    private void generatePersistenceXML(ArrayList tableList, String outDirectory) {
        String className = "";
        TableVO tableVO = null;
        /*TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        tableVO = (TableVO) tableList.get(0);*/
        
        HashMap tableValues = (HashMap) tableList.get(0);
        ArrayList list = (ArrayList) tableValues.get("LIST");
        String lsPersistence = (String) tableValues.get("PERSISTENCE");
        String database = (String) tableValues.get("DATABASE");
        System.out.println("Enter into generateClassLoaderXML");
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        tableVO = (TableVO) list.get(0);
        System.out.println("generatePersistance XML ***********************"+tableVO.getTableName());

        className = HexUtil.initCap(tableVO.getTableName());
        String xmlContent = getPersistenceXML(lsPersistence, database, className, dbVO, tableList);

         outDirectory = outDirectory + "\\WEB-INF\\classes\\META-INF";        
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\persistence.xml";
        HexUtil.writeFile(xmlContent, outputFile);
    }

    public String getPersistenceXML(String persistence, String database,String className, DataBaseVO dbVO,
            ArrayList tableList) {
        StringBuffer result = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\pojo\\jpa\\persistence.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<DataSource>", className);
                line = HexUtil.replaceTags(line, "<LowerClassName>",
                        className.toLowerCase());
                line = HexUtil.replaceTags(line, "<Driver>", dbVO.driverName);
                String url = "";
                //if (line.indexOf("<url>") >= 0) {
                    url = dbVO.getUrl();
                    if (dbVO.getPortNo() != null &&
                            dbVO.getPortNo().trim().length() > 0) {
                        url = url + ":" + dbVO.getPortNo();
                    }
                    if (dbVO.getSchema() != null &&
                            dbVO.getSchema().trim().length() > 0) {
                        url = url + ":" + dbVO.getSchema();
                    }
                //    line = line.replaceAll("<url>", url);
                //}
                line = HexUtil.replaceTags(line, "<username>", dbVO.getUserName());
                line = HexUtil.replaceTags(line, "<password>", dbVO.getPassword());
                line = HexUtil.replaceTags(line, "<ValueObject>", className);
                line = HexUtil.replaceTags(line, "<packageName>", HexUtil.initSmall(className));
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);

                if("JPA(TopLink)".equalsIgnoreCase(persistence)) {
                	line = HexUtil.replaceTags(line, "<ProviderDetails>", "<provider>oracle.toplink.essentials.PersistenceProvider</provider>");
                	line = HexUtil.replaceTags(line, "<PropertyDetails>", "<property name=\"toplink.logging.timestamp\" value=\"false\"/> "+
                														  "<property name=\"toplink.logging.thread\" value=\"false\"/> "+
                														  "<property name=\"toplink.logging.session\" value=\"false\"/> "+
                														  "<property name=\"toplink.jdbc.driver\" value=\"" + dbVO.driverName + "\"/> " +  
                														  "<property name=\"toplink.jdbc.url\" value=\"" + url + "\"/>" +
                														  "<property name=\"toplink.jdbc.user\" value=\"" + dbVO.getUserName()+ "\"/>" +
                														  "<property name=\"toplink.jdbc.password\" value=\"" + dbVO.getPassword()+ "\"/>" +
                														  "<property name=\"toplink.throw.orm.exceptions\" value=\"true\"/>");
                }
                else if("JPA(Hibernate)".equalsIgnoreCase(persistence)) {
                	line = HexUtil.replaceTags(line, "<ProviderDetails>", "<provider>org.hibernate.ejb.HibernatePersistence</provider>");
                	String dialect = "org.hibernate.dialect.OracleDialect";
                    if (database.equalsIgnoreCase("ORACLE")) {
                    	dialect =  "org.hibernate.dialect.OracleDialect";
                    } else if (database.equalsIgnoreCase("MySQL")) {
                    	dialect = "org.hibernate.dialect.MySQLDialect";
                    } else if (database.equalsIgnoreCase("MS-SQL")) {
                    	dialect = "org.hibernate.dialect.SQLServerDialect";
                    } else if (database.equalsIgnoreCase("DB2")) {
                    	dialect = "org.hibernate.dialect.DB2Dialect";
                    } else if (database.equalsIgnoreCase("PointBase")) {
                    	dialect = "org.hibernate.dialect.PointbaseDialect";
                    } else if (database.equalsIgnoreCase("HSQL")) {
                    	dialect = "org.hibernate.dialect.HSQLDialect";
                    } else if (database.equalsIgnoreCase("Sybase")) {
                    	dialect = "org.hibernate.dialect.SybaseDialect";
                    }              	                	                	
                	line = HexUtil.replaceTags(line, "<PropertyDetails>", "<property name=\"hibernate.dialect\" value=\"" + dialect + "\"/>" +
                														  "<property name=\"hibernate.connection.username\" value=\"" + dbVO.getUserName()+ "\"/>" +
                														  "<property name=\"hibernate.connection.password\" value=\"" + dbVO.getPassword()+ "\"/>" +
                														  "<property name=\"hibernate.connection.driver_class\" value=\"" + dbVO.driverName + "\"/> " +
                														  "<property name=\"hibernate.connection.url\" value=\"" + url + "\"/>");  
                	
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

    private void generateClassLoaderXML(ArrayList tableList, String outDirectory) {
        String className = "";
        TableVO tableVO = null;
        /*TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        tableVO = (TableVO) tableList.get(0);*/
        
        HashMap tableValues = (HashMap) tableList.get(0);
        ArrayList list = (ArrayList) tableValues.get("LIST");
        System.out.println("Enter into generateClassLoaderXML");
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        tableVO = (TableVO) list.get(0);
        System.out.println("ClassLoader XML ***********************"+tableVO.getTableName());
        
        className = HexUtil.initCap(tableVO.getTableName());
        String xmlContent = getClassLoaderXMLContent(className);

        outDirectory = outDirectory + "\\WEB-INF\\classes\\META-INF"; 
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\context.xml";
        HexUtil.writeFile(xmlContent, outputFile);
    }

    public String getClassLoaderXMLContent(String className) {
        StringBuffer result = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\pojo\\jpa\\context.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<Application>", className.toLowerCase());
                result.append(line);
                result.append("\n");
            }
            dis.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result.toString();
    }

    private String getPrimaryKeyFields(ArrayList tableList) {
        //System.out.println("Inside getPrimaryKeyFields ");

        int listSize = tableList.size();
        TableVO tableVO = null;
        StringBuffer buffer = new StringBuffer();
        //System.out.println("Inside getPrimaryKeyFields listSize " + listSize);
        if (listSize == 1) {
            for (int count = 0; count < tableList.size(); count++) {
                tableVO = (TableVO) tableList.get(count);
                buffer.append("<id ");
                buffer.append("name=\"");
                buffer.append(tableVO.getColumnName().toLowerCase() + "\">\n ");
                //buffer.append(HexUtil.getJavaDataType(tableVO) + "\">\n");
                buffer.append("\t<column name=\"" + tableVO.getColumnName() + "\"/>\n");
                buffer.append("</id>\n");
            }
        } else {
            buffer.append("<composite-id>\n");
            for (int count = 0; count < tableList.size(); count++) {
                tableVO = (TableVO) tableList.get(count);

                buffer.append("<key-property column=\"");
                buffer.append(tableVO.getColumnName().toUpperCase() + "\" ");
                buffer.append("length=\"");
                buffer.append(tableVO.getDataPrecision() + "\" ");
                buffer.append("name=\"");
                buffer.append(tableVO.getColumnName().toLowerCase() + "\" ");
                buffer.append("type=\"");
                buffer.append(HexUtil.getJavaDataType(tableVO) + "\"/>\n");
            }
            buffer.append("</composite-id>\n");

        }
        //System.out.println("Generated PrimaryKeyFields " + buffer.toString());
        return buffer.toString();
    }

    private String getColumnProperties(ArrayList pkList, ArrayList tableList) {
        //System.out.println("Inside getColumnProperties ");
        TableVO pkVO = null;
        TableVO tableVO = null;
        StringBuffer buffer = new StringBuffer();
        ArrayList nonPKList = new ArrayList();
        Hashtable table = new Hashtable();

        nonPKList = (ArrayList) tableList.clone();

        //System.out.println("PKList SIZE " + pkList.size());
        //System.out.println("tableList SIZE " + tableList.size());

        for (int j = 0; j < pkList.size(); j++) {
            pkVO = (TableVO) pkList.get(j);

            //System.out.println("PK COLUMN " + pkVO.getColumnName());

            if (nonPKList.contains(pkVO)) {
                nonPKList.remove(pkVO);
            //System.out.println("Removing : " + pkVO.getColumnName());
            }
        }
        //System.out.println("nonPKList SIZE " + nonPKList.size());

        for (int i = 0; i < nonPKList.size(); i++) {
            pkVO = (TableVO) nonPKList.get(i);
            if (!"java.util.Date".equals(HexUtil.getJavaDataType(pkVO))) {
                buffer.append("<basic name=\"");
                buffer.append(pkVO.getColumnName().toLowerCase() + "\" />\n");
            } else {
                buffer.append("<basic name=\"");
                buffer.append(pkVO.getColumnName().toLowerCase() + "\">\n");
                buffer.append("\t<temporal>DATE</temporal>\n");
                buffer.append("</basic>\n");
            }
        /*buffer.append("length=\"");
        buffer.append(pkVO.getDataLength() + "\" ");
        buffer.append("name=\"");
        buffer.append(pkVO.getColumnName().toLowerCase() + "\" ");
        buffer.append("type=\"");
        buffer.append(HexUtil.getJavaDataType(pkVO) + "\"/>\n");*/
        }
        //System.out.println("Generated ColumnProperties " + buffer.toString());
        return buffer.toString();
    }

    private void generateHexTemplates(ArrayList tableList, String outDirectory) {

        StringBuffer buffer = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\pojo\\jpa\\HexJpaTemplate.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                buffer.append(line);
                buffer.append("\n");
            }
            dis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        outDirectory = outDirectory + "\\src\\" + lsPackageDir + "\\util";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + "HexJpaTemplate.java";
        HexUtil.writeFile(buffer.toString(), outputFile);
    }

    private String getVOObjectInstance(String className) {
        return "lo" + className + "VO";
    }
}
