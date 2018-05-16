package ui.file_preview;

import folder.IFolder;
import file_preview.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImagePreviewPanel extends FilePreviewPanel {

    private BufferedImage bufferedImage = null;

    ImagePreviewPanel() {
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
        InputStream inputStream = previewFile.getInputStream();
        try {
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException | IllegalArgumentException e) {
            bufferedImage = null;
            System.err.println("Failed to read image " + previewFile.getAbsolutePath() );
            e.printStackTrace();
            throw new PreviewException("Failed to read image.");
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
