package com.icepaq.ServerManagementAPIEndpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;

public class RunCommandThread {

	Codes codes = new Codes();
	ArrayList<String> commands;
	DatabaseAccess a = new DatabaseAccess();
	String command_id = null;
	
	Thread r = null;

	public RunCommandThread(ArrayList<String> commands, String command_id) {
		this.commands = commands;
		this.command_id = command_id;
	}

	//Thread runs inside of this method
	public void run() throws SQLException {
		
		a.addProcess(command_id); // Adding process to database. 
		
		//Created a process. commands is passed from RESTController.runcommands
		ProcessBuilder builder = new ProcessBuilder(commands); 
		builder.redirectErrorStream(true);

		//Run the process
		Process process = null;
		try {
			process = builder.start();
		} catch (IOException e1) {
			System.out.println(e1);
		}

		//Able to read output from console
		InputStream is = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line = null;
		
		// Creating a command id
		SecureRandom sr = new SecureRandom();
		byte bytes[] = new byte[20];
		sr.nextBytes(bytes);
		
		command_id = bytes.toString();
		System.out.println(command_id);
		
		a.addProcess(command_id);
		
		try {
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				
				try {
					a.insertCommand(line, command_id);
				} catch (SQLException e) {
					System.out.println(e);
				}
				
				// If the process id is not found, this means that the process has been removed manually.	
				if (!a.processExists(command_id)) {
					break;
				}
			}
			a.removeProcess(command_id);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void start() {
		//r = (new Thread(new RunCommandThread(commands, command_id)));
		//r.start();
	}

}
