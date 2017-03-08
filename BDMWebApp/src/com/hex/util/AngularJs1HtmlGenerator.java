package com.hex.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.hex.UIController;
import com.hex.dao.TableDAO;
import com.hex.vo.TableVO;

public class AngularJs1HtmlGenerator {

	public String[] getListPageSource(ArrayList tableList, String lsPageTitle) {
		// TODO Auto-generated method stub
		TableVO tableVO = (TableVO) tableList.get(0);
		String beanName = tableVO.getTableName().toLowerCase();
		String beanNameC = beanName.substring(0, 1).toUpperCase()+beanName.substring(1, beanName.length());
		StringBuffer lsIndex = new StringBuffer();
		StringBuffer lsTitleStr = new StringBuffer();
		StringBuffer lsBeanStr = new StringBuffer();
		StringBuffer lsAddText = new StringBuffer();
		StringBuffer lsAdd = new StringBuffer();
		StringBuffer result = new StringBuffer();
		TableDAO tableDAO = TableDAO.getInstance();
		ArrayList pkDetails = tableDAO.getPrimaryKeyDetails(beanName);
		String pkMethod = null;
		
		String lsPk = "";
		if (pkDetails.size() >= 1) {
			pkMethod = HexUtil.initCap(tableVO.getColumnName());
			tableVO = (TableVO) pkDetails.get(0);
			lsPk = tableVO.getColumnName().toLowerCase();
		} else {
			return null;
		}
		System.out.println("**** Primary Key in AngularJs File Generator :"
				+ lsPk);
		for (int i = 0; i < tableList.size(); i++) {
			tableVO = (TableVO) tableList.get(i);
			if (tableVO.isSelect()) {
				String prop = tableVO.getColumnName().toLowerCase();

				lsTitleStr.append("\t\t<th>");
				lsTitleStr.append("\n\t\t\t\t\t\t" + prop.substring(0, 1).toUpperCase()+prop.substring(1, prop.length()) + "\n");
				lsTitleStr.append("\t\t\t\t\t</th>\n");

				lsBeanStr.append("\t\t<td>");
				if (prop.equalsIgnoreCase(lsPk)) {
					lsBeanStr
							.append("\n\t\t\t\t\t<a href=\"\" ng-click=\"edit("+beanName+");\">{{"
									+ beanName + "." + prop + "}}</a>\n");
				} else {
					lsBeanStr.append("\n\t\t\t\t\t{{" + beanName + "." + prop
							+ "}}\n");
				}
				lsBeanStr.append("\t\t\t\t\t</td>\n");
				
			
				lsAddText.append("<tr>\n");
				lsAddText.append("\t\t\t<td>\n");
				lsAddText.append("\t\t\t\t"+prop+"\n");
				lsAddText.append("\t\t\t</td>\n");
				lsAddText.append("\t\t\t<td>\n");
				if(prop.equalsIgnoreCase(lsPk)){
					lsAddText.append("\t\t\t\t<input type=\"text\" ng-model=\""+beanName + "." + prop+"\" ng-readonly=\"!showAddButton\"/>\n");
					
				}
				else
				{
					lsAddText.append("\t\t\t\t<input type=\"text\" ng-model=\""+beanName + "." + prop+"\"/>\n");
					
				}
				lsAddText.append("\t\t\t</td>\n");
				lsAddText.append("\t\t</tr>\n");
			

			}
		}

		
		try {
			InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\AngularJs1\\ListPage.template");
			 
			DataInputStream dis = new DataInputStream(inputStream);
			while (dis.available() > 0) {
				String s = dis.readLine();
				/*
				 * if (s.indexOf("<PageTitle>") >= 0) { s =
				 * s.replaceAll("<PageTitle>", loPageTitle.toString()); }
				 */
				if (s.indexOf("<BeanName>") >= 0) {
					s = s.replaceAll("<BeanName>", beanNameC);
				}
				
				if (s.indexOf("<beanName>") >= 0) {
					s = s.replaceAll("<beanName>", beanName);
				}
				

				if (s.indexOf("<TableHeader>") >= 0) {
					s = s.replaceAll("<TableHeader>", lsTitleStr.toString());
				}
				if (s.indexOf("<BeanValues>") >= 0) {
					s = s.replaceAll("<BeanValues>", lsBeanStr.toString());
				}
				/*
				 * if (s.indexOf("<BeanValues>") >= 0) { s =
				 * s.replaceAll("<BeanValues>", lsBeanStr.toString()); }
				 */
				result.append(s);
				result.append("\n");
			}
			dis.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		try {
			InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\AngularJs1\\Add_Edit.template");
			 
			DataInputStream dis = new DataInputStream(inputStream);
			while (dis.available() > 0) {
				String s = dis.readLine();
			
				if (s.indexOf("<BeanName>") >= 0) {
					s = s.replaceAll("<BeanName>", beanNameC);
				}

				if (s.indexOf("<AddEditData>") >= 0) {
					s = s.replaceAll("<AddEditData>", lsAddText.toString());
				}
				
				lsAdd.append(s);
				lsAdd.append("\n");
			}
			dis.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		try {
			InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\AngularJs1\\index.template");
			 
			DataInputStream dis = new DataInputStream(inputStream);
			while (dis.available() > 0) {
				String s = dis.readLine();
			
				if (s.indexOf("<BeanName>") >= 0) {
					s = s.replaceAll("<BeanName>", beanNameC);
				}
				if (s.indexOf("<beanName>") >= 0) {
					s = s.replaceAll("<beanName>", beanName);
				}

				
				lsIndex.append(s);
				lsIndex.append("\n");
			}
			dis.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		String sources[] = new String[4];
		sources[1] = result.toString();
		sources[2] = lsAdd.toString();
		sources[3] = lsIndex.toString();

		return sources;
	}

}
