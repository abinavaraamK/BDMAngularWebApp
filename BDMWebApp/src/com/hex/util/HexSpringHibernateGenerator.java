package com.hex.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.hex.UIController;
import com.hex.dao.TableDAO;
import com.hex.vo.DataBaseVO;
import com.hex.vo.TableVO;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class HexSpringHibernateGenerator {

    private String lsPackage = "";
    private String lsPackageDir = "";
    private String lsDatabase = "";

    public HexSpringHibernateGenerator() {
    }

    public void generateHibernateFiles(ArrayList list, String templatesLocation) {
        System.out.println("**********Enter into generateHibernateFiles method*********");
        //String outDirectory="D:/Out";
        String outDirectory = "";
        String warFile ="";
        for (int i = 0; i < list.size(); i++) {
            HashMap poMap = (HashMap) list.get(i);
            lsPackage = (String) poMap.get("PACKAGE");
            lsPackageDir = (String) poMap.get("PACKAGE_DIR");
            lsDatabase = (String) poMap.get("DATABASE");            
            ArrayList tableList = (ArrayList) poMap.get("LIST");
            outDirectory = (String) poMap.get("DIRECTORY");
            warFile = (String) poMap.get("WAR_FILE");
            generateDAO(tableList, outDirectory,templatesLocation);        
        }
        generateContextXML(list,outDirectory,warFile,templatesLocation);
        generateMappingXML(list,outDirectory,warFile,templatesLocation);
    }

    private void generateDAO(ArrayList tableList, String outDirectory, String templatesLocation) {
        TableVO tableVO = null;
        String tableName = "";

        String lsSelectColumnsMethods = getSelectColumnsDAOMethod(tableList);
        tableVO = (TableVO) tableList.get(0);
        tableName = HexUtil.initCap(tableVO.getTableName());
        String daoContent = getDataAccessObject(tableName, tableName,
                lsSelectColumnsMethods,templatesLocation);

        outDirectory = outDirectory + "\\src\\" + lsPackageDir + "\\dao";
        System.out.println("outDirectory in dao spring"+outDirectory);
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + tableName + "DaoImpl.java";
        HexUtil.writeFile(daoContent, outputFile);

        lsSelectColumnsMethods = getSelectColumnsDAOInterfaceMethod(tableList);
        daoContent = getDataAccessInterfaceObject(tableName, tableName, lsSelectColumnsMethods,templatesLocation);
        outputFile = outDirectory + "\\" + tableName + "Dao.java";
        HexUtil.writeFile(daoContent, outputFile);
    }

    private String getDataAccessObject(String className, String valueObject,
            String psSelectColumnsMethods,String templatesLocation) {
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
        	/*InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\hibernate\\DAO.template");
			 DataInputStream dis = new DataInputStream(inputStream);*/
			 DataInputStream dis = new DataInputStream(new FileInputStream(templatesLocation+
	                    "\\templates\\hibernate\\DAO.template"));
			 
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
                //String lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
                //" from " + lsTargetTable;
                
                String lsSelectQry = "";
                if("oracle".equalsIgnoreCase(lsDatabase)) {
	                lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
	                " from " + lsTargetTable;
                }
                else {
                	lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
	                " from " + lsTargetTable + " as " + lsTargetTable;
                }                
        
                buffer.append("\tpublic java.util.List " + lsMethodName + "() throws HexApplicationException {\n");
                buffer.append("\t\ttry {\n");
                buffer.append(
                        "\t\t        return getHibernateTemplate().executeFind(new HibernateCallback(){\n");
                buffer.append(
                        "\t\t                public Object doInHibernate(Session session) throws HibernateException, SQLException {\n");
                buffer.append(
                        "\t\t                        ArrayList list = new ArrayList();\n");
                buffer.append("\t\t                        try {\n");
                buffer.append(
                        "\t\t                                Iterator results =\n");
                buffer.append(
                        "\t\t                                        session.createSQLQuery(\"" +
                        lsSelectQry + "\")\n");
                buffer.append(
                        "\t\t                                        .addScalar(\"" +
                        lsKeyColumn + "\", Hibernate.STRING)\n");
                buffer.append(
                        "\t\t                                        .addScalar(\"" +
                        lsValueColumn + "\", Hibernate.STRING)\n");
                buffer.append(
                        "\t\t                                        .list().iterator();\n");
                buffer.append(
                        "\t\t                                while ( results.hasNext() ) {\n");
                buffer.append(
                        "\t\t                                    Object[] row = (Object[]) results.next();\n");
                buffer.append(
                        "\t\t                                    list.add ( row );\n");
                buffer.append("\t\t                                }\n");
                buffer.append(
                        "\t\t                        } catch (Exception exp) {\n");
                buffer.append(
                        "\t\t                                exp.printStackTrace();\n");
                buffer.append("\t\t                        }\n");
                buffer.append("\t\t                        return list;\n");
                buffer.append("\t\t                }\n");
                buffer.append("\t\t        });\n");
                buffer.append("\t\t}catch(Exception exception) {\n");
                buffer.append("\t\t\t    throw new HexApplicationException( exception );\n");
                buffer.append("\t\t}\n");
                buffer.append("\t}\n\n");
            }
        }
        return buffer.toString();
    }

    private String getDataAccessInterfaceObject(String className, String valueObject,
            String psSelectColumnsMethods,String templatesLocation) {
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
        	/*InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\hibernate\\DAO_HibernateInterface.template");
			 DataInputStream dis = new DataInputStream(inputStream);*/
			 DataInputStream dis = new DataInputStream(new FileInputStream(templatesLocation+
	                    "\\templates\\hibernate\\DAO_HibernateInterface.template"));
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

    private void generateContextXML(ArrayList tableList,String outDirectory,String warFile,String templatesLocation) {

        TableVO tableVO = null;
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        System.out.println("Database VO *************" + outDirectory);
        String className = "";

        //tableVO = (TableVO) tableList.get(0);
        // className = HexUtil.initCap(tableVO.getTableName());
        //String xmlContent = getAppContextXML(className, dbVO);
        String xmlContent = getAppContextXML(tableList, dbVO,warFile,templatesLocation);

        outDirectory = outDirectory + "\\WEB-INF\\classes\\";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" +
                "applicationContext.xml";
        HexUtil.writeFile(xmlContent, outputFile);
    }

    public String getAppContextXML(ArrayList list, DataBaseVO dbVO,String warFile,String templatesLocation) {

        TableVO tableVO = null;
        String psObjectName = "";
        //DataBaseVO dbVO=null;
        DataInputStream dis = null;
        StringBuffer result = new StringBuffer();

        try {

            StringBuffer beanId = new StringBuffer();
            StringBuffer beanSession = new StringBuffer();

            for (int i = 0; i < list.size(); i++) {
                HashMap poMap = (HashMap) list.get(i);
                psObjectName = (String) poMap.get("TABLE");

                beanId.append("    <bean id =\"" + HexUtil.initCap(psObjectName) + "\" class=\"" + lsPackage + ".service." + HexUtil.initCap(psObjectName) +"Impl"+ "\">\n");
                beanId.append("       <property name =\"" + psObjectName.toLowerCase() + "Dao" + "\">\n");
                beanId.append("       <ref bean =\"" + HexUtil.initCap(psObjectName) + "Dao" + "\"/>\n");
                beanId.append("       </property>\n");
                beanId.append("   </bean>\n");

                beanSession.append("    <bean id =\"" + HexUtil.initCap(psObjectName)+ "Dao" + "\" class=\"" + lsPackage + ".dao." + HexUtil.initCap(psObjectName) + "DaoImpl" + "\">\n");
                beanSession.append("       <property name =\"" + "sessionFactory" + "\">\n");
                beanSession.append("       <ref bean =\"" + "mySessionFactory" + "\"/>\n");
                beanSession.append("       </property>\n");
                beanSession.append("    </bean>\n");
            }

           // InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\hibernate\\applicationContext.template");
			
            dis = new DataInputStream(new FileInputStream(templatesLocation+
                    "\\templates\\hibernate\\applicationContext.template"));
          //  dis = new DataInputStream(inputStream);
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
                line = HexUtil.replaceTags(line, "<username>", dbVO.getUserName());
                line = HexUtil.replaceTags(line, "<password>", dbVO.getPassword());
                //line = HexUtil.replaceTags(line, "<ClassName>", psObjectName);
                
                 line = HexUtil.replaceTags(line, "<hbm-name>", warFile);
                 

                line = HexUtil.replaceTags(line, "<bean-Dao-mappings>", beanId.toString());
                line = HexUtil.replaceTags(line, "<bean-session-mappings>", beanSession.toString());

                //line = HexUtil.replaceTags(line, "<packageName>", HexUtil.initSmall(psObjectName));
                //line = HexUtil.replaceTags(line, "<Package>", lsPackage);
               
                
                result.append(line);
                result.append("\n");

            }
            dis.close();

        } catch (Exception exception) {

            exception.printStackTrace();
        }

        return result.toString();

    }

    private void generateMappingXML(ArrayList tableList,String outDirectory,String warFile,String templatesLocation) {

        System.out.println("Enter into HBM file generation");
        String className = "";
        TableVO tableVO = null;
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        //tableVO = (TableVO) tableList.get(0);

        // className = HexUtil.initCap(tableVO.getTableName());
        //String xmlContent = getMappingXML(className, dbVO, tableList);

        String xmlContent = getMappingXML(dbVO, tableList,templatesLocation);

        outDirectory = outDirectory + "\\WEB-INF\\classes\\";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + warFile +  ".hbm.xml";       
        HexUtil.writeFile(xmlContent, outputFile);
    }

    //public String getMappingXML(String className, DataBaseVO dbVO,          ArrayList tableList) {
    public String getMappingXML(DataBaseVO dbVO, ArrayList tableList, String templatesLocation) {
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

           // InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\hibernate\\table_hbm.template");
			

           // dis = new DataInputStream(inputStream);
            dis = new DataInputStream(new FileInputStream(templatesLocation+
                    "\\templates\\hibernate\\table_hbm.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                //line = HexUtil.replaceTags(line, "<ClassName>", className);
                // line = HexUtil.replaceTags(line, "<UserName>", dbVO.getUserName());
                //  line = HexUtil.replaceTags(line, "<TableName>", className.toUpperCase());
                //line = HexUtil.replaceTags(line, "<Primary-Key-Fields>", columnProperties.toString());
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
       System.out.println("Inside getPrimaryKeyFields ");

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

        System.out.println("**************************Enter into getcolumn properties **************************");
        for (int j = 0; j < pkList.size(); j++) {
            pkVO = (TableVO) pkList.get(j);
            System.out.println("PK COLUMN " + pkVO.getColumnName());
            System.out.println("PK COLUMN TYPE " + pkVO.getDataType());

        if (nonPKList.contains(pkVO)) {
        nonPKList.remove(pkVO);
        System.out.println("Removing : " + pkVO.getColumnName());
        }
        }
        //System.out.println("nonPKList SIZE " + nonPKList.size());


        for (int i = 0; i < nonPKList.size(); i++) {
            //pkVO = (TableVO) nonPKList.get(i);
            //HashMap pVO = (HashMap) nonPKList.get(i);

            //ArrayList list = (ArrayList) pVO.get("LIST");
            //for (int count = 0; count < list.size(); count++) {
            TableVO tabVO = (TableVO) nonPKList.get(i);
            System.out.println("Column Names ********************************" + tabVO.getColumnName());

            buffer.append("<property column=\"");
            buffer.append(tabVO.getColumnName().toUpperCase() + "\" ");
            buffer.append("length=\"");
            buffer.append(tabVO.getDataLength() + "\" ");
            buffer.append("name=\"");
            buffer.append(tabVO.getColumnName().toLowerCase() + "\" ");
            buffer.append("type=\"");
            buffer.append(HexUtil.getJavaDataType(tabVO) + "\"/>\n");
        //}
        }

        //System.out.println("Generated ColumnProperties " + buffer.toString());
        return buffer.toString();
    }

    private String getVOObjectInstance(String className) {
        return "wo" + className + "VO";
    }
}
