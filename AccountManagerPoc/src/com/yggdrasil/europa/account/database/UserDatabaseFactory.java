package com.yggdrasil.europa.account.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.config.Configuration;

public class UserDatabaseFactory {
	private static Logger logger = LogManager.getLogger(UserDatabaseFactory.class);
	
	private static boolean initialized = false;
	
	static{
		if(!initialized) {
			logger.debug("initilizing UserDirectoryFactory.");
			
			try {
				Class.forName(Configuration.dbJdbcDriver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.debug(e,e);
			}
			
			initialized = true;
			logger.debug("UserDatabaseFactory is initialized successfully.");
		}
	}

	public static UserDatabase getUserDatabase() {
		logger.debug("creating a new UserDatabase instant.");
		UserDatabase udb = null;
		try {
			Connection conn = DriverManager.getConnection(Configuration.dbUrl, 
														  Configuration.dbUsername, 
														  Configuration.dbPassword);
			udb = new UserDatabase(conn);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
		logger.debug("a new UserDatabase instant is created.");
		return(udb);
	}

}
