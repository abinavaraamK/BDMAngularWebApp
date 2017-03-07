package com.hex.util;
import com.hex.vo.TableVO;


import java.net.UnknownHostException;


public class HexPrimeFacesGenerator extends HexFacesGenerator {
	String isClassName = "HexPrimeFacesGenerator";

	
	public String getWebTemplate() {

		return "web_jsf.template";
	}


	public String getTemplateDirectory() {
		return "./templates/primefaces/";
	}


	public String getFacesConfigTemplate() {
		return "faces-config_jsf.template";
	}


	public String getListPageTemplate() {
		return "list_jsf_prime.template";
	}

	public String getAddEditPageTemplate() {
		
		return "add_edit_jsf_prime.template";
	}


	public String getHomePageTemplate() {
		return "index.template";
	}


	public String getJspPageExtension() {
		return "xhtml";
	}

	
	public String getFacesType() {

		return "primefaces";
	}

	@Override
	public void getCodeForEditPageData(TableVO tableVO,
			StringBuffer editPageContents, StringBuffer addPageContents,String beanClassName, String lsPk,
			String tableNameLC, String resouceBundleTablePrefix, Integer i)  {
		
		editPageContents.append("<tr>\n");
		
		
		editPageContents.append(prepareEditPagefieldLabel(
				tableVO.isMandatory(), tableVO, tableVO.getColumnName(),
				resouceBundleTablePrefix));
		if( !(tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX1) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.DATE_BOX2) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.TEXT_BOX1) || tableVO.getColumnName().equalsIgnoreCase(HexFrameConstants.TEXT_BOX2))){
			addPageContents.append("<tr>\n");
			addPageContents.append(prepareEditPagefieldLabel(
					tableVO.isMandatory(), tableVO, tableVO.getColumnName(),
					resouceBundleTablePrefix));
		}
		
	
	
		
		String prop = tableVO.getColumnName().toLowerCase();
		String controlType = tableVO.getControlType();
		
		if (prop.equalsIgnoreCase(lsPk)) {
		
			editPageContents.append(prepareEditPageTag4prime (false, true,
					tableVO, beanClassName, tableNameLC, "p", lsPk, i));
			addPageContents.append(prepareEditPageTag4prime (false, true,
					tableVO, beanClassName, tableNameLC, "p", lsPk, i));
			
		}
		
		else if (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
			
			editPageContents.append(prepareEditPageTag4prime (true,
					tableVO.isReadonly(), tableVO, beanClassName, tableNameLC,
					"h", lsPk, i));
			addPageContents.append(prepareEditPageTag4prime (true,
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
					tableVO, beanClassName, tableNameLC, "p"));
			addPageContents.append(prepareTextAreaTag(tableVO.isReadonly(),
					tableVO, beanClassName, tableNameLC, "p"));
			
       }
	 else if (controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX1)) {
			editPageContents.append(prepareEditPageStaticSelectTag(tableVO,
					beanClassName, tableNameLC));
			addPageContents.append(prepareEditPageStaticSelectTag(tableVO,
					beanClassName, tableNameLC));
		}
		 else if ((controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX1)) || (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX2)) || (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX2)) ||(controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX1))){
			 try {
				 
				 editPageContents.append(prepareEditAuditingTag(tableVO.isReadonly(),tableVO,beanClassName, tableNameLC, "p"));
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
						beanClassName, tableNameLC, lsPk, i)); // normal text
				    									// field
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
		
		return prepareEditPageTag4prime (false, isReadOnly, tableVO, beanClassName,
				voInstanceName, "p", lspk, i);
	}

	private String prepareEditPagefieldLabel(boolean isMandatory,
			TableVO tableVo, String psColumnName,
			String resouceBundleTablePrefix) {

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
		
		
		
		
		String varName = tableVO.getColumnName().toLowerCase();
		
		buffer.append("<f:selectItems value=\"#{" + beanClassName + "."
				+ varName + "}\"  />\n");
		buffer.append("</h:selectOneMenu> \n");
		buffer.append("</td>\n");
		buffer.append("<h:message for=\"" + prop + "\" />\n");
		
		return buffer.toString();
	}

	private String prepareHeaderFacet(String psColumnName,
			String resourceBundleTablePrefix) {
		StringBuffer lbColumn = new StringBuffer(100);
		lbColumn.append("<f:facet name=\"header\">\n");
		lbColumn.append("<b><h:outputText value=\"#{msgs."
				+ resourceBundleTablePrefix + "_" + psColumnName.toUpperCase()
				+ "}\" /></b>\n");
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
	
	@Override
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
			
		} else
		{
	
		listPageContents.append("  <p:column>\n");
		listPageContents.append(prepareHeaderFacet(tableVO.getColumnName(),
				resourceBundleTablePrefix));
		listPageContents.append("\n");
        }
		if (prop.equalsIgnoreCase(lsPk)) {
			// command link
			listPageContents.append(prepareDataWithHyperLinkTagForPrime(dataTableRowVar
					+ "." + prop, beanClassName + ".select", "p"));
			listPageContents.append("\n");
			listPageContents.append("  </p:column>\n");
		} else {
			// normal outputtext
			if (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
				listPageContents.append(prepareDateTag(dataTableRowVar + "."
						+ prop, "h",tableVO.getDateFormat()));
				listPageContents.append("\n");
				listPageContents.append("  </p:column>\n");
			}
			/* Begin: Bug#29 Added by Divya for password */
			else if (controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)) {
				listPageContents.append(preparePasswordTagPrime(dataTableRowVar
						+ "." + prop, "p"));
				listPageContents.append("\n");
				listPageContents.append("  </p:column>\n");
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
				listPageContents.append("  </p:column>\n");
			}
		}
		

	}

}
