package ui.folder_button;

import file_preview.FilePreviewGenerator;
import ui.IOpenFolderListener;

class FolderButtonLevelUp extends FolderButtonSkeleton {

    FolderButtonLevelUp() {
        setText("..");
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        setIcon(previewGenerator.getEmptyFolderPreview());
        setButtonAlignment();
    }

    @Override
    public void notifyIOpenFolderListener(IOpenFolderListener iOpenFolderListener) {
        iOpenFolderListener.levelUp();
    }

}
