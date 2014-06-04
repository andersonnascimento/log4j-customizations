package com.tomcatlog4j;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

public class Log4JInitializer implements LifecycleListener {

	public void lifecycleEvent(LifecycleEvent event) {
		if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType())) {
			try {
				MyRepositorySelector.init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}