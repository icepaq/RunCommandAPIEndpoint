package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TerminateProcess {
	
	public final String status;
	public final String response;
	
	public TerminateProcess(String api_key, String pid) throws NoSuchAlgorithmException, SQLException {
		DatabaseAccess dba = new DatabaseAccess();
		ArrayList<String> content = dba.authenticate(api_key); //Authenticates the API key
		
		if(content.get(0).equals("error")) {
			
			System.out.println("Finished Processes: Error");
			this.response = "";
			this.status = "error";
		}
		else if(content.get(0).equals("wrong key")) {
			this.response = "";
			this.status = "wrong key";
		}
		else {
			this.response = dba.terminateProcess(pid);
			this.status = "success";
		}
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getResponse() {
		return response;
	}
}
