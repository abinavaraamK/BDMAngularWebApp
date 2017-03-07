package com.hex.util;

import com.hex.vo.TableVO;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HexIceFacesGenerator
      extends HexFacesGenerator {
      public HexIceFacesGenerator() {
      }


      public String getTemplateDirectory() {
            return "./templates/icefaces/";
      }

      public String getWebTemplate() {
            return "web_icefaces.template";
      }

      public String getFacesConfigTemplate() {
            return "faces-config_icefaces.template";
      }

      public String getListPageTemplate() {
            return "list_icefaces.template";
      }

      public String getAddEditPageTemplate() {
            return "add_edit_icefaces.template";
      }
      
      public String getHomePageTemplate(){
      return "index.template";
      }

      public String getJspPageExtension() {
            return "jspx";
      }

      public String getFacesType() {
            return "icefaces";
      }

      private String prepareHeaderFacet(String psColumnName,
                                        String resourceBundleTablePrefix) {
            StringBuffer lbColumn = new StringBuffer(100);
            lbColumn.append("<f:facet name=\"header\">\n");
            lbColumn.append("<ice:outputText value=\"#{msgs['" + resourceBundleTablePrefix + "_" +
                            psColumnName.toUpperCase() + "']}\" />\n");
            lbColumn.append("</f:facet>\n");
            return lbColumn.toString();
      }

      public void getCodeForListPageData(TableVO tableVO,
                                         StringBuffer listPageContents,
                                         String dataTableRowVar,
                                         String beanClassName, String lsPk,
                                         String resourceBundleTablePrefix) {
            listPageContents.append(" <ice:column>\n");
            listPageContents.append(prepareHeaderFacet(tableVO.getColumnName(),
                  resourceBundleTablePrefix)); 
            listPageContents.append("\n");

            String prop = tableVO.getColumnName().toLowerCase();
            String controlType = tableVO.getControlType();

            if (prop.equalsIgnoreCase(lsPk)) {
                  // command link
                  listPageContents.append(prepareDataWithHyperLinkTag(dataTableRowVar +
                        "." + prop, beanClassName + ".select", "ice"));
                  listPageContents.append("\n");
            }
            else {
                  // normal outputtext
                  if (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
                        listPageContents.append(prepareDateTag(dataTableRowVar + "." + prop, "ice",tableVO.getDateFormat()));
                        listPageContents.append("\n");
                  }
                  /* Begin : Bug # 29 Added By Divya for password column */
                  else if (controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)) {
                        listPageContents.append(preparePasswordTag(dataTableRowVar + "." + prop,"ice"));
                        listPageContents.append("\n");
                  }
                   /* End : Bug # 29 Added By Divya for password column */
                  else {
                        listPageContents.append(prepareDataTag(dataTableRowVar + "." + prop, "ice"));
                        listPageContents.append("\n");
                  }
            }
            listPageContents.append("  </ice:column>\n");
      }
    /*Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal
    in getCodeForEditPageData i have added one param 'Integer i'  */
      public void getCodeForEditPageData(TableVO tableVO,
                                         StringBuffer editPageContents,  StringBuffer addPageContents,
                                         String beanClassName, String lsPk,
                                         String tableNameLC,
                                         String resouceBundleTablePrefix,Integer i) {
            editPageContents.append("<h:panelGrid columns =\"2\">\n");

            /*Bug #14 dated:24/03/2008 updated By:Sanjit Mandal
            Description: i have added one parameter 'tableVo' in below method  prepareEditPagefieldLabel       
            */ 
            editPageContents.append(prepareEditPagefieldLabel(tableVO.isMandatory(),tableVO ,tableVO.getColumnName(),
                  resouceBundleTablePrefix)); // Siva changed to prop name
            String prop = tableVO.getColumnName().toLowerCase();
            String controlType = tableVO.getControlType();
            if (prop.equalsIgnoreCase(lsPk)) {
            /*Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal
            in prepareEditPageTag i have added two param lsPk,i */
                  editPageContents.append(prepareEditPageTag(false, true, tableVO,
                        beanClassName, tableNameLC, "ice" ,lsPk,i));
            }
            else if (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
                  editPageContents.append(prepareEditPageTag(true, tableVO.isReadonly(),
                        tableVO, beanClassName,
                        tableNameLC, "ice" ,lsPk,i));
             /*Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal
             in prepareEditPageTag i have added two param lsPk,i */
                  
            }
            else if (controlType.equalsIgnoreCase(HexFrameConstants.RADIO)) {
                  editPageContents.append(prepareRadioTag(tableVO.isReadonly(),
                        tableVO, beanClassName,
                        tableNameLC, "ice"));

            }
            else if (controlType.equalsIgnoreCase(HexFrameConstants.CHECK_BOX)) {
                  editPageContents.append(prepareCheckBoxTag(tableVO.isReadonly(),
                        tableVO, beanClassName,
                        tableNameLC, "ice"));

            }
            else if (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_AREA)) {
                  editPageContents.append(prepareTextAreaTag(tableVO.isReadonly(),
                        tableVO, beanClassName,
                        tableNameLC, "ice"));

            }
            /*    else if (controlType.equalsIgnoreCase(HexFrameConstants.RADIO)) {
                }*/
            else {
                  String lsTargetTable = tableVO.getTargetTable();
                  if (lsTargetTable != null && lsTargetTable.length() > 0) {

                        editPageContents.append(prepareEditPageSelectTag(tableVO,
                              beanClassName, tableNameLC)); // select field
                  }
                  else {
                     /*Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal
                       in prepareEditPageTag i have added two param lsPk,i */
                        editPageContents.append(prepareEditPageSimpleFieldTag(tableVO,
                              beanClassName, tableNameLC,lsPk,i)); // normal text field
                  }
            }

            editPageContents.append("</h:panelGrid>\n");

      }
      /*Bug #14 dated:24/03/2008 updated By:Sanjit Mandal
        Description: i have added one parameter 'TableVO tableVo' in below method  prepareEditPagefieldLabel       
        */ 
      private String prepareEditPagefieldLabel(boolean isMandatory,TableVO tableVo, String psColumnName,
                                               String resouceBundleTablePrefix) {

            StringBuffer buffer = new StringBuffer();
            buffer.append("<td>");
            buffer.append("\n");
            buffer.append("<ice:outputText value=\"#{msgs['");
            buffer.append(resouceBundleTablePrefix);
            buffer.append("_");
            buffer.append(psColumnName.toUpperCase());
            buffer.append("']}\" />");
            buffer.append("\n");
	 /*Bug #14 dated:24/03/2008 updated By:Sanjit Mandal*/ 
            if(tableVo.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX))  {
                        buffer.append("<h:outputText value=\" ("+tableVo.getDateFormat()+") \" style=\"color:red;\" />\n");
                        }
          /*Bug #14 dated:24/03/2008 updated By:Sanjit Mandal*/  
            if (isMandatory) {
                  buffer.append("<ice:outputText value=\"*\" style=\"color:red;\" />\n");
            }

            buffer.append("</td>\n");

            return buffer.toString();
      }
     /*Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal
     in prepareEditPageSimpleFieldTag and prepareEditPageTag i have added two param lsPk,i */
      private String prepareEditPageSimpleFieldTag(TableVO tableVO,
            String beanClassName,
            String voInstanceName,String lsPk,Integer i) {
            boolean isReadOnly = tableVO.isReadonly();
            return prepareEditPageTag(false, isReadOnly, tableVO, beanClassName,
                                      voInstanceName, "ice",lsPk ,i);
      }

      public String prepareEditPageSelectTag(TableVO tableVO, String beanClassName,
                                             String voInstanceName) {

            String prop = tableVO.getColumnName().toLowerCase();

            StringBuffer buffer = new StringBuffer();
            buffer.append("<td>\n");
            buffer.append("<ice:selectOneMenu ");
            boolean isReadOnly = tableVO.isReadonly();
            if (isReadOnly) {
                  buffer.append("readonly=\"true\" ");
            }
            buffer.append("id=\"");
            buffer.append(prop);
            buffer.append("\" ");
            buffer.append("value=\"#{");
            buffer.append(beanClassName);
            buffer.append(".");
            String lsTargetTable = tableVO.getTargetTable();
            lsTargetTable = lsTargetTable.toLowerCase();
            String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
            String lsValueColumn = tableVO.getValueColumn().toLowerCase();
            String varName1 = "selected" + HexUtil.initCap(lsTargetTable) + HexUtil.initCap(lsKeyColumn) +
                             HexUtil.initCap(lsValueColumn) + "Var";
           String varName2 = lsTargetTable+ HexUtil.initCap(lsKeyColumn) +
                             HexUtil.initCap(lsValueColumn) + "Var";

            buffer.append(varName1);

            buffer.append("}\" >\n");

            buffer.append("<f:selectItems value=\"#{" + beanClassName + "." + varName2 +
                          "}\"  />\n");
            buffer.append("</ice:selectOneMenu> \n");
            buffer.append("</td>\n");
            buffer.append("<ice:message for=\"" + prop + "\" />\n");
            return buffer.toString();
      }

}