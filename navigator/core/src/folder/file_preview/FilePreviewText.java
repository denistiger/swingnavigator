package folder.file_preview;

import folder.IFolder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilePreviewText implements IFilePreview {

    private class Point2i {
        public int width, height;
        Point2i (int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    private String[] getText(IFolder file) {
        InputStream inputStream = file.getInputStream();
        final int step = 20;
        final int linesCount = 15;
        byte[] data = new byte[step * (linesCount + 5)];
        try {
            int readBytes = inputStream.read(data);
            data = Arrays.copyOf(data, readBytes);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
        String origin = new String(data);
        origin = origin.replace("\r", "");
        String[] originLines = origin.split("\n");
        List<String> resLines = new ArrayList<>();
        for (String st : originLines) {
            while (st.length() > 0 && resLines.size() < linesCount) {
                resLines.add(st.substring(0, Math.min(step, st.length())));
                st = st.substring(Math.min(step, st.length()));
            }
            if (resLines.size() == linesCount) {
                break;
            }
        }
        String[] res = new String[resLines.size()];
        resLines.toArray(res);
        return res;
    }

    private Point2i getMaxTextDimensions(String[] text, Font font) {
        int maxWidth = 1;
        BufferedImage imgTmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgTmp.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int maxHeight = fm.getHeight();
        for (String line : text) {
            int width = fm.stringWidth(line);
            maxWidth = Math.max(maxWidth, width);
        }
        g2d.dispose();
        return new Point2i(maxWidth, maxHeight);
    }

    private ImageIcon getFileImageIcon(IFolder file) {
        String[] text = getText(file);
        Font font = new Font("Arial", Font.PLAIN, 12);
        Point2i maxLineSize = getMaxTextDimensions(text, font);
        int lineHeight = maxLineSize.height;
        int lineWidth = maxLineSize.width;
        int heightInterval = 1 + lineHeight / 4;
        int widthBorder = heightInterval;
        int heightStep = heightInterval + lineHeight;
        int totalHeight = heightInterval + heightStep * text.length;

        BufferedImage img = new BufferedImage(2 * widthBorder + lineWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.fill(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < text.length; ++i) {
            g2d.drawString(text[i], widthBorder, fm.getAscent() + heightInterval + heightStep * i);
        }
        g2d.dispose();
        return new ImageIcon(img);
    }
    @Override
    public ImageIcon getFilePreview(IFolder file) {
        ImageIcon imageIcon = getFileImageIcon(file);
        ImageUtils.resizeImageIconProportional(imageIcon, ICON_WIDTH, ICON_HEIGHT);
        return imageIcon;
    }

    @Override
    public ImageIcon getFilePreviewLarge(IFolder file) {
        ImageIcon imageIcon = getFileImageIcon(file);
        ImageUtils.resizeImageIconProportional(imageIcon, LARGE_ICON_WIDTH, LARGE_ICON_HEIGHT);
        return imageIcon;
    }
}
