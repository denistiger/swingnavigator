package ui.file_preview;

import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;

import javax.swing.*;
import java.awt.*;

public class ImagePreviewPanel extends FilePreviewPanel {
    private Image image = null;

    public ImagePreviewPanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            return;
        }
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }

    @Override
    public void setPreviewFile(IFolder previewFile) throws PreviewException {
        if (previewFile.getType() != IFolder.FolderTypes.IMAGE) {
            throw new PreviewException("Not an image");
        }
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        ImageIcon icon = previewGenerator.getFilePreviewLarge(previewFile);
        image = icon.getImage();
    }
}
