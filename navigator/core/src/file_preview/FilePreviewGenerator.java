package file_preview;

import folder.IFolder;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class FilePreviewGenerator implements IFilePreview {
    private static final int MAX_CACHE_SIZE = 10000;
    private static final Map<IFolder.FolderTypes, IFilePreview> FILE_PREVIEW_MAP, LAZY_PREVEW_MAP;
    private static final IFilePreview DEFAULT_PREVIEW, LAZY_PREVIEW;
    private static final Map<String, ImageIcon> foldersCache = new HashMap<>();
    private static Semaphore semaphore = new Semaphore(1);
    static {
        FILE_PREVIEW_MAP = new HashMap<>();
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.IMAGE, new FilePreviewImage());
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.TEXT_FILE, new FilePreviewText());
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.ZIP,
                new FilePreviewAnyFolder(new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/ZipIcon512.png"))));
        FILE_PREVIEW_MAP.put(IFolder.FolderTypes.FOLDER,
                new FilePreviewAnyFolder(new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/FolderIcon512.png"))));
        DEFAULT_PREVIEW = new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/Question512.png"));
        LAZY_PREVIEW = new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/ClockIcon512.png"));

        LAZY_PREVEW_MAP = new HashMap<>();
        LAZY_PREVEW_MAP.put(IFolder.FolderTypes.IMAGE, LAZY_PREVIEW);
        LAZY_PREVEW_MAP.put(IFolder.FolderTypes.TEXT_FILE, LAZY_PREVIEW);
        LAZY_PREVEW_MAP.put(IFolder.FolderTypes.ZIP,
                new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/ZipIcon512.png")));
        LAZY_PREVEW_MAP.put(IFolder.FolderTypes.FOLDER,
                new FilePreviewNoPreview(FilePreviewGenerator.class.getResource("images/FolderIcon512.png")));
    }

    private void addToCache(IFolder file, ImageIcon imageIcon) {
        if (file == null || file.getAbsolutePath() == null) {
            return;
        }
        if (foldersCache.size() > MAX_CACHE_SIZE) {
            foldersCache.clear();
        }
        foldersCache.put(file.getAbsolutePath(), imageIcon);
    }

    private IFilePreview getLazyPreviewGenerator(IFolder file) {
        if (file == null) {
            return DEFAULT_PREVIEW;
        }
        IFilePreview filePreview = LAZY_PREVEW_MAP.get(file.getType());
        if (filePreview != null) {
            return filePreview;
        }
        return DEFAULT_PREVIEW;
    }

    public ImageIcon getLazyLoadIcon(IFolder file) {
        return getLazyPreviewGenerator(file).getFilePreviewSmall(file);
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
        try {
            semaphore.acquire();
            if (file != null && foldersCache.containsKey(file.getAbsolutePath())) {
                return foldersCache.get(file.getAbsolutePath());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
        ImageIcon imageIcon = getFilePreviewGenerator(file).getFilePreviewSmall(file);
        try {
            semaphore.acquire();
            addToCache(file, imageIcon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
        return imageIcon;
    }

}
