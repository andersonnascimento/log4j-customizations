package com.custom.tomcatlog4j;

import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;

import com.tomcatlog4j.RepositoryClassLoader;

public class MyRepositorySelector1 implements RepositorySelector {
	
	public MyRepositorySelector1() {
	
	}
	
	public LoggerRepository getLoggerRepository() {
		return RepositoryClassLoader.getInstance().getLoggerRepository(Thread.currentThread().getContextClassLoader());
	}
	
	public void remove(ClassLoader cl) {
		RepositoryClassLoader.getInstance().remove(Thread.currentThread().getContextClassLoader());
	}
}
