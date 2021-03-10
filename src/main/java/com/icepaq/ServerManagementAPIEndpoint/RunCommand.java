package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;

public class RunCommand {
	
	Codes codes = new Codes();
	DatabaseAccess dba = new DatabaseAccess();
	
	private final String status;
	private final String pid;
	RunCommandThread rct;
	
	public RunCommand(String api_key, ArrayList<String> commands) throws SQLException, NoSuchAlgorithmException {
		
		ArrayList<String> content = dba.authenticate(api_key); //Authenticates the API key
		
		if(content.get(0).equals("error")) {
			this.status = "error";
			this.pid = "NULL";
		}
		else if(content.get(0).equals("wrong key")) {
			this.status = "wrong key";
			this.pid = "NULL";
		}
		else {
			this.status = "success";
			
			// Created before I knew Spring Boot would thread each HTTP request. Keeping it for organization.
			
			pid = generatePID();
			rct = new RunCommandThread(commands, pid);
			rct.start();
		}
	}
	
	private String generatePID() {
		
		SecureRandom sr = new SecureRandom();
		byte bytes[] = new byte[20];
		sr.nextBytes(bytes);
		
		String command_id = bytes.toString();
		System.out.println(command_id);
		
		return command_id;
	}
	
	public RunCommandThread thread() {
		
		return rct;
	}
	
	public String getstatus() {
		
		return status;
	}
	
	public String getpid() {
		
		return pid;
	}
}
