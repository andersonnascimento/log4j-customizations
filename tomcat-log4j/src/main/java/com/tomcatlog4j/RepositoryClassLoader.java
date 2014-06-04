package com.tomcatlog4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.xml.DOMConfigurator;

@SuppressWarnings("deprecation")
public class RepositoryClassLoader {

	private static RepositoryClassLoader instance = null;
	private static LoggerRepository defaultRepository; 
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
	
	public LoggerRepository getLoggerRepository(ClassLoader classLoader) {
		LoggerRepository loggerRepository = (LoggerRepository) ht.get(classLoader);
		
		synchronized (ht){
			if (loggerRepository == null) {
				loggerRepository = new MyHierarchy(new RootCategory((Level) Level.DEBUG));
				add(classLoader, loggerRepository);
				/*
				//loggerRepository
				URL file;
				try {
					file = new URL(OptionConverter.getSystemProperty("log4j.configuration", null));
					DOMConfigurator.configure(file);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			}
		}
		
		return loggerRepository;
	}

	public synchronized void remove(ClassLoader classLoader){
		ht.remove(classLoader);
	}
	
	public Enumeration<Object> getClassLoaderList(){
		return ht.keys();
	}

	public static LoggerRepository getDefaultRepository() {
		return defaultRepository;
	}

	public static void setDefaultRepository(LoggerRepository defaultRepository) {
		RepositoryClassLoader.defaultRepository = defaultRepository;
	}
}
