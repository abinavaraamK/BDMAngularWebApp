package com.hex.util;

import java.util.ArrayList;
import com.hex.vo.TableVO;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
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
public class HexSpringIBATISGenerator {

    private String lsPackage = "";
    private String lsPackageDir = "";

    public HexSpringIBATISGenerator() {
    }

    //  public void generateIBatisFiles(HashMap poMap) {
    public void generateIBatisFiles(ArrayList list) {

        String outDirectory = "";

        for (int index = 0; index < list.size(); index++) {
            HashMap poMap = (HashMap) list.get(index);
            lsPackage = (String) poMap.get("PACKAGE");
            lsPackageDir = (String) poMap.get("PACKAGE_DIR");
            outDirectory = (String) poMap.get("DIRECTORY");
            ArrayList tableList = (ArrayList) poMap.get("LIST");
            generateDAO(tableList, outDirectory);
            generateMappingXML(tableList, outDirectory);
        }
        generateContextXML(list, outDirectory);       
        generateSqlMapConfigXML(list, outDirectory);
    }

    private String generateDAO(ArrayList tableList, String outDirectory) {
        TableVO tableVO = null;
        String tableName = "";
        outDirectory = outDirectory + "\\src\\" + lsPackageDir + "\\dao";
        HexUtil.makeDirectory(outDirectory);

        String lsSelectColumnsMethods = getSelectColumnsDAOInterfaceMethod(tableList);
        tableVO = (TableVO) tableList.get(0);
        tableName = HexUtil.initCap(tableVO.getTableName());

        String daoContent = getDAOInterface(tableName, tableName, lsSelectColumnsMethods);
        String outputFile = outDirectory + "\\" + tableName + "Dao.java";
        HexUtil.writeFile(daoContent, outputFile);

        lsSelectColumnsMethods = getSelectColumnsDAOMethod(tableList);
        String sqlMapDaoContent = getSqlMapDAOImpl(tableName, tableName, lsSelectColumnsMethods);
        outputFile = outDirectory + "\\" + tableName + "DaoImpl.java";
        HexUtil.writeFile(sqlMapDaoContent, outputFile);
        return tableName;
    }

    private String getDAOInterface(String className, String valueObject,
            String psSelectColumnsMethods) {
        StringBuffer result = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\ibatis\\DAO_IBATIS.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<ValueObject>", valueObject);
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>",
                        psSelectColumnsMethods);
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

    private String getSqlMapDAOImpl(String className, String valueObject,
            String psSelectColumnsMethods) {
        StringBuffer result = new StringBuffer();
        /*TableVO tableVO = null;
        TableDAO tableDAO = TableDAO.getInstance();
        ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(className);
        String pkMethod = "";
        if (pkDetails.size() == 1) {
        tableVO = (TableVO) pkDetails.get(0);
        pkMethod = HexUtil.initCap(tableVO.getColumnName());
        }*/
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\ibatis\\SqlMapDAO_IBATIS.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<ValueObject>", valueObject);
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>",
                        psSelectColumnsMethods);
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
                        HexUtil.initCap(lsKeyColumn) + HexUtil.initCap(lsValueColumn);
                String lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
                        " from " + lsTargetTable;
                buffer.append("\tpublic java.util.List " + lsMethodName + "() throws HexApplicationException {\n");
                buffer.append("\t\t      ArrayList list = new ArrayList();\n");
                buffer.append("\t\t      try\n");
                buffer.append("\t\t      {\n");
                buffer.append("\t\t             Map map = getSqlMapClientTemplate().queryForMap(\"get" + HexUtil.initCap(lsTargetTable) + "\", null, \"" + lsKeyColumn + "\",\"" + lsValueColumn + "\");\n");
                buffer.append("\t\t             for(Iterator it = map.keySet().iterator(); it.hasNext();) {\n");
                buffer.append("\t\t                  Object[] row = new Object[2];\n");
                buffer.append("\t\t                  Object key = it.next();\n");
                buffer.append("\t\t                  row[0] = key.toString();\n");
                buffer.append("\t\t                  row[1] = map.get(key);\n");
                buffer.append("\t\t                  list.add ( row );\n");
                buffer.append("\t\t             }\n");
                buffer.append("\t\t       }\n");
                buffer.append("\t\t       catch (DataAccessException poExp)\n");
                buffer.append("\t\t       {\n");
                buffer.append("\t\t            throw new HexApplicationException( poExp );\n");
                buffer.append("\t\t       }\n");
                buffer.append("\t\t       return list;\n");
                buffer.append("\t}\n\n");

            }
        }
        return buffer.toString();
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
                        HexUtil.initCap(lsKeyColumn) + HexUtil.initCap(lsValueColumn);
                String lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
                        " from " + lsTargetTable;
                buffer.append("\tpublic java.util.List " + lsMethodName + "() throws HexApplicationException ;\n");
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

                beanId.append("       <bean id =\"" + HexUtil.initCap(psObjectName) + "\" class=\"" + lsPackage + ".service." + HexUtil.initCap(psObjectName) + "Impl" + "\">\n");
                beanId.append("         <property name =\"" + psObjectName.toLowerCase() + "Dao" + "\" ref =\"" + HexUtil.initCap(psObjectName) + "Dao" + "\"/>\n");
                beanId.append("       </bean>\n");

                beanEntity.append("    <bean id =\"" + HexUtil.initCap(psObjectName) + "Dao" + "\" class=\"" + lsPackage + ".dao." + HexUtil.initCap(psObjectName) + "DaoImpl" + "\">\n");
                beanEntity.append("       <property name =\"" + "sqlMapClient" + "\">\n");
                beanEntity.append("       <ref bean=\"" + "sqlMapClient" + "\"/>\n");
                beanEntity.append("       </property>\n");
                beanEntity.append("    </bean>\n");
            }

            dis = new DataInputStream(new FileInputStream(
                    "templates\\ibatis\\applicationContext_IBATIS.template"));
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

   
    public void generateMappingXML(ArrayList tableList, String outDirectory) {

        String className = "";
        TableVO tableVO = null;
        //TableDAO tableDAO = TableDAO.getInstance();
        //DataBaseVO dbVO = tableDAO.getDataBaseVO();

       /* TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();
        String xmlContent = getMappingXML(dbVO, tableList);*/

        tableVO = (TableVO) tableList.get(0);
        className = HexUtil.initCap(tableVO.getTableName());
        String xmlContent = getMappingXML(className, tableList);

        outDirectory = outDirectory + "\\WEB-INF\\classes\\";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\" + className + ".xml";
        HexUtil.writeFile(xmlContent, outputFile);

    }

    private String getMappingXML(String className, //DataBaseVO dbVO,
            ArrayList tableList) {
        StringBuffer result = new StringBuffer();
        TableDAO tableDAO = TableDAO.getInstance();
        ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(className);
        String pkFields = getPrimaryKeyFields(pkDetails);
        String columnProp = getColumnProperties(pkDetails, tableList);
        String lsSelectColumns = getSelectColumns(tableList);
        String lsInsertColumns = getInsertColumns(tableList);
        String lsUpdateDetails = getUpdateDetails(tableList);
        String lsResultMapProperties = getResultMapProperties(tableList);
        String lsDestSelectColumns = getPropertiesForSelectColumns(tableList);

        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\ibatis\\Query_IBATIS.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<ClassName>", className);
                line = HexUtil.replaceTags(line, "<Package>", lsPackage);
                line = HexUtil.replaceTags(line, "<TableName>", className.toUpperCase());
                line = HexUtil.replaceTags(line, "<PrimaryKeyColumn>", pkFields);
                line = HexUtil.replaceTags(line, "<PrimaryKeyVOColumn>", pkFields.toLowerCase());
                line = HexUtil.replaceTags(line, "<SelectColumns>", lsSelectColumns);
                line = HexUtil.replaceTags(line, "<InsertDetails>", lsInsertColumns);
                line = HexUtil.replaceTags(line, "<UpdateDetails>", lsUpdateDetails);
                line = HexUtil.replaceTags(line, "<ResultMapProperties>",
                        lsResultMapProperties);
                line = HexUtil.replaceTags(line, "<ValueObject>", className);
                line = HexUtil.replaceTags(line, "<SelectColumnsMethods>", lsDestSelectColumns);
                result.append(line);
                result.append("\n");
            }
            dis.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result.toString();
    }

    private void generateSqlMapConfigXML(ArrayList tableList, String outDirectory) {

        String className = "";
        TableVO tableVO = null;
        //TableDAO tableDAO = TableDAO.getInstance();
        //DataBaseVO dbVO = tableDAO.getDataBaseVO();
        
        /*tableVO = (TableVO) tableList.get(0);
        className = HexUtil.initCap(tableVO.getTableName());        
        String xmlContent = generateSqlMapConfigContent(className); */
        //, tableList);
        
        TableDAO tableDAO = TableDAO.getInstance();
        DataBaseVO dbVO = tableDAO.getDataBaseVO();       
        String xmlContent = getMappingXML(dbVO, tableList);
        
        outDirectory = outDirectory + "\\WEB-INF\\classes\\";
        HexUtil.makeDirectory(outDirectory);
        String outputFile = outDirectory + "\\SqlMapConfig.xml";
        HexUtil.writeFile(xmlContent, outputFile);

    }

   // private String generateSqlMapConfigContent(String className) {
    
    public String getMappingXML(DataBaseVO dbVO, ArrayList tableList) {
        StringBuffer result = new StringBuffer();
        String className="";
        try {
            StringBuffer sqlMap = new StringBuffer();
            for (int i = 0; i < tableList.size(); i++) {
                HashMap poMap = (HashMap) tableList.get(i);
                className = (String) poMap.get("TABLE");                
                sqlMap.append("         <sqlMap resource= \"" + HexUtil.initCap(className) + ".xml" +  "\"/>\n");
            }
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\ibatis\\SqlMapConfig_IBATIS.template"));
            while (dis.available() > 0) {
                String line = dis.readLine();
                line = HexUtil.replaceTags(line, "<SQLMapName>", sqlMap.toString());
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
                /*buffer.append("<id column=\"");
                buffer.append(tableVO.getColumnName().toUpperCase() + "\" ");
                buffer.append("name=\"");
                buffer.append(tableVO.getColumnName().toLowerCase() + "\" ");
                buffer.append("type=\"");
                buffer.append(getJavaDataType(tableVO) + "\">\n");
                buffer.append("<generator class=\"assigned\"/>\n");
                buffer.append("</id>\n");*/
                buffer.append(tableVO.getColumnName().toUpperCase());
            }
        } else {
            //buffer.append("<composite-id>\n");
            for (int count = 0; count < tableList.size(); count++) {
                tableVO = (TableVO) tableList.get(count);
                /*buffer.append("<key-property column=\"");
                buffer.append(tableVO.getColumnName().toUpperCase() + "\" ");
                buffer.append("length=\"");
                buffer.append(tableVO.getDataPrecision() + "\" ");
                buffer.append("name=\"");
                buffer.append(tableVO.getColumnName().toLowerCase() + "\" ");
                buffer.append("type=\"");
                buffer.append(getJavaDataType(tableVO) + "\"/>\n");*/
                buffer.append(tableVO.getColumnName().toUpperCase());
                break;
            }
        //buffer.append("</composite-id>\n");
        }
        //System.out.println("Generated PrimaryKeyFields " + buffer.toString());
        return buffer.toString();
    }

    private String getSelectColumns(ArrayList tableList) {
        //System.out.println("Inside getSelectColumns ");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tableList.size(); i++) {
            TableVO tableVO = (TableVO) tableList.get(i);
            buffer.append(tableVO.getColumnName().toUpperCase());
            if (i < tableList.size() - 1) {
                buffer.append(", ");
            }
        }
        //System.out.println("Generated getSelectColumns : " + buffer.toString());
        return buffer.toString();
    }

    private String getInsertColumns(ArrayList tableList) {
        //System.out.println("Inside getSelectColumns ");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tableList.size(); i++) {
            TableVO tableVO = (TableVO) tableList.get(i);
            buffer.append("#" + tableVO.getColumnName().toLowerCase() + "#");
            if (i < tableList.size() - 1) {
                buffer.append(", ");
            }
        }
        //System.out.println("Generated getInsertColumns : " + buffer.toString());
        return buffer.toString();
    }

    private String getUpdateDetails(ArrayList tableList) {
        //System.out.println("Inside getSelectColumns ");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tableList.size(); i++) {
            TableVO tableVO = (TableVO) tableList.get(i);
            buffer.append(tableVO.getColumnName().toUpperCase() + " = #" +
                    tableVO.getColumnName().toLowerCase() + "#");
            if (i < tableList.size() - 1) {
                buffer.append(", ");
            }
        }
        //System.out.println("Generated getUpdateDetails : " + buffer.toString());
        return buffer.toString();
    }

    private String getResultMapProperties(ArrayList tableList) {
        //System.out.println("Inside getSelectColumns ");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tableList.size(); i++) {
            TableVO tableVO = (TableVO) tableList.get(i);
            buffer.append("<result property=\"" + tableVO.getColumnName().toLowerCase() +
                    "\" column=\"" + tableVO.getColumnName().toUpperCase() +
                    "\" columnIndex=\"" + (i + 1) + "\"/>\n");
        }
        //System.out.println("Generated getResultMapProperties : " + buffer.toString());
        return buffer.toString();
    }

    /*<resultMap id="getDeptMap" class="java.util.HashMap">
    <result property="deptno" column="DEPTNO"/>
    <result property="dname" column="DNAME"/>
    </resultMap>
    <select id="getDept" resultMap="getDeptMap" >
    SELECT DEPTNO, DNAME FROM DEPT
    </select>*/
    private String getPropertiesForSelectColumns(ArrayList tableList) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < tableList.size(); index++) {
            TableVO tableVO = (TableVO) tableList.get(index);
            String lsTargetTable = tableVO.getTargetTable();
            if (lsTargetTable != null && lsTargetTable.length() > 0) {
                lsTargetTable = lsTargetTable.toLowerCase();
                String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
                String lsValueColumn = tableVO.getValueColumn().toLowerCase();
                String lsSelectQry = "select " + lsKeyColumn + ", " + lsValueColumn +
                        " from " + lsTargetTable;
                buffer.append("\t<resultMap id=\"get" + HexUtil.initCap(lsTargetTable) + "Map\" class=\"java.util.HashMap\">\n");
                buffer.append("\t\t<result property=\"" + lsKeyColumn + "\" column=\"" + lsKeyColumn.toUpperCase() + "\"/>\n");
                buffer.append("\t\t<result property=\"" + lsValueColumn + "\" column=\"" + lsValueColumn.toUpperCase() + "\"/>\n");
                buffer.append("\t</resultMap>\n\n");
                buffer.append("\t<select id=\"get" + HexUtil.initCap(lsTargetTable) + "\" resultMap=\"get" + HexUtil.initCap(lsTargetTable) + "Map\" >\n");
                buffer.append("\t\t" + lsSelectQry + "\n");
                buffer.append("\t</select>\n\n");
            }
        }
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
}
