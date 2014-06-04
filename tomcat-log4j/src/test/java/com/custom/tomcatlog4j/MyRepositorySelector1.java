package com.tomcatlog4j;

import java.util.Hashtable;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootCategory;

public class MyRepositorySelector1 implements RepositorySelector {

	// key: current thread's ContextClassLoader,
	// value: Hierarchy instance
	private Hashtable<ClassLoader, Hierarchy> ht;

	public MyRepositorySelector1() {
		ht = new Hashtable<ClassLoader, Hierarchy>();
	}

	// the returned value is guaranteed to be non-null
	public LoggerRepository getLoggerRepository() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Hierarchy hierarchy = (Hierarchy) ht.get(cl);

		if (hierarchy == null) {
			hierarchy = new MyHierarchy(new RootCategory((Level) Level.DEBUG));
			ht.put(cl, hierarchy);
		}
		return hierarchy;
	}

	/**
	 * The Container should remove the entry when the web-application is removed
	 * or restarted.
	 * */
	public void remove(ClassLoader cl) {
		ht.remove(cl);
	}
}
