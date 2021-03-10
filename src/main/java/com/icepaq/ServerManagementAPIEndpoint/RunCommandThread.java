package com.icepaq.ServerManagementAPIEndpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;

public class RunCommandThread extends Thread{

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
	public void run() {
		
		try {
			a.addProcess(command_id); // Adding process to database. 
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
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
		
		try {
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				
				try {
					a.insertCommand(line, command_id);
				} catch (SQLException e) {
					System.out.println(e);
				}
				
				// If the process id is not found, this means that the process has been removed manually.	
				try {
					if (!a.processExists(command_id)) {
						break;
					}
				} catch (SQLException e1) {
					System.out.println(e1);
				}
				
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void start() {
		Thread r = (new Thread(new RunCommandThread(commands, command_id)));
		r.start();
	}

}
