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
//		for(int i = 1; i < 10; i++) {
//			WalletAccount wa = new WalletAccount();
//			String num = String.format("%02d", i);
//			wa.setUsername("tuser" + num);
//			wa.setPassword("tuser" + num);
//			wa.setFirstname("tuser" + num);
//			wa.setLastname("lastname" + num);
//			wa.setEmail("tuser" + num + "@yggdrasil.com");
//			wa.setMobile("08110000" + num);
//			wa.setCitizenId("1234567890123");
//			wa.setCitizenIdType("TH-ID");
//				
//			try {
//				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//				wa.setDateOfBirth(dateFormat.parse("1970-01-01"));
//			} catch (ParseException e) {
//				logger.error(e.getMessage());
//			}
//			
//			wa.setType(1);
//			wa.setStatus("INIT");
//			
//			if(wa.createWalletAccount()) {
//				logger.info("create wallet user account success!");
//			} else {
//				logger.info("create wallet user account failed!");
//			}
//		}

		for(int i = 1; i < 10; i++) {
			WalletAccount wa = new WalletAccount();
			String num = String.format("%02d", i);
			String sessionToken = wa.signon("tuser" + num, "tuser" + num);
			System.out.println(sessionToken);
		}
	}
}
