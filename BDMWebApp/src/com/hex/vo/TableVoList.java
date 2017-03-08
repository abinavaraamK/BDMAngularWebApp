package com.hex.vo;

import java.util.List;

public class TableVoList {

	private List<TableVO> listTableVO ;
	private String tableName;
	
	private String persistenceSelected;
	private String businessSelected;
	private String frameworkSelected;

	private String databasetoGenerate;
	private String destDirectory;
	private String fileName;

	private String packageName;
	
	private String labelName;
	
	
	private String theme;
	
	private String packageDir;
	
	
	
	
	

	public String getPackageDir() {

		return packageDir;
	}

	public void setPackageDir(String packageDir) {
		this.packageDir = packageDir;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPersistenceSelected() {
		return persistenceSelected;
	}

	public void setPersistenceSelected(String persistenceSelected) {
		this.persistenceSelected = persistenceSelected;
	}

	public String getBusinessSelected() {
		return businessSelected;
	}

	public void setBusinessSelected(String businessSelected) {
		this.businessSelected = businessSelected;
	}

	public String getFrameworkSelected() {
		return frameworkSelected;
	}

	public void setFrameworkSelected(String frameworkSelected) {
		this.frameworkSelected = frameworkSelected;
	}

	public String getDatabasetoGenerate() {
		return databasetoGenerate;
	}

	public void setDatabasetoGenerate(String databasetoGenerate) {
		this.databasetoGenerate = databasetoGenerate;
	}

	public String getDestDirectory() {
		return destDirectory;
	}

	public void setDestDirectory(String destDirectory) {
		this.destDirectory = destDirectory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<TableVO> getListTableVO() {
		return listTableVO;
	}

	public void setListTableVO(List<TableVO> listTableVO) {
		this.listTableVO = listTableVO;
	}
	
	
}
