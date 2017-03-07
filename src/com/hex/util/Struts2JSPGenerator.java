package com.hex.util;
import com.hex.vo.TableVO;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.io.*;
import com.hex.dao.TableDAO;
import java.util.HashMap;

import javax.swing.JList;

public class Struts2JSPGenerator {

	String isClassName = "JSPGenerator";

	public Struts2JSPGenerator() {
	}
	public String[] getListPageSource(ArrayList list, String psPageTitle) {
        System.out.println("Inside getListPageSource " + isClassName);
    
        Integer listcount = 0;
        Integer lcDateReadonlyFalseNullTrue = 0;
        Integer lcDateReadonlyFalseNullFalse = 0;
        Integer lcDateReadonlyTrueNullFalse = 0;

        StringBuffer result = new StringBuffer();
        StringBuffer lsBeanStr = new StringBuffer();
        StringBuffer lsTitleStr = new StringBuffer();
        StringBuffer lsBeanTags = new StringBuffer();
        StringBuffer lsBeanTags4edit = new StringBuffer();
        StringBuffer lsHeaderInfo = new StringBuffer();
        StringBuffer lsJavaScriptValidations = new StringBuffer();

        TableVO tableVO = (TableVO) list.get(0);
        String beanName = tableVO.getTableName().toLowerCase();
        System.out.println("Bean Name **************************************************>>>>>>" + beanName);
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
        System.out.println("**** Primary Key in JSP Generator :" + lsPk);

        Integer n = new Integer(0);
        for (int i = 0; i < list.size(); i++) {
            tableVO = (TableVO) list.get(i);
          
			if (tableVO.isSelect()) {
                String prop = tableVO.getColumnName().toLowerCase();
                String controlType = tableVO.getControlType();
               
              
                if (controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
                    //controlType = new String(HexFrameConstants.TEXT_BOX);
                	controlType = new String(HexFrameConstants.DATE_BOX);
                    tableVO.setDataLength(new Integer(10));
                }
                if(controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX1)||controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX1)||controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX2)||controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX2)){
                	controlType = new String(HexFrameConstants.TEXT_BOX);
                    tableVO.setDataLength(new Integer(10));
                }
                if (controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX)) {
                String lsTargetTable = tableVO.getTargetTable();
                if (lsTargetTable != null && lsTargetTable.length() > 0) {
                   lsTargetTable = lsTargetTable.toLowerCase();
                   String lsKeyColumn = tableVO.getKeyColumn().toLowerCase();
                   String lsValueColumn = tableVO.getValueColumn().toLowerCase();
                   String lsMethodName = HexUtil.initCap(lsTargetTable) +
                           HexUtil.initCap(lsKeyColumn) +
                           HexUtil.initCap(lsValueColumn);
   		lsBeanTags.append("\t\t\t\t<%\n");
           lsBeanTags.append(
                   "\t\t\t\t        java.util.List Templist = (java.util.List)request.getAttribute(\"" +
                   lsMethodName + "\");%>\n");
           lsBeanTags4edit.append("\t\t\t\t<%\n");
           lsBeanTags4edit.append(
                   "\t\t\t\t        java.util.List Templist = (java.util.List)request.getAttribute(\"" +
                   lsMethodName + "\");%>\n");
                }
                }
                if (!controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)) {
                    lsBeanStr.append("\t\t\t<td>");
                    if (prop.equalsIgnoreCase(lsPk)) {
                        
                    	lsBeanStr.append(
                                "<s:a href=\"edit.action?key=%{");
                    	lsBeanStr.append(prop);
                    	lsBeanStr.append("}\" ");
                        lsBeanStr.append("name=\"key\">");
                        lsBeanStr.append("<s:property  value=\"%{");
                        lsBeanStr.append(prop);
                        lsBeanStr.append("}\"/>");
                        lsBeanStr.append("</s:a>");
                        
                    } else {
                        lsBeanStr.append("<s:property  value=\"%{");
                        lsBeanStr.append(prop);
                        lsBeanStr.append("}\"/>");
                    }
                        lsBeanStr.append("</td>\n");
                } else {
                        lsBeanStr.append("\t\t\t<td>");
                    if (prop.equalsIgnoreCase(lsPk)) {
                        lsBeanStr.append(
                        		"<s:a href=\"edit.action?key=%{");
                       lsBeanStr.append(prop);
                       lsBeanStr.append("}\"");
                       lsBeanStr.append("name=\"key\">");
                       lsBeanStr.append("{");
                       lsBeanStr.append(prop);
                       lsBeanStr.append("}");
                        lsBeanStr.append("<s:password key=\"password\"  value=\"%{");
                        lsBeanStr.append(prop);
                        lsBeanStr.append("}\"/>");
                        lsBeanStr.append("</s:a>");
                    } else {
                       
                        lsBeanStr.append("<s:password key=\"password\"  value=\"%{");
                        lsBeanStr.append(prop);
                        lsBeanStr.append("}\"/>");
                    }
                    lsBeanStr.append("</td>\n");
                }
                lsTitleStr.append("\t\t<th>");
               
                lsTitleStr.append("<s:text name=\"");
                lsTitleStr.append(beanName.toUpperCase() + "."+ prop.toUpperCase());
                lsTitleStr.append("\"/>");
                lsTitleStr.append("</th>\n");

                lsBeanTags.append("\t\t<tr>\n");
                lsBeanTags.append("\t\t\t<td align=\"left\" valign=\"middle\">\n");
                lsBeanTags.append("\t\t\t\t"); 
                lsBeanTags.append("<s:text name=\"");
                lsBeanTags.append(prop.toUpperCase());
                lsBeanTags.append("\"/>");
                lsBeanTags.append((tableVO.isMandatory() ? "&nbsp;*\n" : "\n"));
                
                lsBeanTags4edit.append("\t\t<tr>\n");
                lsBeanTags4edit.append("\t\t\t<td align=\"left\" valign=\"middle\">\n");
                lsBeanTags4edit.append("\t\t\t\t"); 
                lsBeanTags4edit.append("<s:text name=\"");
                lsBeanTags4edit.append(prop.toUpperCase());
                lsBeanTags4edit.append("\"/>");
                lsBeanTags4edit.append((tableVO.isMandatory() ? "&nbsp;*\n" : "\n"));
                if (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
                    Boolean isdate = new Boolean(true);
                    lsBeanTags.append((isdate ? "&nbsp;(MM-DD-YYYY)\n" : "\n"));
                    lsBeanTags4edit.append((isdate ? "&nbsp;(MM-DD-YYYY)\n" : "\n"));
                    
                }
                lsBeanTags.append("\t\t\t</td>\n");
                lsBeanTags.append("\t\t\t<td>\n");
                lsBeanTags4edit.append("\t\t\t</td>\n");
                lsBeanTags4edit.append("\t\t\t<td>\n");
                
                if (!tableVO.getControlType().equals(HexFrameConstants.RADIO)) {
                	if(tableVO.getControlType().equals(HexFrameConstants.DATE_BOX)){
                		lsBeanTags.append("\t\t\t\t<sx:");
                		lsBeanTags.append("datetimepicker");
                		lsBeanTags4edit.append("\t\t\t\t<sx:");
                		lsBeanTags4edit.append("datetimepicker");
                }
                	
                	else if((!controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX)) && (!controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX1))){
                	lsBeanTags.append("\t\t\t\t<s:");
                	lsBeanTags4edit.append("\t\t\t\t<s:");
                	}
                	if(controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX)){
                	lsBeanTags.append(controlType+"field");
                	lsBeanTags4edit.append(controlType+"field");
                	}
        		    else if(controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX) || (controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX1)) )
        		    {  lsBeanTags.append("\t\t\t\t<");
        		    	lsBeanTags.append("select");
        		    	lsBeanTags4edit.append("\t\t\t\t<");
        		    	lsBeanTags4edit.append("select");
        		    }
                    if (prop.equalsIgnoreCase(lsPk)) {
                        lsBeanTags.append(" <Disabled> ");
                        lsBeanTags4edit.append(" <Disabled> ");
                    }
                    
                    if (!prop.equalsIgnoreCase(lsPk)) {
                        if (tableVO.getNullable().equals(n)) {
                            System.out.println("tableVO.getNullable()" + tableVO.getNullable());
                            if (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
                                if (tableVO.isReadonly()) {
                                    lsBeanTags.append(" <NotNull> ");
                                    lsBeanTags4edit.append(" <NotNull> ");
                                    System.out.println("<NotNull>  ");
                                } else {
                                    lsBeanTags.append(" <NotNullandReadonlyfalse> ");
                                    lsBeanTags4edit.append(" <NotNullandReadonlyfalse> ");
                                }
                            } else {
                                if (!tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.CHECK_BOX)) {
                                    if (tableVO.isReadonly()) {
                                        lsBeanTags.append(" <NotNullReadonlyTrueNoDate> ");
                                        lsBeanTags4edit.append(" <NotNullReadonlyTrueNoDate> ");
                                    } else {
                                        lsBeanTags.append(" <NotNullReadonlyFalseNoDate> ");
                                        lsBeanTags4edit.append(" <NotNullReadonlyFalseNoDate> ");
                                    }
                                }
                            }
                        } else if (tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX)) {
                            if (tableVO.isReadonly()) {
                                lsBeanTags.append(" <NullReadonlyTrueDate> ");
                                lsBeanTags4edit.append(" <NullReadonlyTrueDate> ");
                            } else {
                                lsBeanTags.append(" <NullReadonlyFalseDate> ");
                                lsBeanTags4edit.append(" <NullReadonlyFalseDate> ");
                            }
                        } else {
                            if (!tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.CHECK_BOX)) {
                                if (tableVO.isReadonly()) {
                                    lsBeanTags.append(" <NullReadonlyTrueNoDate> ");
                                    lsBeanTags4edit.append(" <NullReadonlyTrueNoDate> ");
                                } else {
                                    lsBeanTags.append(" <NullReadonlyFalseNoDate> ");
                                    lsBeanTags4edit.append(" <NullReadonlyFalseNoDate> ");
                                }
                            }
                        }
                    }
                    if ((controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX)) || (controlType.equalsIgnoreCase(HexFrameConstants.COMBO_BOX1)))                      
                    {
                    lsBeanTags.append(" property=\"");
                    lsBeanTags.append(prop);
                    lsBeanTags.append("\"");
                    lsBeanTags4edit.append(" property=\"");
                    lsBeanTags4edit.append(prop);
                    lsBeanTags4edit.append("\"");
                    }
                    
                    else{
                    lsBeanTags.append(" name=\"");
                    lsBeanTags.append(beanName+"VO");
                    lsBeanTags.append(".");
                    lsBeanTags.append(prop);
                    lsBeanTags.append("\"");
                    lsBeanTags4edit.append(" name=\"");
                    lsBeanTags4edit.append(beanName+"VO4edit");
                    lsBeanTags4edit.append(".");
                    lsBeanTags4edit.append(prop);
                    lsBeanTags4edit.append("\"");
                    }
                    
                    if (controlType.equalsIgnoreCase(HexFrameConstants.CHECK_BOX)) {
                    	
                        lsBeanTags.append(" value=");  
                        lsBeanTags.append("\"");
                        lsBeanTags.append("y");
                        lsBeanTags.append("\"");
                        lsBeanTags4edit.append(" value=");  
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append("y");
                        lsBeanTags4edit.append("\"");
                    }
                    if(controlType.equalsIgnoreCase(HexFrameConstants.DATE_BOX))
                    		{
                    	lsBeanTags.append(" displayFormat=");
                        lsBeanTags.append("\"");
                        lsBeanTags.append(tableVO.getDateFormat());
                        lsBeanTags.append("\"");
                        lsBeanTags4edit.append(" displayFormat=");
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append(tableVO.getDateFormat());
                        lsBeanTags4edit.append("\"");
                    	   }
                    if (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX) ||
                            controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD)) {
                    	
                        lsBeanTags.append(" size=");
                        lsBeanTags.append("\"");
                        lsBeanTags.append(tableVO.getDataLength());
                        lsBeanTags.append("\"");
                        lsBeanTags4edit.append(" size=");
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append(tableVO.getDataLength());
                        lsBeanTags4edit.append("\"");

                        lsBeanTags.append(" maxlength=");
                        lsBeanTags.append("\"");
                        lsBeanTags.append(tableVO.getDataLength());
                        lsBeanTags.append("\"");
                        lsBeanTags4edit.append(" maxlength=");
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append(tableVO.getDataLength());
                        lsBeanTags4edit.append("\"");
                    }
                    if (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_AREA)) {
                    	
                        lsBeanTags.append(" cols=\"25\"");
                        lsBeanTags.append(" rows=\"5\"");
                        lsBeanTags4edit.append(" cols=\"25\"");
                        lsBeanTags4edit.append(" rows=\"5\"");
                    }
                    if (!(controlType
							.equalsIgnoreCase(HexFrameConstants.COMBO_BOX)||controlType
							.equalsIgnoreCase(HexFrameConstants.COMBO_BOX1))) {
						lsBeanTags.append("/>\n");
						lsBeanTags4edit.append("/>\n");
					} else if (controlType
							.equalsIgnoreCase(HexFrameConstants.COMBO_BOX)) {
						String lsTargetTable = tableVO.getTargetTable();
						lsBeanTags.append(">\n");
						if (lsTargetTable != null && lsTargetTable.length() > 0) {
							lsTargetTable = lsTargetTable.toLowerCase();
							String lsKeyColumn = tableVO.getKeyColumn()
									.toLowerCase();
							String lsValueColumn = tableVO.getValueColumn()
									.toLowerCase();
							String lsMethodName = HexUtil
									.initCap(lsTargetTable)
									+ HexUtil.initCap(lsKeyColumn)
									+ HexUtil.initCap(lsValueColumn);
							lsBeanTags.append("\t\t\t\t<%\n");
							lsBeanTags4edit.append("\t\t\t\t<%\n");
							
							lsBeanTags
									.append("\t\t\t\t        java.util.List list = (java.util.List)request.getAttribute(\""
											+ lsMethodName + "\");\n");
							lsBeanTags4edit.append("\t\t\t\t        java.util.List list = (java.util.List)request.getAttribute(\""
									+ lsMethodName + "\");\n");
							
							lsBeanTags
									.append("\t\t\t\t        if ( list != null ) {\n");
							lsBeanTags4edit.append("\t\t\t\t        if ( list != null ) {\n");
							
							lsBeanTags
									.append("\t\t\t\t          for ( java.util.Iterator results = list.iterator(); results.hasNext(); ) {\n");
							lsBeanTags4edit.append("\t\t\t\t          for ( java.util.Iterator results = list.iterator(); results.hasNext(); ) {\n");
							
							lsBeanTags
									.append("\t\t\t\t            Object[] row = (Object[]) results.next();\n");
							lsBeanTags4edit.append("\t\t\t\t            Object[] row = (Object[]) results.next();\n");
							
							lsBeanTags.append("\t\t\t\t%>\n");
							lsBeanTags4edit.append("\t\t\t\t%>\n");
							
							lsBeanTags
									.append("\t\t\t\t                        <option value=\"<%=(String)row[0]%>\"><%=(String)row[1]%></option>\n");
							lsBeanTags4edit.append("\t\t\t\t                        <option value=\"<%=(String)row[0]%>\"><%=(String)row[1]%></option>\n");
							
							lsBeanTags.append("\t\t\t\t<%\n");
							lsBeanTags4edit.append("\t\t\t\t<%\n");
							
							lsBeanTags.append("\t\t\t\t              }\n");
							lsBeanTags4edit.append("\t\t\t\t              }\n");
							
							lsBeanTags.append("\t\t\t\t        }\n");
							lsBeanTags4edit.append("\t\t\t\t        }\n");
							
							lsBeanTags.append("\t\t\t\t%>\n");
							lsBeanTags4edit.append("\t\t\t\t%>\n");
						}
						
						lsBeanTags.append("\t\t\t\t</select>\n");
						lsBeanTags4edit.append("\t\t\t\t</select>\n");
					}

					else if (controlType
							.equalsIgnoreCase(HexFrameConstants.COMBO_BOX1)) {
						// TableVO staticoptionsvo=new TableVO();
						 lsBeanTags.append(">\n");
						 lsBeanTags4edit.append(">\n");
						String[] staticlist = tableVO.getLabelsStaticSelect();

						for (int check = 0; check < staticlist.length; check++) {
							System.out
									.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
											+ staticlist[check]
											+ "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
							lsBeanTags
									.append("\t\t\t\t <option value=\""
											+ staticlist[check]
											+ "\">"+staticlist[check]+"</option>\n");
							lsBeanTags4edit
							.append("\t\t\t\t <option value=\""
									+ staticlist[check]
									+ "\">"+staticlist[check]+"</option>\n");
						}

						lsBeanTags.append("\t\t\t\t</select>\n");
						lsBeanTags4edit.append("\t\t\t\t</select>\n");

					}
                    if (controlType.equalsIgnoreCase(HexFrameConstants.PASSWORD) && ((!tableVO.isReadonly()) || (tableVO.isReadonly() && Integer.parseInt(tableVO.getNullable().toString()) == 0))) {
                        lsBeanTags.append("\t\t\t</td>\n");
                        lsBeanTags.append("\t\t</tr>\n");
                        lsBeanTags.append("\t\t<tr>\n");
                        lsBeanTags.append("\t\t\t<td align=\"left\" valign=\"middle\" nowrap>\n");
                        lsBeanTags.append("\t\t\t\t"); //+ tableVO.getLabelName());
                        lsBeanTags.append("Confirm ");
                        lsBeanTags.append("<s:text name=\"");
                        lsBeanTags.append(beanName.toUpperCase() + "." + prop.toUpperCase());
                        lsBeanTags.append("\"/>");
                        lsBeanTags.append((tableVO.isMandatory() ? "&nbsp;\n" : "\n"));
                        lsBeanTags.append("\t\t\t</td>\n");
                        lsBeanTags.append("\t\t\t<td>\n");
                        lsBeanTags.append("\t\t\t\t<s:");
                        
                        lsBeanTags4edit.append("\t\t\t</td>\n");
                        lsBeanTags4edit.append("\t\t</tr>\n");
                        lsBeanTags4edit.append("\t\t<tr>\n");
                        lsBeanTags4edit.append("\t\t\t<td align=\"left\" valign=\"middle\" nowrap>\n");
                        lsBeanTags4edit.append("\t\t\t\t"); //+ tableVO.getLabelName());
                        lsBeanTags4edit.append("Confirm ");
                        lsBeanTags4edit.append("<s:text name=\"");
                        lsBeanTags4edit.append(beanName.toUpperCase() + "." + prop.toUpperCase());
                        lsBeanTags4edit.append("\"/>");
                        lsBeanTags4edit.append((tableVO.isMandatory() ? "&nbsp;\n" : "\n"));
                        lsBeanTags4edit.append("\t\t\t</td>\n");
                        lsBeanTags4edit.append("\t\t\t<td>\n");
                        lsBeanTags4edit.append("\t\t\t\t<s:");
                        if(controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX)){
                    		lsBeanTags.append(controlType+"field");
                        lsBeanTags4edit.append(controlType+"field");
                        }
            				else
            				{
                        lsBeanTags.append(controlType);
                        lsBeanTags4edit.append(controlType);
            				}
                        if (!tableVO.isReadonly()) {
                            lsBeanTags.append(
                                    " <passwordReadOnlyfalse>");
                            lsBeanTags4edit.append(
                            " <passwordReadOnlyfalse>");
                        } else if (Integer.parseInt(tableVO.getNullable().toString()) == 0) {
                            lsBeanTags.append(
                                    "<passwordReadOnlytrue>");
                            lsBeanTags4edit.append(
                            "<passwordReadOnlytrue>");
                        }
                        
                        String propPassword = prop + "password";
                        lsBeanTags.append(" value=\"%{");
                        lsBeanTags.append(beanName+"VO");
                        lsBeanTags.append(".");
                        lsBeanTags.append(prop);
                        lsBeanTags.append("}\"");
                        lsBeanTags.append(" size=");
                        lsBeanTags.append("\"");
                        lsBeanTags.append(tableVO.getDataLength());
                        lsBeanTags.append("\"");
                        lsBeanTags.append(" maxlength=");
                        lsBeanTags.append("\"");
                        lsBeanTags.append(tableVO.getDataLength());
                        lsBeanTags.append("\"");
                        lsBeanTags.append("/>\n");
                        
                        lsBeanTags4edit.append(" value=\"%{");
                        lsBeanTags4edit.append(beanName+"VO4edit");
                        lsBeanTags4edit.append(".");
                        lsBeanTags4edit.append(prop);
                        lsBeanTags4edit.append("}\"");
                        lsBeanTags4edit.append(" size=");
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append(tableVO.getDataLength());
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append(" maxlength=");
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append(tableVO.getDataLength());
                        lsBeanTags4edit.append("\"");
                        lsBeanTags4edit.append("/>\n");
                        lsJavaScriptValidations.append("\t\tif ( (document.forms[\"" +
                                beanName + "Form\"]." + propPassword + ".readOnly == false) && " +
                                "! (document.forms[\"" +
                                beanName + "Form\"]." + prop +
                                ".value == document.forms[\"" +
                                beanName + "Form\"]." + propPassword +
                                ".value)  )\n");
                        lsJavaScriptValidations.append("\t\t{\n");
                        lsJavaScriptValidations.append(
                                "\t\t\talert('" + prop + " does not match!!!');\n");
                        
                        lsJavaScriptValidations.append("\t\t\treturn false;\n");
                        lsJavaScriptValidations.append("\t\t}\n\n");
                    }
                    
                    if ((tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX)) && tableVO.isReadonly() && tableVO.getNullable().equals(n)) {
                        listcount = i;
                        
                        lsBeanTags.append(" <TTT> ");  
                        lsBeanTags4edit.append(" <TTT> "); //  T-True
                        if (lsHeaderInfo.length() <= 0) {
                            lsHeaderInfo.append("\t<HEAD>\n");
                            lsHeaderInfo.append("\t<sx:head/>\n");
                           /* lsHeaderInfo.append(
                                    "\t\t<link href=\"css/CalendarControl.css\"  rel=\"stylesheet\" type=\"text/css\">\n");
                            lsHeaderInfo.append(
                                    "\t\t<script src=\"js/CalendarControl.js\"  language=\"javascript\"></script>\n");*/
                            lsHeaderInfo.append("\t</HEAD>");
                        }
                    }
                    if ((tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX)) && !tableVO.isReadonly() && tableVO.getNullable().equals(n)) {
                        lcDateReadonlyFalseNullTrue = i;
                        lsBeanTags.append(" <DateFT> ");
                        lsBeanTags4edit.append(" <DateFT> ");// T-True, F-False
                        if (lsHeaderInfo.length() <= 0) {
                            lsHeaderInfo.append("\t<HEAD>\n");
                            lsHeaderInfo.append("\t<sx:head/>\n");
                            /*lsHeaderInfo.append(
                                    "\t\t<link href=\"css/CalendarControl.css\"  rel=\"stylesheet\" type=\"text/css\">\n");
                            lsHeaderInfo.append(
                                    "\t\t<script src=\"js/CalendarControl.js\"  language=\"javascript\"></script>\n");*/
                            lsHeaderInfo.append("\t</HEAD>");
                        }
                    }
                    if ((tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX)) && tableVO.isReadonly() && !tableVO.getNullable().equals(n)) {
                        lcDateReadonlyTrueNullFalse = i;
                        lsBeanTags.append(" <DateTF> ");
                        lsBeanTags4edit.append(" <DateTF> ");//  T-True, F-False
                        if (lsHeaderInfo.length() <= 0) {
                            lsHeaderInfo.append("\t<HEAD>\n");
                            lsHeaderInfo.append("\t<sx:head/>\n");
                            /*lsHeaderInfo.append(
                                    "\t\t<link href=\"css/CalendarControl.css\"  rel=\"stylesheet\" type=\"text/css\">\n");
                            lsHeaderInfo.append(
                                    "\t\t<script src=\"js/CalendarControl.js\"  language=\"javascript\"></script>\n");*/
                            lsHeaderInfo.append("\t</HEAD>");
                        }
                    }
                    if ((tableVO.getControlType().equalsIgnoreCase(HexFrameConstants.DATE_BOX)) && !tableVO.isReadonly() && !tableVO.getNullable().equals(n)) {
                        lcDateReadonlyFalseNullFalse = i;

                        lsBeanTags.append(" <DateFF> "); 
                        lsBeanTags4edit.append(" <DateFF> ");//T-True, F-False

                        if (lsHeaderInfo.length() <= 0) {
                            lsHeaderInfo.append("\t<HEAD>\n");
                            lsHeaderInfo.append("\t<sx:head/>\n");
                            /*lsHeaderInfo.append(
                                    "\t\t<link href=\"css/CalendarControl.css\"  rel=\"stylesheet\" type=\"text/css\">\n");
                            lsHeaderInfo.append(
                                    "\t\t<script src=\"js/CalendarControl.js\"  language=\"javascript\"></script>\n");*/
                            lsHeaderInfo.append("\t</HEAD>");
                        }
                    }
                    lsBeanTags.append("\t\t\t</td>\n");
                    lsBeanTags.append("\t\t</tr>\n");
                    lsBeanTags4edit.append("\t\t\t</td>\n");
                    lsBeanTags4edit.append("\t\t</tr>\n");
                } else {
                    /*String controlType;*/
					lsBeanTags= prepareRadioTag(lsBeanTags, prop, controlType, tableVO);
					lsBeanTags4edit= prepareRadioTag(lsBeanTags, prop, controlType, tableVO);
                }
                if (tableVO.isMandatory()) {
                    lsJavaScriptValidations.append("\t\tif ( document.forms[\"" +
                            beanName + "Form\"]." + prop +
                            ".value.length == 0 )\n");
                    lsJavaScriptValidations.append("\t\t{\n");
                    lsJavaScriptValidations.append(
                            "\t\t\talert('Please enter the value of " +
                            tableVO.getLabelName() +
                            "');\n");
                    /*lsJavaScriptValidations.append("\t\t\tdocument.forms[\"" + beanName +
                            "Form\"]." + prop + ".focus();\n");*/
                    lsJavaScriptValidations.append("\t\t\treturn false;\n");
                    lsJavaScriptValidations.append("\t\t}\n\n");
                }
            }
        
        }
        /*catch(Exception ex123) {
        	ex123.printStackTrace();
        }*/
    	
        StringBuffer loPageTitle = new StringBuffer();
        loPageTitle.append("<s:text name=\"");
        loPageTitle.append(beanName.toUpperCase() + ".PAGE_TITLE");
        loPageTitle.append("\"/>");
    	
    	

        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\Struts2\\ListPage_Struts2.template"));
            while (dis.available() > 0) {
                String s = dis.readLine();
                if (s.indexOf("<PageTitle>") >= 0) {
                    s = s.replaceAll("<PageTitle>", loPageTitle.toString());
                }
                if (s.indexOf("<BeanName>") >= 0) {
                    s = s.replaceAll("<BeanName>", beanName);
                }
                if (s.indexOf("<UBeanName>") >= 0) {
                    s = s.replaceAll("<UBeanName>", beanName.toUpperCase());
                }
                if (s.indexOf("<TableHeader>") >= 0) {
                    s = s.replaceAll("<TableHeader>", lsTitleStr.toString());
                }
                if (s.indexOf("<BeanValues>") >= 0) {
                    s = s.replaceAll("<BeanValues>", lsBeanStr.toString());
                }
                if (s.indexOf("<PrimaryKey>") >= 0) {
                    s = s.replaceAll("<PrimaryKey>", lsPk);
                }
                result.append(s);
                result.append("\n");
            }
            dis.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        /*StringBuffer lsIndex = new StringBuffer();
        
        lsIndex.append(
                "<%@ taglib uri=\"/WEB-INF/struts-html.tld\" prefix=\"html\"%>\n");
        lsIndex.append("<script Language=\"JavaScript\">\n");
        lsIndex.append("\tfunction set()\n");
        lsIndex.append("\t{\n");
        lsIndex.append("\t\tdocument.forms[\"" + beanName + "Form\"].submit();\n");
        lsIndex.append("\t}\n");
        lsIndex.append("</script>\n");
        lsIndex.append("<html>\n");
        lsIndex.append("<body onload=\"set()\">\n");
        lsIndex.append("\t<html:form action=\"/" + beanName + "\">\n");
        lsIndex.append("\t\t<html:hidden property=\"dispatch\" value=\"list\"/>\n");*/
        
        /*for(int index = 0;index<list.size();index++){
        
            tableVO = (TableVO)list.get(index);
            System.out.println("********* List size in strutsJSPGenerator ********"+list.size()+ "************");
            warFileList.append(tableVO.getTableName());
        
        }*/
        
       /* lsIndex.append("\t</html:form>\n");
        lsIndex.append("</body>\n");
        lsIndex.append("<html>\n");*/

        String lsAddPageTitle = "<s:text name=\"" + beanName.toUpperCase() +
                ".ADD_PAGE_TITLE\"/>";
        String lsEditPageTitle = "<s:text name=\"" + beanName.toUpperCase() +
                ".EDIT_PAGE_TITLE\"/>";
        String lsSaveButton = "<s:text name=\"" + beanName.toUpperCase() +
                ".BUTTON.SAVE\"/>";
        String lsUpdateButton = "<s:text name=\"" + beanName.toUpperCase() +
                ".BUTTON.UPDATE\"/>";

        StringBuffer lsAddText = new StringBuffer();
        StringBuffer lsEditText = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(
                    "templates\\Struts2\\Add_Edit_Struts2.template"));
            while (dis.available() > 0) {
                String lsAdd = dis.readLine();
                if (lsAdd.indexOf("<BeanName>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<BeanName>", beanName);
                }
                if (lsAdd.indexOf("<BeanTags>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<BeanTags>", lsBeanTags.toString());
                }
                if (lsAdd.indexOf("<HeaderInformations>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<HeaderInformations>",
                            lsHeaderInfo.toString());
                }
                if (lsAdd.indexOf("<MadatoryValidations>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<MadatoryValidations>",
                            lsJavaScriptValidations.toString());
                }
                if (lsAdd.indexOf("<UBeanName>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<UBeanName>", beanName.toUpperCase());
                }

                if (lsAdd.indexOf("<Disabled>") >= 0) {

                    lsAdd = lsAdd.replaceAll(" <Disabled> ", "");
                }
                // T-True, F-False
                if (lsAdd.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {

                    /////lsAdd = lsAdd.replaceAll("<NotNullReadonlyTrueNoDate>", " readonly=\"false\"");
                    lsAdd = lsAdd.replaceAll("<NotNullReadonlyTrueNoDate>", "");
                }
                if (lsAdd.indexOf("<NotNullReadonlyFalseNoDate>") >= 0) {

                    /////lsAdd = lsAdd.replaceAll("<NotNullReadonlyFalseNoDate>", " readonly=\"false\"");
                    lsAdd = lsAdd.replaceAll("<NotNullReadonlyFalseNoDate>", "");
                }

                if (lsAdd.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {

                    for (int i = 0; i < list.size(); i++) {
                        tableVO = (TableVO) list.get(i);
                        if (tableVO.isSelect()) {

                            if (lcDateReadonlyFalseNullTrue.equals(i)) {
                                String propimage = tableVO.getColumnName().toLowerCase();
                               // lsAdd = lsAdd.replaceAll(" <DateFT> ", "\t\t\t\t&nbsp;<img src='images/DateIcon.gif' onclick=\"showCalendarControl(document.getElementById('" + propimage + "'))\">\n");

                            }
                        }

                    }
                    /////lsAdd = lsAdd.replaceAll(" <NotNullandReadonlyfalse> ", " readonly=\"false\"");
                    lsAdd = lsAdd.replaceAll(" <NotNullandReadonlyfalse> ", "");
                }

                if (lsAdd.indexOf(" <NotNull> ") >= 0) {
                    for (int i = 0; i < list.size(); i++) {
                        tableVO = (TableVO) list.get(i);
                        if (tableVO.isSelect()) {
                            if (listcount.equals(i)) {
                                String propimage = tableVO.getColumnName().toLowerCase();
                               // lsAdd = lsAdd.replaceAll(" <TTT> ", "\t\t\t\t&nbsp;<img src='images/DateIcon.gif' onclick=\"showCalendarControl(document.getElementById('" +beanName+"VO"+ propimage + "'))\">\n");
                            }
                        }
                    }
                    /////lsAdd = lsAdd.replaceAll(" <NotNull> ", " readonly=\"false\"");
                    lsAdd = lsAdd.replaceAll(" <NotNull> ", "");
                }
                if (lsAdd.indexOf(" <NullReadonlyTrueDate> ") >= 0) {
                    lsAdd = lsAdd.replaceAll(" <DateTF> ", "");
                    lsAdd = lsAdd.replaceAll(" <NullReadonlyTrueDate> ", " readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                if (lsAdd.indexOf("<NullReadonlyFalseDate>") >= 0) {
                    for (int i = 0; i < list.size(); i++) {
                        tableVO = (TableVO) list.get(i);
                        if (tableVO.isSelect()) {
                            if (lcDateReadonlyFalseNullFalse.equals(i)) {
                                String propimage = tableVO.getColumnName().toLowerCase();
                               // lsAdd = lsAdd.replaceAll(" <DateFF> ", "\t\t\t\t&nbsp;<img src='images/DateIcon.gif' onclick=\"showCalendarControl(document.getElementById('" +beanName+"VO"+ propimage + "'))\">\n");
                            }
                        }
                    }
                    /////lsAdd = lsAdd.replaceAll("<NullReadonlyFalseDate>", " readonly=\"false\"");
                    lsAdd = lsAdd.replaceAll("<NullReadonlyFalseDate>", "");
                }
                if (lsAdd.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
                    lsAdd = lsAdd.replaceAll(" <NullReadonlyTrueNoDate> ", " readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                if (lsAdd.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
                    /////lsAdd = lsAdd.replaceAll("<NullReadonlyFalseNoDate>", " readonly=\"false\"");
                    lsAdd = lsAdd.replaceAll("<NullReadonlyFalseNoDate>", "");
                }
                if (lsAdd.indexOf("<PageTitle>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<PageTitle>", lsAddPageTitle);
                }
                if (lsAdd.indexOf("<ButtonCaption>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<ButtonCaption>", lsSaveButton);
                }
                if (lsAdd.indexOf("<ButtonEvent>") >= 0) {
                    lsAdd = lsAdd.replaceAll("<ButtonEvent>", "create");
                }
                if (lsAdd.indexOf("<passwordReadOnlyfalse>") >= 0) {
                    /////lsAdd = lsAdd.replaceAll("<passwordReadOnlyfalse>", " readonly =\"false\" ");
                    lsAdd = lsAdd.replaceAll("<passwordReadOnlyfalse>", "");
                }
                if (lsAdd.indexOf("<passwordReadOnlytrue>") >= 0) {
                    /////lsAdd = lsAdd.replaceAll("<passwordReadOnlytrue>", " readonly =\"false\" ");
                    lsAdd = lsAdd.replaceAll("<passwordReadOnlytrue>", "");
                }
                lsAddText.append(lsAdd);
                lsAddText.append("\n");
            }
            dis.close();

            DataInputStream disEdit = new DataInputStream(new FileInputStream(
                    "templates\\Struts2\\Add_Edit_Struts2.template"));
            while (disEdit.available() > 0) {

                String lsEdit = disEdit.readLine();
                if (lsEdit.indexOf("<BeanName>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<BeanName>", beanName);
                }
                if (lsEdit.indexOf("<BeanTags>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<BeanTags>", lsBeanTags4edit.toString());
                }
                if (lsEdit.indexOf("<HeaderInformations>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<HeaderInformations>",
                            lsHeaderInfo.toString());
                }
                if (lsEdit.indexOf("<MadatoryValidations>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<MadatoryValidations>",
                            lsJavaScriptValidations.toString());
                }
                if (lsEdit.indexOf("<UBeanName>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<UBeanName>", beanName.toUpperCase());
                }
                if (lsEdit.indexOf(" <Disabled> ") >= 0) {
                    lsEdit = lsEdit.replaceAll(" <Disabled> ", " readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                if (lsEdit.indexOf("<NotNullReadonlyTrueNoDate>") >= 0) {
                	lsEdit = lsEdit.replaceAll("<NotNullReadonlyFalseNoDate>", "");
                    //lsEdit = lsEdit.replaceAll("<NotNullReadonlyTrueNoDate>", " readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                if (lsEdit.indexOf("<NotNullReadonlyFalseNoDate>") >= 0) {
                    /////lsEdit = lsEdit.replaceAll("<NotNullReadonlyFalseNoDate>", " readonly=\"false\"");
                    lsEdit = lsEdit.replaceAll("<NotNullReadonlyFalseNoDate>", "");
                }
                if (lsEdit.indexOf(" <NotNull> ") >= 0) {
                    for (int i = 0; i < list.size(); i++) {
                        tableVO = (TableVO) list.get(i);
                        if (tableVO.isSelect()) {
                            if (lcDateReadonlyTrueNullFalse.equals(i)) {
                                String propimage = tableVO.getColumnName().toLowerCase();

                                //lsEdit = lsEdit.replaceAll(" <DateFF> ", "");
                            }
                        }
                    }
                    lsEdit = lsEdit.replaceAll("<NotNullReadonlyFalseNoDate>", "");
                    //lsEdit = lsEdit.replaceAll(" <NotNull> ", " readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                if (lsEdit.indexOf(" <NotNullandReadonlyfalse> ") >= 0) {
                    for (int i = 0; i < list.size(); i++) {
                        tableVO = (TableVO) list.get(i);
                        if (tableVO.isSelect()) {
                            if (lcDateReadonlyFalseNullTrue.equals(i)) {
                                String propimage = tableVO.getColumnName().toLowerCase();
                                //lsEdit = lsEdit.replaceAll(" <DateFT> ", "\t\t\t\t&nbsp;<img src='images/DateIcon.gif' onclick=\"showCalendarControl(document.getElementById('" + propimage + "'))\">\n");
                            }
                        }
                    }
                    /////lsEdit = lsEdit.replaceAll(" <NotNullandReadonlyfalse> ", " readonly=\"false\"");
                    lsEdit = lsEdit.replaceAll(" <NotNullandReadonlyfalse> ", "");
                }
                if (lsEdit.indexOf("<NullReadonlyTrueDate>") >= 0) {
                    lsEdit = lsEdit.replaceAll(" <TTT> ", "");
                    lsEdit = lsEdit.replaceAll(" <NotNullandReadonlyfalse> ", "");
                    //lsEdit = lsEdit.replaceAll(" <NullReadonlyTrueDate> ", " readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                if (lsEdit.indexOf("<NullReadonlyFalseDate>") >= 0) {
                    for (int i = 0; i < list.size(); i++) {
                        tableVO = (TableVO) list.get(i);
                        if (tableVO.isSelect()) {
                            if (lcDateReadonlyFalseNullFalse.equals(i)) {
                                String propimage = tableVO.getColumnName().toLowerCase();
                                //lsEdit = lsEdit.replaceAll(" <DateFF> ", "\t\t\t\t&nbsp;<img src='images/DateIcon.gif' onclick=\"showCalendarControl(document.getElementById('" + propimage + "'))\">\n");
                            }
                        }
                    }
                    /////lsEdit = lsEdit.replaceAll(" <NullReadonlyFalseDate> ", " readonly=\"false\"");
                    lsEdit = lsEdit.replaceAll(" <NullReadonlyFalseDate> ", "");
                }
                if (lsEdit.indexOf("<NullReadonlyTrueNoDate>") >= 0) {
                    lsEdit = lsEdit.replaceAll(" <NullReadonlyTrueNoDate> ", " readonly=\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                if (lsEdit.indexOf("<NullReadonlyFalseNoDate>") >= 0) {
                    /////lsEdit = lsEdit.replaceAll(" <NullReadonlyFalseNoDate> ", " readonly=\"false\"");
                    lsEdit = lsEdit.replaceAll(" <NullReadonlyFalseNoDate> ", "");
                }
                if (lsEdit.indexOf("<PageTitle>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<PageTitle>", lsEditPageTitle);
                }
                if (lsEdit.indexOf("<ButtonCaption>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<ButtonCaption>", lsUpdateButton);
                }
                if (lsEdit.indexOf("<ButtonEvent>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<ButtonEvent>", "update");
                }
                if (lsEdit.indexOf("<passwordReadOnlyfalse>") >= 0) {
                    /////lsEdit = lsEdit.replaceAll("<passwordReadOnlyfalse>", " readonly =\"false\" ");
                    lsEdit = lsEdit.replaceAll("<passwordReadOnlyfalse>", "");
                }
                if (lsEdit.indexOf("<passwordReadOnlytrue>") >= 0) {
                    lsEdit = lsEdit.replaceAll("<passwordReadOnlytrue>", " readonly =\"true\" style=\"background-color:#d3d3d3;\" ");
                }
                lsEditText.append(lsEdit);
                lsEditText.append("\n");
            }
            disEdit.close();
        /*Bug #19 & 14 dated:19/03/2008 updated By:Sanjit Mandal*/
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        
    	
        String sources[] = new String[4];
      // sources[0] = lsIndex.toString();
        sources[1] = result.toString();
        sources[2] = lsAddText.toString();
        sources[3] = lsEditText.toString();
        
    	
    	return sources; 

    }

	/**
	 * Generates the TagLibs to be included
	 * 
	 * @return
	 */
	private String getTagLibs() {
		StringBuffer tagLib = new StringBuffer();
		tagLib.append("\n<%@ taglib uri=\"/WEB-INF/struts-tags.tld\" prefix=\"s\"%>\n");
		tagLib.append("\n<%@ taglib prefix=\"sx\" uri=\"/struts-dojo-tags\"%>\n");
		/*
		 * tagLib.append(
		 * "<%@ taglib uri=\"/WEB-INF/struts-bean.tld\" prefix=\"bean\"%>\n\n");
		 */
		return tagLib.toString();
	}

	/**
	 * Generates the Header tags
	 * 
	 * @param tableName
	 * @return
	 */
	private String getHeader(String tableName) {
		StringBuffer header = new StringBuffer();
		header.append("<s>\n");
		header.append("<TITLE>");
		header.append(tableName.toUpperCase());
		header.append("</TITLE>\n");

		return header.toString();

	}

	/**
	 * Generates the JavaScript tags
	 * 
	 * @return
	 */
	private String getJavaScript() {

		StringBuffer script = new StringBuffer();
		script.append("<SCRIPT LANGUAGE=\"JavaScript\">\n");
		script.append("<!--\n");
		script.append("\n");
		script.append(getDynamicActionFunc());
		script.append("\n");
		script.append("//-->\n");
		script.append("</SCRIPT>\n");

		return script.toString();
	}

	private String getDynamicActionFunc() {
		
		StringBuffer function = new StringBuffer();
		function.append("function set(target) \n");
		function.append("{\n");
		function.append("document.forms[0].dispatch.value=target;\n");
		function.append("document.forms[0].submit();\n");
		function.append("}\n");
		return function.toString();
	}

	/**
	 * Generates Screen Title
	 * 
	 * @param tableName
	 * @return
	 */
	private String getScreenTitle(String tableName) {
		StringBuffer screenTitle = new StringBuffer();
		screenTitle.append("<h1 align = center>");
		screenTitle.append("MAINTENANCE SCREEN FOR TABLE - ");
		screenTitle.append(tableName.toUpperCase());
		screenTitle.append("</h1>");
		return screenTitle.toString();
	}

	/**
	 * Generates the data portion of the screen
	 * 
	 * @param list
	 * @return
	 */
	private String getData(ArrayList list) {
		StringBuffer data = new StringBuffer();
		StringBuffer form = new StringBuffer();
		TableVO table = null;

		data.append("<table border=1>\n");

		for (int count = 0; count < list.size(); count++) {
			table = (TableVO) list.get(count);
			if (table.isSelect()) {
				String controlType = table.getControlType();
				data.append("<tr>\n");
				data.append("<td>\n");
				data.append("<label>");
				data.append(table.getLabelName());
				data.append("</label>");
				data.append("</td>\n");
				data.append("<td>\n");
				data.append("<s:");
			    data.append(controlType);
				data.append(" name=");
				data.append("\"" + table.getColumnName().toLowerCase() + "\"");

				if (controlType.equalsIgnoreCase(HexFrameConstants.TEXT_BOX)) {
					data.append(" size=");
					data.append("\"");
					data.append(table.getDataLength());
					data.append("\"");

					data.append(" maxlength=");
					data.append("\"");
					data.append(table.getDataLength());
					data.append("\"");
				}
				data.append("/>");
				data.append("</td>\n");
				data.append("</tr>\n");
			}
		}

		data.append("</table>\n");

		form.append("\n<body bgcolor=\"skyblue\">\n");
		form.append("<s:form action=\"");
		form.append("/");
		form.append(table.getTableName().toLowerCase());
		form.append("\">\n");

		form.append(data.toString());

		return form.toString();

	}

	public String getErrorJSP(String jspName) {
		return "Error_" + jspName + ".jsp";
	}

	public void generateIndexPage(ArrayList list, String warFile,
			String outDirectory) {

		System.out.println(" Inside generate Home Page ");
		DataInputStream dis = null;
		StringBuffer result = new StringBuffer();

		try {
			StringBuffer lsIndex = new StringBuffer();
			/*
			 * ResourceBundle pr = new PropertyResourceBundle(this.getClass()
			 * .getResourceAsStream("port.properties"));
			 */
			ResourceBundle pr = new PropertyResourceBundle(new FileInputStream(
					"BaseDataManager.properties"));
			for (int index = 0; index < list.size(); index++) {
				HashMap poMap = (HashMap) list.get(index);
				String psObjectName = (String) poMap.get("TABLE");

				/*lsIndex.append("        <a href =\" http://"
						+ pr.getString("server.host") + ":"
						+ pr.getString("server.port") + "/" + warFile + "/"
						+ psObjectName.toLowerCase() + ".do?dispatch=list \">"
						+ psObjectName + "</a>\n\n\n");*/
				
				lsIndex.append("  <s:url action=\"list.action\" var=\"urlTag\">\n");   
				lsIndex.append("  </s:url>\n");
				lsIndex.append("  <a href=\"<s:property value=\"#urlTag\"/>\">" + psObjectName +"</a>\n\n\n");
				
				
				
			}
			System.out.println("LSINDEX ***********" + lsIndex.toString());
			dis = new DataInputStream(new FileInputStream(
					"templates\\Struts2\\index_Struts2.template"));
			result = new StringBuffer();
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<warFileList>",
						lsIndex.toString());
				result.append(line);
				result.append("\n");
			}
			dis.close();
			String jspDirectory = outDirectory + "\\jsp\\";
			HexUtil.makeDirectory(jspDirectory);
			HexUtil.writeFile(result.toString(), jspDirectory + "\\home.jsp");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private StringBuffer prepareRadioTag(StringBuffer lsBeanTags, String prop,
			String controlType, TableVO tableVO) {

		for (int index = 0; index < tableVO.getLabelsRadio().length
				|| index < tableVO.getValuesRadio().length; index++) {
			lsBeanTags.append("\t\t\t\t<s:");
			lsBeanTags.append(controlType);
			lsBeanTags.append(" name=");
			lsBeanTags.append("\"" + prop + "\"");
			lsBeanTags.append(" value=\"%{");
			lsBeanTags.append(tableVO.getValuesRadio()[index]);
			lsBeanTags.append("}\"/>");
			lsBeanTags.append(tableVO.getLabelsRadio()[index].toUpperCase());
			lsBeanTags.append("\n");
			
		}
		lsBeanTags.append("\t\t\t</td>\n");

		return lsBeanTags;
	}
}
