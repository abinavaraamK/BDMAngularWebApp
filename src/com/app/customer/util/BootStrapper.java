package com.app.customer.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootStrapper {

  private static ApplicationContext aContext = null;

  
  /**
   * Creates a new BootStrapper object.
   */
  private BootStrapper() {

  }


  /**
   * This method
   *
   * @return org.springframework.context.ApplicationContext
   */
  public synchronized static ApplicationContext getService() {

      if (aContext == null) {

      try {

        aContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
      } catch (Exception exception) {

        exception.printStackTrace();
      }
    }   

    return aContext;
  }
}
