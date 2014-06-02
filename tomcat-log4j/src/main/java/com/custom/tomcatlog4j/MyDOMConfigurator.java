package com.custom.tomcatlog4j;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

public class MyDOMConfigurator extends DOMConfigurator {

	public MyDOMConfigurator() {
		super();
		Object guard = new Object();
		LogManager.setRepositorySelector(new MyRepositorySelector1(), guard);
	}

}
