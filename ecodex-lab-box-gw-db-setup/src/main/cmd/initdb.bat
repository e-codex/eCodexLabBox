@echo off
rem This is the automatically built startup script for the DomibusStandaloneConnector.
rem To be able to run the JAVA_HOME system environment variable must be set properly.

if exist "%JAVA_HOME%" goto okJava
call setenv.bat
if exist "%JAVA_HOME%" goto okJava
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okJava

rem set "PATH=%PATH%;%JAVA_HOME%\bin"

set "CURRENT_DIR=%cd%"

set "CLASSPATH=%CURRENT_DIR%\bin\*"
echo %CLASSPATH%

set "CONFIG_FOLDER=%CURRENT_DIR%\config\"


@echo on
"%JAVA_HOME%\bin\java" -D"loader.path=./lib" -D"loader.main=eu.ecodex.labbox.gw.setup.SetupGwDatabase" -cp "%CLASSPATH%" "org.springframework.boot.loader.PropertiesLauncher"

:end

