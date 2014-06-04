package com.tomcatlog4j;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.xml.DOMConfigurator;
import org.xml.sax.SAXException;

public class MyRepositorySelector implements RepositorySelector {
	private static Object guard = LogManager.getRootLogger();
	private static boolean initialized = false;
	
	public MyRepositorySelector() {

	}

	public static synchronized void init() throws ServletException, SAXException, IOException, ParserConfigurationException {
		 // set the global RepositorySelector
		if (!initialized) {
			RepositoryClassLoader.setDefaultRepository(LogManager.getLoggerRepository());
			RepositorySelector theSelector = new MyRepositorySelector();
			LogManager.setRepositorySelector(theSelector, guard);
			initialized = true;
		}

		Hierarchy hierarchy = new Hierarchy(new RootCategory(Level.DEBUG));
		loadLog4JConfig(hierarchy);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		RepositoryClassLoader.getInstance().add(loader, hierarchy);

	}

	// load log4j.xml from WEB-INF
	private static void loadLog4JConfig(Hierarchy hierarchy) throws SAXException, IOException, ParserConfigurationException{
		//String log4jFile = "/WEB-INF/log4j.xml";
		String log4jXmlUrl = OptionConverter.getSystemProperty("log4j.configuration", null);
		//InputStream log4JConfig = Thread.currentThread().getContextClassLoader().getResourceAsStream(log4jFile);
		//InputStream log4JConfig = new FileInputStream(new File(log4jFile));
		//Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(log4JConfig);
		//DOMConfigurator conf = new DOMConfigurator();
		//conf.doConfigure(doc.getDocumentElement(), hierarchy);
		DOMConfigurator conf = new DOMConfigurator();
		conf.doConfigure(new URL(log4jXmlUrl), hierarchy);
	}

	public LoggerRepository getLoggerRepository() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return RepositoryClassLoader.getInstance().getLoggerRepository(loader);
	}

	public void remove(ClassLoader cl) {
		RepositoryClassLoader.getInstance().remove(
				Thread.currentThread().getContextClassLoader());
	}
}
