package folder.file_preview;

import folder.IFolder;
import thirdparty.IOUtils;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class FilePreviewImage implements IFilePreview {

    @Override
    public ImageIcon getFilePreview(IFolder file) {
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
}
