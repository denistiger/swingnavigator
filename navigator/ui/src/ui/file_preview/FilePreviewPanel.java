package ui.file_preview;

import folder.IFolder;

import javax.swing.*;

public abstract class FilePreviewPanel extends JPanel {
    public static class PreviewException extends Exception {

        public PreviewException(String message) {
            super(message);
        }
    }

    public abstract void setPreviewFile(IFolder previewFile) throws PreviewException;
}
