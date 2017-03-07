package com.hex.vo;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Method {

  public String property;
  public String returnType;
  public String dateFormat;

  public String getDateFormat() {
	return dateFormat;
}

public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
}

public void setProperty(String property) {
    this.property = property;
  }

  public String getProperty() {
    return this.property;
  }

  public void setReturnType(String returnType) {
    this.returnType = returnType;
  }

  public String getReturnType() {
    return this.returnType;
  }

}