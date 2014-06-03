package com.tomcatlog4j;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RootCategory;

public class RepositoryClassLoader {
	private static RepositoryClassLoader instance = null;
	
	private Hashtable<Object, LoggerRepository> ht;
	
	public RepositoryClassLoader(){
		ht = new Hashtable<Object, LoggerRepository>();
	}
	
	public static synchronized RepositoryClassLoader getInstance(){
		if (instance == null){
			instance = new RepositoryClassLoader();
		}
		
		return instance;
	}
	
	public synchronized void add(ClassLoader classLoader, LoggerRepository loggerRepository){
		ht.put(classLoader, loggerRepository);
	}
	
	public synchronized LoggerRepository getLoggerRepository(ClassLoader classLoader){
		LoggerRepository loggerRepository = (LoggerRepository) ht.get(classLoader);
		
		if (loggerRepository == null) {
			loggerRepository = new MyHierarchy(new RootCategory((Level) Level.DEBUG));
			add(classLoader, loggerRepository);	
		}
		
		return loggerRepository;
	}

	public synchronized void remove(ClassLoader classLoader){
		ht.remove(classLoader);
	}
	
	public Enumeration<Object> getClassLoaderList(){
		return ht.keys();
	}
}
