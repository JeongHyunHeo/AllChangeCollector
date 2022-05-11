@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  app startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and APP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\AllChangeCollector.jar;%APP_HOME%\lib\client.diff-3.0.0.jar;%APP_HOME%\lib\client-3.0.0.jar;%APP_HOME%\lib\gen.jdt-3.0.0.jar;%APP_HOME%\lib\core-3.0.0.jar;%APP_HOME%\lib\simmetrics-core-3.2.3.jar;%APP_HOME%\lib\guava-30.1.1-jre.jar;%APP_HOME%\lib\org.eclipse.jgit-6.0.0.202111291000-r.jar;%APP_HOME%\lib\commons-cli-1.3.jar;%APP_HOME%\lib\rendersnake-1.9.0.jar;%APP_HOME%\lib\commons-io-2.11.0.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.8.0.jar;%APP_HOME%\lib\error_prone_annotations-2.5.1.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\JavaEWAH-1.1.13.jar;%APP_HOME%\lib\spark-core-2.9.1.jar;%APP_HOME%\lib\slf4j-simple-1.7.21.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\classindex-3.10.jar;%APP_HOME%\lib\fastutil-8.3.1.jar;%APP_HOME%\lib\gson-2.8.2.jar;%APP_HOME%\lib\jgrapht-core-1.5.1.jar;%APP_HOME%\lib\org.eclipse.jdt.core-3.26.0.jar;%APP_HOME%\lib\jheaps-0.13.jar;%APP_HOME%\lib\org.eclipse.core.resources-3.16.100.jar;%APP_HOME%\lib\org.eclipse.text-3.12.0.jar;%APP_HOME%\lib\org.eclipse.core.expressions-3.8.100.jar;%APP_HOME%\lib\org.eclipse.core.runtime-3.24.100.jar;%APP_HOME%\lib\org.eclipse.core.filesystem-1.9.300.jar;%APP_HOME%\lib\jetty-webapp-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-server-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-servlet-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-security-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-server-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-servlet-9.4.18.v20190429.jar;%APP_HOME%\lib\junit-4.8.2.jar;%APP_HOME%\lib\commons-lang3-3.1.jar;%APP_HOME%\lib\spring-webmvc-4.1.6.RELEASE.jar;%APP_HOME%\lib\jtidy-r938.jar;%APP_HOME%\lib\guice-3.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\org.eclipse.core.jobs-3.12.100.jar;%APP_HOME%\lib\org.eclipse.core.contenttype-3.8.100.jar;%APP_HOME%\lib\org.eclipse.equinox.app-1.6.100.jar;%APP_HOME%\lib\org.eclipse.equinox.registry-3.11.100.jar;%APP_HOME%\lib\org.eclipse.equinox.preferences-3.9.100.jar;%APP_HOME%\lib\org.eclipse.core.commands-3.10.100.jar;%APP_HOME%\lib\org.eclipse.equinox.common-3.16.0.jar;%APP_HOME%\lib\org.eclipse.osgi-3.17.200.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\websocket-client-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-client-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-http-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-common-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-io-9.4.18.v20190429.jar;%APP_HOME%\lib\jetty-xml-9.4.18.v20190429.jar;%APP_HOME%\lib\websocket-api-9.4.18.v20190429.jar;%APP_HOME%\lib\spring-web-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-context-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-aop-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-beans-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-expression-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-core-4.1.6.RELEASE.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\cglib-2.2.1-v20090111.jar;%APP_HOME%\lib\jetty-util-9.4.18.v20190429.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\asm-3.1.jar


@rem Execute app
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %APP_OPTS%  -classpath "%CLASSPATH%" AllChangeCollector.App %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable APP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%APP_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
