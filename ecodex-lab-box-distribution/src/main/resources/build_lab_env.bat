
@ECHO OFF 
SETLOCAL

set HOME_DIR=%cd%

GOTO ParseParams

:ParseParams
    If "%~1"=="" GOTO SetMavenHome

    SET TempVar=%~1
    IF "%TempVar:~0,7%"=="lab.id:" ( 
		echo.
		echo lab.id Parameter found
        SET LAB_ID=%TempVar:~7,250%
    )
    IF "%TempVar:~0,16%"=="http.proxy.host:" ( 
		echo.
		echo http.proxy.host Parameter found
        SET HTTP_PROXY_HOST=%TempVar:~16,250%
    )
    IF "%TempVar:~0,16%"=="http.proxy.port:" ( 
		echo.
		echo http.proxy.port Parameter found
        SET HTTP_PROXY_PORT=%TempVar:~16,250%
    )
	IF "%TempVar:~0,17%"=="https.proxy.host:" ( 
		echo.
		echo https.proxy.host Parameter found
        SET HTTPS_PROXY_HOST=%TempVar:~17,250%
    )
    IF "%TempVar:~0,17%"=="https.proxy.port:" ( 
		echo.
		echo https.proxy.port Parameter found
        SET HTTPS_PROXY_PORT=%TempVar:~17,250%
    )

    SHIFT

GOTO ParseParams

:SetMavenHome
set MVN_DIR=%MAVEN_HOME%
goto :MavenHomeCheck

:MavenHomeCheck  
if "%MVN_DIR%"=="" (
echo No MAVEN_HOME found! Setting local Maven...
goto :SetMavenHomeLocal
)
else
(
echo MAVEN_HOME found. 
goto :CheckLabId
)


:SetMavenHomeLocal
set "MVN_DIR=%cd%\apache-maven-3.8.1"

:CheckLabId
	If "%LAB_ID%"=="" EXIT 100

echo.
echo LAB_ID is %LAB_ID%

set "MAVEN_CMD=%MVN_DIR%\bin\mvn.cmd "

echo.
echo MAVEN_CMD is %MAVEN_CMD%

SET MAVEN_ARGS=-Dlab.id=%LAB_ID%

IF DEFINED HTTP_PROXY_HOST (SET MAVEN_ARGS=%MAVEN_ARGS% -Dhttp.proxyHost=%HTTP_PROXY_HOST%)
IF DEFINED HTTP_PROXY_PORT (SET MAVEN_ARGS=%MAVEN_ARGS% -Dhttp.proxyPort=%HTTP_PROXY_PORT%)
IF DEFINED HTTPS_PROXY_HOST (SET MAVEN_ARGS=%MAVEN_ARGS% -Dhttps.proxyHost=%HTTPS_PROXY_HOST%)
IF DEFINED HTTPS_PROXY_PORT (SET MAVEN_ARGS=%MAVEN_ARGS% -Dhttps.proxyPort=%HTTPS_PROXY_PORT%)

rem SET MAVEN_ARGS=%MAVEN_ARGS% -Dhttps.nonProxyHosts=localhost

echo MAVEN_ARGS is %MAVEN_ARGS%



echo.
echo ##################### calling %MAVEN_CMD% install %MAVEN_ARGS% #####################
echo.

call "%MAVEN_CMD%" install %MAVEN_ARGS%
IF %ERRORLEVEL% NEQ 0 EXIT 200

echo.
echo ##################### labenv%LAB_ID% built #####################
echo.

echo.
echo ##################### Setup of domibus-gateway database #####################
echo.
call %cd%\labenv%LAB_ID%\domibus-gateway\setup\setupdb.bat
IF %ERRORLEVEL% NEQ 0 EXIT 300


echo.
echo ##################### Setup of domibusConnectorClient-Application database #####################
echo.
call %cd%\labenv%LAB_ID%\domibus-connector-client-application\setup\setupdb.bat
IF %ERRORLEVEL% NEQ 0 EXIT 400




goto end

:usage
echo.
echo ##################### Usage of build_lab_env script #####################
echo.
echo This script is to create a new e-Codex Lab Box with a certain environment id .
echo Parameters are to be entered with name:value pair.
echo.
echo Parameters:
echo.
echo lab.id			   specifies the numeric value of the lab environment to be created. Must be a value between 01 and 09
echo http.proxy.host   specifies what proxy host should be used to connect to the outside world.
echo http.proxy.port   specifies what proxy port should be used to connect to the outside world.
echo https.proxy.host  specifies what proxy host should be used to connect to the outside world via https.
echo https.proxy.port  specifies what proxy port should be used to connect to the outside world via https.
echo.
echo Example:                  build_lab_env.bat lab.id:01
echo Example with https proxy: build_lab_env.bat lab.id:01 https.proxy.host:127.0.0.1 https.proxy.port:80
echo.
echo ########################################################################


:end
exit 0