REM    Please change CATALINA_HOME to the right folder (below line only works if you start from current folder)
REM    set CATALINA_HOME=<YOUR_INSTALLATION_PATH>

set CATALINA_HOME=${project.basedir}\labenv${lab.id}\domibus-gateway
set JAVA_OPTS=%JAVA_OPTS% -Dgw_self=${lab.id} -Ddomibus.config.location=%CATALINA_HOME%\conf\domibus -Dcatalina.home=%CATALINA_HOME% -Xmx4096m -XX:MaxPermSize=4096m