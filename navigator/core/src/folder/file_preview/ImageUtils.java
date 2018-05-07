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

    public static ImageIcon combinedIcon(ImageIcon baseIcon, ImageIcon overlayIcon) {
        double cutOff = 0.2;
        Image baseImage = baseIcon.getImage();
        Image overlayImg = overlayIcon.getImage();
        BufferedImage resImg = new BufferedImage(baseIcon.getIconWidth(), baseIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(baseImage, 0, 0, baseIcon.getIconWidth(), baseIcon.getIconHeight(), null);

        int startX = (int)(baseIcon.getIconWidth() * cutOff);
        int startY = (int)(baseIcon.getIconHeight() * cutOff);
        int width = (int)(baseIcon.getIconWidth() * (1.0 - 2 * cutOff));
        int height = (int)(baseIcon.getIconHeight() * (1.0 - 2 * cutOff));

        double scaleX = (double)overlayIcon.getIconWidth() / (double) width;
        double scaleY = (double)overlayIcon.getIconHeight() / (double) height;
        double scale = Math.max(scaleX, scaleY);
        int iconWidth = (int) (overlayIcon.getIconWidth() / scale);
        int iconHeight = (int) (overlayIcon.getIconHeight() / scale);

        startX += (width - iconWidth) / 2;
        startY += (height - iconHeight) / 2;

        g2.drawImage(overlayImg, startX, startY, iconWidth, iconHeight,null);
        g2.dispose();
        ImageIcon imageIcon = new ImageIcon();
        imageIcon.setImage(resImg);
        return imageIcon;
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
        int iconWidth = (int) (ratio * imageIcon.getIconWidth());
        int iconHeight = (int) (ratio * imageIcon.getIconHeight());
        Image srcImg = imageIcon.getImage();
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, (width - iconWidth) / 2, (height - iconHeight) / 2, iconWidth, iconHeight, null);
        g2.dispose();
        imageIcon.setImage(resizedImg);
    }

}
