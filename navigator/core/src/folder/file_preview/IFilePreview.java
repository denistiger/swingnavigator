package folder.file_preview;

import folder.IFolder;

import javax.swing.ImageIcon;

interface IFilePreview {
    int ICON_WIDTH = 64;
    int ICON_HEIGHT = 64;
    ImageIcon getFilePreviewSmall(IFolder file);
}
