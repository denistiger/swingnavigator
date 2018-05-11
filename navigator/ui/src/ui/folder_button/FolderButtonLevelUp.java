package ui.folder_button;

import file_preview.FilePreviewGenerator;

class FolderButtonLevelUp extends FolderButtonSkeleton {

    FolderButtonLevelUp() {
        setText("..");
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        setIcon(previewGenerator.getEmptyFolderPreview());
        setButtonAlignment();
    }
}
