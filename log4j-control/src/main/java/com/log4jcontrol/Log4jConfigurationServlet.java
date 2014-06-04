package com.log4jcontrol;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.loader.StandardClassLoader;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.tomcatlog4j.RepositoryClassLoader;

/**
*
* openutils for Log4j (http://www.openmindlab.com/lab/products/openutilslog4j.html)
* Copyright(C) ${project.inceptionYear}-2012, Openmind S.r.l. http://www.openmindonline.it
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
public class Log4jConfigurationServlet extends HttpServlet {

	/**
	 * Stable <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 64182;

	/**
	 * The response content type: text/html
	 */
	private static final String CONTENT_TYPE = "text/html";

	/**
	 * Should not print html head and body?
	 */
	private static final String CONFIG_FRAGMENT = "fragment";

	/**
	 * The root appender.
	 */
	private static final String ROOT = "Root";

	/**
	 * The name of the class / package.
	 */
	private static final String PARAM_CLASS = "class";

	/**
	 * The logging level.
	 */
	private static final String PARAM_LEVEL = "level";

	/**
	 * Sort by level?
	 */
	private static final String PARAM_SORTBYLEVEL = "sortbylevel";

	/**
	 * All the log levels.
	 */
	private static final String[] LEVELS = new String[] { 
			Level.OFF.toString(),
			Level.FATAL.toString(), 
			Level.ERROR.toString(),
			Level.WARN.toString(), 
			Level.INFO.toString(),
			Level.DEBUG.toString(),
			Level.TRACE.toString(),
			Level.ALL.toString() };

	/**
	 * Don't include html head.
	 */
	private boolean isFragment;

	/**
	 * Filter by package parameter name
	 */
	private static final String FILTER_CRITERIA = "filterbypackage";
	/**
	 * New package name
	 */
	private static final String ADD_PACKAGE_NAME = "newpackagename";
	/**
	 * Delimiter to add as many package you want.
	 */
	private static final String PACKAGE_DELIM = ",";

	private static final String SERVER_CLASSLOADER = "server";
	private static final String PARAM_CLASSLOADER = "context";
	private Hashtable<String, ClassLoader> classLoaders;
	
		
	/**
	 * Print the status of all current <code>Logger</code> s and an option to
	 * change their respective logging levels.
	 * 
	 * @param request
	 *            a <code>HttpServletRequest</code> value
	 * @param response
	 *            a <code>HttpServletResponse</code> value
	 * @exception ServletException
	 *                if an error occurs
	 * @exception IOException
	 *                if an error occurs
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String className = request.getParameter(PARAM_CLASS);
		String level = request.getParameter(PARAM_LEVEL);

		String filter = request.getParameter(FILTER_CRITERIA);
		String newPackageName = request.getParameter(ADD_PACKAGE_NAME);

		String currentClassLoader = request.getParameter(PARAM_CLASSLOADER);
		

		if (className != null && level != null) {
			setClass(className, level);
		}

		if (newPackageName != null) {
			addPackageToLoader(newPackageName);
		}

		String sortByLevelParam = request.getParameter(PARAM_SORTBYLEVEL);
		boolean sortByLevel = ("true".equalsIgnoreCase(sortByLevelParam) || "yes".equalsIgnoreCase(sortByLevelParam));

		PrintWriter out = response.getWriter();
		
		displayClassLoaders(out);

		
		
		
		List<Logger> loggers = getSortedLoggers(currentClassLoader, sortByLevel);
		int loggerNum = 0;

		
		
		displayFilter(out, filter, newPackageName);
		if (!isFragment) {
			response.setContentType(CONTENT_TYPE);

			// print title and header
			printHeader(out, request);
		}


		out.print("<br>");
		out.print("<br>");
		
		out.println("<a href=\"" + request.getRequestURI() + "\">Refresh</a>");
		
		
		// print scripts
		out.println("<a href=\"" + request.getRequestURI() + "\">Refresh</a>");

		out.println("<table class=\"log4jtable\">");
		out.println("<thead><tr>");

		out.println("<th title=\"Logger name\">");
		out.println("<a href=\"?" + PARAM_SORTBYLEVEL + "=false\">Class</a>");
		out.println("</th>");

		out.println("<th title=\"Is logging level inherited from parent?\" style=\"text-align:right\" >*</th>");
		out.println("<th title=\"Logger level\">");
		out.println("<a href=\"?" + PARAM_SORTBYLEVEL + "=true\">Level</a>");
		out.println("</th>");

		out.println("</tr></thead>");
		out.println("<tbody>");

		// print the root Logger
		displayLogger(out, Logger.getRootLogger(), loggerNum++, filter, request);

		// print the rest of the loggers
		Iterator<Logger> iterator = loggers.iterator();

		while (iterator.hasNext()) {
			displayLogger(out, iterator.next(), loggerNum++, filter, request);
		}

		out.println("</tbody>");
		out.println("</table>");
		out.println("<a href=\"\">Refresh</a>");

		if (!isFragment) {
			out.println("</body></html>");
			out.flush();
			out.close();
		}
	}
	
	
	public void displayClassLoaders(PrintWriter out){
		Enumeration<Object> keys = RepositoryClassLoader.getInstance().getClassLoaderList();
		 
		classLoaders = new Hashtable<String, ClassLoader>();
		int i = 1;
		while (keys.hasMoreElements()) {
			ClassLoader cl = (ClassLoader) keys.nextElement();
			//WebappClassLoader context: /examples delegate: false repositories: /WEB-INF/classes/ ----------> Parent Classloader: org.apache.catalina.loader.StandardClassLoader@3ff72465 
			
			if (cl instanceof StandardClassLoader){
				out.println("<a href=\"?" + PARAM_CLASSLOADER + "="+SERVER_CLASSLOADER+" \">Server classloader</a>");
				classLoaders.put(SERVER_CLASSLOADER, cl);
			}
			else {
				out.println("<a href=\"?" + PARAM_CLASSLOADER + "="+i+" \"> Context: "+ ((WebappClassLoader) cl).getContextName() +"</a>");
				classLoaders.put(""+i, cl);	
			}
			
			out.print(cl);
			out.print("<br>");
			i++;
		}		
	}

	/**
	 * Change a <code>Logger</code>'s level, then call <code>doGet</code> to
	 * refresh the page.
	 * 
	 * @param request
	 *            a <code>HttpServletRequest</code> value
	 * @param response
	 *            a <code>HttpServletResponse</code> value
	 * @exception ServletException
	 *                if an error occurs
	 * @exception IOException
	 *                if an error occurs
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Print a Logger and its current level.
	 * 
	 * @param out
	 *            the output writer.
	 * @param logger
	 *            the logger to output.
	 * @param row
	 *            the row number in the table this logger will appear in.
	 * @param request
	 *            the servlet request.
	 */
	private void displayLogger(PrintWriter out, Logger logger, int row, String filter, HttpServletRequest request) {
		String color = null;
		String loggerName = (logger.getName().equals("") ? ROOT : logger.getName());

		if (null != filter && !"".equalsIgnoreCase(filter) && !loggerName.startsWith(filter)) {
			return;
		}
		else if (null == filter) {
			filter = "";
		}
		
		color = ((row % 2) == 1) ? "even" : "odd";

		out.println("<tr class=\"" + color + "\">");

		// class logger
	       out.println("<td>");
	       out.println(logger.getClass().getName());
	       out.println("</td>");
		
		
		// logger
		out.println("<td style=\"min-width:300px\">");
		out.println(loggerName);
		out.println("</td>");

		// level inherited?
		out.println("<td style=\"text-align:right\">");
		if ((logger.getLevel() == null)) {
			out.println("*");
		}
		out.println("</td>");

		// level and selection
		out.println("<td>");
		out.println("<form action=\"\" method=\"post\">");
		printLevelSelector(out, logger.getEffectiveLevel().toString());
		out.println("<input type=\"hidden\" name=\"" + PARAM_CLASS + "\" value=\"" + loggerName + "\">");

	    out.println("<input type=\"hidden\" name=\"filterbypackage\" value=\"" + filter + "\"/>");

		out.print("<input type=\"submit\" name=\"Set\" value=\"Set \">");
		out.println("</form>");
		out.println("</td>");

		out.println("</tr>");
	}

	/**
	 * Set a logger's level.
	 * 
	 * @param className
	 *            class name of the logger to set.
	 * @param level
	 *            the level to set the logger to.
	 * @return String return message for display.
	 */
	private synchronized String setClass(String className, String level) {
		Logger logger = null;

		try {
			logger = (ROOT.equalsIgnoreCase(className) ? Logger.getRootLogger()
					: Logger.getLogger(className));
			logger.setLevel(Level.toLevel(level));
		} catch (Throwable e) {
			System // permetti system.out
			.out.println("ERROR Setting LOG4J Logger:" + e);
		}

		return "Message Set For "
				+ (logger.getName().equals("") ? ROOT : logger.getName());
	}

	/**
	 * Get a sorted list of all current loggers.
	 * 
	 * @param sortByLevel
	 *            if <code>true</code> sort loggers by level instead of name.
	 * @return classLoaderList the list of sorted loggers.
	 */
	@SuppressWarnings("unchecked")
	private List<Logger> getSortedLoggers(String currentClassLoader, boolean sortByLevel) {
		//Enumeration<Logger> enm = LogManager.getCurrentLoggers();
		ClassLoader cl = null;
		if ((currentClassLoader != null) && (!currentClassLoader.isEmpty()) && (!currentClassLoader.equals(""))) {
			cl = classLoaders.get(currentClassLoader); 
		}
		else {
			cl = Thread.currentThread().getContextClassLoader();
		}

		Enumeration<Logger> enm = RepositoryClassLoader.getInstance().getLoggerRepository(cl).getCurrentLoggers();

		List<Logger> list = new ArrayList<Logger>();

		// Add all current loggers to the list
		while (enm.hasMoreElements()) {
			list.add(enm.nextElement());
		}

		// sort the loggers
		Collections.sort(list, new LoggerComparator(sortByLevel));

		return list;
	}

	/**
	 * Prints the page header.
	 * 
	 * @param out
	 *            The output writer
	 * @param request
	 *            The request
	 */
	private void printHeader(PrintWriter out, HttpServletRequest request) {
		out.println("<html><head><title>Log4J Control Console</title>");

		out.println("<style type=\"text/css\">");
		out.println("body{ background-color:#fff; }");
		out.println("body, td, th, select, input{ font-family:Verdana, Geneva, Arial, sans-serif; font-size: 8pt;}");
		out.println("select, input{ border: 1px solid #ccc;}");
		out.println("table.log4jtable, table.log4jtable td { border-collapse:collapse; border: 1px solid #ccc; ");
		out.println("white-space: nowrap; text-align: left; }");
		out.println("form { margin:0; padding:0; }");
		out.println("table.log4jtable thead tr th{ background-color: #5991A6; padding: 2px; }");
		out.println("table.log4jtable tr.even { background-color: #eee; }");
		out.println("table.log4jtable tr.odd { background-color: #fff; }");
		out.println("</style>");

		out.println("</head>");
		out.println("<body>");
		out.println("<h3>Log4J Control Console</h3>");
	}

	/**
	 * Prints the Level select HTML.
	 * 
	 * @param out
	 *            The output writer
	 * @param currentLevel
	 *            the current level for the log (the selected option).
	 */
	private void printLevelSelector(PrintWriter out, String currentLevel) {
		out.println("<select id=\"" + PARAM_LEVEL + "\" name=\"" + PARAM_LEVEL + "\">");

		for (int j = 0; j < LEVELS.length; j++) {
			out.print("<option");
			if (LEVELS[j].equals(currentLevel)) {
				out.print(" selected=\"selected\"");
			}
			out.print(">");
			out.print(LEVELS[j]);
			out.println("</option>");
		}
		out.println("</select>");
	}

	/**
	 * Compare the names of two <code>Logger</code>s. Used for sorting.
	 */
	private class LoggerComparator implements Comparator<Logger> {

		/**
		 * Sort by level? (default is sort by class name)
		 */
		private boolean sortByLevel;

		/**
		 * instantiate a new LoggerComparator
		 * 
		 * @param sortByLevel
		 *            if <code>true</code> sort loggers by level instead of
		 *            name.
		 */
		public LoggerComparator(boolean sortByLevel) {
			this.sortByLevel = sortByLevel;
		}

		/**
		 * Compare the names of two <code>Logger</code>s.
		 * 
		 * @param object1
		 *            an <code>Object</code> value
		 * @param object2
		 *            an <code>Object</code> value
		 * @return an <code>int</code> value
		 */
		public int compare(Logger logger1, Logger logger2) {
			if (!sortByLevel) {
				return logger1.getName().compareTo(logger2.getName());
			}
			return logger1.getEffectiveLevel().toInt()
					- logger2.getEffectiveLevel().toInt();
		}

		/**
		 * Return <code>true</code> if the <code>Object</code> is a
		 * <code>LoggerComparator</code> instance.
		 * 
		 * @param object
		 *            an <code>Object</code> value
		 * @return a <code>boolean</code> value
		 */
		@Override
		public boolean equals(Object object) {
			if (!(object instanceof LoggerComparator)) {
				return false;
			}
			return this.sortByLevel == ((LoggerComparator) object).sortByLevel;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		String fragmentParam = config.getInitParameter(CONFIG_FRAGMENT);
		isFragment = ("true".equalsIgnoreCase(fragmentParam) || "yes".equalsIgnoreCase(fragmentParam));
		super.init(config);
	}

	/**
	 * Adde's new package/category to the log4j loader.
	 * 
	 * @param className
	 *            class name of the logger to set.
	 * @param level
	 *            the level to set the logger to.
	 * @return String return message for display.
	 */
	private synchronized void addPackageToLoader(String packageName) {
		StringTokenizer st = new StringTokenizer(packageName, PACKAGE_DELIM);
		while (st.hasMoreTokens()) {
			Logger.getLogger(st.nextToken()).setAdditivity(false);
		}
	}

	private void displayFilter(PrintWriter out, String filter, String newPackageName) {
		String color = "even";
		out.println("<table>");
		out.println("<tr align=\"right\" class=\"" + color + "\">");
		// filter
		out.println("<form action=\"\" method=\"post\">");
		out.println("<td>");
		if (null == filter) {
			filter = "";
		}
		out.print("<input name=\"filterbypackage\" value=\"" + filter + "\"/>");
		out.println("<td>");
		out.print("<input type=\"submit\" name=\"Set Filter\" value=\"Set Filter\">");
		out.println("</td>");

		out.println("<td>");
		out.print("<input name=\"newpackagename\" value=\"\"/>");
		out.println("</td>");
		out.println("<td>");
		out.print("<input type=\"submit\" name=\"Add Package\" value=\"Add Package\">");
		out.println("</td>");
		out.println("</form>");

		out.println("</tr>");
	}

}