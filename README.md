# RunCommandAPIEndpoint
Be able to run commands on a server remotely through a REST API. Convienient for automation and managing several servers.

## Before Running
- Set up a MySQL server which will store CLI output. Name the databse servermanager and run the commands: 
`CREATE TABLE api_tokens(token VARCHAR(255))`

`CREATE TABLE runncommands(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, date DATETIME, command TEXT)`

`CREATE TABLE processes(process_id VARCHAR(120), date DATETIME, active BOOLEAN);`

- Modify src/Codes.java and add the appropriate hostname, username and password.
- Modify the application port by adding `application.port=<port>` in resources/application.properties

## Running
Type `mvnw spring-boot:run` and let the application build and run itself.

More information about running spring boot projects: https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-running-your-application.html

## Docker
Dockerfile and docker-compose files have been created. This will containerize both the API and the MySQL server.
Instructions to run with Docker:
1. mvnw package
2. docker build -t apiserver .
3. docker network create api-network
4. docker-compose up

## API calls

**/setup?api_key=NEWAPIKEY**
This will create an API key.

**/runcommand?api_key=APIKEY&commands=command**
Example: /runcommand?api_key=DLK4J3F834GFDS&commands=ping,localhost,"-n 10"

This program uses the Java Process Builder to run commands. More information about structuring commands: https://docs.oracle.com/javase/7/docs/api/java/lang/ProcessBuilder.html

**/getoutput?api_key=APIKEY**


## Understanding the Program
1. When you go to a URL such as /getoutput this URL is hard coded into the code. The mapping then calls the GetOutput class.
2. The GetOutput constructor validates the API key provided.
3. Once the API key is validates, the constructor calls DatabaseAccess.getCommands()
4. getCommands() accesses the database and returns all the entries made.
5. The GetOutput class returns the status and output. Spring Boot uses Jackson to automatically convert this data into JSON.
