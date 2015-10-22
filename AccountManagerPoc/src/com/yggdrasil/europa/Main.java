package com.yggdrasil.europa;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.WalletAccount;
import com.yggdrasil.europa.account.database.UserDatabase;
import com.yggdrasil.europa.account.database.UserDatabaseFactory;
import com.yggdrasil.europa.account.database.model.UserDatabaseEntity;

public class Main {
	private static Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {	
		
		WalletAccount wa = new WalletAccount();
//		String num = "07";
//		wa.setUsername("tuser" + num);
//		wa.setPassword("tuser" + num);
//		wa.setFirstname("tuser" + num);
//		wa.setLastname("lastname" + num);
//		wa.setEmail("tuser" + num + "@yggdrasil.com");
//		wa.setMobile("08110000" + num);
//		wa.setCitizenId("1234567890123");
//		wa.setCitizenIdType("TH-ID");
//			
//		try {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			wa.setDateOfBirth(dateFormat.parse("1970-01-01"));
//		} catch (ParseException e) {
//			logger.error(e.getMessage());
//		}
//		
//		wa.setType(1);
//		wa.setStatus("INIT");
//		
//		if(wa.createWalletAccount()) {
//			logger.info("create wallet user account success!");
//		} else {
//			logger.info("create wallet user account failed!");
//		}

//		WalletAccount wa = new WalletAccount();
		String sessionToken = wa.signon("tuser07", "tuser07");
		System.out.println(sessionToken);
		
		sessionToken = wa.signon("tuser06", "tuser06");
		System.out.println(sessionToken);
		
		sessionToken = wa.signon("tuser05@yggdrasil.com", "tuser05");
		System.out.println(sessionToken);
		
		sessionToken = wa.signon("0811000004", "tuser04");
		System.out.println(sessionToken);
		

	}
}
