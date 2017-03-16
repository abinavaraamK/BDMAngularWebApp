package com.hex.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.hex.UIController;
import com.hex.vo.TableVO;

public class AngularJs1ScriptFileGenerator {

	
	public void generateScriptJs(String outDirectory, ArrayList tableList, String warFile) throws IOException{
		
		 	System.out.println(" Inside generateScriptJs ");
	        StringBuffer buffer = new StringBuffer();
	        TableVO tableVO = (TableVO) tableList.get(0);
			String beanName = tableVO.getTableName().toLowerCase();
			String beanNameC = beanName.substring(0, 1).toUpperCase()+beanName.substring(1, beanName.length());
			StringBuffer columns = new StringBuffer();
			
	        try {
	        	/*DataInputStream dis = new DataInputStream(new FileInputStream(
                "E:\\Internaltools\\BDM\\BaseDataManager\\Run Time\\templates\\AngularJs1\\ScripsJs.template"));*/
	       
	        	InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\AngularJs1\\ScripsJs.template");
				 
				DataInputStream dis = new DataInputStream(inputStream);   
	           
	            for (int i = 0; i < tableList.size(); i++) {
        			tableVO = (TableVO) tableList.get(i);
        			if (tableVO.isSelect()) {
        			
        				String prop = tableVO.getColumnName().toLowerCase();
        				columns.append("\""+prop+"\",");
        			}
        		}
	        	while (dis.available() > 0) {
	                String line = dis.readLine();
	              
	            
	                if (line.indexOf("<BeanName>") >= 0) {
						line = line.replaceAll("<BeanName>", beanName);
					}
	                if (line.indexOf("<BeanNameC>") >= 0) {
						line = line.replaceAll("<BeanNameC>", beanNameC);
					}
	                if (line.indexOf("<ColumnNames>") >= 0) {
						line = line.replaceAll("<ColumnNames>", columns.toString());
					}
	                if (line.indexOf("<ProjectName>") >= 0) {
						line = line.replaceAll("<ProjectName>", warFile);
					}
	                buffer.append(line);
	                buffer.append("\n");
	            }
	            dis.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        String outD = outDirectory + "\\js\\";
	        HexUtil.makeDirectory(outD);
	        HexUtil.writeFile(buffer.toString(),
	                outD + "\\script.js");
	        
	        new AngularJs1ScriptFileGenerator().angularScripts(outD);
		
	}
	
	
	public void angularScripts(String outD) throws IOException{
		

		String file = UIController.class.getResource("com\\hex\\templates\\templates\\AngularJs1\\js").getFile();
				
		FileUtils.copyDirectory(new File(file), new File(outD));
		
		
		
	}
	
	
	
}
