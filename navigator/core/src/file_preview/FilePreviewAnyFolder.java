package file_preview;

import folder.IFolder;

import javax.swing.*;
import java.util.List;

public class FilePreviewAnyFolder implements IFilePreview {

    private IFilePreview defaultFilePreviewGenerator;
    private static IFilePreview imagePreviewGenerator = new FilePreviewImage();

    FilePreviewAnyFolder(IFilePreview defaultFilePreviewGenerator) {
        this.defaultFilePreviewGenerator = defaultFilePreviewGenerator;
    }

    @Override
    public ImageIcon getFilePreviewSmall(IFolder file) {
        ImageIcon basePreview = defaultFilePreviewGenerator.getFilePreviewSmall(file);
        if (file == null) {
            return basePreview;
        }
        List<IFolder> folders = file.getItems();
        if (folders == null) {
            return basePreview;
        }
        ImageIcon imagePreview = null;

        if (folders.size() > 5) {
            IFolder folder = folders.get(folders.size() / 2);
            if (folder.getType() == IFolder.FolderTypes.IMAGE) {
                imagePreview = imagePreviewGenerator.getFilePreviewSmall(folder);
            }
        }

        if (imagePreview == null) {
            for (IFolder folder : folders) {
                if (folder.getType() == IFolder.FolderTypes.IMAGE) {
                    imagePreview = imagePreviewGenerator.getFilePreviewSmall(folder);
                    break;
                }
            }
        }
        if (imagePreview == null) {
            return basePreview;
        }
        return ImageUtils.combinedIcon(basePreview, imagePreview);
    }
}
