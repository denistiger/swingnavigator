package ui.file_preview;

import folder.IFolder;

import javax.swing.*;
import java.awt.*;

public class UnknownFilePreviewPanel extends FilePreviewPanel {

    private JLabel label;

    public UnknownFilePreviewPanel() {
        label = new JLabel("No file preview");
        label.setFont(new Font("Arial Black", Font.BOLD, 30));
        add(label);
    }

    @Override
    public void setPreviewFile(IFolder previewFile) {
        label.setText("No file preview for " + previewFile.getName());
    }
}
