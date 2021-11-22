REM    Runs the database scripts whithin this folder against a h2 database to setup a
REM   database instance for running the domibus gw.
REM

"%JAVA_HOME%\bin\java" -cp ${project.basedir}\labenv${lab.id}\domibus-gateway\lib\h2.jar org.h2.tools.RunScript -url jdbc:h2:file:${project.basedir}\labenv${lab.id}\domibus-gateway\work\database;MODE=Oracle -user edelivery -password edelivery -script ${project.basedir}\labenv${lab.id}\domibus-gateway\setup\create_schema.sql
"%JAVA_HOME%\bin\java" -cp ${project.basedir}\labenv${lab.id}\domibus-gateway\lib\h2.jar org.h2.tools.RunScript -url jdbc:h2:file:${project.basedir}\labenv${lab.id}\domibus-gateway\work\database;SCHEMA=domibus;MODE=Oracle -user edelivery -password edelivery -script ${project.basedir}\labenv${lab.id}\domibus-gateway\setup\oracle-${domibus-gw.version}.ddl
"%JAVA_HOME%\bin\java" -cp ${project.basedir}\labenv${lab.id}\domibus-gateway\lib\h2.jar org.h2.tools.RunScript -url jdbc:h2:file:${project.basedir}\labenv${lab.id}\domibus-gateway\work\database;SCHEMA=domibus;MODE=Oracle -user edelivery -password edelivery -script ${project.basedir}\labenv${lab.id}\domibus-gateway\setup\oracle-${domibus-gw.version}-data.ddl