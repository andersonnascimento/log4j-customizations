package com.custom.tomcatlog4j;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootLogger;

public class Log4JInitializer implements LifecycleListener
{
	private static Object guard = null;
	/*

    private String configurationFile;
 
    public String getConfigurationFile()
    {
        return this.configurationFile;
    }
 
    public void setConfigurationFile(String configurationFile)
    {
        this.configurationFile = configurationFile;
    }
    */
 
    public void lifecycleEvent(LifecycleEvent event)
    {
        if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType()))
        {
            //initializeLog4j();
        	Hierarchy h = new MyHierarchy(new RootLogger((Level) Level.DEBUG));
        	RepositorySelector selector = new DefaultRepositorySelector(h);
        	LogManager.setRepositorySelector(selector, guard);
        }
        //else if (Lifecycle.AFTER_STOP_EVENT.equals(event.getType())){
        //	LogManager.shutdown();
        //}
        
        
    }
 /*
    private void initializeLog4j()
    {
        // configure from file, and let log4j monitor the file for changes
        PropertyConfigurator.configureAndWatch(configurationFile);
 
        // shutdown log4j (and its monitor thread) on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                LogManager.shutdown();
            }
        });
    }
    */
}