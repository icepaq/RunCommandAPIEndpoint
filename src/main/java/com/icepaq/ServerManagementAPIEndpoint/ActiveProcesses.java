package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActiveProcesses {
	
	DatabaseAccess dba = new DatabaseAccess();
	private final String status;
	private final Map<String, ArrayList<String>> data = new HashMap<>();
	
	
	public ActiveProcesses(String api_key) throws NoSuchAlgorithmException, SQLException {
		System.out.println("Active Processes");
		ArrayList<String> content = dba.authenticate(api_key); //Authenticates the API key
		
		if(content.get(0).equals("error")) {
			System.out.println("Active Processes: Error");
			this.status = "error";
		}
		else if(content.get(0).equals("wrong key")) {
			this.status = "wrong key";
		}
		else {
			this.status = "success";
			
			ArrayList<String> al = dba.getActiveProcesses();
			data.put("Processes", al);
		}
		
	}
	
	public String getStatus() {
		
		return status;
	}
	
	public Map<String, ArrayList<String>> getData() {
		
		return data;
	}
}
