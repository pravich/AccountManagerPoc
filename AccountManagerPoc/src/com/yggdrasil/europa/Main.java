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
		wa.setUsername("tuser06");
		wa.setPassword("tuser06");
		wa.setFirstname("tuser06");
		wa.setLastname("lastname06");
		wa.setEmail("tuser06@yggdrasil.com");
		wa.setMobile("0811000006");
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
