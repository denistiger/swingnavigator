package ui.file_preview;

import folder.IFolder;

import java.util.HashMap;
import java.util.Map;

public class GenericPreviewPanel extends FilePreviewPanel {

    private static Map<IFolder.FolderTypes, FilePreviewPanel> previewPanelMap;

    public GenericPreviewPanel() {
        previewPanelMap = new HashMap<>();
        previewPanelMap.put(IFolder.FolderTypes.IMAGE, new ImagePreviewPanel());
        previewPanelMap.put(IFolder.FolderTypes.TEXT_FILE, new TextFilePreviewPanel());
    }

    public void setParentFolder(IFolder parentFolder) {

    }

    @Override
    public void setPreviewFile(IFolder previewFile) throws PreviewException {

    }
}
