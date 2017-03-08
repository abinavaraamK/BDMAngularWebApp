package com.hex.entity;

import java.util.List;

public class DbDetailsContainer {

	private List<DbDetails> dbDetailsList;
	
	
	private DbDetails dbDetails;

	public DbDetails getDbDetails() {
		return dbDetails;
	}

	public void setDbDetails(DbDetails dbDetails) {
		this.dbDetails = dbDetails;
	}

	public List<DbDetails> getDbDetailsList() {
		return dbDetailsList;
	}

	public void setDbDetailsList(List<DbDetails> dbDetailsList) {
		this.dbDetailsList = dbDetailsList;
	}
	
	
	
}
