package com.yggdrasil.europa;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.WalletAccount;

public class Main {
	private static Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {	
		
		WalletAccount wa = new WalletAccount();
		wa.setUsername("tuser01");
		wa.setPassword("tuser01");
		wa.setFirstname("tuser01");
		wa.setLastname("lastname01");
		wa.setEmail("tuser01@yggdrasil.com");
		wa.setMobileNumber("0811000000");
		wa.setCitizenId("1234567890123");
		wa.setCitizenIdType("TH-ID");
			
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			wa.setDateOfBirth(dateFormat.parse("1970-01-01"));
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		
		wa.setType(1);
		wa.setStatus("INIT");
		if(wa.createWalletAccount()) {
			logger.info("create wallet success!");
		} else {
			logger.info("create wallet failed!");
		}
	}
}
