package utils;

import org.apache.log4j.Logger;

public class Log {
	
	private static Logger log = Logger.getLogger(Log.class.getName());
	
	public static void startTestCase(String testCaseName) {
		log.info("Started test case");
	}
	
	public static void endTestCase(String testCaseName) {
		log.info("Ended test case");
	}
	
	public static void info (String message) {
		log.info(message);
	}
	
	public static Logger getLog() {
		return log;
	}
	
}
