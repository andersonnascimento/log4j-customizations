package com.tomcatlog4j;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4JInitializer implements LifecycleListener
{
	private static Object guard = null;

	public void lifecycleEvent(LifecycleEvent event)
    {
        if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType()))
        {
        	//Hlierarchy h = new MyHierarchy(new RootLogger((Level) Level.DEBUG));
        	//RepositorySelector selector = new DefaultRepositorySelector(h);
        	/*
        	LoggerFactory factory = new MyLoggerFactory();
        	Hierarchy hierarchy = new MyHierarchy(LogManager.getRootLogger());
        	Enumeration<Logger> enm = LogManager.getCurrentLoggers();

            List<Logger> list = new ArrayList<Logger>();

            // Add all current loggers to the list
            while (enm.hasMoreElements()){
            	Logger logger = enm.nextElement();
                hierarchy.getLogger(logger.getName(),factory);
            }

        	MyRepositorySelector selector = new MyRepositorySelector();
        	selector.addLoggerRepository(hierarchy, Thread.currentThread().getContextClassLoader());
*/
  
        	MyRepositorySelector selector = new MyRepositorySelector();
        	LogManager.setRepositorySelector(selector,guard);
        	
        	//DOMConfigurator.configure(OptionConverter.getSystemProperty("log4j.configuration", null));
        }
    }
}