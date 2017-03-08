package com.hex.util;

import com.hex.vo.TableVO;
import java.io.*;
import java.net.UnknownHostException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class HexJSFGenerator extends HexFacesGenerator {
	String isClassName = "HexJSFGenerator";

	public HexJSFGenerator() {
	}

	public String getTemplateDirectory() {
		return "./templates/jsf/";
	}

	public String getWebTemplate() {
		return "web_jsf.template";
	}

	public String getFacesConfigTemplate() {
		return "faces-config_jsf.template";
	}

	public String getListPageTemplate() {
		return "list_jsf.template";
	}

	public String getAddEditPageTemplate() {
		return "add_edit_jsf.template";
	}

	public String getHomePageTemplate() {
		return "index.template";
	}

	public String getJspPageExtension() {
		return "jsp";
	}

	public String getFacesType() {
		return "sunfaces";
	}

	public void getCodeForListPageData(TableVO tableVO,
			StringBuffer listPageContents, String dataTableRowVar,
			String beanClassName, String lsPk, String resourceBundleTablePrefix) {
		String prop = tableVO.getColumnName().toLowerCase();
		String controlType = tableVO.getControlType();
		if ((controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX1)) || (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX1)) || (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX2)) || (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX2)))
		{
			listPageContents.append(prepareHeaderFacetAuditing(tableVO.getColumnName(),
					resourceBundleTablePrefix));
			listPageContents.append("\n");
			
		}
		else
		{
		listPageContents.append("  <h:column>\n");
		listPageContents.append(prepareHeaderFacet(tableVO.getColumnName(),
				resourceBundleTablePrefix));
		listPageContents.append("\n");

		}

		if (prop.equalsIgnoreCase(lsPk)) {
			// command link
			listPageContents.append(prepareDataWithHyperLinkTag(dataTableRowVar
					+ "." + prop, beanClassName + ".select", "h"));
			listPageContents.append("\n");
			listPageContents.append("  </h:column>\n");
		} else {
			// normal outputtext
			if (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
				listPageContents.append(prepareDateTag(dataTableRowVar + "."
						+ prop, "h", tableVO.getDateFormat()));
				listPageContents.append("\n");
				listPageContents.append("  </h:column>\n");
			}
			/* Begin: Bug#29 Added by Divya for password */
			else if (controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)) {
				listPageContents.append(preparePasswordTag(dataTableRowVar
						+ "." + prop, "h"));
				listPageContents.append("\n");
				listPageContents.append("  </h:column>\n");
			}
			/* End: Bug#29 Added by Divya for password */
			else if ((controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX1)) || (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX1)) || (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX2)) || (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX2))) {
				listPageContents.append(prepareAuditingTag(dataTableRowVar
						+ "." + prop, "h"));
			
				
			}
			else {
				listPageContents.append(prepareDataTag(dataTableRowVar + "."
						+ prop, "h"));
				listPageContents.append("\n");
				listPageContents.append("  </h:column>\n");
			}
		}
	
	}

	/*
	 * Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal in
	 * getCodeForEditPageData i have added one param 'Integer i'
	 */
	public void getCodeForEditPageData(TableVO tableVO,
			StringBuffer editPageContents,StringBuffer addPageContents, String beanClassName, String lsPk,
			String tableNameLC, String resouceBundleTablePrefix, Integer i) {
		editPageContents.append("<tr>\n");
		
		editPageContents.append(prepareEditPagefieldLabel(
				tableVO.isMandatory(), tableVO, tableVO.getColumnName(),
				resouceBundleTablePrefix));// Siva changed to prop name
		if( !(tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX1) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.DATE_BOX2) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.TEXT_BOX1) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.TEXT_BOX2))){
			addPageContents.append("<tr>\n");
			addPageContents.append(prepareEditPagefieldLabel(
					tableVO.isMandatory(), tableVO, tableVO.getColumnName(),
					resouceBundleTablePrefix));
		}
		
		
		String prop = tableVO.getColumnName().toLowerCase();
		String controlType = tableVO.getControlType();
		
		if (prop.equalsIgnoreCase(lsPk)) {
			editPageContents.append(prepareEditPageTag(false, true, tableVO,
					beanClassName, tableNameLC, "h", lsPk, i));
			
			addPageContents.append(prepareEditPageTag(false, true, tableVO,
					beanClassName, tableNameLC, "h", lsPk, i));
		

		}
		
		else if (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
			editPageContents.append(prepareEditPageTag(true,
					tableVO.isReadonly(), tableVO, beanClassName, tableNameLC,
					"h", lsPk, i));
			addPageContents.append(prepareEditPageTag(true,
					tableVO.isReadonly(), tableVO, beanClassName, tableNameLC,
					"h", lsPk, i));
		
		} else if (controlType.equalsIgnoreCase(HexFrameConstants.RADIO)) {
			editPageContents.append(prepareRadioTag(tableVO.isReadonly(),
					tableVO, beanClassName, tableNameLC, "h"));
			addPageContents.append(prepareRadioTag(tableVO.isReadonly(),
					tableVO, beanClassName, tableNameLC, "h"));
			
		} else if (controlType.equalsIgnoreCase(HexFrameConstants.CHECK_BOX)) {
			editPageContents.append(prepareCheckBoxTag(tableVO.isReadonly(),
					tableVO, beanClassName, tableNameLC, "h"));
			addPageContents.append(prepareCheckBoxTag(tableVO.isReadonly(),
					tableVO, beanClassName, tableNameLC, "h"));
			
		
		} else if (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_AREA)) {
			editPageContents.append(prepareTextAreaTag(tableVO.isReadonly(),
					tableVO, beanClassName, tableNameLC, "h"));
			addPageContents.append(prepareTextAreaTag(tableVO.isReadonly(),
					tableVO, beanClassName, tableNameLC, "h"));
			
		}
		else if (controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX1)) {
			
			editPageContents.append(prepareEditPageStaticSelectTag(tableVO,
					beanClassName, tableNameLC));
			addPageContents.append(prepareEditPageStaticSelectTag(tableVO,
					beanClassName, tableNameLC));
		}
		else if ((controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX1)) || (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX2)) || (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX2)) ||(controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX1))){
			 try {
				 editPageContents.append(prepareEditAuditingTag(tableVO.isReadonly(),
							tableVO, beanClassName, tableNameLC, "h"));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		 }
		else {
			String lsTargetTable = tableVO.getTargetTable();
			if (lsTargetTable != null && lsTargetTable.length() > 0) {
				editPageContents.append(prepareEditPageSelectTag(tableVO,
						beanClassName, tableNameLC)); // select field
				addPageContents.append(prepareEditPageSelectTag(tableVO,
						beanClassName, tableNameLC)); 
				
			} else {
				
				editPageContents.append(prepareEditPageSimpleFieldTag(tableVO,
						beanClassName, tableNameLC, lsPk, i)); 
				addPageContents.append(prepareEditPageSimpleFieldTag(tableVO,
						beanClassName, tableNameLC, lsPk, i));
			}
		} 
		editPageContents.append("</tr>\n");
		if( !(tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX1) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.DATE_BOX2) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.TEXT_BOX1) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.TEXT_BOX2))){
		addPageContents.append("</tr>\n");
		}
	}

	
	private String prepareEditPageSimpleFieldTag(TableVO tableVO,
			String beanClassName, String voInstanceName, String lspk, Integer i) {
		boolean isReadOnly = tableVO.isReadonly();
		
		return prepareEditPageTag(false, isReadOnly, tableVO, beanClassName,
				voInstanceName, "h", lspk, i);
	}

	

	private String prepareEditPagefieldLabel(boolean isMandatory,
			TableVO tableVo, String psColumnName,
			String resouceBundleTablePrefix) {
		/*StringBuffer buffer = new StringBuffer();
		if ((tableVo.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX1)) || (tableVo.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX2)) || (tableVo.getControlType().equalsIgnoreCase(HexFrameConstants.TEXT_BOX2)) || (tableVo.getControlType().equalsIgnoreCase(HexFrameConstants.TEXT_BOX1))){
			
			buffer.append("<td>");
		
		
		}
		else{
		
	
		buffer.append("<td>");
		buffer.append("\n");
		buffer.append("<h:outputText value=\"#{msgs.");
		buffer.append(resouceBundleTablePrefix);
		buffer.append("_");
		buffer.append(psColumnName.toUpperCase());

		buffer.append("}\" />");
		buffer.append("\n");
		}

		if (tableVo.getControlType().equalsIgnoreCase(
				HexFrameConstants.DATE_BOX)) {
			
		}

		if (isMandatory) {
			buffer.append("<h:outputText value=\"*\" style=\"color:red;\" />\n");
		}

		buffer.append("</td>\n");

		return buffer.toString();*/
		StringBuffer buffer = new StringBuffer();
		buffer.append("<td>");
		buffer.append("\n");
		buffer.append("<h:outputText value=\"#{msgs.");
		buffer.append(resouceBundleTablePrefix);
		buffer.append("_");
		buffer.append(psColumnName.toUpperCase());

		buffer.append("}\" />");
		buffer.append("\n");

		/* Bug #14 dated:24/03/2008 updated By:Sanjit Mandal */
		if (tableVo.getControlType().equalsIgnoreCase(
				HexFrameConstants.DATE_BOX)) {
			buffer.append("<h:outputText value=\" ("+tableVo.getDateFormat()+")\" style=\"color:red;\" />\n");

		}
		/* Bug #14 dated:24/03/2008 updated By:Sanjit Mandal */
		if (isMandatory) {
			buffer.append("<h:outputText value=\"*\" style=\"color:red;\" />\n");
		}

		buffer.append("</td>\n");

		return buffer.toString();
	}

	private String prepareHeaderFacet(String psColumnName,
			String resourceBundleTablePrefix) {
		StringBuffer lbColumn = new StringBuffer(100);
		lbColumn.append("<f:facet name=\"header\">\n");
		lbColumn.append("<h:outputText value=\"#{msgs."
				+ resourceBundleTablePrefix + "_" + psColumnName.toUpperCase()
				+ "}\" />\n");
		lbColumn.append("</f:facet>\n");
		return lbColumn.toString();
	}

	private String prepareHeaderFacetAuditing(String psColumnName,
			String resourceBundleTablePrefix) {
		StringBuffer lbColumn = new StringBuffer(100);

		lbColumn.append("<h:inputHidden value=\"#{msgs."
				+ resourceBundleTablePrefix + "_" + psColumnName.toUpperCase()
				+ "}\" />\n");
			return lbColumn.toString();

	}

	public String prepareEditPageSelectTag(TableVO tableVO,
			String beanClassName, String voInstanceName) {

		String prop = tableVO.getColumnName().toLowerCase();

		StringBuffer buffer = new StringBuffer();
		buffer.append("<td>\n");
		buffer.append("<h:selectOneMenu ");
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
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);

		buffer.append("}\" >\n");
		String lsTargetTable = tableVO.getTargetTable();
		lsTargetTable = lsTargetTable.toLowerCase();
		String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
		String lsValueColumn = tableVO.getValueColumn().toLowerCase();
		String varName = lsTargetTable + HexUtil.initCap(lsKeyColumn)
				+ HexUtil.initCap(lsValueColumn) + "Var";

		buffer.append("<f:selectItems value=\"#{" + beanClassName + "."
				+ varName + "}\"  />\n");
		buffer.append("</h:selectOneMenu> \n");
		buffer.append("</td>\n");
		buffer.append("<h:message for=\"" + prop + "\" />\n");
		return buffer.toString();
	}

	public String prepareEditPageStaticSelectTag(TableVO tableVO,
			String beanClassName, String voInstanceName) {

		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<td>\n");
		buffer.append("<h:selectOneMenu ");
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
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);

		buffer.append("}\" >\n");
		
		
		
		
		String varName = "staticSelect";
		
		buffer.append("<f:selectItems value=\"#{" + beanClassName + "."
				+ varName + "}\"  />\n");
		buffer.append("</h:selectOneMenu> \n");
		buffer.append("</td>\n");
		buffer.append("<h:message for=\"" + prop + "\" />\n");
		
		return buffer.toString();
	}

}
