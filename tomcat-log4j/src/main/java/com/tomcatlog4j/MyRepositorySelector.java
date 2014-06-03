package com.tomcatlog4j;

import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;

public class MyRepositorySelector implements RepositorySelector {

	public MyRepositorySelector() {
	
	}
	
	public LoggerRepository getLoggerRepository() {
		return RepositoryClassLoader.getInstance().getLoggerRepository(Thread.currentThread().getContextClassLoader());
	}
	
	public void remove(ClassLoader cl) {
		RepositoryClassLoader.getInstance().remove(Thread.currentThread().getContextClassLoader());
	}
}
