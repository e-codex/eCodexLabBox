# E-Codex Lab Box Tutorial

The LAB Box contains a collection of e-Codex components to make it easier to 
setup a **local** running e-codex environment for Testing purposes. 

The lab box is designed as an maven project and distributed with the 
maven-wrapper script. The maven-project ensures the download of the
required e-codex components. So the only requirement for running this project
is that the JAVA_HOME environment variable points at least to a java 8 JRE.

## Downloading the e-Codex components

1. Make sure that your JAVA_HOME variable is set
2. run maven:

 on Windows:
````batch
mvnw.bat package
````

 on Unix:
````bash
./mvnw.sh package
````
 This command will download all required e-Codex components

