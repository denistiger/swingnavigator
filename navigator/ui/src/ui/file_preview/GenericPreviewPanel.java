package ui.file_preview;

import folder.IFolder;
import ui.FolderIterator;

import java.util.HashMap;
import java.util.Map;

public class GenericPreviewPanel extends FilePreviewPanel {

    private static FilePreviewPanel unknownFilePreview = new UnknownFilePreviewPanel();
    private static Map<IFolder.FolderTypes, FilePreviewPanel> previewPanelMap;
    private FolderIterator folderIterator;
    private FilePreviewPanel currentPreview = null;

    public GenericPreviewPanel(FolderIterator folderIterator) {
        this.folderIterator = folderIterator;
        previewPanelMap = new HashMap<>();
        previewPanelMap.put(IFolder.FolderTypes.IMAGE, new ImagePreviewPanel());
        previewPanelMap.put(IFolder.FolderTypes.TEXT_FILE, new TextFilePreviewPanel());
        previewPanelMap.put(IFolder.FolderTypes.OTHER_FILE, unknownFilePreview);
        previewPanelMap.put(IFolder.FolderTypes.FOLDER, unknownFilePreview);
        previewPanelMap.put(IFolder.FolderTypes.ZIP, unknownFilePreview);
        previewPanelMap.put(IFolder.FolderTypes.UNKNOWN, unknownFilePreview);
    }

    public void updatePreviewFile() {
        IFolder file = folderIterator.getIFolder();
        if (file != null) {
            try {
                setPreviewFile(file);
            } catch (PreviewException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setPreviewFile(IFolder previewFile) throws PreviewException {
        if (currentPreview != null) {
            remove(currentPreview);
        }
        currentPreview = previewPanelMap.get(previewFile.getType());
        if (currentPreview == null) {
            currentPreview = unknownFilePreview;
        }
        currentPreview.setPreviewFile(previewFile);
        add(currentPreview);
    }
}
