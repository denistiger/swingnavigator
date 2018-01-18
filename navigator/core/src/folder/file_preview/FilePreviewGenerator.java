package folder.file_preview;

import folder.IFolder;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class FilePreviewGenerator implements IFilePreview {
    private static final Map<IFolder.FolderTypes, IFilePreview> FILE_PREVIEW_MAP;
    private static final IFilePreview DEFAULT_PREVIEW;
    static {
        FILE_PREVIEW_MAP = new HashMap<>();
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.IMAGE, new FilePreviewImage());
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.TEXT_FILE, new FilePreviewText());
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.ZIP,
                new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/ZipIcon512.png")));
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.FOLDER,
                new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/FolderIcon512.png")));
        DEFAULT_PREVIEW = new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/Question512.png"));
    }

    @Override
    public ImageIcon getFilePreview(IFolder file) {
        IFilePreview filePreview = FILE_PREVIEW_MAP.get(file.getType());
        if (filePreview != null) {
            ImageIcon imageIcon = filePreview.getFilePreview(file);
            if (imageIcon != null) {
                ImageUtils.resizeImageIcon(imageIcon, ICON_WIDTH, ICON_HEIGHT);
                return imageIcon;
            }
        }
        return DEFAULT_PREVIEW.getFilePreview(file);
    }

}
