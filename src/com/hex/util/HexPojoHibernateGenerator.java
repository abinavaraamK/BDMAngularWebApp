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
public class HexPojoHibernateGenerator {

    private String lsPackage = "";
    private String lsPackageDir = "";
    private String lsDatabase = "";

    public HexPojoHibernateGenerator() {

    }

        public void generateHibernateFiles(ArrayList list) {

        System.out.println("**********Enter into HexPojoHibernateGenerator method*********");
        //String outDirectory = "D:/Out";
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
            generateConfigurationXML(list, outDirectory);
            generateMappingXML(list, outDirectory);
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
                    /*D:\\BaseDataManager\\*/"templates\\pojo\\hibernate\\DaoImpl.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<packageName>", HexUtil.initSmall(className));
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<ValueObject>", valueObject);
                line = HexUtil.replaceTags(line, "<PrimaryKey>", pkMethod);
                line = HexUtil.replaceTags(line, "<ValueObjectInstance>", getVOObjectInstance(valueObject));
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>", psSelectColumnsMethods);
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                if("oracle".equalsIgnoreCase(lsDatabase)) {
                    line = HexUtil.replaceTags(line, "<HexFind>", "HexHibernateTemplate.find(\"from "+ valueObject+"\")");
                    }
                    else{
                    	line = HexUtil.replaceTags(line, "<HexFind>", "HexHibernateTemplate.find(\"from "+valueObject+" as "+ valueObject+"\")");
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

    private String getSelectColumnsDAOMethod(ArrayList poTableList) {
    	System.out.println("getSelectColumnsDAOMethod in Hexpojojpahibcalled");
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < poTableList.size(); index++) {
            TableVO tableVO = (TableVO) poTableList.get(index);
            String lsTargetTable = tableVO.getTargetTable();
            System.out.println("----------------------"+lsTargetTable);
            if (lsTargetTable != null && lsTargetTable.length() > 0) {
                lsTargetTable = lsTargetTable.toLowerCase();
                String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
                String lsValueColumn = tableVO.getValueColumn().toLowerCase();
                String lsMethodName = "getSelect" + HexUtil.initCap(lsTargetTable) +
                        HexUtil.initCap(lsKeyColumn) +
                        HexUtil.initCap(lsValueColumn);
                /*String lsSelectQry = "select " +lsKeyColumn + "," + lsValueColumn +
                " from " + lsTargetTable +" as " + lsTargetTable;*/
                String lsSelectQry = "";
                if("oracle".equalsIgnoreCase(lsDatabase)) {
	                lsSelectQry = "select " +lsKeyColumn+"."+lsTargetTable+", "+lsValueColumn+"."+lsTargetTable+
	               " from " + lsTargetTable;
                }
                else {
                	lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
	                " from " + lsTargetTable + " as " + lsTargetTable;
                }                   
                buffer.append("\tpublic java.util.List " + lsMethodName + "() throws HexApplicationException {\n");
                buffer.append("\t\ttry {\n");
                buffer.append("\t\t\t return HexHibernateTemplate.getRecords(\"" + lsSelectQry + "\",\"" + lsKeyColumn + "\",\"" + lsValueColumn + "\");\n");
                //return HexHibernateTemplate.getRecords("select deptno, dname from dept","deptno","dname");
                buffer.append("\t\t}catch(Exception exception) {\n");
                buffer.append("\t\t\t    throw new HexApplicationException( exception );\n");
                buffer.append("\t\t}\n");
                buffer.append("\t}\n\n");
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
                    /*D:\\BaseDataManager\\*/"templates\\pojo\\hibernate\\DaoInterface.template"));
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

    private void generateConfigurationXML(ArrayList tableList, String outDirectory) {

        TableVO tableVO = null;
        String className = "";
        HashMap tableValues = (HashMap) tableList.get(0);
        ArrayList list = (ArrayList) tableValues.get("LIST");
        System.out.println("Enter into generateClassLoaderXML");
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        tableVO = (TableVO) list.get(0);
        System.out.println("ClassLoader XML ***********************"+tableVO.getTableName());       
        className = HexUtil.initCap(tableVO.getTableName());
        String xmlContent = getConfigurationXMLContent(className, dbVO);
        outDirectory = outDirectory + "\\WEB-INF\\classes\\";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" +
                "hibernate.cfg.xml";
        HexUtil.writeFile(xmlContent, outputFile);
    }

    public String getConfigurationXMLContent(String className, DataBaseVO dbVO) {
        StringBuffer result = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    /*D:\\BaseDataManager\\*/"templates\\pojo\\hibernate\\hibernate.cfg.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<DataSource>", className);

                line = HexUtil.replaceTags(line, "<LowerClassName>",
                        className.toLowerCase());
                line = HexUtil.replaceTags(line, "<Driver>", dbVO.driverName);

                if (lsDatabase.equalsIgnoreCase("ORACLE")) {
                    line = HexUtil.replaceTags(line, "<Dialect>", "org.hibernate.dialect.OracleDialect");
                } else if (lsDatabase.equalsIgnoreCase("MySQL")) {
                    line = HexUtil.replaceTags(line, "<Dialect>", "org.hibernate.dialect.MySQLDialect");
                } else if (lsDatabase.equalsIgnoreCase("MS-SQL")) {
                    line = HexUtil.replaceTags(line, "<Dialect>", "org.hibernate.dialect.SQLServerDialect");
                } else if (lsDatabase.equalsIgnoreCase("DB2")) {
                    line = HexUtil.replaceTags(line, "<Dialect>", "org.hibernate.dialect.DB2Dialect");
                } else if (lsDatabase.equalsIgnoreCase("PointBase")) {
                    line = HexUtil.replaceTags(line, "<Dialect>", "org.hibernate.dialect.PointbaseDialect");
                } else if (lsDatabase.equalsIgnoreCase("HSQL")) {
                    line = HexUtil.replaceTags(line, "<Dialect>", "org.hibernate.dialect.HSQLDialect");
                } else if (lsDatabase.equalsIgnoreCase("Sybase")) {
                    line = HexUtil.replaceTags(line, "<Dialect>", "org.hibernate.dialect.SybaseDialect");
                }
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
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<packageName>", HexUtil.initSmall(className));
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

    private void generateMappingXML(ArrayList tableList, String outDirectory) {

        String className = "";
        TableVO tableVO = null;
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        /*tableVO = (TableVO) tableList.get(0);
        className = HexUtil.initCap(tableVO.getTableName());
        String xmlContent = getMappingXML(className, dbVO, tableList);*/
        
        String xmlContent = getMappingXML(dbVO, tableList);

        outDirectory = outDirectory + "\\WEB-INF\\classes\\";
        HexUtil.makeDirectory(outDirectory);
        //String outputFile = outDirectory + "\\" +  ".hbm.xml";
        String outputFile = outDirectory + "\\" + "test" +
                ".hbm.xml";
        HexUtil.writeFile(xmlContent, outputFile);
    }

    //public String getMappingXML(String className, DataBaseVO dbVO,            ArrayList tableList) {
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
                System.out.println("Class Name in HBM &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + className.trim());
                ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(className);
                System.out.println("Primary Key list size &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + pkDetails.size());
                pkFields = getPrimaryKeyFields(pkDetails);
                System.out.println("Primary key Properties ***********************************" + pkFields);
                // columnProp = getColumnProperties(pkDetails, tableList);
                columnProp = getColumnProperties(pkDetails, list);
                System.out.println("Column Properties ***********************************" + i + columnProp);

                //String buffer added 
                
                columnProperties.append("<class name =\"" + lsPackage +".vo."+ HexUtil.initCap(className) + "\" table=\"" + className.toUpperCase() +"\" lazy=\"false\"" + ">\n");
                columnProperties.append(pkFields);                
                columnProperties.append(columnProp);                
                columnProperties.append("</class>\n");

            }
        
            dis = new DataInputStream(new FileInputStream(
                    /*D:\\BaseDataManager\\*/"templates\\pojo\\hibernate\\table_hbm.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
               /* line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<UserName>", dbVO.getUserName());
                line = HexUtil.replaceTags(line, "<TableName>", className.toUpperCase());
                line = HexUtil.replaceTags(line, "<Primary-Key-Fields>", pkFields);*/
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

    private String getPrimaryKeyFields(ArrayList tableList) {
        //System.out.println("Inside getPrimaryKeyFields ");

        int listSize = tableList.size();
        TableVO tableVO = null;
        StringBuffer buffer = new StringBuffer();
        //System.out.println("Inside getPrimaryKeyFields listSize " + listSize);
        if (listSize == 1) {
            for (int count = 0; count < tableList.size(); count++) {
                tableVO = (TableVO) tableList.get(count);
                buffer.append("<id column=\"");
                buffer.append(tableVO.getColumnName().toUpperCase() + "\" ");
                buffer.append("name=\"");
                buffer.append(tableVO.getColumnName().toLowerCase() + "\" ");
                buffer.append("type=\"");
                buffer.append(HexUtil.getJavaDataType(tableVO) + "\">\n");
                buffer.append("<generator class=\"assigned\"/>\n");
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

            buffer.append("<property column=\"");
            buffer.append(pkVO.getColumnName().toUpperCase() + "\" ");
            buffer.append("length=\"");
            buffer.append(pkVO.getDataLength() + "\" ");
            buffer.append("name=\"");
            buffer.append(pkVO.getColumnName().toLowerCase() + "\" ");
            buffer.append("type=\"");
            buffer.append(HexUtil.getJavaDataType(pkVO) + "\"/>\n");

        }
        //System.out.println("Generated ColumnProperties " + buffer.toString());
        return buffer.toString();
    }

    private void generateHexTemplates(ArrayList tableList, String outDirectory) {

        StringBuffer buffer = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    /*D:\\BaseDataManager\\*/"templates\\pojo\\hibernate\\HexHibernateTemplate.template"));
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
        String outputFile = outDirectory + "\\" + "HexHibernateTemplate.java";
        HexUtil.writeFile(buffer.toString(), outputFile);
        buffer = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    /*D:\\BaseDataManager\\*/"templates\\pojo\\hibernate\\HibernateUtil.template"));
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
        //outDirectory = outDirectory + "\\src\\" + lsPackageDir + "\\util";
        //HexUtil.makeDirectory(outDirectory);
        outputFile = outDirectory + "\\" + "HibernateUtil.java";
        HexUtil.writeFile(buffer.toString(), outputFile);
    }

    private String getVOObjectInstance(String className) {
        return "wo" + className + "VO";
    }
}
