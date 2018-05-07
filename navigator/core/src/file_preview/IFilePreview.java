package file_preview;

import folder.IFolder;

import javax.swing.ImageIcon;

interface IFilePreview {
    int ICON_WIDTH = 96;
    int ICON_HEIGHT = 96;
    ImageIcon getFilePreviewSmall(IFolder file);
}
