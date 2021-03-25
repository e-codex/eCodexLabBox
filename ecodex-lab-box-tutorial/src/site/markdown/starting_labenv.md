# Starting a Lab Environment



The folder should contain two lab environments labenv02, labenv03 these environments are located in the subfolders.

## Support

This lab box environment comes without any support! It is meant for playing around with the e-codex
components. Support is only provided via the project you are participating like

* EXEC
* MeCodex
* MeCodex2
* ProCodex
* ...


## Required Software

As the e-codex components are java software you need a Java Runtime Environment for
running this components. You also have to set your JAVA_HOME environment variable so 
it points to your java runtime environment.

## Installing the Software

This folder contains a maven project which downloads the labbox. Just type

mvn -De-codex.repo.username=<username> -De-codex.repo.password=<password> -Dlab.id=${labid} install

install to download all the e-codex components and create a preconfigured labbox-environment with the
corresponding id.

If you do not have maven installed you can run

mvnw.bat which uses the maven-wrapper project to download and install maven and execute it afterwards. 

Anyway you have to setup the passwords for the e-codex repository. Username and password are available via the e-codex project: https://www.e-codex.eu

NOTE: if you are behind a corporate proxy you should configure maven according to this guide:
https://maven.apache.org/guides/mini/guide-proxies.html

## Required Internet Connection

The connector is loading trusted lists of lists from the EC. For this purpose a internet connection
should be available.
  

## Required Ports 

Within the lab environments all components are configured to work on one pc. This means that all started components are 
configured to use different ports. The following components are preconfigured within this environment:

 * Domibus Gateway
 * Domibus Connector
 * Domibus Connector Client
 * Domibus Connector Client Standalone
 
Usually for opening this ports under windows **Administrative Privileges are required**! 
 
So the required ports are:

 
### Domibus Gateway: 
 
| Port          | Description                      |
|---------------|----------------------------------|
| 8${lab.id}9   | Tomcat Shutdown Port             |
| 8${lab.id}0   | Tomcat HTTP Port                 |
| 9${lab.id}1   | JMX-connector Port               |
| 9${lab.id}2   | RMI port                         |
| 9${lab.id}3   | ActiveMQ JMS port                |
 
 This means if you start the domibus gw of the labenv02 the gateway will need the ports
  8029, 8020, 9021, 9022, 9023
  
  If you like to change these ports you can do that. Only the Tomcat HTTP port
  is also required to be known by the connector communicating with this gateway.
  Also the other gateways need to know how to reach the gateway of labenv02, this
  is configured within the p-modes.
  
### Domibus Connector:
 
| Port          | Description                      |
|---------------|----------------------------------|
| 8${lab.id}1   | HTTP-Port                        |
 

This means the labenv02 connector binds to port 8021.

All other required ports are determined dynamically. JMX is disabled within the connector.



### Domibus Connector Client:

The domibus connector client is an active connector client. This means it
creates an webinterface to enable the connector to push messages to it. This 
connector client also comes with an rest interface. This connector client
is currently under development and should become a show case how to use the
connector client interface.

| Port          | Description                      |
|---------------|----------------------------------|
| 8${lab.id}2   | HTTP-Port                        |


### Domibus Connector Client Standalone:

The domibus connector client standalone is a passive client. This means he does
not open any ports. It is regularly asking the connector if there are messages
he can fetch.


## Starting the components

All components are preconfigured and are ready to run within this labenvironment.

**All following paths are meant to start from your labenv. So you have to be in your labenv fodler (eg. labenv02)**

![Labenvironment Folder](images/screenshot_labenv_folder.png)


### Starting the gateway

1) Open the folder 'domibus-gateway/setup'. Execute the setupdb.bat script.
 This script will create an embedded database (H2-database) for the gateway.
 It also loads some basic configuration.
 The database will be created at 'domibus-gateway/work/database.mv'.
 
2) After the database is initialized start the gateway. This can be done by
 running the script 'domibus-gateway/bin/startup.bat'
 Wait until the last log line in the console prints out 'Server startup in ...'
 
![Startup Screenshot](images/screenshot_gateway_startup.png)
 
 
3) Please wait a little bit and start the script 'domibus-gateway/setup/setuppmodes.bat'.
 This will upload the p-modes file 'pmodex/pmodes-template.xml' to the gateway. 
 As an alternative you can also upload the p-modex by yourself from the file 'pmodex/pmodes-template.xml'.
 
4) Now you should be able to login into the domibus gateway administration console
 which sould be reachable under http://localhost:8${lab.id}0/domibus
 
 
### Starting the connector
 
1) Run the startupscript of the connector 'domibus-connector/start.bat'. The
 connector within this labenvironment is preconfigured with all necessary informations
 like p-modes, address of the gateway, keystores, truststores, ...
 
2) Wait a little bit and check if you can reach the connector administration gui 
 under http://localhost:8${lab.id}1/admin
 The default username is **admin** and the password is **123456**.
 For further questions consult the domibus gateway administration guide.
 
 
### Starting the standalone client
 
1) Run the startscript in 'domibus-connector-client-standalone/DomibusConnectorClientGUI.bat'
 
 
### Start a second labenv
 Start the other labenv so you can send messages between these two environments.
 
 
 
 
 
 
 