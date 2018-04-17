package ui.folder_button;

import folder.file_preview.FilePreviewGenerator;

public class FolderButtonLevelUp extends FolderButtonSkeleton {

    FolderButtonLevelUp() {
        setText("..");
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        setIcon(previewGenerator.getEmptyFolderPreview());
        setButtonAlignment();
    }
}
