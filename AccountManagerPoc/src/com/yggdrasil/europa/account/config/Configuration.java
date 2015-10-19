package com.yggdrasil.europa.account.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {
	private static Logger logger = LogManager.getLogger(Configuration.class);
	
	// probably in static block cannot refer to any relative path in WebContent.
	// Then have to use absolute "file system" path. 
	private static String configFilename = "/home/wiz/logs/config.properties";
	public static final String dirProviderUrl;
	public static final String dirSecurityAuthentication;
	public static final String dirSecurityPrincipal;
	public static final String dirSecurityCredential;
	public static final String dbJdbcDriver;
	public static final String dbUrl;
	public static final String dbUsername;
	public static final String dbPassword;
	
	public static final Hashtable<String, String> properties;
	
	static {
		Properties prop = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(configFilename);
			prop.load(inputStream);
		} catch(FileNotFoundException excp) {
			logger.error("Cannot find " + configFilename);
		} catch(IOException excp) {
			logger.error("IOException: " + excp.getMessage());
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch(IOException excp) {
					excp.printStackTrace();
				}
			}
		}
			
		dirProviderUrl = prop.getProperty("server.ldap.providerUrl");
		dirSecurityAuthentication = prop.getProperty("server.ldap.securityAuthentication");
		dirSecurityPrincipal = prop.getProperty("server.ldap.securityPrincipal");
		dirSecurityCredential = prop.getProperty("server.ldap.securityCredential");
		dbJdbcDriver = prop.getProperty("server.database.jdbcDriver");
		dbUrl = prop.getProperty("server.database.url");
		dbUsername = prop.getProperty("server.database.username");
		dbPassword = prop.getProperty("server.database.password");
		
		properties = new Hashtable<String, String>();
		Enumeration<?> keys = prop.propertyNames();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			properties.put(key, prop.getProperty(key));
		}
	}
}
