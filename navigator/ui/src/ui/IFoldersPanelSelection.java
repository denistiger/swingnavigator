package ui;

import ui.folder_button.FolderButtonSkeleton;

public interface IFoldersPanelSelection {
    void setSelection(FolderButtonSkeleton folderButtonSkeleton);
    void setSelection(String folderName);
    void next();
    void prev();
    void up();
    void down();
    void pageUp();
    void pageDown();
    void begin();
    void end();
    FolderButtonSkeleton getSelection();
}
