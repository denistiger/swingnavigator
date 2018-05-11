package ui.file_preview;

import folder.IFolder;

import javax.swing.*;
import java.awt.*;

public class UnknownFilePreviewPanel extends FilePreviewPanel {

    private JLabel label;

    UnknownFilePreviewPanel() {
        label = new JLabel("<html><center>No file preview</center></html>");
        label.setFont(new Font("Arial Black", Font.BOLD, 30));
        add(label);
    }

    @Override
    public void setPreviewFile(IFolder previewFile) {
        label.setText("<html><center>No file preview for file<br>" + previewFile.getName() + "</center></html>");
    }
}
