package com.webtests.log4j.ex1;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.RootLogger;

import com.custom.tomcatlog4j.MyHierarchy;
import com.webtests.log4j.lib.Something;

public class Log4jServlet extends HttpServlet{
	
	private static Logger LOGGER = Logger.getLogger(Log4jServlet.class.getName());

	private static final long serialVersionUID = 1L;

		public void init(){
			LOGGER.info("com.webtests.log4j.ex1.doInit() - info");
		       //MyHierarchy hierarchy = new MyHierarchy(new RootLogger((Level) Level.DEBUG));
		       //LogManager.setRepositorySelector(new DefaultRepositorySelector(hierarchy), "MyLogger");
		       
		}
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
			//String s = PropertyConfigurator.LOGGER_FACTORY_KEY;
			//Logger LOGGER = LogManager.getLogger(Log4jServlet.class.getName());
			//Logger LOGGER = Logger.getLogger(Log4jServlet.class.getName());
			
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			
			LOGGER.error("com.webtests.log4j.ex1.doGet() - error");
			LOGGER.info("com.webtests.log4j.ex1.doGet() - info");
			LOGGER.warn("com.webtests.log4j.ex1.doGet() - warn");
			
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("com.webtests.log4j.ex1.doGet() - debug");	
			}
			
			if (LOGGER.isTraceEnabled()){
				LOGGER.trace("com.webtests.log4j.ex1.doGet() - trace");
			}
			
			Something something = new Something();
			something.doSomething();
						
			out.println("<h1>Log4jServlet - 1</h1>");
			out.println("</body>");
			out.println("</html>");	
		}
}
