package folder.file_preview;

import folder.IFolder;

import javax.swing.*;
import java.net.URL;

public class FilePreviewNoPreview implements IFilePreview {

    private final ImageIcon imageIcon;

    public FilePreviewNoPreview(URL fileName) {
        imageIcon = new ImageIcon(fileName);
        ImageUtils.resizeImageIcon(imageIcon, ICON_WIDTH, ICON_HEIGHT);
    }
    @Override
    public ImageIcon getFilePreview(IFolder file) {
        return imageIcon;
    }
}
