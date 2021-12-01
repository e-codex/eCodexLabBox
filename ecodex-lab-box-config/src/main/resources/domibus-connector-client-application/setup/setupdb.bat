REM    Runs the database scripts whithin this folder against a h2 database to setup a
REM   database instance for running the domibusConnectorClient with a prepared test message.
REM

"%JAVA_HOME%\bin\java" -cp ${project.basedir}\labenv${lab.id}\domibus-connector-client-application\lib\h2.jar org.h2.tools.RunScript -url jdbc:h2:file:${project.basedir}\labenv${lab.id}\domibus-connector-client-application\database\connector-client-h2-db;MODE=Oracle -user connectorclient -password connectorclient -script ${project.basedir}\labenv${lab.id}\domibus-connector-client-application\setup\create_schema.sql
"%JAVA_HOME%\bin\java" -cp ${project.basedir}\labenv${lab.id}\domibus-connector-client-application\lib\h2.jar org.h2.tools.RunScript -url jdbc:h2:file:${project.basedir}\labenv${lab.id}\domibus-connector-client-application\database\connector-client-h2-db;MODE=Oracle -user connectorclient -password connectorclient -script ${project.basedir}\labenv${lab.id}\domibus-connector-client-application\setup\insert_testmessage.sql
