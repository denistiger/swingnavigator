package ui.file_preview;

import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;

import javax.swing.*;
import java.awt.*;

public class ImagePreviewPanel extends FilePreviewPanel {
    private Image image;

    public ImagePreviewPanel(IFolder folder) throws PreviewException {
        if (folder.getType() != IFolder.FolderTypes.IMAGE) {
            throw new PreviewException("Not an image");
        }
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        ImageIcon icon = previewGenerator.getFilePreviewLarge(folder);
        image = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }
}
