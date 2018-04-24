package ui;

import folder.IFolder;
import ui.folder_button.FolderButtonSkeleton;

public interface IOpenFolderListener {
    void openFolder(IFolder folder);
    void selectFolder(FolderButtonSkeleton folderButton);
    void levelUp();
}
