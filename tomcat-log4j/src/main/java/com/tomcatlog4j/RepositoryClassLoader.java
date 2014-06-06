package com.tomcatlog4j;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
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
				loggerRepository = new Hierarchy(new RootCategory((Level) Level.DEBUG));
				
				try {
					String log4jXmlUrl = OptionConverter.getSystemProperty("log4j.configuration", null);
					DOMConfigurator conf = new DOMConfigurator();
					conf.doConfigure(new URL(log4jXmlUrl), loggerRepository);
				} catch (Exception e) {
					e.printStackTrace();
				}

				add(classLoader, loggerRepository);
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
