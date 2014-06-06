package com.tomcatlog4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MyLogger extends Logger {

  static String FQCN = MyLogger.class.getName() + ".";


  private static MyLoggerFactory myFactory = new MyLoggerFactory();

  public MyLogger(String name) {
    super(name);
  }

  public MyLogger(Logger logger){
	  super(logger.getName());
  }
  
  
  public void debug(Object message) {
    super.log(FQCN, Level.DEBUG, message + " world.", null);    
  }

  public static Logger getLogger(String name) {
    return Logger.getLogger(name, myFactory); 
  }

  public void trace(Object message) {
    super.log(FQCN, Level.TRACE, message, null); 
  }
  
  public boolean isDebugEnabled(){
	  String value = "false";//(String) MDC.get("SHOW");
	  return value.equals("") || value.equals("true");
  }
  
  public boolean isTraceEnabled(){
	  String value = "false";//(String) MDC.get("SHOW");
	  return value.equals("") || value.equals("true");
  }
}


