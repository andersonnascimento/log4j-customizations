
package com.tomcatlog4j;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class MyLoggerFactory implements LoggerFactory {

  public MyLoggerFactory() {
  }

  public Logger makeNewLoggerInstance(String name) {
    return new MyLogger(name);
  }
}
