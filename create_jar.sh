#!/bin/bash
rm -rf swingnavigator
mkdir swingnavigator
cp -r out/production/ui/ui swingnavigator
cp -r out/production/core/folder swingnavigator
cp -r out/production/thirdparty/thirdparty swingnavigator
cd swingnavigator
jar cvfe swingnavigator.jar ui.FolderNavigatorUI ui/*.class ui/folder_button/*.class ui/file_preview/*.class folder/*.class folder/file_preview/*.class folder/zip_folder/*.class thirdparty/*.class folder/file_preview/images/*.png
mv swingnavigator.jar ..
cd ..
rm -rf swingnnavigator
