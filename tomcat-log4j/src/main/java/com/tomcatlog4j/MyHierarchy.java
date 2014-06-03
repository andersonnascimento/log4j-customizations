package com.tomcatlog4j;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class MyHierarchy extends Hierarchy{

	private LoggerFactory defaultFactory;
	  
	public MyHierarchy(Logger root) {
		super(root);
		defaultFactory = new MyLoggerFactory();
	}
	
	@Override
	public Logger getLogger(String name) {
	    return super.getLogger(name, defaultFactory);
	}

}
