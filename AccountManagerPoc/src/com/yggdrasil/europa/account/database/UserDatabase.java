package com.yggdrasil.europa.account.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.database.model.UserDatabaseEntity;

public class UserDatabase {
	private static Logger logger = LogManager.getLogger(UserDatabase.class);
	
	private Connection conn; 

	@Override
	protected void finalize() throws Throwable {
		try {
			if(conn != null) {
				conn.close();
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
	
	public UserDatabase(Connection conn) {
		this.conn = conn;
	}
	
	public boolean CreateUser(UserDatabaseEntity user) {
		
		String command = "INSERT INTO account ("
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
		
		try {
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString( 1 , user.username);
			pstmt.setString( 2 , user.firstname);
			pstmt.setString( 3 , user.lastname);
			pstmt.setString( 4 , user.middlename);
			pstmt.setString( 5 , user.title);
			pstmt.setDate  ( 6 , new java.sql.Date(user.dateOfBirth.getTime()));
			pstmt.setString( 7 , user.gender);
			pstmt.setString( 8 , user.citizenId);
			pstmt.setString( 9 , user.citizenIdType);
			pstmt.setString(10, user.mobile);
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
			
			int accountId = -1;
			//Statement stmt = conn.createStatement();
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				accountId = rs.getInt(1);
			} else {
				conn.rollback();
				return false;
			}
			
			conn.commit();
		} catch(SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return false;
		}
		
		return true;
	}

}
