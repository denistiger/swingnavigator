package testing

import file_preview.ImageUtils
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

    void testBMPImagePreview() {
        FolderManager manager = new FolderManager();
        manager.openPath("../../testImages/");
        List<IFolder> folders = manager.getFoldersAtPath();
        assertEquals("Images count is 15", 15, folders.size());
        for (IFolder folder : folders) {
            assert(folder.type == IFolder.FolderTypes.IMAGE);
            FilePreviewImage imagePreview = new FilePreviewImage();
            ImageIcon icon = imagePreview.readImage(folder);
            assertTrue ("Icon is not null", icon != null);
            assertTrue ("Icon 64+ pixels width and height",
                    icon.getIconWidth() >= 64 && icon.getIconHeight() >= 64);
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
