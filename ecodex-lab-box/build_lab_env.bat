
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

    SHIFT

GOTO ParseParams

:SetMavenHome
set "MAVEN_HOME=%cd%\apache-maven-3.8.1\bin\"

:CheckLabId
	If "%LAB_ID%"=="" GOTO :usage

echo.
echo LAB_ID is %LAB_ID%

set "MAVEN_CMD=%MAVEN_HOME%mvn.cmd "

echo.
echo MAVEN_CMD is %MAVEN_CMD%

rem set "MAVEN_ARGS=install -Dlab.id=%LAB_ID%"

rem echo MAVEN_ARGS is %MAVEN_ARGS%



echo.
echo ##################### calling %MAVEN_CMD% install -Dlab.id=%LAB_ID% #####################
echo.

call "%MAVEN_CMD%" install -Dlab.id=%LAB_ID%

echo.
echo ##################### labenv%LAB_ID% built #####################
echo.

echo.
echo ##################### Setup of domibus-gateway database #####################
echo.
call %cd%\labenv%LAB_ID%\domibus-gateway\setup\setupdb.bat

rem call %cd%\labenv%LAB_ID%\domibus-gateway\setup\setuppmodes.bat

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
echo lab.id			specifies the numeric value of the lab environment to be created. Must be a value between 01 and 10
echo.
echo Example: build_lab_env.bat lab.id:01
echo.
echo ########################################################################


:end