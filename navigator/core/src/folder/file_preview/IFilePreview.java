package folder.file_preview;

import folder.IFolder;

import javax.swing.ImageIcon;

interface IFilePreview {
    ImageIcon getFilePreview(IFolder file);
}
