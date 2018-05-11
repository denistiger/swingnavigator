rd /s /q swingnavigator
mkdir swingnavigator
xcopy out\production\ui\ui swingnavigator\ui /E /H /K /I
xcopy out\production\core\folder swingnavigator\folder /E /H /K /I
xcopy out\production\core\folder_management swingnavigator\folder_management /E /H /K /I
xcopy out\production\core\file_preview swingnavigator\file_preview /E /H /K /I
xcopy out\production\thirdparty\thirdparty swingnavigator\thirdparty /E /H /K /I
xcopy third-party\commons-net-3.6\commons-net-3.6.jar swingnavigator
cd swingnavigator
"%JAVA_HOME%\bin\jar.exe" xf commons-net-3.6.jar
set files_list=
setlocal enabledelayedexpansion
set cut=%CD%
echo cut
for /D /r org %%x in ("*") do (
	for /F %%i in ('dir /b %%x"\*.*"') do (
	   echo "Local file: "%%i
	   set tmp=%%x
	   set files_list=!files_list! !tmp:~59!\*.*
	   goto NextFolder
	)
	:NextFolder	
	echo "Folder done "%%x
)
echo !files_list!
"%JAVA_HOME%\bin\jar.exe" cvfe swingnavigator.jar ui.FolderNavigatorUI ui\*.class ui\folder_button\*.class ui\file_preview\*.class folder\*.class folder_management/*.class file_preview\*.class folder\ftp_folder\*.class folder\factory\*.class folder\zip_folder\*.class thirdparty\*.class file_preview\images\*.png %files_list%
endlocal
move swingnavigator.jar ..
cd ..
rem rd /s /q swingnavigator
