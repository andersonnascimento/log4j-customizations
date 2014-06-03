package com.tomcatlog4j;

import java.util.Hashtable;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootCategory;

public class MyRepositorySelector implements RepositorySelector {

	private Hashtable<Object, Hierarchy> ht;

	public MyRepositorySelector() {
		ht = new Hashtable<Object, Hierarchy>();
		
		ht.put(Thread.currentThread().getContextClassLoader(), (Hierarchy) LogManager.getLoggerRepository());
		
		System.out.println(Thread.currentThread().getContextClassLoader().toString());
	}

	@SuppressWarnings("deprecation")
	public LoggerRepository getLoggerRepository() {
		System.out.println(Thread.currentThread().getContextClassLoader().toString());
		
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Hierarchy hierarchy = (Hierarchy) ht.get(cl);
		if (hierarchy == null) {
			synchronized (ht) {
				hierarchy = new MyHierarchy(new RootCategory((Level) Level.DEBUG));
				ht.put(cl, hierarchy);	
			}
			
		}
		return hierarchy;
	}
	
	public void remove(ClassLoader cl) {
		synchronized (ht) {
			ht.remove(cl);
		}
	}
}
