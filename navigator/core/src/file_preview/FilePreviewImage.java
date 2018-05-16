package file_preview;

import folder.IFolder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FilePreviewImage implements IFilePreview {

    public ImageIcon readImage(IFolder file) {
        InputStream inputStream = file.getInputStream();
        try {
            Image image = ImageIO.read(inputStream);
            inputStream.close();
            if (image == null) {
                System.err.println("No image for " + file.getAbsolutePath());
                return null;
            }
            return new ImageIcon(image);
        } catch (IOException e) {
            System.err.println("Fail with icon for file " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ImageIcon getFilePreviewSmall(IFolder file) {
        ImageIcon imageIcon = readImage(file);
        if (imageIcon != null) {
            ImageUtils.resizeImageIconProportional(imageIcon, ICON_WIDTH, ICON_HEIGHT);
        }
        return imageIcon;
    }

}
