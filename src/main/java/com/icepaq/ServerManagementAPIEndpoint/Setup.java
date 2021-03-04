package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Setup {
	
	DatabaseAccess a = new DatabaseAccess();
	
	public final String status;
	
	public Setup(String api_key) throws SQLException, NoSuchAlgorithmException {
		this.status = a.setup(api_key);
	}
	
	public String getStatus() {
		return status;
	}
}
