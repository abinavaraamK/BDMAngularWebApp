package com.hex.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hex.vo.DataBaseVO;
import com.hex.vo.TableVO;
import com.hex.vo.TableVoList;

import java.sql.DatabaseMetaData;

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

public class TableDAO extends HexFrameDAO {

	private static TableDAO ioTableDAO;

	public static TableDAO getInstance() {

		if (ioTableDAO == null) {
			ioTableDAO = new TableDAO();
		}
		return ioTableDAO;
	}

	private TableDAO() {

	}

	public TableVoList getAllTables() {

		Connection woCon = null;
		PreparedStatement woPstmt = null;
		StringBuffer woStrBuff = new StringBuffer();
		ResultSet woResultSet = null;
		List<TableVO> list = null;
		TableVoList tableVoList = new TableVoList();
		TableVO tableVO = null;
		try {
			list = new ArrayList<>();
			woCon = getConnection();
			String lsTypes[] = { "TABLE" };
			DatabaseMetaData lsDBMetaData = woCon.getMetaData();
			ResultSet rs = lsDBMetaData.getTables(null, null, null, lsTypes);

			while (rs.next()) {

				String table = rs.getString("TABLE_NAME");
				String schema = rs.getString("TABLE_SCHEM");

				if (schema == null)
					schema = "";
				if (schema.equals("null"))
					schema = "";
				if (table.indexOf("$") < 0
						&& (schema != null && !schema.equals("CTXSYS")
								&& !schema.equals("MDSYS")
								&& !schema.equals("SYS")
								&& !schema.equals("WKSYS") && !schema
									.equals("XDB"))) {

					tableVO = new TableVO();
					tableVO.setTableName(table);
					list.add(tableVO);

				}
			}
			tableVoList.setListTableVO(list);
			rs.close();

		} catch (SQLException poSQLException) {
			poSQLException.printStackTrace();
		} finally {
			closePreparedStatement(woPstmt);
			releaseConnection(woCon);
		}
		return tableVoList;
	}

	public List<TableVO> getTableDetails(String tableName) {
		// System.out.println("Inside getTableDetails in DAO ");

		Connection woCon = null;
		PreparedStatement woPstmt = null;
		StringBuffer woStrBuff = new StringBuffer();
		ResultSet woResultSet = null;
		List<TableVO> list = null;
		TableVO table = null;

		try {
			list = new ArrayList<>();
			woCon = getConnection();
			DatabaseMetaData lsDBMetaData = woCon.getMetaData();
			woResultSet = lsDBMetaData.getColumns(null, null, tableName, null);

			while (woResultSet.next()) {

				table = new TableVO();
				Integer dataPrecision = null;
				Integer dataScale = null;

				String coumn = woResultSet.getString("COLUMN_NAME");
				String dataType = woResultSet.getString("TYPE_NAME");
				Integer dataLength = new Integer(
						woResultSet.getInt("COLUMN_SIZE"));
				String nullable = woResultSet.getString("NULLABLE");
				dataPrecision = dataLength;
				dataScale = dataLength;

				table.setTableName(tableName);
				table.setColumnName(coumn);
				table.setDataType(dataType);
				table.setDataLength(dataLength);
				table.setDataPrecision(dataPrecision);
				table.setDataScale(dataScale);
				if ("0".equals(nullable))
					table.setNullable(new Integer(0));
				else
					table.setNullable(new Integer(1));
				list.add(table);
			}

			woResultSet.close();
		} catch (SQLException poSQLException) {
			poSQLException.printStackTrace();
		} finally {
			closePreparedStatement(woPstmt);
			releaseConnection(woCon);
		}

		return list;
	}
public ArrayList getPrimaryKeyDetails(String tableName) {
	
		
		
		tableName = tableName.toUpperCase();
		System.out.println("Inside getPrimaryKeyDetails in DAO:" + tableName
				+ ":");
		Connection woCon = null;
		PreparedStatement woPstmt = null;
		StringBuffer woStrBuff = new StringBuffer();
		ResultSet woResultSet = null;
		ArrayList list = null;
		TableVO table = null;

		try {
			list = new ArrayList();
			woCon = getConnection();
			DatabaseMetaData lsDBMetaData = woCon.getMetaData();
			woResultSet = lsDBMetaData.getPrimaryKeys(null, null, tableName);
			System.out.println("Before while");
			while (woResultSet.next()) {

				System.out.println("Enter into while cond...");
				table = new TableVO();
				Integer dataPrecision = null;
				Integer dataScale = null;
				String column = woResultSet.getString("COLUMN_NAME");
				table = getColumnDetails(tableName, column);
				System.out.println("PRIMARY KEY : " + column);
				list.add(table);

			}
		
			woResultSet.close();
		} catch (SQLException poSQLException) {
			poSQLException.printStackTrace();
		} finally {
			closePreparedStatement(woPstmt);
			releaseConnection(woCon);
		}

		return list;
	}

	public TableVO getColumnDetails(String tableName, String columnName) {
		// System.out.println("Inside getTableDetails in DAO ");

		Connection woCon = null;
		PreparedStatement woPstmt = null;
		StringBuffer woStrBuff = new StringBuffer();
		ResultSet woResultSet = null;
		ArrayList list = null;
		TableVO table = null;

		try {
			woCon = getConnection();
			DatabaseMetaData lsDBMetaData = woCon.getMetaData();
			woResultSet = lsDBMetaData.getColumns(null, null, tableName,
					columnName);
			if (woResultSet.next()) {

				table = new TableVO();
				Integer dataPrecision = null;
				Integer dataScale = null;

				String coumn = woResultSet.getString("COLUMN_NAME");
				
				String dataType = woResultSet.getString("TYPE_NAME");
				Integer dataLength = new Integer(
						woResultSet.getInt("COLUMN_SIZE"));

				if (woResultSet.getString("DECIMAL_DIGITS") != null) {
					dataPrecision = new Integer(
							woResultSet.getInt("DECIMAL_DIGITS"));
				}
				if (woResultSet.getString("DECIMAL_DIGITS") != null) {
					dataScale = new Integer(
							woResultSet.getInt("DECIMAL_DIGITS"));
				}
				table.setTableName(tableName);
				table.setColumnName(coumn);
				table.setDataType(dataType);
				table.setDataLength(dataLength);
				table.setDataPrecision(dataPrecision);
				table.setDataScale(dataScale);
			}
			woResultSet.close();
		} catch (SQLException poSQLException) {
			poSQLException.printStackTrace();
		} finally {
			closePreparedStatement(woPstmt);
			releaseConnection(woCon);
		}
		return table;
	}

}