package com.yggdrasil.europa;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.WalletAccount;
import com.yggdrasil.europa.account.database.UserDatabase;
import com.yggdrasil.europa.account.database.UserDatabaseFactory;
import com.yggdrasil.europa.account.database.model.UserDatabaseEntity;
import com.yggdrasil.europa.account.model.WalletAccountEntity;

public class Main {
	private static Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {	
		WalletAccount wa = new WalletAccount();
		
		for(int i = 1; i < 10; i++) {

			WalletAccountEntity waEntity = new WalletAccountEntity();
			
			String num = String.format("%02d", i);
			waEntity.setUsername("tuser" + num);
			waEntity.setPassword("tuser" + num);
			waEntity.setFirstname("tuser" + num);
			waEntity.setLastname("lastname" + num);
			waEntity.setEmail("tuser" + num + "@yggdrasil.com");
			waEntity.setMobile("08110000" + num);
			waEntity.setCitizenId("1234567890123");
			waEntity.setCitizenIdType("TH-ID");
				
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				waEntity.setDateOfBirth(dateFormat.parse("1970-01-01"));
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}
			
			waEntity.setType(1);
			waEntity.setStatus("INIT");
			
			if(wa.createWalletAccount(waEntity)) {
				logger.info("create wallet user account success!");
			} else {
				logger.info("create wallet user account failed!");
			}
		}

//		for(int i = 1; i < 10; i++) {
//			WalletAccount wa = new WalletAccount();
//			String num = String.format("%02d", i);
//			String sessionToken = wa.signon("tuser" + num, "tuser" + num);
//			System.out.println(sessionToken);
//		}
	}
}
