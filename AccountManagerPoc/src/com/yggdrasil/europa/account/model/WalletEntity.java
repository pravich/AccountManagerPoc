package com.yggdrasil.europa.account.model;

import java.math.BigDecimal;
import java.sql.Date;

public class WalletEntity {
	public int walletId; 
	public int accountId;
	public BigDecimal availableBalance;
	public BigDecimal currentBalance;
	public BigDecimal pendingDeposit;
	
	public int currencyId;
	public String status;
	public Date creationDatetime;
	public Date modificationDatetime;
}
