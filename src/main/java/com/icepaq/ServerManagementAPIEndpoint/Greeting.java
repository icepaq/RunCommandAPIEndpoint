package com.icepaq.ServerManagementAPIEndpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;

public class Greeting {
	
	Codes codes = new Codes();
	
	private final long id;
	
	private final ArrayList<String> content;

	public Greeting(long id, ArrayList<String> content) throws SQLException {
		
		this.id = id;
		this.content = content;
	}
	
	public long getId() {
		return id;
	}

	public ArrayList<String> getContent() throws SQLException {
		
		return content;
	}
	
	
}
