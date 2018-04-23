package ui.file_preview;

import javax.swing.*;

public class FilePreviewPanel extends JPanel {
    public class PreviewException extends Exception {

        public PreviewException(String message) {
            super(message);
        }
    }
}
