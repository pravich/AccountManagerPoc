package com.yggdrasil.europa.account.cache;

import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WalletAccountCache {
	private static Logger logger = LogManager.getLogger(WalletAccountCache.class);
	
	private static Hashtable<Integer, String> forwardCache = new Hashtable<Integer, String> (10000);
	private static Hashtable<String, WalletAccountCacheItem> reverseCache = new Hashtable<String, WalletAccountCacheItem>(10000);
	
	public static String getSessionToken(String accountId) {
		String sessionToken = forwardCache.get(accountId);
		if(sessionToken == null) {
			logger.debug("not found session token of " + accountId + ".");
		} else {
			logger.debug("accountId=" + accountId + " gets session token.");
		}
		return(sessionToken);
	}
	
	public static WalletAccountCacheItem getUserItem(String sessionToken) {
		WalletAccountCacheItem cacheItem = reverseCache.get(sessionToken);
		if(cacheItem == null) {
			logger.debug("not found UserAccountCacheItem of " + sessionToken + ".");
		} else {
			logger.debug("session token read [" + sessionToken + "] ");
		}
		return(cacheItem);
	}
	
	public static void setSessionToken(String sessionToken, WalletAccountCacheItem cacheItem) {
		logger.debug("accountId=" + cacheItem.accountId + " sets session token [" + sessionToken + "].");
		
		if(forwardCache.containsKey(cacheItem.accountId)) {
			// removes previous session if exists.
			reverseCache.remove(forwardCache.get(cacheItem.accountId));
		}
		
		forwardCache.put(cacheItem.accountId, sessionToken);
		reverseCache.put(sessionToken, cacheItem);
	}

}
