package folder.file_preview;

import folder.IFolder;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class FilePreviewGenerator implements IFilePreview {
    private static final Map<IFolder.FolderTypes, IFilePreview> FILE_PREVIEW_MAP;
    private static final IFilePreview DEFAULT_PREVIEW, LAZY_PREVIEW;
    static {
        FILE_PREVIEW_MAP = new HashMap<>();
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.IMAGE, new FilePreviewImage());
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.TEXT_FILE, new FilePreviewText());
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.ZIP,
                new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/ZipIcon512.png")));
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.FOLDER,
                new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/FolderIcon512.png")));
        DEFAULT_PREVIEW = new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/Question512.png"));
        LAZY_PREVIEW = new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/ClockIcon512.png"));
    }

    public ImageIcon getLazyLoadIcon(IFolder file) {
        return LAZY_PREVIEW.getFilePreview(file);
    }

    private ImageIcon getFilePreviewImageIcon(IFolder file) {
        IFilePreview filePreview = FILE_PREVIEW_MAP.get(file.getType());
        if (filePreview != null) {
            return filePreview.getFilePreview(file);
        }
        return DEFAULT_PREVIEW.getFilePreview(file);
    }

    @Override
    public ImageIcon getFilePreview(IFolder file) {
        ImageIcon imageIcon = getFilePreviewImageIcon(file);
        if (imageIcon != null) {
            ImageUtils.resizeImageIconProportional(imageIcon, ICON_WIDTH, ICON_HEIGHT);
        }
        return imageIcon;
    }

    @Override
    public ImageIcon getFilePreviewLarge(IFolder file) {
        ImageIcon imageIcon = getFilePreviewImageIcon(file);
        if (imageIcon != null) {
            ImageUtils.resizeImageIconProportional(imageIcon, LARGE_ICON_WIDTH, LARGE_ICON_HEIGHT);
        }
        return imageIcon;
    }

}
