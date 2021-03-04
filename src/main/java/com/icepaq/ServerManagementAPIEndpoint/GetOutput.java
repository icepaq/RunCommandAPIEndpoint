package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class GetOutput {
	
	DatabaseAccess a = new DatabaseAccess();
	
	private final String status;
	private final ArrayList<Map<String, String>> content;
	
	public GetOutput(String api_key, String command_id) throws SQLException {
		
		ArrayList<String> authenticate = new ArrayList<String>();
		
		try {
			authenticate = a.authenticate(api_key);
		} 
		catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
		
		if(authenticate.get(0).equals("error")) {
			
			this.status = "error";
			content = null;
		}
		else if(authenticate.get(0).equals("wrong key")) {
			
			this.status = "wrong key";
			content = null;
		}
		else {
			this.status = "success";
			content = a.getCommands(command_id);
		}
	}
	
	public String getStatus() {
		return status;
	}
	
	public ArrayList<Map<String, String>> getContent() {
		return content;
	}
}
