package ui;

import ui.folder_button.FolderButtonSkeleton;

public interface IFoldersPanelSelection {
    void setSelection(FolderButtonSkeleton folderButtonSkeleton);
    void next();
    void prev();
    void up();
    void down();
    FolderButtonSkeleton getSelection();
}
