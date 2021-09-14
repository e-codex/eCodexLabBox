
@ECHO OFF 
SETLOCAL

set HOME_DIR=%cd%


:SetMavenHome
set "MAVEN_HOME=%cd%\apache-maven-3.8.1\bin\"


set "MAVEN_CMD=%MAVEN_HOME%mvn.cmd "

echo.
echo MAVEN_CMD is %MAVEN_CMD%

rem set "MAVEN_ARGS=install -Dlab.id=%LAB_ID%"

rem echo MAVEN_ARGS is %MAVEN_ARGS%

echo.
echo ##################### Building ecodex-lab-box #####################
echo.

call "%MAVEN_CMD%" -f ..\pom.xml clean install
exit
