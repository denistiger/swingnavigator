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
"%JAVA_HOME%\bin\jar.exe" cvfe swingnavigator.jar ui.FolderNavigatorUI ui\*.class ui\folder_button\*.class ui\file_preview\*.class folder\*.class folder_management/*.class file_preview\*.class folder\ftp_folder\*.class folder\factory\*.class folder\zip_folder\*.class thirdparty\*.class file_preview\images\*.png
move swingnavigator.jar ..
cd ..
rem rd /s /q swingnavigator
