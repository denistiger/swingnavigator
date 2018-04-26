rd /s /q swingnavigator
mkdir swingnavigator
xcopy out\production\ui\ui swingnavigator\ui /O /X /E /H /K /I
xcopy out\production\core\folder swingnavigator\folder /O /X /E /H /K /I
xcopy out\production\thirdparty\thirdparty swingnavigator\thirdparty /O /X /E /H /K /I
cd swingnavigator
"C:\Program Files\Java\jdk1.8.0_151\bin\jar.exe" cvfe swingnavigator.jar ui.FolderNavigatorUI ui\*.class ui\folder_button\*.class ui\file_preview\*.class folder\*.class folder\file_preview\*.class folder\ftp_folder\*.class folder\factory\*.class folder\zip_folder\*.class thirdparty\*.class folder\file_preview\images\*.png
move swingnavigator.jar ..
cd ..
rd /s /q swingnavigator
