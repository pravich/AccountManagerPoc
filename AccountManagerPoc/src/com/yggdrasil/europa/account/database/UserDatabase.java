package com.yggdrasil.europa.account.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
		
		PreparedStatement pstmt = null;
		
		String command = "INSERT INTO account ("
				+ "  first_name"
				+ ", last_name"
				+ ", middle_name"
				+ ", title"
				+ ", date_of_birth"
				+ ", gender"
				+ ", citizen_id"
				+ ", citizen_id_type"
				+ ", mobile_number"
				+ ", email"
				+ ", alt_email"
				+ ", postal_address"
				+ ", city"
				+ ", zipcode"
				+ ", country"
				+ ", type"
				+ ", status"
				+ ", dir_role"
				+ ", mtime"
				+ ", ctime"
				+ ") "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "         ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
		
	
		
		try {
			pstmt = conn.prepareStatement(command);
			pstmt.setString( 1, user.firstname);
			pstmt.setString( 2, user.lastname);
			pstmt.setString( 3, user.middlename);
			pstmt.setString( 4, user.title);
			pstmt.setDate  ( 5, new java.sql.Date(user.dateOfBirth.getTime()));
			pstmt.setString( 6, user.gender);
			pstmt.setString( 7, user.citizenId);
			pstmt.setString( 8, user.citizenIdType);
			pstmt.setString( 9, user.mobile);
			pstmt.setString(10, user.email);
			pstmt.setString(11, user.alternateEmail);
			pstmt.setString(12, user.postalAddress);
			pstmt.setString(13, user.city);
			pstmt.setString(14, user.zipcode);
			pstmt.setString(15, user.country);
			pstmt.setInt   (16, user.type);
			pstmt.setString(17, user.status);
			pstmt.setString(18, user.directoryRole);
			
//			Calendar calendar = Calendar.getInstance();
//			java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
//			
//			pstmt.setDate  ( 19, date);
//			pstmt.setDate  ( 20, date);
			
			pstmt.executeUpdate();
			
		} catch(SQLException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return false;
		}
		
		return true;
	}

}
