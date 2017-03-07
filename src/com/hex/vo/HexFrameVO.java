package com.hex.vo;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class HexFrameVO
    implements Serializable {

  public String currentScreen;
  public String nextScreen;

  public void setCurrentScreen(String currentScreen) {
    this.currentScreen = currentScreen;
  }

  public String gtCurrentScreen() {
    return this.currentScreen;
  }

  public void setNextScreen(String nextScreen) {
    this.nextScreen = nextScreen;
  }

  public String getNextScreen() {
    return this.nextScreen;
  }

  public HexFrameVO() {
  }

}