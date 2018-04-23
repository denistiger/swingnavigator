package ui.file_preview;

import folder.IFolder;

public class FilePreviewPanelFactory {

    public static FilePreviewPanel createFilePreviewPanel(IFolder folder) {
        try {
            switch (folder.getType()) {
                case IMAGE:
                    return new ImagePreviewPanel(folder);
                case TEXT_FILE:
                    return new TextFilePreviewPanel(folder);
                case UNKNOWN:
                case OTHER_FILE:
                    return new UnknownFilePreviewPanel(folder);
                default:
                    return null;
            }
        } catch (FilePreviewPanel.PreviewException er) {
            er.printStackTrace();
            return null;
        }
    }
}
