package folder.testing

import folder.FolderManager
import folder.IFolder
import folder.file_preview.FilePreviewImage

import javax.swing.ImageIcon

class FilePreviewImageTest extends GroovyTestCase {
    void testGetFilePreview() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://127.0.0.1:2121/folder/folder_in.zip");
        List<IFolder> folders = manager.getFoldersAtPath();
        assertEquals("Folders count in folder_in.zip is 5", 5, folders.size());
        for (IFolder folder : folders) {
            if (folder.type == IFolder.FolderTypes.IMAGE) {
                FilePreviewImage imagePreview = new FilePreviewImage();
                ImageIcon icon = imagePreview.getFilePreview(folder);
                assertTrue ("Icon is not null", icon != null);
                assertEquals ("Icon height is 360", 360, icon.getIconHeight());
            }

        }
    }
}
