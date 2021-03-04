package com.icepaq.ServerManagementAPIEndpoint;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAccess {
	
	Codes codes = new Codes();
	
	public String updateAPIKey(String new_key) throws SQLException, NoSuchAlgorithmException {
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		
		String delete_key = "DELETE FROM servermanager.api_tokens";
		String insert_key = "INSERT INTO servermanager.api_tokens VALUES(?)";
		
		PreparedStatement stmt = conn.prepareStatement(delete_key);
		PreparedStatement stmt2 = conn.prepareStatement(insert_key);
		
		stmt2.setString(1, hash(new_key));
		
		try {
			stmt.executeUpdate();
			stmt2.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		return "";
	}
	
	public boolean processExists(String process_id) throws SQLException {
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		String query = "SELECT * FROM servermanager.processes WHERE process_id = ? AND active = 0";
		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet rs = null;
		
		stmt.setString(1, process_id);
		
		int count = 0;
		try {
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				count++;
			}
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		if(count > 0) return true;
		
		return false;
	}
	
	public String removeProcess(String process_id) throws SQLException {
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		String query = "DELETE FROM servermanager.processes WHERE process_id = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		
		stmt.setString(1, process_id);
		try {
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		return "";
	}
	
	public String addProcess(String process_id) throws SQLException {
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		String query = "INSERT INTO servermanager.processes VALUES(?, NOW())";
		PreparedStatement stmt = conn.prepareStatement(query);
		
		stmt.setString(1, process_id);
		try {
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		return "";
	}	
	
	public String setup(String api_key) throws SQLException, NoSuchAlgorithmException {
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		String[] queries = new String[3];
		queries[0] = "CREATE DATABASE IF NOT EXISTS servermanager";
		queries[1] = "CREATE TABLE IF NOT EXISTS servermanager.api_tokens(token VARCHAR(255))";
		queries[2] = "CREATE TABLE IF NOT EXISTS servermanager.runcommands("
				+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
				+ "date DATETIME, "
				+ "command TEXT, "
				+ "command_id VARCHAR(255))";
		PreparedStatement stmt = null;
		
		String return_statement = "success";
		
		//Creates tables
		for(int i = 0; i < queries.length; i++) {
			stmt = conn.prepareStatement(queries[i]);
			
			try {
				stmt.executeUpdate();
			}
			catch(SQLException e) {
				System.out.println(e);
				return_statement = "error";
			}
		}
		
		String check_rows = "SELECT * FROM servermanager.api_tokens";
		stmt = conn.prepareStatement(check_rows);
		int counter = 0;
		
		try {
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				counter++;
			}
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		//Checking if an API key already is in the database
		if(counter == 0) {
			String insert_key = "INSERT INTO servermanager.api_tokens VALUES(?)";
			stmt = conn.prepareStatement(insert_key);
			stmt.setString(1, hash(api_key));
			
			try {
				stmt.executeUpdate();
			}
			catch(SQLException e) {
				return_statement = "error";
			} 
		}
		
		conn.close();
		stmt.close();
		
		return return_statement;
	}
	
	public ArrayList<Map<String, String>> getCommands(String command_id) throws SQLException {
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		String query = "SELECT * FROM servermanager.runcommands ORDER BY id ASC";
		ResultSet rs = null;
		
		PreparedStatement stmt = conn.prepareStatement(query);
		//stmt.setString(1, command_id);
		
		ArrayList<Map<String, String>> result = new ArrayList<>();
		try {
			
			rs = stmt.executeQuery();
			
			int counter = 1;
			while(rs.next()) {
				
				Map<String, String> results = new HashMap<String, String>();
				
				results.put("id", Integer.toString(counter));
				results.put("date", rs.getString(2));
				results.put("data", rs.getString(3));
				
				result.add(results);
				
				counter++;
			}
		}
		catch(SQLException e) {
			System.out.println(e);
		} finally {
			conn.close();
			stmt.close();
		}
		
		return result;
	}
	
	public String insertCommand(String command, String command_id) throws SQLException {
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		String query = "INSERT INTO servermanager.runcommands VALUES(NULL, NOW(), ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(query);
		
		stmt.setString(1, command);
		stmt.setString(2, command_id);
		
		try {
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		} finally {
			conn.close();
			stmt.close();
		}
		return "";
	}
	
	public String hash(String api_key) throws NoSuchAlgorithmException {
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(api_key.getBytes(StandardCharsets.UTF_8)); //Salted hashes coming soon
		
		StringBuffer hashed_key = new StringBuffer();
	    
	    for (int i = 0; i < hash.length; i++) {
		     
	    	String hex = Integer.toHexString(0xff & hash[i]);
		    
	    	if(hex.length() == 1) {
		    	hashed_key.append('0');
		    }
		        
	    	hashed_key.append(hex);
	    }
	    
		return hashed_key.toString();
	}

	public ArrayList<String> authenticate(String api_key) throws SQLException, NoSuchAlgorithmException{
		
		Connection conn = DriverManager.getConnection(codes.host_name, codes.db_username, codes.db_password);
		String query = "SELECT * FROM servermanager.api_tokens";
		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet rs = null;
		
		ArrayList<String> al = new ArrayList<>(); //Placeholder
		
		ArrayList<String> wrong_key = new ArrayList<>(); //This will be returned if the API key is not valid
		wrong_key.add("wrong key");
		
		String hashed_key = hash(api_key);
		
		boolean authenticated = false;
		
		try {
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				al.add("");
				
				//Validating API key
				if(rs.getString(1).equals(hashed_key)) {
					authenticated = true;
				}
			}
		}
		catch(SQLException e) {
			System.out.println(e);
			al.add("error");
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
		
		if(authenticated == true) {
			return al; //Returns the API key as a place holder
		}
		else {
			return wrong_key;
		}
	}
}
