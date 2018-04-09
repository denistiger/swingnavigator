package folder.file_preview;

import folder.IFolder;

import javax.swing.*;
import java.net.URL;

public class FilePreviewNoPreview implements IFilePreview {

    private final ImageIcon imageIcon;
    private final ImageIcon imageIconLarge;

    public FilePreviewNoPreview(URL fileName) {
        imageIcon = new ImageIcon(fileName);
        imageIconLarge = new ImageIcon(fileName);
        ImageUtils.resizeImageIcon(imageIcon, ICON_WIDTH, ICON_HEIGHT);
        ImageUtils.resizeImageIcon(imageIconLarge, LARGE_ICON_WIDTH, LARGE_ICON_HEIGHT);
    }
    @Override
    public ImageIcon getFilePreview(IFolder file) {
        return imageIcon;
    }

    @Override
    public ImageIcon getFilePreviewLarge(IFolder file) {
        return imageIconLarge;
    }
}
