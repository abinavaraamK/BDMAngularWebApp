package com.hex.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hex.vo.TableVO;
import com.hex.vo.TableVoList;

public class WebAppObjecBinder {

	ArrayList selectedColumnList = new ArrayList();
	ArrayList tableCollectionList = new ArrayList();

	public ArrayList generateOutput(TableVoList tableVoList,String baseLocation) {
		System.out.println("TableList*************** inside generateOutput");
		String outDirectory = tableVoList.getDestDirectory();
		System.out.println("OUTPUT DIRECTORY 1" + outDirectory);
		String warFile = tableVoList.getFileName();

		if (warFile.equals("")) {
			warFile = "test";
		}
		//outDirectory = outDirectory + "\\" + warFile.toLowerCase() + "\\";
		outDirectory = baseLocation + "/" + "NewWebAppArchive"+"/"+warFile.toLowerCase();
		System.out.println("OUTPUT DIRECTORY 2" + outDirectory);
		String table = tableVoList.getTableName().replace(",", "");

		if (new File(outDirectory).isDirectory() == false) {
			new File(outDirectory).mkdirs();
		}

		String lsPackage = tableVoList.getPackageName();
		String lsPackageDir = lsPackage.replace('.', '/');
		HashMap woHash = new HashMap();

		selectedColumnList = new WebAppObjecBinder().setColumnList(tableVoList
				.getListTableVO());

		woHash.put("LIST", selectedColumnList);
		woHash.put("DIRECTORY", outDirectory);
		System.out.println("****************************"
				+ woHash.get("DIRECTORY") + "*******************");
		woHash.put("TABLE", table);
		woHash.put("PAGE_TITLE", tableVoList.getLabelName());
		System.out.println("****************************"
				+ woHash.get("PAGE_TITLE") + "*******************");
		woHash.put("PACKAGE", lsPackage);
		woHash.put("PACKAGE_DIR", lsPackageDir);
		woHash.put("WAR_FILE", warFile);
		woHash.put("PRESENTATION", tableVoList.getFrameworkSelected());
		woHash.put("BUSINESS", tableVoList.getBusinessSelected());
		woHash.put("PERSISTENCE", tableVoList.getPersistenceSelected());
		woHash.put("DATABASE", tableVoList.getDatabasetoGenerate());
		if (tableVoList.getTheme() != null)
			woHash.put("THEME", tableVoList.getTheme());
		for (int count = 0; count < selectedColumnList.size(); count++) {
			TableVO tableVO = (TableVO) selectedColumnList.get(count);
		}
		tableCollectionList.add(woHash);
		return tableCollectionList;

	}

	public ArrayList setColumnList(List<TableVO> list) {
		System.out.println("Inside setColumnList " + list.size());
		String[] data = new String[list.size()];

		for (int count = 0; count < list.size(); count++) {
			TableVO tableVO = (TableVO) list.get(count);
			System.out.println("Column Name " + tableVO.getColumnName());
			data[count] = (String) tableVO.getColumnName();
			TableVO tableVO1 = new TableVO();
			tableVO1.setTableName(tableVO.getTableName());

			/* Bug #19 dated:18/03/2008 updated By:Sanjit Mandal */
			tableVO1.setNullable(tableVO.getNullable());

			/* Bug #19 dated:18/03/2008 updated By:Sanjit Mandal */
			tableVO1.setColumnName(tableVO.getColumnName().replace("/", ""));
			tableVO1.setLabelName(tableVO.getLabelName().replace("/", ""));
			tableVO1.setSelect(true);
			System.out.println("Lable Name :" + tableVO1.getLabelName());
			/* added by jay Bug#23 Dated 20 March */

			int a = tableVO.getNullable();
			if (a != 0) {
				tableVO1.setMandatory(false);
				tableVO1.setMandatoryEditable(true);
			} else {
				tableVO1.setMandatory(true);
				tableVO1.setMandatoryEditable(false);
			}

			/* End : added by jay Bug#23 Dated 20 March */

			tableVO1.setDataLength(tableVO.getDataLength());
			tableVO1.setDataType(tableVO.getDataType());
			System.out.println(tableVO.getDataType()+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+tableVO1.getDataType());
			tableVO1.setControlType(HexFrameConstants.TEXT_BOX);
			selectedColumnList.add(tableVO1);
			if (tableVO1.getColumnName().equalsIgnoreCase("createddate")) {

				tableVO1.setControlType(HexFrameConstants.DATE_BOX1);
			}
			if (tableVO1.getColumnName().equalsIgnoreCase("createdby")) {

				tableVO1.setControlType(HexFrameConstants.TEXT_BOX1);
			}
			if (tableVO1.getColumnName().equalsIgnoreCase("modifieddate")) {

				tableVO1.setControlType(HexFrameConstants.DATE_BOX2);
			}
			if (tableVO1.getColumnName().equalsIgnoreCase("modifiedby")) {

				tableVO1.setControlType(HexFrameConstants.TEXT_BOX2);
			}

		}
		System.out.println("lenght of data---------------------------------"
				+ data.length);

		return selectedColumnList;
	}

}
