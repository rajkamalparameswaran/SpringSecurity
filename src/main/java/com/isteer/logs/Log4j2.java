package com.isteer.logs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2 {
	private Log4j2()
	{
		
	}
	private static final Logger LOGGER=LogManager.getLogger("com.isteer");
	private static final Logger AUDITLOG=LogManager.getLogger("AuditLogs");

	public static Logger getLogger() {
		return LOGGER;
	}

	public static Logger getAuditlog() {
		return AUDITLOG;
	}
	
	
	
	

}
