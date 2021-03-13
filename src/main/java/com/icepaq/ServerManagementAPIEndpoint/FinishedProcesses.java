package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinishedProcesses {

	public final String status;
	public final ArrayList<String> al;
	public final Map<String, ArrayList<String>> result = new HashMap<>();
	
	DatabaseAccess dba = new DatabaseAccess();
	
	public FinishedProcesses(String api_key) throws SQLException, NoSuchAlgorithmException {
		
		this.al = dba.getProcesses(0);
		
		ArrayList<String> content = dba.authenticate(api_key); //Authenticates the API key
		
		if(content.get(0).equals("error")) {
			
			System.out.println("Finished Processes: Error");
			this.status = "error";
		}
		else if(content.get(0).equals("wrong key")) {
			
			this.status = "wrong key";
		}
		else {
			
			this.status = "wrong key";
			this.result.put("Processes", al);
		}
	}
	
	public String getStatus() {
		return status;
	}
	
	public Map<String, ArrayList<String>> getResult() {
		return result;
	}
}
