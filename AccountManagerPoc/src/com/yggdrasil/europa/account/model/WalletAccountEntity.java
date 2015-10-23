package com.yggdrasil.europa.account.model;

import java.util.Date;
import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WalletAccountEntity {
	
	private static Logger logger = LogManager.getLogger(WalletAccountEntity.class);
	
	// Key Attributes
	private int 	accountId;
	private String 	username; 
	private String 	email;			// mail
	private String 	mobile;
	private String 	password;		// userPassword
	
	// Directory Attributes
	private String 	firstname;		// gn, givenName
	private String 	lastname;		// sn
	private String 	role;			// title
	private String 	description;	// description
	
	
	// Database Attributes
//	private String 	firstname;
//	private String 	lastname;
	private String 	middlename;
	private String 	title;
	private Date 	dateOfBirth;
	private String 	gender;
	private String 	citizenId;
	private String 	citizenIdType;
//	private String 	mobile;
//	private String 	email;
	private String 	alternateEmail;
	private String 	postalAddress;
	private String 	city;
	private String 	zipcode;
	private String 	country;
	private String 	homePhone;
	private String 	company;
	private String 	officeAddress;
	private String 	officePhone;
	private int 		type;
	private String 	status;
	private String 	directoryRole;
	private Date 	modificationDate;
	private Date 	creationDate;
	
	public void log() {
		// write all attributes to log file.
	}
	
	public void load(Hashtable<String, String> updateList) {
		
	}
	
	public String formatMobileNumber(String mobile) {
		// allow number only in mobile attribute
		char[] chMobile = mobile.toCharArray();
		char[] chResult = new char[mobile.length()];
		int pos = 0;
		for(int i = 0; i < chMobile.length; i++) {
			if((chMobile[i] >= '0') && (chMobile[i] <= '9')) {
				chResult[pos++] = chMobile[i];
			}
		}
		
		return (new String(chResult)).trim();
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = formatMobileNumber(mobile);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public String getCitizenIdType() {
		return citizenIdType;
	}

	public void setCitizenIdType(String citizenIdType) {
		this.citizenIdType = citizenIdType;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = formatMobileNumber(homePhone);
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = formatMobileNumber(officePhone);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDirectoryRole() {
		return directoryRole;
	}

	public void setDirectoryRole(String directoryRole) {
		this.directoryRole = directoryRole;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
