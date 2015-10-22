package com.yggdrasil.europa.account.directory;

import java.util.Hashtable;

import javax.naming.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.config.Configuration;
import com.yggdrasil.europa.account.directory.exceptions.DirectoryConnectionException;

public class UserDirectoryFactory {
	
	private static Logger logger = LogManager.getLogger(UserDirectoryFactory.class);
	
	private static Hashtable<String, String> env;
	
	private static boolean initialized = false;
	
	static{
		if(!initialized) {
			logger.debug("initilizing UserDirectoryFactory.");
			env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, Configuration.dirProviderUrl);
			env.put(Context.SECURITY_AUTHENTICATION, Configuration.dirSecurityAuthentication);
			env.put(Context.SECURITY_PRINCIPAL, Configuration.dirSecurityPrincipal);
			env.put(Context.SECURITY_CREDENTIALS, Configuration.dirSecurityCredential);
			
			initialized = true;
			
			logger.debug("UserDirectoryFactory is initialized successfully.");
		}
	}

	public static UserDirectory getUserDirectory() throws DirectoryConnectionException {
		logger.debug("creating a new UserDirectory instant.");
		UserDirectory ud = new UserDirectory(env);
		logger.debug("a new UserDirectory instant is created.");
		return(ud);
	}
}
