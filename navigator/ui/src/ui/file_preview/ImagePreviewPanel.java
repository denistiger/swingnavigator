package ui.file_preview;

import folder.IFolder;
import file_preview.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagePreviewPanel extends FilePreviewPanel {

    private BufferedImage bufferedImage = null;

    public ImagePreviewPanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bufferedImage == null) {
            return;
        }
        super.paintComponent(g);
        double ratio = ImageUtils.getImageScaleRatio(bufferedImage.getWidth(), bufferedImage.getHeight(), getWidth(), getHeight());
        int width = (int) (ratio * bufferedImage.getWidth());
        int height = (int) (ratio * bufferedImage.getHeight());
        int widthOffset = (getWidth() - width) / 2;
        int heightOffset = (getHeight() - height) / 2;

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(bufferedImage, widthOffset, heightOffset, width, height, this);
    }

    @Override
    public void setPreviewFile(IFolder previewFile) throws PreviewException {
        if (previewFile.getType() != IFolder.FolderTypes.IMAGE) {
            throw new PreviewException("Not an image");
        }
        try {
            bufferedImage = ImageIO.read(previewFile.getInputStream());
        } catch (IOException e) {
            bufferedImage = null;
            e.printStackTrace();
        }
    }

}
