package file_preview;

import folder.IFolder;
import thirdparty.IOUtils;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class FilePreviewImage implements IFilePreview {

    private ImageIcon readImage(IFolder file) {
        InputStream inputStream = file.getInputStream();
        try {
            byte[] imageData = IOUtils.readFully(inputStream, -1, true);
            inputStream.close();
            ImageIcon imageIcon = new ImageIcon(imageData);
            return imageIcon;
        } catch (IOException e) {
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
