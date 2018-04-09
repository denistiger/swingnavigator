package folder.file_preview;

import folder.IFolder;

import javax.swing.ImageIcon;

interface IFilePreview {
    int ICON_WIDTH = 64;
    int ICON_HEIGHT = 64;
    int LARGE_ICON_WIDTH = 1024;
    int LARGE_ICON_HEIGHT = 1024;
    ImageIcon getFilePreview(IFolder file);
    ImageIcon getFilePreviewLarge(IFolder file);
}
