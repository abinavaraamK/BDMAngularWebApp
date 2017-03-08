package com.hex.vo;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DataBaseVO
    extends HexFrameVO {

  public ConnectionName name;
  public String driverName;
  public String url;
  public String portNo;
  public String schema;
  public String userName;
  public String password;

  public void setDriverName(String driver) {
    this.driverName = driver;
  }

  public String getDriverName() {
    return driverName;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setPortNo(String port) {
    this.portNo = port;
  }

  public String getPortNo() {
    return portNo;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getSchema() {
    return schema;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public String getConnectionURl() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(getUrl());
    if( getPortNo() != null && getPortNo().trim().length()>0) {
      buffer.append(":");
      buffer.append(getPortNo());
    }
    if( getSchema() != null && getSchema().trim().length()>0) {
      buffer.append("/");
      buffer.append(getSchema());
    }

    System.out.println(buffer.toString());
    return buffer.toString();
  }

  public DataBaseVO() {
  }

    public ConnectionName getName() {
        return name;
    }

    public void setName(ConnectionName connectionName) {
        this.name = connectionName;
    }
}