#!/bin/sh
#Please change CATALINA_HOME to the right folder
export CATALINA_HOME=${project.basedir}/labenv${lab.id}/domibus-gateway
JAVA_OPTS="$JAVA_OPTS -Xms4096m -Xmx4096m -XX:MaxPermSize=4096m -Ddomibus.config.location=$CATALINA_HOME/conf/domibus -Dcatalina.home=$CATALINA_HOME"


