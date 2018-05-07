#!/bin/bash
rm -rf swingnavigator
mkdir swingnavigator
cp -r out/production/ui/ui swingnavigator
cp -r out/production/core/folder swingnavigator
cp -r out/production/core/file_preview swingnavigator
cp -r out/production/core/folder_management swingnavigator
cp -r out/production/thirdparty/thirdparty swingnavigator
cp third-party/commons-net-3.6/commons-net-3.6.jar swingnavigator
cd swingnavigator
unzip commons-net-3.6.jar 
rm commons-net-3.6.jar
jar cvfe swingnavigator.jar ui.FolderNavigatorUI ui/*.class ui/folder_button/*.class ui/file_preview/*.class folder/*.class folder_management/*.class file_preview/*.class folder/ftp_folder/*.class folder/factory/*.class folder/zip_folder/*.class thirdparty/*.class file_preview/images/*.png $(find org -name '*.class')
mv swingnavigator.jar ..
cd ..
rm -rf swingnnavigator
