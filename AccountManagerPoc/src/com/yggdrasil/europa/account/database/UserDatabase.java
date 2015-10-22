package com.yggdrasil.europa.account.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.config.Configuration;
import com.yggdrasil.europa.account.database.exceptions.DatabaseConnectionException;
import com.yggdrasil.europa.account.database.model.UserDatabaseEntity;

public class UserDatabase {
	private static Logger logger = LogManager.getLogger(UserDatabase.class);
	
	private Connection conn;

	@Override
	protected void finalize() throws Throwable {
		try {
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch(SQLException e) {
			logger.error(e.toString());
			logger.debug(e, e);
		} finally {
			// finalize method in supper class is not automatically called by Java, like constructor.
			// need manually call it. 
			// http://javarevisited.blogspot.com/2012/03/finalize-method-in-java-tutorial.html
			super.finalize();
		}
	}
	
	public UserDatabase() throws DatabaseConnectionException {
		try {
			conn = DriverManager.getConnection(Configuration.dbUrl, 
											   Configuration.dbUsername, 
											   Configuration.dbPassword);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			throw new DatabaseConnectionException("Database connection initialization failed", e.getCause());
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
	}
	
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException e){
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
	}
	
	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e){
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
	}
	
	public int createUser(UserDatabaseEntity user) {
		// If user created successfully, return accountId. 
		
		String cmdInsert = "INSERT INTO account ("
				+ "  username"			//  1
				+ ", first_name"		//  2
				+ ", last_name"			//  3
				+ ", middle_name"		//  4
				+ ", title"				//  5
				+ ", date_of_birth"		//  6
				+ ", gender"			//  7
				+ ", citizen_id"		//  8
				+ ", citizen_id_type"	//  9
				+ ", mobile_number"		// 10
				+ ", email"				// 11
				+ ", alt_email"			// 12
				+ ", postal_address"	// 13
				+ ", city"				// 14
				+ ", zipcode"			// 15
				+ ", country"			// 16
				+ ", home_phone"		// 17
				+ ", company"			// 18
				+ ", office_address"	// 19
				+ ", office_phone"		// 20
				+ ", type"				// 21
				+ ", status"			// 22
				+ ", dir_role"			// 23
				+ ", mtime"				// 24
				+ ", ctime"				// 25
				+ ") "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " 		//  1 - 10
				+ "         ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "			// 11 - 20
				+ "         ?, ?, ?, NOW(), NOW())";
		
		
		PreparedStatement pstmt = null;
		int accountId = -1;
		
		try {
			user.mobile = formatMobileNumber(user.mobile);
			
			pstmt = conn.prepareStatement(cmdInsert, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString( 1 , user.username);
			pstmt.setString( 2 , user.firstname);
			pstmt.setString( 3 , user.lastname);
			pstmt.setString( 4 , user.middlename);
			pstmt.setString( 5 , user.title);
			pstmt.setDate  ( 6 , new java.sql.Date(user.dateOfBirth.getTime()));
			pstmt.setString( 7 , user.gender);
			pstmt.setString( 8 , user.citizenId);
			pstmt.setString( 9 , user.citizenIdType);
			pstmt.setString(10 , user.mobile);
			pstmt.setString(11, user.email);
			pstmt.setString(12, user.alternateEmail);
			pstmt.setString(13, user.postalAddress);
			pstmt.setString(14, user.city);
			pstmt.setString(15, user.zipcode);
			pstmt.setString(16, user.country);
			pstmt.setString(17, user.homePhone);
			pstmt.setString(18, user.company);
			pstmt.setString(19, user.officeAddress);
			pstmt.setString(20, user.officePhone);
			pstmt.setInt   (21, user.type);
			pstmt.setString(22, user.status);
			pstmt.setString(23, user.directoryRole);
			
//			Calendar calendar = Calendar.getInstance();
//			java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
//			
//			pstmt.setDate  ( 19, date);
//			pstmt.setDate  ( 20, date);
			
			pstmt.executeUpdate();
			
			// String query = "SELECT LAST_INSERT_ID()";
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				accountId = rs.getInt(1);
			} else {
				conn.rollback();
			}
			
			// not commit until user creation in directory is done. 
			// conn.commit();
			
		} catch(SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
		
		return accountId;
	}
	
	public boolean deleteUser(int accountId) {
		String cmdDelete = "DELETE FROM account WHERE aid = " +  accountId + ";";
		Statement stmt;
		
		try {
			stmt = conn.createStatement();
			stmt.execute(cmdDelete);
			
			conn.commit();
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return false;
		}
		return true;
	}
	
	public UserDatabaseEntity getUserAccountByAccountId(int accountId) {
		UserDatabaseEntity udbEntity = new UserDatabaseEntity();
		udbEntity.accountId = -1;
		
		String query = "SELECT * FROM account WHERE aid=" + accountId + ";";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				udbEntity.accountId = rs.getInt("aid");
				udbEntity.username = rs.getString("username");
				udbEntity.firstname = rs.getString("first_name");
				udbEntity.lastname = rs.getString("last_name");
				udbEntity.middlename = rs.getString("middle_name");
				udbEntity.title = rs.getString("title");
				udbEntity.dateOfBirth = rs.getDate("date_of_birth");
				udbEntity.gender = rs.getString("gender");
				udbEntity.citizenId = rs.getString("citizen_id");
				udbEntity.mobile = rs.getString("mobile_number");
				udbEntity.email = rs.getString("email");
				udbEntity.alternateEmail = rs.getString("alt_email");
				udbEntity.postalAddress = rs.getString("postal_address");
				udbEntity.city = rs.getString("city");
				udbEntity.zipcode = rs.getString("zipcode");
				udbEntity.country = rs.getString("country");
				udbEntity.homePhone = rs.getString("home_phone");
				udbEntity.company = rs.getString("company");
				udbEntity.officeAddress = rs.getString("office_address");
				udbEntity.officePhone = rs.getString("office_phone");
				udbEntity.type = rs.getInt("type");
				udbEntity.status = rs.getString("status");
				udbEntity.directoryRole = rs.getString("dir_role");
				udbEntity.creationDate = rs.getDate("ctime");
				udbEntity.modificationDate = rs.getDate("mtime");
			}		
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
		
		return udbEntity;
	}
	
	public int getAccountIdByEmail(String email) {
		int accountId = -1;
		String query = "SELECT aid FROM account WHERE email='" + email + "';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				accountId = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
		return accountId;
	}
	
	public int getAccountIdByMobile(String mobile) {
		int accountId = -1;
		String query = "SELECT aid FROM account WHERE mobile_number='" + mobile + "';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				accountId = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
		return accountId;
	}
	
	public int getAccountIdByUsername(String username) {
		int accountId = -1;
		String query = "SELECT aid FROM account WHERE username='" + username + "';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				accountId = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
		}
		return accountId;		
	}
	
	public boolean updateUserAttribute(int accountId, String attributeName, String attributeValue) {
		
		return false;
	}
	
	public boolean enableUser(int accountId) {
		return false;
	}
	
	public boolean disableUser(int accountId) {
		return false;
	}
	
	public boolean verifyEmailFormat(String email) {
		int atSignPosition = email.indexOf('@');
		
		// if '@' at 1st or last position or no '@', return false.
		if (atSignPosition <= 0 || atSignPosition == (email.length() - 1)) {
			return false;
		}
		
		// if there are '@' more than one, return false.
		if(email.substring(atSignPosition).indexOf('@') >= 0) {
			return false;
		}
		
		return true;
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

}
