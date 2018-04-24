package folder.file_preview;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static void resizeImageIcon(ImageIcon imageIcon, int width, int height) {
        Image srcImg = imageIcon.getImage();
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();
        imageIcon.setImage(resizedImg);
    }

    public static double getImageScaleRatio(ImageIcon imageIcon, int width, int height) {
        return getImageScaleRatio(imageIcon.getIconWidth(), imageIcon.getIconHeight(), width, height);
    }

    public static double getImageScaleRatio(int imageWidth, int imageHeight, int width, int height) {
        double widthRatio = (double)width / (double)imageWidth;
        double heightRatio = (double)height / (double)imageHeight;
        return Math.min(widthRatio, heightRatio);
    }


    public static void resizeImageIconProportional(ImageIcon imageIcon, int width, int height) {
        double ratio = getImageScaleRatio(imageIcon, width, height);
        width = (int) (ratio * imageIcon.getIconWidth());
        height = (int) (ratio * imageIcon.getIconHeight());
        Image srcImg = imageIcon.getImage();
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();
        imageIcon.setImage(resizedImg);
    }

}
