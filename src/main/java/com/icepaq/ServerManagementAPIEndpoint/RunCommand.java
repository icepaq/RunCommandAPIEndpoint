package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RunCommand {
	
	Codes codes = new Codes();
	DatabaseAccess dba = new DatabaseAccess();
	
	private final String status;
	RunCommandThread rct;
	
	public RunCommand(String api_key, ArrayList<String> commands, String command_id) throws SQLException, NoSuchAlgorithmException {
		
		ArrayList<String> content = dba.authenticate(api_key); //Authenticates the API key
		
		if(content.get(0).equals("error")) {
			this.status = "error";
		}
		else if(content.get(0).equals("wrong key")) {
			this.status = "wrong key";
		}
		else {
			this.status = "success";
			
			// Created before I knew Spring Boot would thread each HTTP request. Keeping it for organization.
			rct = new RunCommandThread(commands, command_id);
			rct.run();
		}
	}
	
	public RunCommandThread thread() {
		
		return rct;
	}
	
	public String getstatus() {
		
		return status;
	}
}
