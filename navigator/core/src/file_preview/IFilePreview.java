package file_preview;

import folder.IFolder;

import javax.swing.ImageIcon;

interface IFilePreview {
    int ICON_WIDTH = (int)(96 * 1.5);
    int ICON_HEIGHT = (int)(96 * 1.5);
    ImageIcon getFilePreviewSmall(IFolder file);
}
