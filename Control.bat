set ip=
set mask=

if "%ip%" == "" goto :noip

start "" explorer "Control.html?ip=%ip%"
start "" java -jar StreamViewer.jar %ip% %mask%
goto :eof

:noip
start "" explorer "Control.html"
start "" java -jar StreamViewer.jar

