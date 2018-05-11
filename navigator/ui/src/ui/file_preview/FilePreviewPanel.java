package ui.file_preview;

import folder.IFolder;

import javax.swing.*;

public abstract class FilePreviewPanel extends JPanel {
    static class PreviewException extends Exception {

        PreviewException(String message) {
            super(message);
        }
    }

    public abstract void setPreviewFile(IFolder previewFile) throws PreviewException;
}
