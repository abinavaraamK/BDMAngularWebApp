package com.hex.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.hex.UIController;

public class HexAngularJs1Generator {

	private String lsPackageDir;
	private String lsPackage;

	private String initCap(String ps) {
		ps = ps.substring(0, 1).toUpperCase() + ps.substring(1).toLowerCase();
		return ps;
	}

	public void generatePresentationTierFiles(ArrayList list, String templatesLocation) throws Exception {

		String outDirectory = "";
		String table = "";
		String warFile = "";
		String logger = "";
		ArrayList tableList = null;
		String lsPackage = null;
		AngularJs1HtmlGenerator angularJs1HtmlGenerator = new AngularJs1HtmlGenerator();
		AngularJs1ScriptFileGenerator angularJs1ScriptFileGenerator = new AngularJs1ScriptFileGenerator();

		for (int i = 0; i < list.size(); i++) {
			HashMap poMap = (HashMap) list.get(i);
			tableList = (ArrayList) poMap.get("LIST");
			outDirectory = (String) poMap.get("DIRECTORY");
			table = (String) poMap.get("TABLE");
			String lsPageTitle = (String) poMap.get("PAGE_TITLE");
			lsPackage = (String) poMap.get("PACKAGE");
			String lsPresentation = (String) poMap.get("PRESENTATION");
			String lsBusiness = (String) poMap.get("BUSINESS");
			String lsPersistence = (String) poMap.get("PERSISTENCE");
			String lsPackageDir = (String) poMap.get("PACKAGE_DIR");
			warFile = (String) poMap.get("WAR_FILE");
			String lsPageSource[] = angularJs1HtmlGenerator.getListPageSource(
					tableList, lsPageTitle,templatesLocation);
			String jspDirectory = outDirectory ;
			HexUtil.makeDirectory(jspDirectory);
			String outputFile;

			outputFile = jspDirectory + "\\" + initCap(table) + "List.html";
			HexUtil.writeFile(lsPageSource[1], outputFile);
			outputFile = jspDirectory + "\\" + initCap(table) + "Add.html";
			HexUtil.writeFile(lsPageSource[2], outputFile);
			outputFile = jspDirectory + "\\" + "index.html";
			HexUtil.writeFile(lsPageSource[3], outputFile);

		}
		System.out.println("Out directory in Angular *****************"
				+ outDirectory);
		angularJs1ScriptFileGenerator.generateScriptJs(outDirectory, tableList,warFile,templatesLocation);
		generateWebXML(table, outDirectory,lsPackage,templatesLocation);
	}

	public static void main(String[] args) {
		
		 InputStream inputStream= UIController.class.getResourceAsStream("\\templates\\templates\\build\\HexFrameBuild.template");
		
		System.out.println(UIController.class.getResource("\\templates\\templates\\build\\HexFrameBuild.template").getFile());
		//new HexAngularJs1Generator().generateWebXML("Product", "E://oute", "com.hex");
	}
	
	public void generateWebXML(String tableName,
			String outDirectory, String lsPackage,String templatesLocation) {

		System.out.println(" Inside generateWebXML ");

		String className = HexUtil.initCap(tableName);
		StringBuffer buffer = new StringBuffer();
		try {
		
			DataInputStream dis = new DataInputStream(new FileInputStream(templatesLocation+
					"/templates/AngularJs1/web.template"));
			/*InputStream inputStream= UIController.class.getResourceAsStream("\\templates/templates/AngularJs1/web.template");
			DataInputStream dis = new DataInputStream(inputStream);*/
	/*		DataInputStream dis = new DataInputStream(new FileInputStream(
			"E:\\Internaltools\\BDM\\BaseDataManager\\Run Time/templates/AngularJs1/web.template"));*/
			
			while (dis.available() > 0) {
				String line = dis.readLine();
				line = HexUtil.replaceTags(line, "<ClassName>", className);
				line = HexUtil.replaceTags(line, "<LowerClassName>", className
						.toLowerCase());
				line = HexUtil.replaceTags(line, "<packagename>", lsPackage);
				buffer.append(line);
				buffer.append("\n");
			}
			dis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String outD = outDirectory + "/WEB-INF/";
		HexUtil.makeDirectory(outD);
		HexUtil.writeFile(buffer.toString(), outD + "/web.xml");

	}
}
