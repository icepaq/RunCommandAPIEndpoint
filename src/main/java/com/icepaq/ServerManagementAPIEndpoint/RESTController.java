package com.icepaq.ServerManagementAPIEndpoint;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RESTController {
	
	RunCommandThread rt;
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/finishedProcesses")
	public FinishedProcesses finishedProcesses(@RequestParam(value = "api_key") String api_key) throws SQLException, NoSuchAlgorithmException {
		
		return new FinishedProcesses(api_key);
	}
	
	@GetMapping("/activeProcesses")
	public ActiveProcesses activeProcesses(@RequestParam(value = "api_key") String api_key) throws NoSuchAlgorithmException, SQLException {
		
		return new ActiveProcesses(api_key);
	}
	
	@GetMapping("/updateAPIKey")
	public UpdateKey updatekey(@RequestParam(value = "api_key") String api_key, 
							   @RequestParam(value = "new_key") String new_key) throws NoSuchAlgorithmException, SQLException {
		
		System.out.println("/updateKey");
		
		return new UpdateKey(api_key, new_key);
	}
	
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") ArrayList<String> names) throws SQLException {
		
		return new Greeting(counter.incrementAndGet(), names);
	}
	
	//The command is recognized as an Array List to make the process simple in the first line of RunCommandThread.run()
	@CrossOrigin
	@GetMapping("/runcommand")
	public RunCommand echo(@RequestParam(value = "api_key", defaultValue = "null") String api_key, @RequestParam(value = "commands") ArrayList<String> commands) throws SQLException, NoSuchAlgorithmException {
		
		RunCommand rc = new RunCommand(api_key, commands);
		rt = rc.thread();
		return rc;
	}
	
	//Returns all entries in the database
	@CrossOrigin
	@GetMapping("/getouput")
	public GetOutput getOutput(@RequestParam(value = "api_key", defaultValue = "null") String api_key) throws SQLException {
		
		return new GetOutput(api_key, "");
	}
	
	//Runs an initial database setup
	@GetMapping("/setup")
	public Setup setup(@RequestParam(value = "api_key") String api_key) throws SQLException, NoSuchAlgorithmException {
		
		return new Setup(api_key);
	}
}
