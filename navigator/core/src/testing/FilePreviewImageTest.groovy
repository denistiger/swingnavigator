package testing

import folder_management.FolderManager
import folder.IFolder
import file_preview.FilePreviewImage

import javax.swing.ImageIcon

class FilePreviewImageTest extends GroovyTestCase {
    void testGetFilePreviewImageInZipOnFTP() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://127.0.0.1:2121/folder/folder_in.zip");
        List<IFolder> folders = manager.getFoldersAtPath();
        assertEquals("Folders count in folder_in.zip is 5", 5, folders.size());
        for (IFolder folder : folders) {
            if (folder.type == IFolder.FolderTypes.IMAGE) {
                FilePreviewImage imagePreview = new FilePreviewImage();
                ImageIcon icon = imagePreview.getFilePreviewSmall(folder);
                assertTrue ("Icon is not null", icon != null);
//                assertEquals ("Icon height is 360", 360, icon.getIconHeight());
            }

        }
    }

    void testGetFilePreviewImageInZip() {
        FolderManager manager = new FolderManager();
        manager.openPath("../../testData/folder/folder_in.zip");
        List<IFolder> folders = manager.getFoldersAtPath();
        assertEquals("Folders count in folder_in.zip is 5", 5, folders.size());
        for (IFolder folder : folders) {
            if (folder.type == IFolder.FolderTypes.IMAGE) {
                FilePreviewImage imagePreview = new FilePreviewImage();
                ImageIcon icon = imagePreview.getFilePreviewSmall(folder);
                assertTrue ("Icon is not null", icon != null);
//                assertEquals ("Icon height is 360", 360, icon.getIconHeight());
            }

        }
    }

    void testGetFilePreviewImage() {
        FolderManager manager = new FolderManager();
        manager.openPath("../../testData/folder/top2");
        List<IFolder> folders = manager.getFoldersAtPath();
        assertEquals("Folders count in top0 folder is 6", 6, folders.size());
        for (IFolder folder : folders) {
            if (folder.type == IFolder.FolderTypes.IMAGE) {
                FilePreviewImage imagePreview = new FilePreviewImage();
                ImageIcon icon = imagePreview.getFilePreviewSmall(folder);
                assertTrue ("Icon is not null", icon != null);
//                assertEquals ("Icon height is 360", 360, icon.getIconHeight());
            }

        }
    }

}
