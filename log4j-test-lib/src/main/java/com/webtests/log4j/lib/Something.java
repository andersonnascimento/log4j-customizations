
package com.webtests.log4j.lib;

import org.apache.log4j.Logger;

public class Something {
	
	private static Logger logger = Logger.getLogger(Something.class);
	 
	public Something(){
		logger.info("com.webtests.log4j.lib.something() - Creating object something.");
    }
    
	
    public void doSomething(){
    	logger.info("com.webtests.log4j.lib.doSomething() - " + Something.class.getClassLoader().getResource("").toString());
    	
    	logger.info("com.webtests.log4j.lib.doSomething() - doSomething.");
    	
    	if (logger.isDebugEnabled()) {
    		logger.debug("com.webtests.log4j.lib.doSomething() - doSomething debug enabled.");
    	}
    	if (logger.isTraceEnabled()) {
    		logger.debug("com.webtests.log4j.lib.doSomething() - doSomething trace enabled.");
    	}
    }
}
