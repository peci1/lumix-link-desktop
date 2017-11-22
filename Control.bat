rem Usage: Control.bat [browser [camera_ip [camera_ip_mask]]] 
rem     Control.bat (opens the page in Internet Explorer, asks for IP address and network mask of the camera)
rem     Control.bat firefox (opens the page in firefox, asks for IP address and network mask of the camera)
rem     Control.bat firefox 192.168.0.1 (opens the page in firefox, doesn't ask for anything, assumes network mask /24)
rem     Control.bat firefox 192.168.0.1 16 (opens the page in firefox, doesn't ask for anything, uses the given network mask /16)
rem
rem For ease of use, you can make a shortcut to this .bat file that will launch it with your favorite parameters.

set browser=iexplore
set ip=
set mask=24

set scriptdir=%~dp0

if not "%1"=="" (
    set browser=%1
)

if not "%2"=="" (
    set ip=%2
)

if not "%3"=="" (
    set mask=%3
)

if "%ip%" == "" goto :noip

start "" "%browser%" "file://%scriptdir%Control.html?ip=%ip%"
start "" java -jar StreamViewer.jar %ip% %mask%
goto :eof

:noip
start "" "%browser%" "file://%scriptdir%Control.html"
start "" java -jar StreamViewer.jar

