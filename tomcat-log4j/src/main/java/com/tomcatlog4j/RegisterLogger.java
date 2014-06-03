package com.tomcatlog4j;

import org.apache.log4j.LogManager;

public class RegisterLogger {

	static{
		Object guard = new Object();
		
		LogManager.setRepositorySelector(new MyRepositorySelector1(), guard);

		org.apache.log4j.BasicConfigurator.configure();
	}
}
