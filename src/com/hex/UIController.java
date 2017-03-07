package com.hex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hex.dao.TableDAO;
import com.hex.entity.DbDetails;
import com.hex.util.WebAppObjecBinder;
import com.hex.util.CodeGenerator;
import com.hex.vo.DataBaseVO;
import com.hex.vo.TableVO;
import com.hex.vo.TableVoList;

@Controller
public class UIController {

	@RequestMapping(value = "/path", method = RequestMethod.GET)
	@ResponseBody
	public DbDetails post(
			@RequestParam("configurationName") String configurationName) {

		UIController uiController = new UIController();
		Map<String, DbDetails> dbDetailsMap = uiController.getdbDetails();
		DbDetails dbDetails = new DbDetails();
		dbDetails = dbDetailsMap.get(configurationName);

		return dbDetails;
	}

	@RequestMapping(value = "/getTableData", method = RequestMethod.GET)
	@ResponseBody
	public List<TableVO> getTableData(
			@RequestParam("tableName") String tableName) {
		System.out.println("Table Name :" + tableName);

		TableDAO tableDAO = TableDAO.getInstance();

		List<TableVO> tableVoList = new ArrayList<>();
		tableVoList = tableDAO.getTableDetails(tableName);

		return tableVoList;
	}

	@RequestMapping(value = "/baseData", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView dataBaseDetails() {

		List<String> dbList = new ArrayList<>();
		dbList.add("HEXTRM3 - Oracle");
		dbList.add("Scott - Oracle");
		dbList.add("Hex-Static");
		dbList.add("MySql-Db");
		dbList.add("MySql-Bluemix");

		DbDetails dbDetails = new DbDetails();

		ModelAndView modelAndView = new ModelAndView("DataBase", "dbDetails",
				dbDetails);
		modelAndView.addObject("ListConfigDetails", dbList);
		return modelAndView;

	}

	@RequestMapping(value = "/generate", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView generate(@ModelAttribute TableVoList tableVoList)
			throws Exception {

		System.out.println("table name" + tableVoList.getTableName());
		System.out.println("label:" + tableVoList.getLabelName());
		System.out.println("framewok" + tableVoList.getFrameworkSelected());
		System.out.println("business" + tableVoList.getBusinessSelected());
		System.out.println("persistence:"
				+ tableVoList.getPersistenceSelected());
		System.out.println("file name" + tableVoList.getFileName());
		System.out.println("pkgname" + tableVoList.getPackageName());
		System.out.println("dest dir" + tableVoList.getDestDirectory());
		System.out.println("theme" + tableVoList.getTheme());

		for (TableVO tableVO : tableVoList.getListTableVO()) {
			System.out.println(tableVO.getTableName());
			System.out.println(tableVO.getColumnName().replace("/",""));
			System.out.println(tableVO.getDataType());
			System.out.println(tableVO.getDataLength());
		}

		WebAppObjecBinder appObjecBinder = new WebAppObjecBinder();
		ArrayList list = new ArrayList<>();

		list = appObjecBinder.generateOutput(tableVoList);
		CodeGenerator codeGenerator = new com.hex.util.CodeGenerator();
		try {
			codeGenerator.generateOutput(list);
		} catch (Exception exp) {
			throw exp;
		}

		return null;

	}

	@RequestMapping(value = "/tableDetails", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView getTableDetails(@ModelAttribute DbDetails dbDetails,
			Model model) throws IOException {

		System.out.println("inside table details:" + dbDetails.getUserName());

		DataBaseVO dataBaseVO = new DataBaseVO();
		dataBaseVO.setDriverName(dbDetails.getDriverName());
		dataBaseVO.setUrl(dbDetails.getUrl());
		dataBaseVO.setSchema(dbDetails.getSchemaName());
		dataBaseVO.setPortNo(dbDetails.getPortNo());
		dataBaseVO.setUserName(dbDetails.getUserName());
		dataBaseVO.setPassword(dbDetails.getPassword());
		TableVoList tabList = new UIController().getAllTables(dataBaseVO);

		List<String> dbtoGenerate = new ArrayList<>();
		dbtoGenerate.add("ORACLE");
		dbtoGenerate.add("MS-SQL");
		dbtoGenerate.add("MySQL");
		dbtoGenerate.add("DB2");
		dbtoGenerate.add("PointBase");
		dbtoGenerate.add("HSQL");

		List<String> frameworkLayer = new ArrayList<>();
		frameworkLayer.add("JSF");
		frameworkLayer.add("Struts");
		frameworkLayer.add("Struts 2");
		frameworkLayer.add("PrimeFaces");
		frameworkLayer.add("AngularJs 1.x");
		frameworkLayer.add("Angular 2");

		List<String> businessLayer = new ArrayList<>();
		businessLayer.add("Spring");
		businessLayer.add("POJO");

		List<String> persistenceLayer = new ArrayList<>();
		persistenceLayer.add("Hibernate");
		persistenceLayer.add("StruJPA(TopLink)");
		persistenceLayer.add("JPA(Hibernate)");

		tabList = new UIController().getAllTables(dataBaseVO);
		ModelAndView modelAndView = new ModelAndView("TableList");
		modelAndView.addObject("tableList", tabList);

		modelAndView.addObject("tableVoList", new TableVoList());
		modelAndView.addObject("frameworkLayerList", frameworkLayer);
		modelAndView.addObject("businessLayerList", businessLayer);
		modelAndView.addObject("persistenceLayerList", persistenceLayer);
		modelAndView.addObject("databaseList", dbtoGenerate);

		return modelAndView;

	}

	public Map<String, DbDetails> getdbDetails() {

		DbDetails dbDetails = new DbDetails();
		dbDetails.setConfigurationName("HEXTRM3 - Oracle");
		dbDetails.setDriverName("oracle.jdbc.driver.OracleDriver");
		dbDetails.setUrl("jdbc:oracle:thin:@172.25.108.59:1521:orcl");
		dbDetails.setUserName("hextrm3");
		dbDetails.setPassword("hextrm3");
		dbDetails.setPortNo("3016");
		dbDetails.setSchemaName("orcl");

		DbDetails dbDetails2 = new DbDetails();
		dbDetails2.setConfigurationName("Scott - Oracle");
		dbDetails2.setDriverName("oracle.jdbc.driver.OracleDriver");
		dbDetails2.setUrl("jdbc:oracle:thin:@172.25.108.59:1521:orcl");
		dbDetails2.setUserName("scott");
		dbDetails2.setPassword("tiger");
		dbDetails2.setPortNo("3016");
		dbDetails2.setSchemaName("orcl");

		DbDetails dbDetails3 = new DbDetails();
		dbDetails3.setConfigurationName("Hex-Static");
		dbDetails3.setDriverName("oracle.jdbc.driver.OracleDriver");
		dbDetails3.setUrl("jdbc:oracle:thin:@172.25.108.59:1521:orcl");
		dbDetails3.setUserName("hexdev");
		dbDetails3.setPassword("hexdev");
		dbDetails3.setPortNo("3016");
		dbDetails3.setSchemaName("orcl");

		DbDetails dbDetails4 = new DbDetails();
		dbDetails4.setConfigurationName("MySql-Db");
		dbDetails4.setDriverName("com.mysql.jdbc.Driver");
		dbDetails4.setUrl("jdbc:mysql://localhost:3306/test");
		dbDetails4.setUserName("root");
		dbDetails4.setPassword("password123");
		dbDetails4.setPortNo("");
		dbDetails4.setSchemaName("");
		
		DbDetails dbDetails5 = new DbDetails();
		dbDetails5.setConfigurationName("MySql-Bluemix");
		dbDetails5.setDriverName("com.mysql.jdbc.Driver");
		dbDetails5.setUrl("jdbc:mysql://sl-us-dal-9-portal.5.dblayer.com:20514/compose");
		dbDetails5.setUserName("admin");
		dbDetails5.setPassword("NYMQZUCFGDSICGZD");
		dbDetails5.setPortNo("");
		dbDetails5.setSchemaName("");

		Map<String, DbDetails> dbMap = new HashMap<>();
		dbMap.put("HEXTRM3 - Oracle", dbDetails);
		dbMap.put("Scott - Oracle", dbDetails2);
		dbMap.put("Hex-Static", dbDetails3);
		dbMap.put("MySql-Db", dbDetails4);
		dbMap.put("MySql-Bluemix",dbDetails5);

		return dbMap;

	}

	public TableVoList getAllTables(DataBaseVO dataBaseVO) {

		TableDAO tableDAO = TableDAO.getInstance();
		tableDAO.setDataBaseVO(dataBaseVO);
		TableVoList tableVoList = new TableVoList();

		tableVoList = tableDAO.getAllTables();

	return tableVoList;

	}

	

}
