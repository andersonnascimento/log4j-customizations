package com.custom.tomcatlog4j;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.RepositorySelector;

public class Log4JInitializer implements LifecycleListener
{
	private static Object guard = null;

	public void lifecycleEvent(LifecycleEvent event)
    {
        if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType()))
        {
        	//Hierarchy h = new MyHierarchy(new RootLogger((Level) Level.DEBUG));
        	//RepositorySelector selector = new DefaultRepositorySelector(h);
        	
        	RepositorySelector selector = new MyRepositorySelector();
        	LogManager.setRepositorySelector(selector, guard);
        }
      
    }
}