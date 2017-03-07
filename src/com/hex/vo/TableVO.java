/*
 * Created on Feb 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hex.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sha3137
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TableVO implements Serializable, Cloneable {

	public String tableName;
	public String columnName;
	public String dataType;
	public Integer dataLength;
	public Integer dataPrecision;
	public Integer dataScale;
	public boolean select;
	public String labelName;
	public String controlType;
	public boolean readonly;
	public boolean mandatory;
	public String keyColumn;
	public String valueColumn;
	public String targetTable;
	public String validationType = "No Validation";
	public String fromDate;
	public String toDate;

	


	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String targetTable2;
	public String[] labelsStaticSelect, valuesStaticSelect;

	public String getTargetTable2() {
		return targetTable2;
	}

	public void setTargetTable2(String targetTable2) {
		this.targetTable2 = targetTable2;
	}

	public String[] getLabelsStaticSelect() {
		return labelsStaticSelect;
	}

	public void setLabelsStaticSelect(String[] labelsStaticSelect) {
		this.labelsStaticSelect = labelsStaticSelect;
	}

	public String[] getValuesStaticSelect() {
		return valuesStaticSelect;
	}

	public void setValuesStaticSelect(String[] valuesStaticSelect) {
		this.valuesStaticSelect = valuesStaticSelect;
	}

	public String dateFormat = "dd/MM/yyyy";

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/* Added By Jay Bug #23 Dated:March 20 */
	public Integer nullable;
	public boolean mandatoryEditable;

	private String[] labelsRadio;
	private String[] valuesRadio;

	// Added by Amutha

	private TableVO[] tableVO;

	public void setMandatoryEditable(boolean s) {
		this.mandatoryEditable = s;
	}

	public boolean isMandatoryEditable() {
		return this.mandatoryEditable;
	}

	public void setNullable(Integer table) {

		this.nullable = table;

	}

	public Integer getNullable() {

		return this.nullable;

	}

	/* End Added By Jay Bug #23 Dated:March 20 */
	public void setTableName(String table) {
		this.tableName = table;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setColumnName(String column) {
		this.columnName = column;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setLabelName(String column) {
		this.labelName = column;
	}

	public String getLabelName() {
		return this.labelName;
	}

	public void setDataType(String type) {
		this.dataType = type;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataLength(Integer length) {
		this.dataLength = length;
	}

	public Integer getDataLength() {
		return this.dataLength;
	}

	public void setDataPrecision(Integer precision) {
		this.dataPrecision = precision;
	}

	public Integer getDataPrecision() {
		return this.dataPrecision;
	}

	public void setDataScale(Integer scale) {
		this.dataScale = scale;
	}

	public Integer getDataScale() {
		return this.dataScale;
	}

	public void setSelect(boolean s) {
		this.select = s;
	}

	public boolean isSelect() {
		return this.select;
	}

	public void setControlType(String control) {
		this.controlType = control;
	}

	public String getControlType() {
		return this.controlType;
	}

	public void setReadonly(boolean s) {
		this.readonly = s;
	}

	public boolean isReadonly() {
		return this.readonly;
	}

	public void setMandatory(boolean s) {
		this.mandatory = s;
	}

	public boolean isMandatory() {
		return this.mandatory;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getTargetTable() {
		return this.targetTable;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public String getKeyColumn() {
		return this.keyColumn;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	public String getValueColumn() {
		return this.valueColumn;
	}

	public String[] getLabelsRadio() {
		return labelsRadio;
	}

	public void setLabelsRadio(String[] labelsRadio) {
		this.labelsRadio = labelsRadio;
	}

	public String[] getValuesRadio() {
		return valuesRadio;
	}

	public void setValuesRadio(String[] valuesRadio) {
		this.valuesRadio = valuesRadio;
	}

	public boolean equals(Object obj) {

		TableVO tableVO = (TableVO) obj;
		if (tableVO.getColumnName().equalsIgnoreCase(getColumnName())) {
			return true;
		}

		return false;
	}

	public Object clone() {
		return this.clone();
	}

	public String toString() {
		return tableName + " : " + columnName + " : " + labelName + " : "
				+ dataType + " : " + controlType + " : " + dataLength + " : "
				+ select + " : " + readonly;
	}

	public TableVO[] getTableVO() {
		return tableVO;
	}

	public void setTableVO(TableVO[] tableVO) {
		this.tableVO = tableVO;
	}
}
