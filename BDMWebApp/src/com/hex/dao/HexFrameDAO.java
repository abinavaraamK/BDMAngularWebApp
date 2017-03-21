package com.hex.dao;

import com.hex.vo.DataBaseVO;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class HexFrameDAO {

  public DataBaseVO ioDataBaseVO;
  private static HexFrameDAO ioHexFrameDAO;

  public void setDataBaseVO(DataBaseVO dataVO) {
    this.ioDataBaseVO = dataVO;
  }

  public DataBaseVO getDataBaseVO() {
    return this.ioDataBaseVO;
  }

  public HexFrameDAO() {
	  
  }


  public Connection getConnection() {

    System.out.println("Inside getConnection ");

    Connection con = null;
    try {
      Class.forName(getDataBaseVO().getDriverName());
      con = DriverManager.getConnection(getDataBaseVO().getConnectionURl(),
                                        getDataBaseVO().getUserName(),
                                        getDataBaseVO().getPassword());
      //System.out.println("Got Connection ");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return con;

  }

  public void closePreparedStatement(PreparedStatement poStmt) {
    try {
      if (poStmt != null) {
        poStmt.close();
      }
    }
    catch (SQLException woSQLException) {
      woSQLException.printStackTrace();
    }
  }

  public void releaseConnection(Connection poConn) {
    //System.out.println("Inside releaseConnection() - ");

    try {
      if ( (poConn != null) && (!poConn.isClosed())) {
        poConn.close();
      }
    }
    catch (SQLException woSQLException) {
      woSQLException.printStackTrace();
    }
  }

}