@ECHO OFF
REM
REM $Id$
REM
REM See: http://code.google.com/p/zaproxy/wiki/AuthoringHelp
REM

SETLOCAL

set JHINDEXER=c:\Java\javahelp-2.0.5\javahelp\bin\jhindexer.bat
set CWD=%CD%

cd %CWD%\zaphelp
call %JHINDEXER% zaphelp
jar -cf "%CWD%\zaphelp.jar" *
cd %CWD%
if exist "%CWD%\..\..\lib\zaphelp.jar" del "%CWD%\..\..\lib\zaphelp.jar"
copy "%CWD%\zaphelp.jar" "%CWD%\..\..\lib\zaphelp.jar"
del "%CWD%\zaphelp.jar"

ENDLOCAL