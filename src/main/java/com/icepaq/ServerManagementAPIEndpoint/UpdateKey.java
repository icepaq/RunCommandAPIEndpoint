package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateKey {
	
	Codes codes = new Codes();
	DatabaseAccess dba = new DatabaseAccess();
	
	private final String status;
	
	public UpdateKey(String api_key, String new_key) throws NoSuchAlgorithmException, SQLException {
		
		System.out.println("UpdateKey");
		ArrayList<String> content = dba.authenticate(api_key); //Authenticates the API key
		
		if(content.get(0).equals("error")) {
			System.out.println("UpdateKey: Error");
			this.status = "error";
		}
		else if(content.get(0).equals("wrong key")) {
			this.status = "wrong key";
		}
		else {
			this.status = "success";
			
			dba.updateAPIKey(new_key);
			
		}
	}
	
	public String getStatus() {
		return status;
	}

}
