package com.hex.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.hex.dao.TableDAO;
import com.hex.vo.TableVO;
import com.hex.vo.TemplateVO;

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
public abstract class HexFacesGenerator {

	String isClassName = "HexFacesGenerator";

	public HexFacesGenerator() {

		System.out.println("Enter into HexFacesGenerator");
	}

	public void generateJsfPresentationTierFiles(ArrayList list)
			throws Exception {

		String outDirectory = "";
		System.out.println("Enter into FacesGenerator");
		String ispresent="";
		String lsTheme="";
		for (int i = 0; i < list.size(); i++) {
			HashMap poMap = (HashMap) list.get(i);
			ArrayList tablelist = (ArrayList) poMap.get("LIST");
			String tableName = (String) poMap.get("TABLE");
			outDirectory = (String) poMap.get("DIRECTORY");
			String psPageTitle = (String) poMap.get("PAGE_TITLE");
			System.out.println("*****************Title in generateJsfPresentationTierFiles FacesGenerator"+psPageTitle);
			String lsPackage = (String) poMap.get("PACKAGE");
			String lsPresentation = (String) poMap.get("PRESENTATION");
			 lsTheme = (String) poMap.get("THEME");
			ispresent=lsPresentation;
			TableVO tableVO = (TableVO) tablelist.get(0);
			String beanName = tableVO.getTableName().toLowerCase();
			String tableNameLC = tableVO.getTableName().toLowerCase();
			String beanClassName = HexUtil.initCap(beanName) + "Bean";
			String tableNameInitCap = HexUtil.initCap(beanName);
			String beanPackageName = lsPackage + ".mbean";
			if("RichFaces".equalsIgnoreCase(lsPresentation)) {
				generateJsfRichJSPFiles(tablelist, tableName, psPageTitle, outDirectory);
			}else if("PrimeFaces".equalsIgnoreCase(lsPresentation)) {
				
				generateJsfPrimeJSPFiles(tablelist, tableName, psPageTitle, outDirectory);
			}
			else {
				generateJsfJSPFiles(tablelist, tableName, psPageTitle, outDirectory);
			}
			//generateJsfJSPFiles(tablelist, tableName, psPageTitle, outDirectory);
			generateManagedBeanFile(poMap);
			if ("JSF".equalsIgnoreCase(lsPresentation)) {
				generateJSFCheckBoxRenderer(poMap, beanPackageName);
			} else if ("IceFaces".equalsIgnoreCase(lsPresentation)) {
				generateIceFacesCheckBoxRenderer(poMap, beanPackageName);
			} else if ("RichFaces".equalsIgnoreCase(lsPresentation)) {
				generateRichFacesCheckBoxRenderer(poMap, beanPackageName);
			} else if ("PrimeFaces".equalsIgnoreCase(lsPresentation)) {
				generatePrimeFacesCheckBoxRenderer(poMap, beanPackageName);
			}
		}
		
		generateApplicationResourcesProperties(list, outDirectory);
		if("PrimeFaces".equalsIgnoreCase(ispresent)) {
			generateFacesConfigXML4prime(list, outDirectory);
			generateHomeJSPFile4prime(list, outDirectory);
			generateWebXMLForPrime(lsTheme,outDirectory);
		}
		else{
		generateFacesConfigXML(list, outDirectory);
		generateHomeJSPFile(list, outDirectory);
		generateWebXML(outDirectory);
		}
		
	}
	
	
	public String prepareEditAuditingTag(boolean isReadonly, TableVO tableVO,String beanClassName, String voInstanceName, String prefix) throws UnknownHostException{
		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();

				
		
	if (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX1))
			
		{   buffer.append("<td>");
		    buffer.append("<" + prefix + ":inputText ");
		    buffer.append("value=\"#{");
			buffer.append(beanClassName);
			buffer.append(".");
			buffer.append(voInstanceName);
			buffer.append(".");
			buffer.append(prop);
			buffer.append("}\"");
			buffer.append(" ");
			buffer.append("style=\"");
		    buffer.append("background-color:#d3d3d3;\"");
		    buffer.append(" readonly=\"true\">\n");
		    buffer.append("\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\" timeZone=\"GMT+5.30\"/>");
		    buffer.append("</"+prefix+":inputText>");
			
			
		}
		else if (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX2))
		{
			 buffer.append("<td>");
			    buffer.append("<" + prefix + ":inputText ");
			    buffer.append("value=\"#{");
				buffer.append(beanClassName);
				buffer.append(".");
				buffer.append(voInstanceName);
				buffer.append(".");
				buffer.append(prop);
				buffer.append("}\"");
				buffer.append(" ");
				buffer.append("style=\"");
			    buffer.append("background-color:#d3d3d3;\"");
			    buffer.append(" readonly=\"true\">\n");
			    buffer.append("\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\" timeZone=\"GMT+5.30\"/>");
			    buffer.append("</"+prefix+":inputText>");
			    
				
		}
		else if(tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.TEXT_BOX2)) 
		{
			 buffer.append("<td>");
			    buffer.append("<" + prefix + ":inputText ");
			    buffer.append("value=\"#{");
				buffer.append(beanClassName);
				buffer.append(".");
				buffer.append(voInstanceName);
				buffer.append(".");
				buffer.append(prop);
				buffer.append("}\"");
				buffer.append(" ");
				 buffer.append("style=\"");
				    buffer.append("background-color:#d3d3d3;\"");
			    buffer.append(" readonly=\"true\"/>\n");
			   
			   
				
		}
		else if(tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.TEXT_BOX1))
	    {
		
			 buffer.append("<td>");
			    buffer.append("<" + prefix + ":inputText ");
			    buffer.append("value=\"#{");
				buffer.append(beanClassName);
				buffer.append(".");
				buffer.append(voInstanceName);
				buffer.append(".");
				buffer.append(prop);
				buffer.append("}\"");
				buffer.append(" ");
				buffer.append("style=\"");
			    buffer.append("background-color:#d3d3d3;\"");
				
			    buffer.append(" readonly=\"true\"/>\n");
			    
	    }
		buffer.append("</td>");
		return buffer.toString();
	}





  public String prepareAuditingTag(String psValueAtt,String prefix) {
		StringBuffer lbData = new StringBuffer(100);
		lbData.append("<" + prefix + ":inputHidden value=\"#{");
		lbData.append(psValueAtt);
		lbData.append("}\" />");
		return lbData.toString();
	}

	
	

	public abstract String getWebTemplate();

	/* Bug #19 & 14 dated:19/03/2008 updated By:Sanjit Mandal */
	Integer listcount = 0;
	Integer lcDateReadonlyFalseNullTrue = 0;
	Integer lcDateReadonlyFalseNullFalse = 0;
	Integer lcDateReadonlyTrueNullFalse = 0;
	String voInstanceNameTTT = "";
	String prefixTTT = "";
	String voInstanceNameTTF = "";
	String prefixTTF = "";
	String voInstanceNameTFT = "";
	String prefixTFT = "";
	String voInstanceNameTFF = "";
	String prefixTFF = "";

	/* Bug #19 & 14 dated:19/03/2008 updated By:Sanjit Mandal */

	public abstract String getTemplateDirectory();

	public abstract String getFacesConfigTemplate();

	public abstract String getListPageTemplate();

	public abstract String getAddEditPageTemplate();

	public abstract String getHomePageTemplate();

	public abstract String getJspPageExtension();

	public abstract String getFacesType();

	public void generateJavascriptValidations(StringBuffer buffer,
			TableVO tableVO, String formName) {
		String prop = tableVO.getColumnName().toLowerCase();
		buffer.append("\t\tif ( document.forms[\"" + formName
				+ "Form\"].elements[\"" + formName + "Form:" + prop
				+ "\"].value.length == 0 )\n");
		buffer.append("\t\t{\n");
		buffer.append("\t\t\talert('Please enter the value of "
				+ tableVO.getLabelName() + "');\n");
		buffer.append("\t\t\tdocument.forms[\"" + formName
				+ "Form\"].elements[\"" + formName + "Form:" + prop
				+ "\"].focus();\n");
		buffer.append("\t\t\treturn false;\n");
		buffer.append("\t\t}\n\n");
		/* Begin : Bug#29 Added by Divya for password validation */
		if (tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.PASSWORD)) {
			buffer.append("\t\tif (document.forms[\"" + formName
					+ "Form\"].elements[\"" + formName + "Form:" + prop
					+ "password\"].readOnly == false) {\n "
					+ "\t\tif(!(document.forms[\"" + formName
					+ "Form\"].elements[\"" + formName + "Form:" + prop
					+ "\"].value == document.forms[\"" + formName
					+ "Form\"].elements[\"" + formName + "Form:" + prop
					+ "password" + "\"].value ) )\n");
			buffer.append("\t\t{\n");
			buffer.append("\t\t\talert('" + prop + " does not match!!!');\n");
			buffer.append("\t\t\tdocument.forms[\"" + formName
					+ "Form\"].elements[\"" + formName + "Form:" + prop
					+ "\"].focus();\n");
			buffer.append("\t\t\treturn false;\n");
			buffer.append("\t\t}\n\n");
			buffer.append("\t}\n\n");
		}
		/* End : Bug#29 Added by Divya for password validation */
	}

	/*
	 * Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal in
	 * getCodeForEditPageData i have added one param 'Integer i'
	 */
	public abstract void getCodeForEditPageData(TableVO tableVO,
			StringBuffer editPageContents,StringBuffer addPageContents,  String beanClassName, String lsPk,
			String tableNameLC, String resouceBundleTablePrefix, Integer i) throws UnknownHostException;

	public abstract void getCodeForListPageData(TableVO tableVO,
			StringBuffer listPageContents, String dataTableRowVar,
			String beanClassName, String lsPk, String resourceBundleTablePrefix);

	private void generateWebXML(String outDirectory) {
		try {
			CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
			TemplateVO woTemplateVO = new TemplateVO();
			woTemplateVO.setTemplateDirectory(getTemplateDirectory());
			woTemplateVO.setTemplateFileName(getWebTemplate());
			woTemplateVO.setOutputDirectory(outDirectory + "\\WEB-INF\\");
			woTemplateVO.setOutputFileName("web.xml");
			HashMap map = new HashMap();
			woTemplateVO.setDynamicContent(map);
			woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void generateWebXMLForPrime(String theme,String outDirectory) {
		TemplateVO woTemplateVO = null;
		StringBuffer result = new StringBuffer();
		DataInputStream dis = null;
		FileOutputStream fos = null;
		
		try {
			StringBuffer lsIndex = new StringBuffer();
				woTemplateVO = new TemplateVO();
				woTemplateVO.setTemplateDirectory(getTemplateDirectory());
				System.out.println("Template Directory"
						+ woTemplateVO.getTemplateDirectory());
				woTemplateVO.setOutputDirectory(outDirectory + "\\WEB-INF\\");
				lsIndex.append("<context-param>\n <param-name>" +
						"primefaces.THEME" +"</param-name>\n");
		
				lsIndex.append("<param-value>"+theme+"</param-value>\n" +"</context-param>");
			
			dis = new DataInputStream(new FileInputStream(
					"templates\\primefaces\\web_jsf.template"));
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<warFileList>", lsIndex
						.toString());
				result.append(line);
				result.append("\n");
			}


			HexUtil.writeFile(result.toString(), woTemplateVO.getOutputDirectory() + "\\web.xml");
			System.out.println("Ended with web.xml generation");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		/*try {
			StringBuffer themeCase = new StringBuffer();
			StringBuffer result = new StringBuffer();
			DataInputStream dis = null;
			FileOutputStream fos = null;
			dis = new DataInputStream(new FileInputStream(
			"templates\\primefaces\\web_jsf.template"));

			themeCase.append("<context-param>\n <param-name>" +
							"primefaces.THEME" +"</param-name>\n");
			
			themeCase.append("<param-name>"+theme+"</param-value>\n" +"</context-param>");
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line,"<context-param>",themeCase.toString());
				result.append(line);
				result.append("\n");
			}
			CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
			TemplateVO woTemplateVO = new TemplateVO();
			woTemplateVO.setTemplateDirectory(getTemplateDirectory());
			woTemplateVO.setTemplateFileName(getWebTemplate());
			woTemplateVO.setOutputDirectory(outDirectory + "\\WEB-INF\\");
			woTemplateVO.setOutputFileName("web.xml");
			HashMap map = new HashMap();
			woTemplateVO.setDynamicContent(map);
			woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		} catch (Exception ex) {
			ex.printStackTrace();
		}*/
	}

	private void generateHomeJSPFile4prime(ArrayList list, String outDirectory) {
		TemplateVO woTemplateVO = null;
		HashMap map = new HashMap();
		StringBuffer result = new StringBuffer();
		DataInputStream dis = null;
		FileOutputStream fos = null;

		try {
			StringBuffer lsIndex = new StringBuffer();
			ResourceBundle pr = new PropertyResourceBundle(new FileInputStream(
	          "BaseDataManager.properties"));
			  /*DataInputStream dis = new DataInputStream(new FileInputStream(
	          "BaseDataManager.properties"));*/
			for (int i = 0; i < list.size(); i++) {
				woTemplateVO = new TemplateVO();
				woTemplateVO.setTemplateDirectory(getTemplateDirectory());
				System.out.println("Template Directory"
						+ woTemplateVO.getTemplateDirectory());
				woTemplateVO.setTemplateFileName(getHomePageTemplate());
				woTemplateVO.setOutputDirectory(outDirectory + "\\WEB-INF\\");
				woTemplateVO.setOutputFileName("home.xhtml");
				HashMap poMap = (HashMap) list.get(i);
				String psObjectName = (String) poMap.get("TABLE");
				String warFile = (String) poMap.get("WAR_FILE");
				String pagetitle=(String)poMap.get("PAGE_TITLE");
				lsIndex.append("<a href =\" http://" + pr.getString("server.host") + ":"
						+ pr.getString("server.port") + "/" + warFile
						+ "/faces/pages/" + HexUtil.initCap(psObjectName)
						+ "List.xhtml \">" + pagetitle + "</a>\n\n\n ");
			}
			dis = new DataInputStream(new FileInputStream(
					"templates\\primefaces\\index.template"));
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<warFileList>", lsIndex
						.toString());
				result.append(line);
				result.append("\n");
			}

			String jspDirectory = outDirectory + "\\pages\\";
			HexUtil.makeDirectory(jspDirectory);
			HexUtil.writeFile(result.toString(), jspDirectory + "\\Home.xhtml");
			System.out.println("Ended with faces-config generation");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void generateHomeJSPFile(ArrayList list, String outDirectory) {
		TemplateVO woTemplateVO = null;
		HashMap map = new HashMap();
		StringBuffer result = new StringBuffer();
		DataInputStream dis = null;
		FileOutputStream fos = null;

		try {
			StringBuffer lsIndex = new StringBuffer();
			/*ResourceBundle pr = new PropertyResourceBundle(this.getClass()
					.getResourceAsStream("port.properties"));*/
			ResourceBundle pr = new PropertyResourceBundle(new FileInputStream(
	          "BaseDataManager.properties"));
			for (int i = 0; i < list.size(); i++) {
				woTemplateVO = new TemplateVO();
				woTemplateVO.setTemplateDirectory(getTemplateDirectory());
				System.out.println("Template Directory"
						+ woTemplateVO.getTemplateDirectory());
				woTemplateVO.setTemplateFileName(getHomePageTemplate());
				woTemplateVO.setOutputDirectory(outDirectory + "\\WEB-INF\\");
				woTemplateVO.setOutputFileName("home.jsp");
				HashMap poMap = (HashMap) list.get(i);
				String psObjectName = (String) poMap.get("TABLE");
				String warFile = (String) poMap.get("WAR_FILE");
				String pagetitle=(String)poMap.get("PAGE_TITLE");
				lsIndex.append("<a href =\" http://" +pr.getString("server.host") +":"
						+ pr.getString("server.port") + "/" + warFile
						+ "/faces/pages/" + HexUtil.initCap(psObjectName)
						+ "List.jsp \">" + pagetitle + "</a>\n<br>\n\n ");
			}
			dis = new DataInputStream(new FileInputStream(
					"templates\\jsf\\index.template"));
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<warFileList>", lsIndex
						.toString());
				result.append(line);
				result.append("\n");
			}

			String jspDirectory = outDirectory + "\\pages\\";
			HexUtil.makeDirectory(jspDirectory);
			HexUtil.writeFile(result.toString(), jspDirectory + "\\Home.jsp");
			System.out.println("Ended with faces-config generation");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void generateFacesConfigXML4prime(ArrayList list, String outDirectory) {
		System.out.println("Enter into faces config");
		StringBuffer result = new StringBuffer();
		DataInputStream dis = null;
		FileOutputStream fos = null;

		try {
			// CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
			TemplateVO woTemplateVO = null;
			HashMap map = new HashMap();
			ArrayList templateVOList = new ArrayList();
			StringBuffer navigationCase = new StringBuffer();
			StringBuffer managedBeanList = new StringBuffer();

			for (int i = 0; i < list.size(); i++) {
				woTemplateVO = new TemplateVO();
				woTemplateVO.setTemplateDirectory(getTemplateDirectory());
				System.out.println("Template Directory"
						+ woTemplateVO.getTemplateDirectory());
				woTemplateVO.setTemplateFileName(getFacesConfigTemplate());
				woTemplateVO.setOutputDirectory(outDirectory + "\\WEB-INF\\");
				woTemplateVO.setOutputFileName("faces-config.xml");

				HashMap poMap = (HashMap) list.get(i);
				String beanClassName = (String) poMap.get("TABLE");
				String beanPackageName = (String) poMap.get("PACKAGE");
				System.out.println("Bean class name" + beanClassName);

				navigationCase
						.append("<navigation-case>\n <from-outcome>"
								+ beanClassName.toLowerCase()
								+ "Add</from-outcome> \n");
				navigationCase.append("    <to-view-id>/pages/"
						+ HexUtil.initCap(beanClassName)
						+ "Add.xhtml</to-view-id> \n");
				navigationCase.append("</navigation-case>\n\n");

				navigationCase.append("<navigation-case>\n <from-outcome>"
						+ beanClassName.toLowerCase()
						+ "Edit</from-outcome> \n");
				navigationCase.append("    <to-view-id>/pages/"
						+ HexUtil.initCap(beanClassName)
						+ "Edit.xhtml</to-view-id> \n");
				navigationCase.append("</navigation-case>\n\n");

				navigationCase.append("<navigation-case>\n <from-outcome>"
						+ beanClassName.toLowerCase()
						+ "List</from-outcome> \n");
				navigationCase.append("    <to-view-id>/pages/"
						+ HexUtil.initCap(beanClassName)
						+ "List.xhtml</to-view-id> \n");
				navigationCase.append("</navigation-case>\n\n");

				managedBeanList.append("<managed-bean>\n <managed-bean-name>"
						+ HexUtil.initCap(beanClassName)
						+ "Bean</managed-bean-name> \n");
				managedBeanList.append("    <managed-bean-class>"
						+ beanPackageName + ".mbean."
						+ HexUtil.initCap(beanClassName)
						+ "Bean</managed-bean-class> \n");
				managedBeanList
						.append("    <managed-bean-scope> session </managed-bean-scope> \n");
				managedBeanList.append("</managed-bean>\n");

			}
			 
			dis = new DataInputStream(new FileInputStream(
					"templates\\primefaces\\faces-config_jsf.template"));
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<navigation-case>",
						navigationCase.toString());
				line = HexUtil.replaceTags(line, "<managed-bean>",
						managedBeanList.toString());
				result.append(line);
				result.append("\n");
			}

			String facesConfigDirectory = outDirectory + "\\WEB-INF\\";
			HexUtil.makeDirectory(facesConfigDirectory);
			HexUtil.writeFile(result.toString(), facesConfigDirectory
					+ "\\faces-config.xml");
			System.out.println("Ended with faces-config generation");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void generateFacesConfigXML(ArrayList list, String outDirectory) {
		System.out.println("Enter into faces config");
		StringBuffer result = new StringBuffer();
		DataInputStream dis = null;
		FileOutputStream fos = null;

		try {
			// CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
			TemplateVO woTemplateVO = null;
			HashMap map = new HashMap();
			ArrayList templateVOList = new ArrayList();
			StringBuffer navigationCase = new StringBuffer();
			StringBuffer managedBeanList = new StringBuffer();

			for (int i = 0; i < list.size(); i++) {
				woTemplateVO = new TemplateVO();
				woTemplateVO.setTemplateDirectory(getTemplateDirectory());
				System.out.println("Template Directory"
						+ woTemplateVO.getTemplateDirectory());
				woTemplateVO.setTemplateFileName(getFacesConfigTemplate());
				woTemplateVO.setOutputDirectory(outDirectory + "\\WEB-INF\\");
				woTemplateVO.setOutputFileName("faces-config.xml");

				HashMap poMap = (HashMap) list.get(i);
				String beanClassName = (String) poMap.get("TABLE");
				String beanPackageName = (String) poMap.get("PACKAGE");
				System.out.println("Bean class name" + beanClassName);

				navigationCase
						.append("<navigation-case>\n <from-outcome>"
								+ beanClassName.toLowerCase()
								+ "Add</from-outcome> \n");
				navigationCase.append("    <to-view-id>/pages/"
						+ HexUtil.initCap(beanClassName)
						+ "Add.jsp</to-view-id> \n");
				navigationCase.append("</navigation-case>\n\n");

				navigationCase.append("<navigation-case>\n <from-outcome>"
						+ beanClassName.toLowerCase()
						+ "Edit</from-outcome> \n");
				navigationCase.append("    <to-view-id>/pages/"
						+ HexUtil.initCap(beanClassName)
						+ "Edit.jsp</to-view-id> \n");
				navigationCase.append("</navigation-case>\n\n");

				navigationCase.append("<navigation-case>\n <from-outcome>"
						+ beanClassName.toLowerCase()
						+ "List</from-outcome> \n");
				navigationCase.append("    <to-view-id>/pages/"
						+ HexUtil.initCap(beanClassName)
						+ "List.jsp</to-view-id> \n");
				navigationCase.append("</navigation-case>\n\n");

				managedBeanList.append("<managed-bean>\n <managed-bean-name>"
						+ HexUtil.initCap(beanClassName)
						+ "Bean</managed-bean-name> \n");
				managedBeanList.append("    <managed-bean-class>"
						+ beanPackageName + ".mbean."
						+ HexUtil.initCap(beanClassName)
						+ "Bean</managed-bean-class> \n");
				managedBeanList
						.append("    <managed-bean-scope> session </managed-bean-scope> \n");
				managedBeanList.append("</managed-bean>\n");

			}

			dis = new DataInputStream(new FileInputStream(
					"templates\\jsf\\faces-config_jsf.template"));
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<navigation-case>",
						navigationCase.toString());
				line = HexUtil.replaceTags(line, "<managed-bean>",
						managedBeanList.toString());
				result.append(line);
				result.append("\n");
			}

			String facesConfigDirectory = outDirectory + "\\WEB-INF\\";
			HexUtil.makeDirectory(facesConfigDirectory);
			HexUtil.writeFile(result.toString(), facesConfigDirectory
					+ "\\faces-config.xml");
			System.out.println("Ended with faces-config generation");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void generateApplicationResourcesProperties(ArrayList list,
			String outDirectory) {
		System.out.println("Enter into ResourceBundle");
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
				System.out.println("Table Name in ResourceBundle"
						+ psObjectName);
				lsResourceProperties = psObjectName.toUpperCase()
						+ "_PAGE_TITLE=" + page_title + "\n";
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_LIST_PAGE_TITLE="
								+ page_title + " - List\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_ADD_PAGE_TITLE="
								+ page_title + " - Add\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_EDIT_PAGE_TITLE="
								+ page_title + " - Edit\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_List_Scroller1="
								+ psObjectName + "_List_Scroller1\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_List_Scroller2="
								+ psObjectName + "_List_Scroller2\n");
				ArrayList tableVOList = (ArrayList) poMap.get("LIST");
				for (int count = 0; count < tableVOList.size(); count++) {

					TableVO tableVO = (TableVO) tableVOList.get(count);
					String prop = tableVO.getColumnName().toLowerCase();
					lsResourceProperties = lsResourceProperties
							+ (psObjectName.toUpperCase() + "_"
									+ prop.toUpperCase() + "="
									+ tableVO.getLabelName() + "\n");
				}

				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_BUTTON_ADD=Add\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_BUTTON_UPDATE=Update\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_BUTTON_LIST=List\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_BUTTON_DELETE=Delete\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_CAPTION_DELETE=Delete\n");
				lsResourceProperties = lsResourceProperties
						+ (psObjectName.toUpperCase() + "_BUTTON_SAVE=Save\n");

				resouceBundle.append(lsResourceProperties);
			}

			exceptionProperties = exceptionProperties
					+ ("ERROR.DATANOTAVAILABLE=The value you have entered is not available in the Database...\n");
			resouceBundle.append(exceptionProperties);
			CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
			TemplateVO woTemplateVO = new TemplateVO();

			woTemplateVO.setTemplateDirectory("./templates/jsf/");
			woTemplateVO.setTemplateFileName("appRes.template");

			String configDirectory = outDirectory + "\\WEB-INF\\"
					+ "\\classes\\";
			HexUtil.makeDirectory(configDirectory);

			woTemplateVO.setOutputDirectory(configDirectory); // WEB-INF/
																// classes...
			woTemplateVO.setOutputFileName("ApplicationResources.properties");

			HashMap map = new HashMap();
			map.put("<AppResources>", resouceBundle.toString());

			woTemplateVO.setDynamicContent(map);
			woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
			woTemplateVO
					.setOutputFileName("ApplicationResources_en.properties");
			woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}
	public String prepareDataWithHyperLinkTagForPrime(String psValueAtt,
			String psAction, String prefix) {
		StringBuffer lbData = new StringBuffer(200);
		lbData.append("<" + prefix + ":commandLink action=\"#{");
		lbData.append(psAction);
		lbData.append("}\"\t");
		lbData.append("\tajax=");
		lbData.append("\"false\">\n");
		lbData.append("<" + "h" + ":outputText value=\"#{");
		lbData.append(psValueAtt);
		lbData.append("}\" />\n\t");
		// lbData.append("<f:param name=\"pkValue\" value=\"#{");
		// lbData.append(psValueAtt);
		// lbData.append("}\" />\n");
		lbData.append("</" + prefix + ":commandLink>");
		return lbData.toString();

	}

	public String prepareDataWithHyperLinkTag(String psValueAtt,
			String psAction, String prefix) {
		StringBuffer lbData = new StringBuffer(200);
		lbData.append("<" + prefix + ":commandLink action=\"#{");
		lbData.append(psAction);
		lbData.append("}\"\t>\n");
		lbData.append("<" + "h" + ":outputText value=\"#{");
		lbData.append(psValueAtt);
		lbData.append("}\" />\n\t");
		// lbData.append("<f:param name=\"pkValue\" value=\"#{");
		// lbData.append(psValueAtt);
		// lbData.append("}\" />\n");
		lbData.append("</" + prefix + ":commandLink>");
		return lbData.toString();

	}

	public String prepareDataTag(String psValueAtt, String prefix) {
		StringBuffer lbData = new StringBuffer(100);
		lbData.append("<" + prefix + ":outputText value=\"#{");
		lbData.append(psValueAtt);
		lbData.append("}\" />");
		return lbData.toString();
	}

	public String prepareDateTag(String psValueAtt, String prefix,String DateFormat) {
		StringBuffer lbDate = new StringBuffer(200);
		lbDate.append("<" + prefix + ":outputText value=\"#{");
		lbDate.append(psValueAtt);
		lbDate
				.append("}\" >\n<f:convertDateTime type=\"date\" pattern=\""+DateFormat+"\"/>");
		lbDate.append("</" + prefix + ":outputText>");
		return lbDate.toString();
	}

	/* Begin: Bug#29 Added By Divya for password column */

	public String preparePasswordTag(String psValueAtt, String prefix) {
		// name='commandjsfRow' id='command_id' readonly='true'/> " />
		StringBuffer lbPassword = new StringBuffer(100);
		lbPassword
				.append("<"
						+ prefix
						+ ":inputSecret redisplay=\"true\" readonly=\"true\" style=\"border: 0px;background-color:#D8F8FE\" value=\"#{");
		lbPassword.append(psValueAtt);
		lbPassword.append("}\"/> ");
		return lbPassword.toString();
	}
	public String preparePasswordTagPrime(String psValueAtt, String prefix) {
		// name='commandjsfRow' id='command_id' readonly='true'/> " />
		StringBuffer lbPassword = new StringBuffer(100);
		lbPassword
				.append("<"
						+ prefix
						+ ":password readonly=\"true\" value=\"#{");
		lbPassword.append(psValueAtt);
		lbPassword.append("}\"/> ");
		return lbPassword.toString();
	}
	/* End: Bug#29 Added By Divya for password column */

	private void generateJsfJSPFiles(ArrayList list, String tableName,
			String psPageTitle, String outDirectory) throws Exception {
		/*
		 * List Page dynamic values: PageTitle PageHeading
		 * PageValueChangeListener currentPageValue selectedItemsList
		 * dataTableValue dataTableVar dataTableID dataTableContents
		 * deleteAction
		 */
		/*
		 * Add Edit Page dynamic contents PageTitle PageHeading
		 * AddEditPageContents AddEditBtnID AddEditAction AddEditValue
		 * ListAction ListValue
		 */

		System.out.println("generateJsfJSPFiles start " + isClassName);
		HashMap loListFileHashMap = new HashMap();
		HashMap loAddFileHashMap = new HashMap();
		HashMap loEditFileHashMap = new HashMap();

		TableVO tableVO = (TableVO) list.get(0);
		String beanName = tableVO.getTableName().toLowerCase();
		String tableNameLC = tableVO.getTableName().toLowerCase();

		String beanClassName = HexUtil.initCap(beanName) + "Bean";
		String tableNameInitCap = HexUtil.initCap(beanName);

		TableDAO tableDAO = TableDAO.getInstance();

		ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(beanName);
		String pkMethod = null;
		String lsPk = "";

		if (pkDetails.size() >= 1) {
			pkMethod = HexUtil.initCap(tableVO.getColumnName());
			tableVO = (TableVO) pkDetails.get(0);
			lsPk = tableVO.getColumnName().toLowerCase();
		} else {

			throw new Exception("Primary Key not found for this table!");
		}
		String resourceBundleTablePrefix = tableNameLC.toUpperCase();
		loAddFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loEditFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loListFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loAddFileHashMap.put("<MODE>", "ADD");
		loAddFileHashMap.put("<AddEditBtnID>", "save");
		loAddFileHashMap.put("<AddEditAction>", beanClassName
				+ ".createNewRecord");
		loAddFileHashMap.put("<AddEditValue>", "ADD");
		loAddFileHashMap.put("<ListAction>", beanClassName + ".getList");
		loEditFileHashMap.put("<MODE>", "EDIT");
		loEditFileHashMap.put("<AddEditBtnID>", "update");
		loEditFileHashMap.put("<AddEditAction>", beanClassName
				+ ".updateRecord");
		loEditFileHashMap.put("<AddEditValue>", "UPDATE");
		loEditFileHashMap.put("<ListAction>", beanClassName + ".getList");
		loEditFileHashMap.put("<ListValue>", "List"); // i18n
		String dataTableRowVar = tableNameLC + "Row";
		// loListFileHashMap.put("<PageTitle>", psPageTitle);
		// loListFileHashMap.put("<PageHeading>", "LIST");
		loListFileHashMap.put("<PageValueChangeListener>", beanClassName
				+ ".pageValueChanged");
		loListFileHashMap.put("<currentPageValue>", beanClassName
				+ ".currentPage");
		loListFileHashMap.put("<selectedItemsList>", beanClassName
				+ ".pageList");
		loListFileHashMap.put("<voInstanceName>", tableNameLC);

		loListFileHashMap.put("<VOClassName>", HexUtil.initCap(tableNameLC));

		loListFileHashMap.put("<dataTableVar>", dataTableRowVar);
		loListFileHashMap.put("<dataTableID>", tableNameLC + "List");
		loListFileHashMap.put("<deleteAction>", beanClassName + ".delete");

		StringBuffer listPageContents = new StringBuffer(400);
		StringBuffer editPageContents = new StringBuffer(400);
		 StringBuffer addPageContents = new StringBuffer(400);
		StringBuffer javaScriptValidations = new StringBuffer(200);

		for (int i = 0; i < list.size(); i++) {
			tableVO = (TableVO) list.get(i);
			if (tableVO.isSelect()) {
				getCodeForListPageData(tableVO, listPageContents,
						dataTableRowVar, beanClassName, lsPk,
						resourceBundleTablePrefix);
				/*
				 * Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal in
				 * getCodeForEditPageData i have added one param 'Integer i'
				 */
				getCodeForEditPageData(tableVO, editPageContents,addPageContents,
						beanClassName, lsPk, tableNameLC,
						resourceBundleTablePrefix, i) ;
				// Bug #14 dated:1/04/2008 updated By:Sanjit Mandal
				if (tableVO.isMandatory()) {
					this.generateJavascriptValidations(javaScriptValidations,
							tableVO, tableNameLC);
				}
			}
		} // end for
		/* dated:1/04/2008 commented By:Sanjit Mandal */
		// String editContents = editPageContents.toString();

		/* Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal */
		String customEditContents = editPageContents.toString();
		String customAddContents = addPageContents.toString();
		if (customAddContents.indexOf("<Disabled>") >= 0) {
			customAddContents = customAddContents
					.replaceAll(" <Disabled> ", "");
		}
		if (customAddContents.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NotNullReadonlyTrueNoDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<NotNullReadonlyfalseNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NotNullReadonlyfalseNoDate>", " readonly=\"false\" ");
		}

		if (customAddContents.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullTrue.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TFT> ",
										">\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\" />\n </"
												+ prefixTFT
												+ ":inputText>\n <h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
												+ voInstanceNameTFT + "Form:"
												+ propimage + "'))\" />\n <"
												+ prefixTFT + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customAddContents = customAddContents.replaceAll(
					" <NotNullandReadonlyfalse> ", " readonly=\"false\" ");
		}

		if (customAddContents.indexOf(" <NotNull> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (listcount.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TTT> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\"/>\n </"
												+ prefixTTT
												+ ":inputText>\n <h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
												+ voInstanceNameTTT + "Form:"
												+ propimage + "'))\" />\n <"
												+ prefixTTT + ":message for=\""
												+ propimage + "\" />\n"); // ImageTTT
					}
				}
			}
			customAddContents = customAddContents.replaceAll(" <NotNull> ",
					" readonly=\"false\" ");
		}
		if (customAddContents.indexOf(" <NullReadonlyTrueDate> ") >= 0) {
			if (prefixTTF.equalsIgnoreCase("ice")) {
				customAddContents = customAddContents.replaceAll(" <TTF> ",
						" > </ice:inputText>");
			} else {
				customAddContents = customAddContents.replaceAll(" <TTF> ",
						" > </h:inputText>");
			}
			customAddContents = customAddContents.replaceAll(
					" <NullReadonlyTrueDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customAddContents.indexOf("<NullReadonlyFalseDate>") >= 0) {

			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullFalse.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TFF> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\"/>\n </"
												+ prefixTFF
												+ ":inputText>\n <h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
												+ voInstanceNameTFF + "Form:"
												+ propimage + "'))\" />\n <"
												+ prefixTFF + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customAddContents = customAddContents.replaceAll(
					"<NullReadonlyFalseDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					" <NullReadonlyTrueNoDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customAddContents.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NullReadonlyFalseNoDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<EditReadOnlyTrue>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<EditReadOnlyTrue>", "");
		}
		String addContents = customAddContents.toString();

		if (customEditContents.indexOf(" <Disabled> ") >= 0) {
			customEditContents = customEditContents.replaceAll(" <Disabled> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<NotNullReadonlyTrueNoDate>",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customEditContents.indexOf("<NotNullReadonlyfalseNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<NotNullReadonlyfalseNoDate>", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<EditReadOnlyTrue>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<EditReadOnlyTrue>", "  disabled=\"true\" ");
		}
		if (customEditContents.indexOf(" <NotNull> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (listcount.equals(i)) {
						if (prefixTTT.equalsIgnoreCase("ice")) {
							customEditContents = customEditContents.replaceAll(
									" <TTT> ", " > </ice:inputText>"); // T-True
						} else {
							customEditContents = customEditContents.replaceAll(
									" <TTT> ", " > </h:inputText>"); // //
																		// T-True
						}
					}
				}
			}
			customEditContents = customEditContents.replaceAll(" <NotNull> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullTrue.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customEditContents = customEditContents
								.replaceAll(
										" <TFT> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\"/>\n </"
												+ prefixTFT
												+ ":inputText>\n <h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
												+ voInstanceNameTFT + "Form:"
												+ propimage + "'))\" />\n <"
												+ prefixTFT + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customEditContents = customEditContents.replaceAll(
					" <NotNullandReadonlyfalse> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyTrueDate>") >= 0) {
			if (prefixTTF.equalsIgnoreCase("ice")) {
				customEditContents = customEditContents.replaceAll(" <TTF> ",
						" > </ice:inputText>");
			} else {
				customEditContents = customEditContents.replaceAll(" <TTF> ",
						" > </h:inputText>");
			}
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyTrueDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customEditContents.indexOf("<NullReadonlyFalseDate>") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullFalse.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customEditContents = customEditContents
								.replaceAll(
										" <TFF> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\"/>\n </"
												+ prefixTFF
												+ ":inputText>\n <h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
												+ voInstanceNameTFF + "Form:"
												+ propimage + "'))\" />\n <"
												+ prefixTFF + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyFalseDate> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyTrueNoDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyFalseNoDate> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<passwordReadOnlyfalse>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<passwordReadOnlyfalse>", " readonly =\"false\" ");
		}
		if (customEditContents.indexOf("<passwordReadOnlytrue>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<passwordReadOnlytrue>",
					" readonly =\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (addContents.indexOf("<passwordReadOnlyfalse>") >= 0) {
			addContents = addContents.replaceAll("<passwordReadOnlyfalse>",
					" readonly =\"false\" ");
		}
		if (addContents.indexOf("<passwordReadOnlytrue>") >= 0) {
			addContents = addContents.replaceAll("<passwordReadOnlytrue>",
					" readonly =\"false\" ");
		}
		String editContents = customEditContents.toString();
		/* Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal */

		/*
		 * Bug #19 & 14 dated:1/04/2008 below line Comented by sanjit By:Sanjit
		 * Mandal
		 */
		// String addContents = editContents.replaceAll("readonly=\"true\"",
		// "");

		loListFileHashMap.put("<dataTableContents>", listPageContents
				.toString());
		loListFileHashMap.put("<formid>", tableNameLC + "Form");
		loAddFileHashMap.put("<AddEditPageContents>", addContents);
		loEditFileHashMap.put("<AddEditPageContents>", editContents);

		loEditFileHashMap.put("<formid>", tableNameLC + "Form");
		loAddFileHashMap.put("<formid>", tableNameLC + "Form");
		String lsJavaScriptValidations = javaScriptValidations.toString();
		loEditFileHashMap
				.put("<MandatoryValidations>", lsJavaScriptValidations);
		loAddFileHashMap.put("<MandatoryValidations>", lsJavaScriptValidations);
		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();

		TemplateVO woTemplateVO = new TemplateVO();
		woTemplateVO.setTemplateDirectory(getTemplateDirectory());
		woTemplateVO.setTemplateFileName(getListPageTemplate());
		woTemplateVO.setOutputDirectory(outDirectory + "/pages/");

		// Added by amutha

		String listFile = HexUtil.initCap(tableName) + "List."
				+ getJspPageExtension();
		woTemplateVO.setOutputFileName(listFile);
		woTemplateVO.setDynamicContent(loListFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		woTemplateVO.setTemplateFileName(getAddEditPageTemplate());
		String addFile = HexUtil.initCap(tableName) + "Add."
				+ getJspPageExtension();
		woTemplateVO.setOutputFileName(addFile);
		woTemplateVO.setDynamicContent(loAddFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		String editFile = HexUtil.initCap(tableName) + "Edit."
				+ getJspPageExtension();
		woTemplateVO.setOutputFileName(editFile);
		woTemplateVO.setDynamicContent(loEditFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
	}
	private void generateJsfRichJSPFiles(ArrayList list, String tableName,
			String psPageTitle, String outDirectory) throws Exception {
		/*
		 * List Page dynamic values: PageTitle PageHeading
		 * PageValueChangeListener currentPageValue selectedItemsList
		 * dataTableValue dataTableVar dataTableID dataTableContents
		 * deleteAction
		 */
		/*
		 * Add Edit Page dynamic contents PageTitle PageHeading
		 * AddEditPageContents AddEditBtnID AddEditAction AddEditValue
		 * ListAction ListValue
		 */

		System.out.println("generateJsfRichJSPFiles start " + isClassName);
		HashMap loListFileHashMap = new HashMap();
		HashMap loAddFileHashMap = new HashMap();
		HashMap loEditFileHashMap = new HashMap();

		TableVO tableVO = (TableVO) list.get(0);
		String beanName = tableVO.getTableName().toLowerCase();
		String tableNameLC = tableVO.getTableName().toLowerCase();

		String beanClassName = HexUtil.initCap(beanName) + "Bean";
		String tableNameInitCap = HexUtil.initCap(beanName);

		TableDAO tableDAO = TableDAO.getInstance();

		ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(beanName);
		String pkMethod = null;
		String lsPk = "";

		if (pkDetails.size() >= 1) {
			pkMethod = HexUtil.initCap(tableVO.getColumnName());
			tableVO = (TableVO) pkDetails.get(0);
			lsPk = tableVO.getColumnName().toLowerCase();
		} else {

			throw new Exception("Primary Key not found for this table!");
		}
		String resourceBundleTablePrefix = tableNameLC.toUpperCase();
		loAddFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loEditFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loListFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loAddFileHashMap.put("<MODE>", "ADD");
		loAddFileHashMap.put("<AddEditBtnID>", "save");
		loAddFileHashMap.put("<AddEditAction>", beanClassName
				+ ".createNewRecord");
		loAddFileHashMap.put("<AddEditValue>", "ADD");
		loAddFileHashMap.put("<ListAction>", beanClassName + ".getList");
		loEditFileHashMap.put("<MODE>", "EDIT");
		loEditFileHashMap.put("<AddEditBtnID>", "update");
		loEditFileHashMap.put("<AddEditAction>", beanClassName
				+ ".updateRecord");
		loEditFileHashMap.put("<AddEditValue>", "UPDATE");
		loEditFileHashMap.put("<ListAction>", beanClassName + ".getList");
		loEditFileHashMap.put("<ListValue>", "List"); // i18n
		String dataTableRowVar = tableNameLC + "Row";
		// loListFileHashMap.put("<PageTitle>", psPageTitle);
		// loListFileHashMap.put("<PageHeading>", "LIST");
		loListFileHashMap.put("<PageValueChangeListener>", beanClassName
				+ ".pageValueChanged");
		loListFileHashMap.put("<currentPageValue>", beanClassName
				+ ".currentPage");
		loListFileHashMap.put("<selectedItemsList>", beanClassName
				+ ".pageList");
		loListFileHashMap.put("<voInstanceName>", tableNameLC);

		loListFileHashMap.put("<VOClassName>", HexUtil.initCap(tableNameLC));

		loListFileHashMap.put("<dataTableVar>", dataTableRowVar);
		loListFileHashMap.put("<dataTableID>", tableNameLC + "List");
		loListFileHashMap.put("<deleteAction>", beanClassName + ".delete");

		StringBuffer listPageContents = new StringBuffer(400);
		StringBuffer editPageContents = new StringBuffer(400);
		 StringBuffer addPageContents = new StringBuffer(400);
		StringBuffer javaScriptValidations = new StringBuffer(200);

		for (int i = 0; i < list.size(); i++) {
			tableVO = (TableVO) list.get(i);
			if (tableVO.isSelect()) {
				getCodeForListPageData(tableVO, listPageContents,
						dataTableRowVar, beanClassName, lsPk,
						resourceBundleTablePrefix);
				/*
				 * Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal in
				 * getCodeForEditPageData i have added one param 'Integer i'
				 */
				getCodeForEditPageData(tableVO, editPageContents,addPageContents,
						beanClassName, lsPk, tableNameLC,
						resourceBundleTablePrefix, i);
				// Bug #14 dated:1/04/2008 updated By:Sanjit Mandal
				if (tableVO.isMandatory()) {
					this.generateJavascriptValidations(javaScriptValidations,
							tableVO, tableNameLC);
				}
			}
		} // end for
		/* dated:1/04/2008 commented By:Sanjit Mandal */
		// String editContents = editPageContents.toString();

		/* Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal */
		String customEditContents = editPageContents.toString();
		String customAddContents = addPageContents.toString();
		if (customAddContents.indexOf("<Disabled>") >= 0) {
			customAddContents = customAddContents
					.replaceAll(" <Disabled> ", "");
		}
		if (customAddContents.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NotNullReadonlyTrueNoDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<NotNullReadonlyfalseNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NotNullReadonlyfalseNoDate>", " readonly=\"false\" ");
		}

		if (customAddContents.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullTrue.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TFT> ",
										">\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\"/>\n</rich:calendar> \n <"
												+ prefixTFT + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customAddContents = customAddContents.replaceAll(
					" <NotNullandReadonlyfalse> ", " readonly=\"false\" ");
		}

		if (customAddContents.indexOf(" <NotNull> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (listcount.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TTT> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\"/>\n</rich:calendar> \n  <"
												+ prefixTTT + ":message for=\""
												+ propimage + "\" />\n"); // ImageTTT
					}
				}
			}
			customAddContents = customAddContents.replaceAll(" <NotNull> ",
					" readonly=\"false\" ");
		}
		if (customAddContents.indexOf(" <NullReadonlyTrueDate> ") >= 0) {
			if (prefixTTF.equalsIgnoreCase("ice")) {
				customAddContents = customAddContents.replaceAll(" <TTF> ",
						" > </ice:inputText>");
			} else {
				customAddContents = customAddContents.replaceAll(" <TTF> ",
						" > </h:inputText>");
			}
			customAddContents = customAddContents.replaceAll(
					" <NullReadonlyTrueDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customAddContents.indexOf("<NullReadonlyFalseDate>") >= 0) {

			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullFalse.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TFF> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\"/>\n</rich:calendar> \n <"
												+ prefixTFF + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customAddContents = customAddContents.replaceAll(
					"<NullReadonlyFalseDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					" <NullReadonlyTrueNoDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customAddContents.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NullReadonlyFalseNoDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<EditReadOnlyTrue>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<EditReadOnlyTrue>", "");
		}
		String addContents = customAddContents.toString();

		if (customEditContents.indexOf(" <Disabled> ") >= 0) {
			customEditContents = customEditContents.replaceAll(" <Disabled> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<NotNullReadonlyTrueNoDate>",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customEditContents.indexOf("<NotNullReadonlyfalseNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<NotNullReadonlyfalseNoDate>", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<EditReadOnlyTrue>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<EditReadOnlyTrue>", "  disabled=\"true\" ");
		}
		if (customEditContents.indexOf(" <NotNull> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (listcount.equals(i)) {
						if (prefixTTT.equalsIgnoreCase("ice")) {
							customEditContents = customEditContents.replaceAll(
									" <TTT> ", " > </ice:inputText>"); // T-True
						} else {
							customEditContents = customEditContents.replaceAll(
									" <TTT> ", " > </h:inputText>"); // //
																		// T-True
						}
					}
				}
			}
			customEditContents = customEditContents.replaceAll(" <NotNull> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullTrue.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customEditContents = customEditContents
								.replaceAll(
										" <TFT> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\"/>\n</rich:calendar> \n <"
												+ prefixTFT + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customEditContents = customEditContents.replaceAll(
					" <NotNullandReadonlyfalse> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyTrueDate>") >= 0) {
			if (prefixTTF.equalsIgnoreCase("ice")) {
				customEditContents = customEditContents.replaceAll(" <TTF> ",
						" > </ice:inputText>");
			} else {
				customEditContents = customEditContents.replaceAll(" <TTF> ",
						" > </h:inputText>");
			}
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyTrueDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customEditContents.indexOf("<NullReadonlyFalseDate>") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullFalse.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customEditContents = customEditContents
								.replaceAll(
										" <TFF> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\"/>\n</rich:calendar> \n <"
												+ prefixTFF + ":message for=\""
												+ propimage + "\" />\n");
					}
				}
			}
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyFalseDate> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyTrueNoDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyFalseNoDate> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<passwordReadOnlyfalse>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<passwordReadOnlyfalse>", " readonly =\"false\" ");
		}
		if (customEditContents.indexOf("<passwordReadOnlytrue>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<passwordReadOnlytrue>",
					" readonly =\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (addContents.indexOf("<passwordReadOnlyfalse>") >= 0) {
			addContents = addContents.replaceAll("<passwordReadOnlyfalse>",
					" readonly =\"false\" ");
		}
		if (addContents.indexOf("<passwordReadOnlytrue>") >= 0) {
			addContents = addContents.replaceAll("<passwordReadOnlytrue>",
					" readonly =\"false\" ");
		}
		String editContents = customEditContents.toString();
		/* Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal */

		/*
		 * Bug #19 & 14 dated:1/04/2008 below line Comented by sanjit By:Sanjit
		 * Mandal
		 */
		// String addContents = editContents.replaceAll("readonly=\"true\"",
		// "");

		loListFileHashMap.put("<dataTableContents>", listPageContents
				.toString());
		loListFileHashMap.put("<formid>", tableNameLC + "Form");
		loAddFileHashMap.put("<AddEditPageContents>", addContents);
		loEditFileHashMap.put("<AddEditPageContents>", editContents);

		loEditFileHashMap.put("<formid>", tableNameLC + "Form");
		loAddFileHashMap.put("<formid>", tableNameLC + "Form");
		String lsJavaScriptValidations = javaScriptValidations.toString();
		loEditFileHashMap
				.put("<MandatoryValidations>", lsJavaScriptValidations);
		loAddFileHashMap.put("<MandatoryValidations>", lsJavaScriptValidations);
		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();

		TemplateVO woTemplateVO = new TemplateVO();
		woTemplateVO.setTemplateDirectory(getTemplateDirectory());
		woTemplateVO.setTemplateFileName(getListPageTemplate());
		woTemplateVO.setOutputDirectory(outDirectory + "/pages/");

		// Added by amutha

		String listFile = HexUtil.initCap(tableName) + "List."
				+ getJspPageExtension();
		woTemplateVO.setOutputFileName(listFile);
		woTemplateVO.setDynamicContent(loListFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		woTemplateVO.setTemplateFileName(getAddEditPageTemplate());
		String addFile = HexUtil.initCap(tableName) + "Add."
				+ getJspPageExtension();
		woTemplateVO.setOutputFileName(addFile);
		woTemplateVO.setDynamicContent(loAddFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		String editFile = HexUtil.initCap(tableName) + "Edit."
				+ getJspPageExtension();
		woTemplateVO.setOutputFileName(editFile);
		woTemplateVO.setDynamicContent(loEditFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
	}

	
	
private void generateJsfPrimeJSPFiles(ArrayList list, String tableName,
			String psPageTitle, String outDirectory) throws Exception {
		/*
		 * List Page dynamic values: PageTitle PageHeading
		 * PageValueChangeListener currentPageValue selectedItemsList
		 * dataTableValue dataTableVar dataTableID dataTableContents
		 * deleteAction
		 */
		/*
		 * Add Edit Page dynamic contents PageTitle PageHeading
		 * AddEditPageContents AddEditBtnID AddEditAction AddEditValue
		 * ListAction ListValue
		 */

		System.out.println("generateJsfPrimeJSPFiles start " + isClassName);
		HashMap loListFileHashMap = new HashMap();
		HashMap loAddFileHashMap = new HashMap();
		HashMap loEditFileHashMap = new HashMap();

		TableVO tableVO = (TableVO) list.get(0);
		String beanName = tableVO.getTableName().toLowerCase();
		String tableNameLC = tableVO.getTableName().toLowerCase();

		String beanClassName = HexUtil.initCap(beanName) + "Bean";
		String tableNameInitCap = HexUtil.initCap(beanName);

		TableDAO tableDAO = TableDAO.getInstance();

		ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(beanName);
		String pkMethod = null;
		String lsPk = "";

		if (pkDetails.size() >= 1) {
			pkMethod = HexUtil.initCap(tableVO.getColumnName());
			tableVO = (TableVO) pkDetails.get(0);
			lsPk = tableVO.getColumnName().toLowerCase();
		} else {

			throw new Exception("Primary Key not found for this table!");
		}
		
		String resourceBundleTablePrefix = tableNameLC.toUpperCase();
		
		
		loAddFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loEditFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		loListFileHashMap.put("<resourceBundleTablePrefix>",
				resourceBundleTablePrefix);
		
		loAddFileHashMap.put("<resourceBunbleTableintcapprefix>",
				beanClassName);
		loEditFileHashMap.put("<resourceBunbleTableintcapprefix>",
				beanClassName);
		loListFileHashMap.put("<resourceBunbleTableintcapprefix>",
				beanClassName);
		
		loAddFileHashMap.put("<resourceBunbleTablesmallrefix>",
				tableNameLC);
		loEditFileHashMap.put("<resourceBunbleTablesmallrefix>",
				tableNameLC);
		loListFileHashMap.put("<resourceBunbleTablesmallrefix>",
				tableNameLC);
		
		
		
		
		loAddFileHashMap.put("<MODE>", "ADD");
		loAddFileHashMap.put("<AddEditBtnID>", "save");
		loAddFileHashMap.put("<AddEditAction>", beanClassName
				+ ".createNewRecord");
		loAddFileHashMap.put("<AddEditValue>", "ADD");
		loAddFileHashMap.put("<ListAction>", beanClassName + ".getList");
		loEditFileHashMap.put("<MODE>", "EDIT");
		loEditFileHashMap.put("<AddEditBtnID>", "update");
		loEditFileHashMap.put("<AddEditAction>", beanClassName
				+ ".updateRecord");
		loEditFileHashMap.put("<AddEditValue>", "UPDATE");
		loEditFileHashMap.put("<ListAction>", beanClassName + ".getList");
		loEditFileHashMap.put("<ListValue>", "List"); // i18n
		String dataTableRowVar = tableNameLC + "Row";
		// loListFileHashMap.put("<PageTitle>", psPageTitle);
		// loListFileHashMap.put("<PageHeading>", "LIST");
		loListFileHashMap.put("<PageValueChangeListener>", beanClassName
				+ ".pageValueChanged");
		loListFileHashMap.put("<currentPageValue>", beanClassName
				+ ".currentPage");
		loListFileHashMap.put("<selectedItemsList>", beanClassName
				+ ".pageList");
		loListFileHashMap.put("<voInstanceName>", tableNameLC);

		loListFileHashMap.put("<VOClassName>", HexUtil.initCap(tableNameLC));

		loListFileHashMap.put("<dataTableVar>", dataTableRowVar);
		loListFileHashMap.put("<dataTableID>", tableNameLC + "List");
		loListFileHashMap.put("<deleteAction>", beanClassName + ".delete");

		StringBuffer listPageContents = new StringBuffer(400);
		StringBuffer editPageContents = new StringBuffer(400);
		StringBuffer addPageContents = new StringBuffer(400);
		
		// StringBuffer addPageContents = new StringBuffer(400);
		StringBuffer javaScriptValidations = new StringBuffer(200);

		for (int i = 0; i < list.size(); i++) {
			tableVO = (TableVO) list.get(i);
			if (tableVO.isSelect()) {
				getCodeForListPageData(tableVO, listPageContents,
						dataTableRowVar, beanClassName, lsPk,
						resourceBundleTablePrefix);
				/*
				 * Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal in
				 * getCodeForEditPageData i have added one param 'Integer i'
				 */
				getCodeForEditPageData(tableVO, editPageContents,addPageContents,
						beanClassName, lsPk, tableNameLC,
						resourceBundleTablePrefix, i);
				// Bug #14 dated:1/04/2008 updated By:Sanjit Mandal
				if (tableVO.isMandatory()) {
					this.generateJavascriptValidations(javaScriptValidations,
							tableVO, tableNameLC);
				}
			}
		} // end for
		/* dated:1/04/2008 commented By:Sanjit Mandal */
		// String editContents = editPageContents.toString();

		/* Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal */
		String customEditContents = editPageContents.toString();
		String customAddContents = addPageContents.toString();
		if (customAddContents.indexOf("<Disabled>") >= 0) {
			customAddContents = customAddContents
					.replaceAll(" <Disabled> ", "");
		}
		if (customAddContents.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NotNullReadonlyTrueNoDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<NotNullReadonlyfalseNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NotNullReadonlyfalseNoDate>", " readonly=\"false\" ");
		}

		if (customAddContents.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullTrue.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TFT> ",
										">\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\" />\n</p:calendar> \n ");
					}
				}
			}
			customAddContents = customAddContents.replaceAll(
					" <NotNullandReadonlyfalse> ", " readonly=\"false\" ");
		}

		if (customAddContents.indexOf(" <NotNull> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (listcount.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TTT> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\" />\n</p:calendar> \n "); // ImageTTT
					}
				}
			}
			customAddContents = customAddContents.replaceAll(" <NotNull> ",
					" readonly=\"false\" ");
		}
		if (customAddContents.indexOf(" <NullReadonlyTrueDate> ") >= 0) {
				customAddContents = customAddContents.replaceAll(" <TTF> ",
						" > </p:inputText>");
			customAddContents = customAddContents.replaceAll(
					" <NullReadonlyTrueDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customAddContents.indexOf("<NullReadonlyFalseDate>") >= 0) {

			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullFalse.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customAddContents = customAddContents
								.replaceAll(
										" <TFF> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\" />\n</p:calendar> \n");
					}
				}
			}
			customAddContents = customAddContents.replaceAll(
					"<NullReadonlyFalseDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					" <NullReadonlyTrueNoDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customAddContents.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<NullReadonlyFalseNoDate>", " readonly=\"false\" ");
		}
		if (customAddContents.indexOf("<EditReadOnlyTrue>") >= 0) {
			customAddContents = customAddContents.replaceAll(
					"<EditReadOnlyTrue>", "");
		}
		String addContents = customAddContents.toString();

		if (customEditContents.indexOf(" <Disabled> ") >= 0) {
			customEditContents = customEditContents.replaceAll(" <Disabled> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<NotNullReadonlyTrueNoDate>",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customEditContents.indexOf("<NotNullReadonlyfalseNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<NotNullReadonlyfalseNoDate>", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<EditReadOnlyTrue>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<EditReadOnlyTrue>", "  disabled=\"true\" ");
		}
		if (customEditContents.indexOf(" <NotNull> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (listcount.equals(i)) {
							customEditContents = customEditContents.replaceAll(
									" <TTT> ", " > </p:inputText>"); // T-True	
					}
				}
			}
			customEditContents = customEditContents.replaceAll(" <NotNull> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullTrue.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customEditContents = customEditContents
								.replaceAll(
										" <TFT> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\" />\n</p:calendar> \n");
					}
				}
			}
			customEditContents = customEditContents.replaceAll(
					" <NotNullandReadonlyfalse> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyTrueDate>") >= 0) {
			
				customEditContents = customEditContents.replaceAll(" <TTF> ",
						" > </p:inputText>");
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyTrueDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ");
		}
		if (customEditContents.indexOf("<NullReadonlyFalseDate>") >= 0) {
			for (int i = 0; i < list.size(); i++) {
				tableVO = (TableVO) list.get(i);
				if (tableVO.isSelect()) {
					if (lcDateReadonlyFalseNullFalse.equals(i)) {
						String propimage = tableVO.getColumnName()
								.toLowerCase();
						customEditContents = customEditContents
								.replaceAll(
										" <TFF> ",
										" >\n<f:convertDateTime type=\"date\" pattern=\""+tableVO.getDateFormat()+"\" />\n</p:calendar> \n");
					}
				}
			}
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyFalseDate> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyTrueNoDate> ",
					" readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (customEditContents.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					" <NullReadonlyFalseNoDate> ", " readonly=\"false\" ");
		}
		if (customEditContents.indexOf("<passwordReadOnlyfalse>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<passwordReadOnlyfalse>", " readonly =\"false\" ");
		}
		if (customEditContents.indexOf("<passwordReadOnlytrue>") >= 0) {
			customEditContents = customEditContents.replaceAll(
					"<passwordReadOnlytrue>",
					" readonly =\"true\" style=\"background-color:#d3d3d3;\" ");
		}
		if (addContents.indexOf("<passwordReadOnlyfalse>") >= 0) {
			addContents = addContents.replaceAll("<passwordReadOnlyfalse>",
					" readonly =\"false\" ");
		}
		if (addContents.indexOf("<passwordReadOnlytrue>") >= 0) {
			addContents = addContents.replaceAll("<passwordReadOnlytrue>",
					" readonly =\"false\" ");
		}
		String editContents = customEditContents.toString();
		/* Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal */

		/*
		 * Bug #19 & 14 dated:1/04/2008 below line Comented by sanjit By:Sanjit
		 * Mandal
		 */
		// String addContents = editContents.replaceAll("readonly=\"true\"",
		// "");

		loListFileHashMap.put("<dataTableContents>", listPageContents
				.toString());
		loListFileHashMap.put("<formid>", tableNameLC + "Form");
		loAddFileHashMap.put("<AddEditPageContents>", addContents);
		loEditFileHashMap.put("<AddEditPageContents>", editContents);

		loEditFileHashMap.put("<formid>", tableNameLC + "Form");
		loAddFileHashMap.put("<formid>", tableNameLC + "Form");
		String lsJavaScriptValidations = javaScriptValidations.toString();
		loEditFileHashMap
				.put("<MandatoryValidations>", lsJavaScriptValidations);
		loAddFileHashMap.put("<MandatoryValidations>", lsJavaScriptValidations);
		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();

		TemplateVO woTemplateVO = new TemplateVO();
		woTemplateVO.setTemplateDirectory(getTemplateDirectory());
		woTemplateVO.setTemplateFileName(getListPageTemplate());
		woTemplateVO.setOutputDirectory(outDirectory + "/pages/");

		// Added by amutha

		String listFile = HexUtil.initCap(tableName) + "List.xhtml";
		woTemplateVO.setOutputFileName(listFile);
		woTemplateVO.setDynamicContent(loListFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		woTemplateVO.setTemplateFileName(getAddEditPageTemplate());
		String addFile = HexUtil.initCap(tableName) + "Add.xhtml";
		woTemplateVO.setOutputFileName(addFile);
		woTemplateVO.setDynamicContent(loAddFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
		String editFile = HexUtil.initCap(tableName) + "Edit.xhtml";
		woTemplateVO.setOutputFileName(editFile);
		woTemplateVO.setDynamicContent(loEditFileHashMap);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
	}

private void generateManagedBeanFile(HashMap poMap) {

		System.out.println("Enter into  Managed Bean");
		ArrayList list = (ArrayList) poMap.get("LIST");
		String outDirectory = (String) poMap.get("DIRECTORY");
		String psPageTitle = (String) poMap.get("PAGE_TITLE");
		System.out.println("*****************Title in generateManagedBeanFile FacesGenerator"+psPageTitle);
		
		String lsPackage = (String) poMap.get("PACKAGE");
		HashMap mBeanContents = new HashMap();
		String beanPackageName = "";
		String beanClassName = "";
		/*String conditionupdate="";
		String conditionadd="";*/
		for (int count = 0; count < list.size(); count++) {
			TableVO tableVO = (TableVO) list.get(count);
			String beanName = tableVO.getTableName().toLowerCase();
			String tableNameLC = tableVO.getTableName().toLowerCase();
			beanClassName = HexUtil.initCap(beanName) + "Bean";
			String tableNameInitCap = HexUtil.initCap(beanName);
			beanPackageName = lsPackage + ".mbean";
			String column_name=tableVO.getColumnName();
	
			/*if(column_name.equalsIgnoreCase("createddate")){
				conditionadd="try{"+tableNameLC+".setCreatedby(InetAddress.getLocalHost().getHostName());"+tableNameLC+".setModifiedby(InetAddress.getLocalHost().getHostName());	}catch(UnknownHostException e){e.printStackTrace();}"+tableNameLC+".setCreateddate(new Date());"+tableNameLC+".setModifieddate(new Date());";
				conditionupdate="try{"+tableNameLC+".setModifiedby(InetAddress.getLocalHost().getHostName());}catch(UnknownHostException e){e.printStackTrace();}"+tableNameLC+".setModifieddate(new Date());";
				
				mBeanContents.put("<conditionadd>", conditionadd);
				mBeanContents.put("<conditionupdate>", conditionupdate);
			}*/
			
			mBeanContents.put("<Package>", lsPackage);
			mBeanContents.put("<packageName>", beanPackageName);
			mBeanContents.put("<VOClassImport>", lsPackage + ".vo."
					+ tableNameInitCap);
			mBeanContents.put("<BusinessDelegateClassImport>", lsPackage
					+ ".delegate." + tableNameInitCap + "BusinessDelegate");
			mBeanContents.put("<MBeanClassName>", beanClassName);
			mBeanContents.put("<VOClassName>", tableNameInitCap);
			mBeanContents.put("<voInstanceName>", tableNameLC);
			mBeanContents.put("<formid>", tableNameLC + "Form");
		
			/*mBeanContents.put("<conditionadd>", conditionadd);
			mBeanContents.put("<conditionupdate>", conditionupdate);*/
		}

		/*mBeanContents.put("<conditionadd>", conditionadd);
		mBeanContents.put("<conditionupdate>", conditionupdate);*/
		// populateSelectColumnsMBeanMethod(mBeanContents, list,
		// tableNameInitCap);
		populateSelectColumnsMBeanMethod(mBeanContents, list);
		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
		TemplateVO woTemplateVO = new TemplateVO();
		woTemplateVO.setTemplateDirectory(getTemplateDirectory());
		woTemplateVO.setTemplateFileName("mbean_jsf.template");
		// String packageDirectory = beanPackageName.replaceAll(".", "/");
		String packageDirectory = replaceMethod(beanPackageName);

		// woTemplateVO.setOutputDirectory("D:/Out/" + "src/" + packageDirectory
		// + "/");
		woTemplateVO.setOutputDirectory(outDirectory + "src/"
				+ packageDirectory + "/");
		System.out.println("Out Directory" + woTemplateVO.getOutputDirectory());

		woTemplateVO.setOutputFileName(beanClassName + ".java");
		woTemplateVO.setDynamicContent(mBeanContents);
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);
	}

private void populateSelectColumnsMBeanMethod(HashMap map,
			ArrayList poTableList) {
		
		// SelectMethodInstanceVariables
		StringBuffer selectMethodInstanceVariables = new StringBuffer(100);
		StringBuffer selectMethodGettersSetters = new StringBuffer(200);
		StringBuffer selectMdData = new StringBuffer(200);

		StringBuffer buffer = new StringBuffer();
		boolean blnObjectCreated = false;
		for (int index = 0; index < poTableList.size(); index++) {

			TableVO tableVO = (TableVO) poTableList.get(index);

			String beanName = tableVO.getTableName().toLowerCase();
			String tableNameInitCap = HexUtil.initCap(beanName);
			String psClassName = tableNameInitCap + "BusinessDelegate";

			String lsTargetTable = tableVO.getTargetTable();
			
			
			String[] lsTargetStaticSelect=(String[])tableVO.getLabelsStaticSelect();
			//System.out.println("lsTargetStaticSelect b'4....");
			//System.out.println("lsTargetTable2..........."+lsTargetTable2);
			
			System.out.println("lsTargetStaticSelect...."+lsTargetStaticSelect);

			if (lsTargetTable != null && lsTargetTable.length() > 0) {

				selectMethodInstanceVariables.append("private ArrayList ");
				lsTargetTable = lsTargetTable.toLowerCase();
				String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
				String lsValueColumn = tableVO.getValueColumn().toLowerCase();
				String lsMethodName = HexUtil.initCap(lsTargetTable)
						+ HexUtil.initCap(lsKeyColumn)
						+ HexUtil.initCap(lsValueColumn);
				String varName = lsTargetTable + HexUtil.initCap(lsKeyColumn)
						+ HexUtil.initCap(lsValueColumn) + "Var";
				selectMethodInstanceVariables.append(varName
						+ " = new ArrayList();\n");
				String getMethodName = HexUtil.initCap(lsTargetTable)
						+ HexUtil.initCap(lsKeyColumn)
						+ HexUtil.initCap(lsValueColumn) + "Var";

				System.out.println("Get Method Name" + getMethodName);
				System.out.println(" HexUtil.initCap(lsTargetTable)"
						+ HexUtil.initCap(lsTargetTable));
				System.out.println(" HexUtil.initCap(lsKeyColumn)"
						+ HexUtil.initCap(lsKeyColumn));
				System.out.println("  HexUtil.initCap(lsValueColumn)"
						+ HexUtil.initCap(lsValueColumn));

				selectMethodGettersSetters
						.append("public ArrayList get" + getMethodName
								+ "(){\n\treturn " + varName + ";\n}\n");
				selectMethodGettersSetters.append("public void set"
						+ getMethodName + "(ArrayList " + varName
						+ "){\n\tthis." + varName + "=" + varName + ";\n}\n");

				// adding one more method in case of icefaces...
				if ("icefaces".equals(getFacesType())) {
					String varName2 = "selected"
							+ HexUtil.initCap(lsTargetTable)
							+ HexUtil.initCap(lsKeyColumn)
							+ HexUtil.initCap(lsValueColumn) + "Var";
					selectMethodInstanceVariables.append("private String ");
					selectMethodInstanceVariables.append(varName2);
					selectMethodInstanceVariables.append(";\n");

					String getMethodName2 = "Selected"
							+ HexUtil.initCap(lsTargetTable)
							+ HexUtil.initCap(lsKeyColumn)
							+ HexUtil.initCap(lsValueColumn) + "Var";

					// String getMethodName2 = "Selected" +
					// HexUtil.initCap(lsTargetTable) +
					// HexUtil.initCap(lsKeyColumn) + "Var";

					selectMethodGettersSetters.append("public String get");
					selectMethodGettersSetters.append(getMethodName2);
					selectMethodGettersSetters.append("(){\n\treturn "
							+ varName2 + ";\n}\n");
					StringBuffer sVOsetterStatement = new StringBuffer(100);

					String dataType = HexUtil.getDataType(tableVO);
					String tableNameLC = tableVO.getTableName().toLowerCase();
					String property1 = HexUtil.initCap(tableVO.getColumnName());
					sVOsetterStatement.append(tableNameLC);
					sVOsetterStatement.append(".set");
					sVOsetterStatement.append(property1);
					if ("Integer".equals(dataType)) {
						sVOsetterStatement.append(" (new Integer (");
						sVOsetterStatement.append(varName2);
						sVOsetterStatement.append("));");

					} else if ("Double".equals(dataType)) {
						sVOsetterStatement.append(" (new Double (");
						sVOsetterStatement.append(varName2);
						sVOsetterStatement.append("));");

					} else if ("Date".equals(dataType)) {
						sVOsetterStatement.append(" (new java.util.Date(");
						sVOsetterStatement.append(varName2);
						sVOsetterStatement.append("));");
					} else {
						// String data type.
						sVOsetterStatement.append(" (");
						sVOsetterStatement.append(varName2);
						sVOsetterStatement.append(");");
					}

					selectMethodGettersSetters.append("public void set");
					selectMethodGettersSetters.append(getMethodName2);
					selectMethodGettersSetters.append("(String " + varName2
							+ "){\n");
					selectMethodGettersSetters.append("this." + varName2
							+ " = " + varName2 + ";\n");
					selectMethodGettersSetters.append(sVOsetterStatement);
					selectMethodGettersSetters.append("\n}\n");
					// end icefaces - variable & method addition.

				}

				if (!blnObjectCreated) {
					selectMdData.append("\t\ttry{\n");
					selectMdData.append("\t\t" + psClassName + " lo"
							+ psClassName + " = new " + psClassName + "();\n");
				}
				selectMdData.append("\t\tList lo" + lsMethodName + " = lo"
						+ psClassName + ".getSelect" + lsMethodName + "();\n");
				blnObjectCreated = true;
				selectMdData.append("for(int index=0;index<lo" + lsMethodName
						+ ".size();index++){\n");
				selectMdData.append("Object[] obj1 = (Object[])lo"
						+ lsMethodName + ".get(index);\n");

				String KeyDataType = tableVO.getDataType();
				if ("icefaces".equals(getFacesType())) {
					selectMdData
							.append(varName
									+ ".add(new SelectItem(obj1[0].toString(),obj1[1].toString()));\n}\n");
				} else {

					if ("NUMBER".equalsIgnoreCase(KeyDataType.trim())) {
						if (tableVO.getDataLength().intValue() > 6) {
							selectMdData
									.append(varName
											+ ".add(new SelectItem(new Integer((String)obj1[0]),obj1[1].toString()));\n}\n");
						} else {
							selectMdData
									.append(varName
											+ ".add(new SelectItem(new Integer((String)obj1[0]),obj1[1].toString()));\n}\n");
						}
					} else {
						selectMdData
								.append(varName
										+ ".add(new SelectItem(obj1[0].toString(),obj1[1].toString()));\n}\n");
					}
					if (blnObjectCreated) {
						selectMdData
								.append("\t\t\t} catch (HexApplicationException exception) {\n");
						selectMdData.append("\t\t\t       exception.printStackTrace();\n");
						selectMdData
								.append("\t\t\t       FacesContext.getCurrentInstance ().addMessage ( \"Error\",new FacesMessage ( FacesMessage.SEVERITY_INFO, exception.getMessage(), exception.getMessage()));\n");
						selectMdData.append("\t\t\t} catch (Exception ex) {\n");
						selectMdData.append("\t\t\t       ex.printStackTrace();\n");
						selectMdData
								.append("\t\t\t       FacesContext.getCurrentInstance ().addMessage ( \"Error\",new FacesMessage ( FacesMessage.SEVERITY_INFO, ex.getMessage(), ex.getMessage()));\n");
						selectMdData.append("\t\t\t}");
			
					}
					map.put("<SelectMethodInstanceVariables>",
							selectMethodInstanceVariables.toString());

					map.put("<selectMethodGettersSetters>", selectMethodGettersSetters
							.toString());

					System.out.println("Inside select OPTION");

					System.out.println(" select getter method"
							+ selectMethodGettersSetters.toString());

					map.put("<selectMethods>", selectMdData.toString());
				}
			}
			
			
			
			
			else if(lsTargetStaticSelect != null && lsTargetStaticSelect.length > 0) {
				
				selectMethodInstanceVariables.append("private ArrayList ");
				String StaticSelect = "StaticSelect";
				System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE  "+StaticSelect);
				String lsMethodName =HexUtil.initCap(tableVO.getColumnName().toLowerCase());
				String lsVariableName=tableVO.getColumnName();
				selectMethodInstanceVariables.append(lsVariableName
						+ " = new ArrayList();\n");

				selectMethodGettersSetters
						.append("public ArrayList get" + lsMethodName
								+ "(){\n\treturn " + lsVariableName + ";\n}\n");
				selectMethodGettersSetters.append("public void set"
						+ lsMethodName + "(ArrayList " + lsVariableName
						+ "){\n\tthis." + lsVariableName + "=" + lsVariableName + ";\n}\n");

				if (!blnObjectCreated) {
					//selectMdData.append("\t\ttry{\n");
					
				}
				List templist=new ArrayList();
				blnObjectCreated = true;
				System.out.println("lsTargetStaticSelect.length----------------->"+lsTargetStaticSelect.length);
			for(int i=0;i<lsTargetStaticSelect.length;i++){
				System.out.println(lsTargetStaticSelect[i]+";;;;;;;;;;;;;;;;;;;;;;");
				templist.add(lsTargetStaticSelect[i]);
			}
			System.out.println(templist);
			
			for(int a=0;a<lsTargetStaticSelect.length;a++){
				selectMdData.append(""+lsVariableName+".add(new SelectItem(\""+lsTargetStaticSelect[a]+"\"));");
			}
			
			
		

		if (blnObjectCreated) {
			/*selectMdData
					.append("\t\t\t} catch (HexApplicationException exception) {\n");
			selectMdData.append("\t\t\t       exception.printStackTrace();\n");
			selectMdData
					.append("\t\t\t       FacesContext.getCurrentInstance ().addMessage ( \"Error\",new FacesMessage ( FacesMessage.SEVERITY_INFO, exception.getMessage(), exception.getMessage()));\n");
			selectMdData.append("\t\t\t} catch (Exception ex) {\n");
			selectMdData.append("\t\t\t       ex.printStackTrace();\n");
			selectMdData
					.append("\t\t\t       FacesContext.getCurrentInstance ().addMessage ( \"Error\",new FacesMessage ( FacesMessage.SEVERITY_INFO, ex.getMessage(), ex.getMessage()));\n");
			selectMdData.append("\t\t\t}");
*/
		}
		map.put("<SelectMethodInstanceVariables>",
				selectMethodInstanceVariables.toString());

		map.put("<selectMethodGettersSetters>", selectMethodGettersSetters
				.toString());

		System.out.println("Inside select OPTION");

		System.out.println(" select getter method"
				+ selectMethodGettersSetters.toString());

		map.put("<selectMethods>", selectMdData.toString());

		
			}
	}
		map.put("<SelectMethodInstanceVariables>",
				selectMethodInstanceVariables.toString());

		map.put("<selectMethodGettersSetters>", selectMethodGettersSetters
				.toString());

		System.out.println("Inside select OPTION");

		System.out.println(" select getter method"
				+ selectMethodGettersSetters.toString());

		map.put("<selectMethods>", selectMdData.toString());
	}

private String replaceMethod(String name) {
		char ch = '.';
		char ch1 = '/';
		int len = name.length();
		for (int i = 0; i < len; i++) {
			if ((name.charAt(i)) == (ch)) {
				name = name.replace(name.charAt(i), ch1);
			}
		}
		return name;
	}
	
public String prepareEditPageTag_Rich(boolean isDate, boolean isReadOnly,
			TableVO tableVO, String beanClassName, String voInstanceName,
			String prefix, String Pk, Integer i) {
		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();
		/* Bug #19 ,14 dated:01/04/2008 updated By:Sanjit Mandal */
		int n = 0;
		String lsPk = Pk.toString();
		/* Bug #19 dated:01/04/2008 updated By:Sanjit Mandal */
		buffer.append("<td>\n");
		// add by jay krishna Bug no:10 dated 20/03
		if (tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.PASSWORD)) {
			buffer.append("<" + prefix + ":inputSecret redisplay=\"true\"");
		} // end
		else if (tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.DATE_BOX)) {
			buffer
					.append("<rich:calendar  popup=\"true\" cellWidth=\"24px\" cellHeight=\"22px\" style=\"width:200px\"  datePattern=\""+tableVO.getDateFormat()+"\"");
		} else {
			buffer.append("<" + prefix + ":inputText ");
		}
		/* Bug #19 dated:31/03/2008 updated By:Sanjit Mandal */
		if (prop.equalsIgnoreCase(lsPk)) {
			buffer.append(" <Disabled> "); // Disabled
		}
		if (!prop.equalsIgnoreCase(lsPk)) {
			if (tableVO.getNullable().equals(n)) {
				if (tableVO.getControlType().equalsIgnoreCase(
						HexFrameConstants.DATE_BOX)) {
					if (tableVO.isReadonly()) {
						buffer.append(" <NotNull> ");
					} else {
						buffer.append(" <NotNullandReadonlyfalse> ");
					}
				} else {
					if (tableVO.isReadonly()) {
						buffer.append(" <NotNullReadonlyTrueNoDate> ");
					} else {
						buffer.append(" <NotNullReadonlyfalseNoDate> ");
					}
				}
			} else if (tableVO.getControlType().equalsIgnoreCase(
					HexFrameConstants.DATE_BOX)) {
				if (tableVO.isReadonly()) {
					buffer.append(" <NullReadonlyTrueDate> ");
				} else {
					buffer.append(" <NullReadonlyFalseDate> ");
				}
			} else {
				if (tableVO.isReadonly()) {
					buffer.append(" <NullReadonlyTrueNoDate> ");
				} else {
					buffer.append(" <NullReadonlyFalseNoDate> ");
				}
			}
		}
		/* Bug #19 dated:31/03/2008 updated By:Sanjit Mandal */
		/* Bug #19 dated:31/03/2008 below Commented By:Sanjit Mandal */

		// if (isReadOnly) {
		// buffer.append("readonly=\"true\" ");
		// }
		
		Integer dataLength = new Integer(10);
		if (!isDate) {
			buffer.append("size=\"");
			dataLength = tableVO.getDataLength();
			buffer.append(dataLength);
			buffer.append("\" ");
			buffer.append("maxlength=\"");
			buffer.append(dataLength);
			buffer.append("\" ");
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

		buffer.append("}\" ");
		/*
		 * Bug #14 dated:31/03/2008 updated By:Sanjit Mandal T-True, F-False
		 */
		if ((isDate && tableVO.isReadonly() && tableVO.getNullable().equals(n))) {
			listcount = i;
			buffer.append(" <TTT> "); // T-True
			voInstanceNameTTT = voInstanceName;
			prefixTTT = prefix;
		} else if (isDate && !tableVO.isReadonly()
				&& tableVO.getNullable().equals(n)) {
			lcDateReadonlyFalseNullTrue = i;
			buffer.append(" <TFT> "); // T-True, F-False
			voInstanceNameTFT = voInstanceName;
			prefixTFT = prefix;
		} else if (isDate && tableVO.isReadonly()
				&& !tableVO.getNullable().equals(n)) {
			lcDateReadonlyTrueNullFalse = i;
			buffer.append(" <TTF> "); // T-True, F-False
			voInstanceNameTTF = voInstanceName;
			prefixTTF = prefix;
		} else if (isDate && !tableVO.isReadonly()
				&& !tableVO.getNullable().equals(n)) {
			lcDateReadonlyFalseNullFalse = i;
			buffer.append(" <TFF> "); // T-True, F-False
			voInstanceNameTFF = voInstanceName;
			prefixTFF = prefix;
		} else {
			buffer.append(" />\n");
			buffer.append("<" + prefix + ":message for=\"" + prop + "\" />\n");
		}
		/* Bug # 14 dated:31/03/2008 updated By:Sanjit Mandal */

		/* Bug #14 dated:31/03/2008 Commented By:Sanjit Mandal */
		/*
		 * if (isDate) { buffer.append(
		 * " >\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\"/>\n");
		 * buffer.append("</" + prefix + ":inputText>\n"); buffer.append(
		 * "<h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
		 * + voInstanceName + "Form:" + prop + "'))\" />\n"); buffer.append("<"
		 * + prefix + ":message for=\"" + prop + "\" />\n"); }
		 */
		buffer.append("</td>\n");
		/* Begin: Bug#29 Added by Divya for password controlType */
		if (tableVO.controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)
				&& ((!tableVO.isReadonly()) || (tableVO.isReadonly() && tableVO
						.getNullable() == 0))) {

			buffer.append("</tr>\n");
			buffer.append("<tr>\n");
			buffer
					.append("<td>");
			buffer.append("\n");
			buffer.append("Confirm ");
			buffer.append("<h:outputText value=\"");
			buffer.append(prop);
			buffer.append("\" />");
			if (tableVO.mandatory) {
				buffer
						.append("<h:outputText value=\"*\" style=\"color:red;\" />\n");
			}
			buffer.append("</td>");
			buffer.append("<td>\n");
			buffer.append("<" + prefix + ":inputSecret  ");
			if (!tableVO.isReadonly()) {
				buffer.append(" <passwordReadOnlyfalse>");
			} else if (Integer.parseInt(tableVO.getNullable().toString()) == 0) {
				buffer.append("<passwordReadOnlytrue>");
			}
			buffer.append("size=\"");
			buffer.append(dataLength);
			buffer.append("\" ");
			buffer.append("maxlength=\"");
			buffer.append(dataLength);
			buffer.append("\" ");
			buffer.append("id=\"");
			String proppasswrd = prop + "password";
			buffer.append(proppasswrd);
			buffer.append("\" ");
			buffer.append("value=\"#{");
			buffer.append(beanClassName);
			buffer.append(".");
			buffer.append(voInstanceName);
			buffer.append(".");
			buffer.append(prop);
			buffer.append("}\" ");
			buffer.append(" />\n");
			buffer.append("</td>\n");
		}
		/* End: Bug#29 Added by Divya for password controlType */
		return buffer.toString();
	}

	/*
	 * Bug #19 & 14 dated:1/04/2008 updated By:Sanjit Mandal in
	 * prepareEditPageTag i have added two param String Pk,Integer i
	 */
	public String prepareEditPageTag(boolean isDate, boolean isReadOnly,
			TableVO tableVO, String beanClassName, String voInstanceName,
			String prefix, String Pk, Integer i) {
		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();
		/* Bug #19 ,14 dated:01/04/2008 updated By:Sanjit Mandal */
		int n = 0;
		String lsPk = Pk.toString();
		/* Bug #19 dated:01/04/2008 updated By:Sanjit Mandal */
		buffer.append("<td>\n");
		// add by jay krishna Bug no:10 dated 20/03
		if (tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.PASSWORD)) {
			buffer.append("<" + prefix + ":inputSecret redisplay=\"true\"");
		} // end
		 else {
			buffer.append("<" + prefix + ":inputText ");
		}
		/* Bug #19 dated:31/03/2008 updated By:Sanjit Mandal */
		if (prop.equalsIgnoreCase(lsPk)) {
			buffer.append(" <Disabled> "); // Disabled
		}
		if (!prop.equalsIgnoreCase(lsPk)) {
			if (tableVO.getNullable().equals(n)) {
				if (tableVO.getControlType().equalsIgnoreCase(
						HexFrameConstants.DATE_BOX)) {
					if (tableVO.isReadonly()) {
						buffer.append(" <NotNull> ");
					} else {
						buffer.append(" <NotNullandReadonlyfalse> ");
					}
				} else {
					if (tableVO.isReadonly()) {
						buffer.append(" <NotNullReadonlyTrueNoDate> ");
					} else {
						buffer.append(" <NotNullReadonlyfalseNoDate> ");
					}
				}
			} else if (tableVO.getControlType().equalsIgnoreCase(
					HexFrameConstants.DATE_BOX)) {
				if (tableVO.isReadonly()) {
					buffer.append(" <NullReadonlyTrueDate> ");
				} else {
					buffer.append(" <NullReadonlyFalseDate> ");
				}
			} else {
				if (tableVO.isReadonly()) {
					buffer.append(" <NullReadonlyTrueNoDate> ");
				} else {
					buffer.append(" <NullReadonlyFalseNoDate> ");
				}
			}
		}
		/* Bug #19 dated:31/03/2008 updated By:Sanjit Mandal */
		/* Bug #19 dated:31/03/2008 below Commented By:Sanjit Mandal */

		// if (isReadOnly) {
		// buffer.append("readonly=\"true\" ");
		// }
		buffer.append("size=\"");
		Integer dataLength = new Integer(10);
		if (!isDate) {
			dataLength = tableVO.getDataLength();
		}
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("maxlength=\"");
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("id=\"");
		buffer.append(prop);
		buffer.append("\" ");
		buffer.append("value=\"#{");
		buffer.append(beanClassName);
		buffer.append(".");
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);

		buffer.append("}\" ");
		/*
		 * Bug #14 dated:31/03/2008 updated By:Sanjit Mandal T-True, F-False
		 */
		if ((isDate && tableVO.isReadonly() && tableVO.getNullable().equals(n))) {
			listcount = i;
			buffer.append(" <TTT> "); // T-True
			voInstanceNameTTT = voInstanceName;
			prefixTTT = prefix;
		} else if (isDate && !tableVO.isReadonly()
				&& tableVO.getNullable().equals(n)) {
			lcDateReadonlyFalseNullTrue = i;
			buffer.append(" <TFT> "); // T-True, F-False
			voInstanceNameTFT = voInstanceName;
			prefixTFT = prefix;
		} else if (isDate && tableVO.isReadonly()
				&& !tableVO.getNullable().equals(n)) {
			lcDateReadonlyTrueNullFalse = i;
			buffer.append(" <TTF> "); // T-True, F-False
			voInstanceNameTTF = voInstanceName;
			prefixTTF = prefix;
		} else if (isDate && !tableVO.isReadonly()
				&& !tableVO.getNullable().equals(n)) {
			lcDateReadonlyFalseNullFalse = i;
			buffer.append(" <TFF> "); // T-True, F-False
			voInstanceNameTFF = voInstanceName;
			prefixTFF = prefix;
		} else {
			buffer.append(" />\n");
			buffer.append("<" + prefix + ":message for=\"" + prop + "\" />\n");
		}
		/* Bug # 14 dated:31/03/2008 updated By:Sanjit Mandal */

		/* Bug #14 dated:31/03/2008 Commented By:Sanjit Mandal */
		/*
		 * if (isDate) { buffer.append(
		 * " >\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\"/>\n");
		 * buffer.append("</" + prefix + ":inputText>\n"); buffer.append(
		 * "<h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
		 * + voInstanceName + "Form:" + prop + "'))\" />\n"); buffer.append("<"
		 * + prefix + ":message for=\"" + prop + "\" />\n"); }
		 */
		buffer.append("</td>\n");
		/* Begin: Bug#29 Added by Divya for password controlType */
		if (tableVO.controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)
				&& ((!tableVO.isReadonly()) || (tableVO.isReadonly() && tableVO
						.getNullable() == 0))) {

			buffer.append("</tr>\n");
			buffer.append("<tr>\n");
			buffer
					.append("<td>");
			buffer.append("\n");
			buffer.append("Confirm ");
			buffer.append("<h:outputText value=\"");
			buffer.append(prop);
			buffer.append("\" />");
			if (tableVO.mandatory) {
				buffer
						.append("<h:outputText value=\"*\" style=\"color:red;\" />\n");
			}
			buffer.append("</td>");
			buffer.append("<td>\n");
			buffer.append("<" + prefix + ":inputSecret  ");
			if (!tableVO.isReadonly()) {
				buffer.append(" <passwordReadOnlyfalse>");
			} else if (Integer.parseInt(tableVO.getNullable().toString()) == 0) {
				buffer.append("<passwordReadOnlytrue>");
			}
			buffer.append("size=\"");
			buffer.append(dataLength);
			buffer.append("\" ");
			buffer.append("maxlength=\"");
			buffer.append(dataLength);
			buffer.append("\" ");
			buffer.append("id=\"");
			String proppasswrd = prop + "password";
			buffer.append(proppasswrd);
			buffer.append("\" ");
			buffer.append("value=\"#{");
			buffer.append(beanClassName);
			buffer.append(".");
			buffer.append(voInstanceName);
			buffer.append(".");
			buffer.append(prop);
			buffer.append("}\" ");
			buffer.append(" />\n");
			buffer.append("</td>\n");
		}
		/* End: Bug#29 Added by Divya for password controlType */
		return buffer.toString();
	}
	public String prepareEditPageTag4prime(boolean isDate, boolean isReadOnly,
			TableVO tableVO, String beanClassName, String voInstanceName,
			String prefix, String Pk, Integer i) {
		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();
		/* Bug #19 ,14 dated:01/04/2008 updated By:Sanjit Mandal */
		int n = 0;
		String lsPk = Pk.toString();
		/* Bug #19 dated:01/04/2008 updated By:Sanjit Mandal */
		buffer.append("<td>\n");
		// add by jay krishna Bug no:10 dated 20/03
		if (tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.PASSWORD)) {
			buffer.append("<" + prefix + ":password ");
		} // end
		 else if (tableVO.getControlType().equalsIgnoreCase(
					HexFrameConstants.DATE_BOX)){
			buffer.append("<p:calendar ");
		}
		 else {
				buffer.append("<" + prefix + ":inputText ");
			}
		/* Bug #19 dated:31/03/2008 updated By:Sanjit Mandal */
		if (prop.equalsIgnoreCase(lsPk)) {
			buffer.append(" <Disabled> "); // Disabled
		}
		if (!prop.equalsIgnoreCase(lsPk)) {
			if (tableVO.getNullable().equals(n)) {
				if (tableVO.getControlType().equalsIgnoreCase(
						HexFrameConstants.DATE_BOX)) {
					if (tableVO.isReadonly()) {
						buffer.append(" <NotNull> ");
					} else {
						buffer.append(" <NotNullandReadonlyfalse> ");
					}
				} else {
					if (tableVO.isReadonly()) {
						buffer.append(" <NotNullReadonlyTrueNoDate> ");
					} else {
						buffer.append(" <NotNullReadonlyfalseNoDate> ");
					}
				}
			} else if (tableVO.getControlType().equalsIgnoreCase(
					HexFrameConstants.DATE_BOX)) {
				if (tableVO.isReadonly()) {
					buffer.append(" <NullReadonlyTrueDate> ");
				} else {
					buffer.append(" <NullReadonlyFalseDate> ");
				}
			} else {
				if (tableVO.isReadonly()) {
					buffer.append(" <NullReadonlyTrueNoDate> ");
				} else {
					buffer.append(" <NullReadonlyFalseNoDate> ");
				}
			}
		}
		/* Bug #19 dated:31/03/2008 updated By:Sanjit Mandal */
		/* Bug #19 dated:31/03/2008 below Commented By:Sanjit Mandal */

		// if (isReadOnly) {
		// buffer.append("readonly=\"true\" ");
		// }
		buffer.append("size=\"");
		Integer dataLength = new Integer(10);
		if (!isDate) {
			dataLength = tableVO.getDataLength();
		}
		
		if((tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.DATE_BOX))&&(tableVO.getValidationType().equalsIgnoreCase("No Validation"))){
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("maxlength=\"");
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("id=\"");
		buffer.append(prop);
		buffer.append("\" ");
		buffer.append("pattern=\""+tableVO.getDateFormat()+"\" ");
		buffer.append("value=\"#{");
		buffer.append(beanClassName);
		buffer.append(".");
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);
		buffer.append("}\" showOn=\"button\"");
		}
		
		
		
		else if((tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.DATE_BOX))&&(tableVO.getValidationType().equalsIgnoreCase("Future Date Validation"))){
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("maxlength=\"");
		buffer.append(dataLength);
		buffer.append("\" ");
		
		
		DateFormat dateFormat = new SimpleDateFormat(tableVO.getDateFormat());
		Date date=new Date();
		buffer.append("mindate=\"");
		buffer.append(dateFormat.format(date));
		buffer.append("\" ");
		
		buffer.append("id=\"");
		buffer.append(prop);
		buffer.append("\" ");
		buffer.append("pattern=\""+tableVO.getDateFormat()+"\" ");
		buffer.append("value=\"#{");
		buffer.append(beanClassName);
		buffer.append(".");
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);
		buffer.append("}\" showOn=\"button\"");
		}
		
		else if((tableVO.getControlType().equalsIgnoreCase(
				HexFrameConstants.DATE_BOX))&&(tableVO.getValidationType().equalsIgnoreCase("Date Range Validation"))){
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("maxlength=\"");
		buffer.append(dataLength);
		buffer.append("\" ");
		
		
		
		buffer.append("mindate=\"");
		buffer.append(tableVO.getFromDate());
		buffer.append("\" ");
		
		buffer.append("maxdate=\"");
		buffer.append(tableVO.getToDate());
		buffer.append("\" ");
		
		buffer.append("id=\"");
		buffer.append(prop);
		buffer.append("\" ");
		buffer.append("pattern=\""+tableVO.getDateFormat()+"\" ");
		buffer.append("value=\"#{");
		buffer.append(beanClassName);
		buffer.append(".");
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);
		buffer.append("}\" showOn=\"button\"");
		}
		
		else{
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("maxlength=\"");
		buffer.append(dataLength);
		buffer.append("\" ");
		buffer.append("id=\"");
		buffer.append(prop);
		buffer.append("\" ");
	
		buffer.append("value=\"#{");
		buffer.append(beanClassName);
		buffer.append(".");
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);
		buffer.append("}\" ");
		}
		/*
		 * Bug #14 dated:31/03/2008 updated By:Sanjit Mandal T-True, F-False
		 */
		if ((isDate && tableVO.isReadonly() && tableVO.getNullable().equals(n))) {
			listcount = i;
			buffer.append(" <TTT> "); // T-True
			voInstanceNameTTT = voInstanceName;
			prefixTTT = prefix;
		} else if (isDate && !tableVO.isReadonly()
				&& tableVO.getNullable().equals(n)) {
			lcDateReadonlyFalseNullTrue = i;
			buffer.append(" <TFT> "); // T-True, F-False
			voInstanceNameTFT = voInstanceName;
			prefixTFT = prefix;
		} else if (isDate && tableVO.isReadonly()
				&& !tableVO.getNullable().equals(n)) {
			lcDateReadonlyTrueNullFalse = i;
			buffer.append(" <TTF> "); // T-True, F-False
			voInstanceNameTTF = voInstanceName;
			prefixTTF = prefix;
		} else if (isDate && !tableVO.isReadonly()
				&& !tableVO.getNullable().equals(n)) {
			lcDateReadonlyFalseNullFalse = i;
			buffer.append(" <TFF> "); // T-True, F-False
			voInstanceNameTFF = voInstanceName;
			prefixTFF = prefix;
		} else {
			buffer.append(" />\n");
			//buffer.append("<" + prefix + ":message for=\"" + prop + "\" />\n");
		}
		/* Bug # 14 dated:31/03/2008 updated By:Sanjit Mandal */

		/* Bug #14 dated:31/03/2008 Commented By:Sanjit Mandal */
		/*
		 * if (isDate) { buffer.append(
		 * " >\n<f:convertDateTime type=\"date\" pattern=\"MM-dd-yyyy\"/>\n");
		 * buffer.append("</" + prefix + ":inputText>\n"); buffer.append(
		 * "<h:graphicImage url=\"/images/DateIcon.gif\" onclick=\"showCalendarControl(document.getElementById('"
		 * + voInstanceName + "Form:" + prop + "'))\" />\n"); buffer.append("<"
		 * + prefix + ":message for=\"" + prop + "\" />\n"); }
		 */
		buffer.append("</td>\n");
		/* Begin: Bug#29 Added by Divya for password controlType */
		if (tableVO.controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)
				&& ((!tableVO.isReadonly()) || (tableVO.isReadonly() && tableVO
						.getNullable() == 0))) {

			buffer.append("</tr>\n");
			buffer.append("<tr>\n");
			buffer
					.append("<td>");
			buffer.append("\n");
			buffer.append("Confirm ");
			buffer.append("<h:outputText value=\"");
			buffer.append(prop.toUpperCase());
			buffer.append("\" />");
			if (tableVO.mandatory) {
				buffer
						.append("<h:outputText value=\"*\" style=\"color:red;\" />\n");
			}
			buffer.append("</td>");
			buffer.append("<td>\n");
			buffer.append("<" + prefix + ":password  ");
			if (!tableVO.isReadonly()) {
				buffer.append(" <passwordReadOnlyfalse>");
			} else if (Integer.parseInt(tableVO.getNullable().toString()) == 0) {
				buffer.append("<passwordReadOnlytrue>");
			}
			buffer.append("size=\"");
			buffer.append(dataLength);
			buffer.append("\" ");
			buffer.append("maxlength=\"");
			buffer.append(dataLength);
			buffer.append("\" ");
			buffer.append("id=\"");
			String proppasswrd = prop + "password";
			buffer.append(proppasswrd);
			buffer.append("\" ");
			buffer.append("value=\"#{");
			buffer.append(beanClassName);
			buffer.append(".");
			buffer.append(voInstanceName);
			buffer.append(".");
			buffer.append(prop);
			buffer.append("}\" ");
			buffer.append(" />\n");
			buffer.append("</td>\n");
		}
		/* End: Bug#29 Added by Divya for password controlType */
		return buffer.toString();
	}
	private void generateJSFCheckBoxRenderer(HashMap poMap,
			String beanPackageName) {

		String outDirectory = (String) poMap.get("DIRECTORY");
		String lsPackage = (String) poMap.get("PACKAGE");
		String packageDirectory = replaceMethod(beanPackageName);

		TemplateVO woTemplateVO = new TemplateVO();

		woTemplateVO.setTemplateDirectory("./templates/jsf/");
		woTemplateVO.setTemplateFileName("HexCheckBoxRenderer.template");
		woTemplateVO.setOutputDirectory(outDirectory + "src/"
				+ packageDirectory + "/");
		woTemplateVO.setOutputFileName("HexCheckBoxRenderer.java");

		HashMap mBeanContents = new HashMap();
		mBeanContents.put("<packageName>", beanPackageName);
		woTemplateVO.setDynamicContent(mBeanContents);

		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);

	}

	private void generateIceFacesCheckBoxRenderer(HashMap poMap,
			String beanPackageName) {

		String outDirectory = (String) poMap.get("DIRECTORY");
		String lsPackage = (String) poMap.get("PACKAGE");
		String packageDirectory = replaceMethod(beanPackageName);

		TemplateVO woTemplateVO = new TemplateVO();

		woTemplateVO.setTemplateDirectory("./templates/jsf/");
		woTemplateVO
				.setTemplateFileName("HexIceFacesCheckBoxRenderer.template");
		woTemplateVO.setOutputDirectory(outDirectory + "src/"
				+ packageDirectory + "/");
		woTemplateVO.setOutputFileName("HexIceFacesCheckBoxRenderer.java");

		HashMap mBeanContents = new HashMap();
		mBeanContents.put("<packageName>", beanPackageName);
		woTemplateVO.setDynamicContent(mBeanContents);

		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);

	}

	private void generateRichFacesCheckBoxRenderer(HashMap poMap,
			String beanPackageName) {

		String outDirectory = (String) poMap.get("DIRECTORY");
		String lsPackage = (String) poMap.get("PACKAGE");
		String packageDirectory = replaceMethod(beanPackageName);

		TemplateVO woTemplateVO = new TemplateVO();

		woTemplateVO.setTemplateDirectory("./templates/jsf/");
		woTemplateVO
				.setTemplateFileName("HexRichFacesCheckBoxRenderer.template");
		woTemplateVO.setOutputDirectory(outDirectory + "src/"
				+ packageDirectory + "/");
		woTemplateVO.setOutputFileName("HexRichFacesCheckBoxRenderer.java");

		HashMap mBeanContents = new HashMap();
		mBeanContents.put("<packageName>", beanPackageName);
		woTemplateVO.setDynamicContent(mBeanContents);

		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);

	}
	private void generatePrimeFacesCheckBoxRenderer(HashMap poMap,
			String beanPackageName) {

		String outDirectory = (String) poMap.get("DIRECTORY");
		String lsPackage = (String) poMap.get("PACKAGE");
		String packageDirectory = replaceMethod(beanPackageName);

		TemplateVO woTemplateVO = new TemplateVO();

		woTemplateVO.setTemplateDirectory("./templates/jsf/");
		woTemplateVO
				.setTemplateFileName("HexPrimeFacesCheckBoxRenderer.template");
		woTemplateVO.setOutputDirectory(outDirectory + "src/"
				+ packageDirectory + "/");
		woTemplateVO.setOutputFileName("HexPrimeFacesCheckBoxRenderer.java");

		HashMap mBeanContents = new HashMap();
		mBeanContents.put("<packageName>", beanPackageName);
		woTemplateVO.setDynamicContent(mBeanContents);

		CodeGeneratorUtil woCodeGeneratorUtil = new CodeGeneratorUtil();
		woCodeGeneratorUtil.replaceAndWriteContentsToFile(woTemplateVO);

	}
	protected String prepareRadioTag(boolean isReadonly, TableVO tableVO,
			String beanClassName, String voInstanceName, String prefix) {
		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<td>\n");
		buffer.append("<" + prefix + ":selectOneRadio");
		buffer.append(" ");
		buffer.append("id=\"");
		buffer.append(prop);
		buffer.append("\" ");
		if (isReadonly) {
			buffer.append("<EditReadOnlyTrue>");
		}
		buffer.append("value=\"#{");
		buffer.append(beanClassName);
		buffer.append(".");
		buffer.append(voInstanceName);
		buffer.append(".");
		buffer.append(prop);
		buffer.append("}\" >");
		buffer.append("\n");

		for (int i = 0; i < tableVO.getLabelsRadio().length
				|| i < tableVO.getValuesRadio().length; i++) {
			buffer.append("<f:selectItem");
			buffer.append(" ");
			buffer.append("itemLabel=\"");
			buffer.append(tableVO.getLabelsRadio()[i]);
			buffer.append("\" ");
			buffer.append("itemValue=\"");
			buffer.append(tableVO.getValuesRadio()[i].toLowerCase());
			buffer.append("\"");
			buffer.append("/>");
			buffer.append("\n");
		}
		buffer.append("</" + prefix + ":selectOneRadio>");
		buffer.append("\n");
		buffer.append(prepareMessage(prop, prefix));
		buffer.append("</td>\n");

		return buffer.toString();
	}

	protected String prepareCheckBoxTag(boolean isReadonly, TableVO tableVO,
			String beanClassName, String voInstanceName, String prefix) {
		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<td>\n");
		buffer.append("<" + prefix + ":selectBooleanCheckbox ");

		boolean isReadOnly = tableVO.isReadonly();
		if (isReadOnly) {
			buffer.append("<EditReadOnlyTrue>");
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
		buffer.append("}\"/>");
		buffer.append("\n");
		buffer.append(prepareMessage(prop, prefix));
		buffer.append("</td>\n");
		return buffer.toString();
	}

	public String prepareTextAreaTag(boolean isReadonly, TableVO tableVO,
			String beanClassName, String voInstanceName, String prefix) {
		String prop = tableVO.getColumnName().toLowerCase();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<td>\n");
		
		buffer.append("<" + prefix + ":inputTextarea rows=\"4\" cols=\"");
		Integer cols = tableVO.getDataLength().intValue() > 15 ? tableVO
				.getDataLength() : new Integer(15);
		buffer.append(cols);
		buffer.append("\" ");
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
		buffer.append("}\" />\n");
		buffer.append(prepareMessage(prop, prefix));
		buffer.append("</td>\n");
		return buffer.toString();
	}

	public String prepareMessage(String id, String prefix) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + prefix + ":message for=\"");
		buffer.append(id);
		buffer.append("\"/>");
		return buffer.toString();
	}
}
