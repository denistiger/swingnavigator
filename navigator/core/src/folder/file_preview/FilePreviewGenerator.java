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
        return LAZY_PREVIEW.getFilePreviewSmall(file);
    }

    public ImageIcon getEmptyFolderPreview() {
        return FILE_PREVIEW_MAP.get(IFolder.FolderTypes.FOLDER).getFilePreviewSmall(null);
    }

    private IFilePreview getFilePreviewGenerator(IFolder file) {
        if (file == null) {
            return DEFAULT_PREVIEW;
        }
        IFilePreview filePreview = FILE_PREVIEW_MAP.get(file.getType());
        if (filePreview != null) {
            return filePreview;
        }
        return DEFAULT_PREVIEW;
    }

    @Override
    public ImageIcon getFilePreviewSmall(IFolder file) {
        return getFilePreviewGenerator(file).getFilePreviewSmall(file);
    }

    @Override
    public ImageIcon getFilePreviewLarge(IFolder file) {
        return getFilePreviewGenerator(file).getFilePreviewLarge(file);
    }

}
