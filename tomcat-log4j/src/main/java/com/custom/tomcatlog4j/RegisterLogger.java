package com.custom.tomcatlog4j;

import org.apache.log4j.LogManager;

public class RegisterLogger {

	static{
		Object guard = new Object();
		
		LogManager.setRepositorySelector(new MyRepositorySelector(), guard);

		org.apache.log4j.BasicConfigurator.configure();
	}
}
