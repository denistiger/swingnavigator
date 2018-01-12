package folder.testing

import folder.FolderManager
import folder.IFolder

class FolderManagerTest extends GroovyTestCase {
    void testOpenPath() {
        FolderManager manager = new FolderManager();
        manager.openPath("../../testData/folder");

        List<IFolder> folders = manager.getFoldersAtPath();
        String res = "";
        for (IFolder folder : folders) {
            res += folder.getName() + " ";
        }
        String origin = "top0 top1 top2 top3 1.jpg 2.jpg 3.jpg 4.jpg folder_in.zip ";
        assertEquals("Check if files list is equal", origin, res);

        manager.openFolder(folders.get(2));
        folders = manager.getFoldersAtPath();
        res = "";
        for (IFolder folder : folders) {
            res += folder.getName() + " ";
        }
        origin = "sub0 sub1 sub2 2.jpg 3.jpg top2.zip ";
        assertEquals("Go one level deeper", origin, res);
    }

    void testOpenFTPPath() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://anonymous@127.0.0.1:2121");
        manager.openFolder(manager.getFoldersAtPath().get(0));

        List<IFolder> folders = manager.getFoldersAtPath();
        String res = "";
        for (IFolder folder : folders) {
            res += folder.getName() + " ";
        }
        String origin = "top0 top1 top2 top3 1.jpg 2.jpg 3.jpg 4.jpg folder_in.zip ";
        assertEquals("Check if files list is equal", origin, res);

        manager.openFolder(folders.get(2));
        folders = manager.getFoldersAtPath();
        res = "";
        for (IFolder folder : folders) {
            res += folder.getName() + " ";
        }
        origin = "sub0 sub1 sub2 2.jpg 3.jpg top2.zip ";
        assertEquals("Go one level deeper", origin, res);
    }

    void checkOpenFTPPathWithFolder(FolderManager manager) {
        List<IFolder> folders = manager.getFoldersAtPath();
        String res = "";
        for (IFolder folder : folders) {
            res += folder.getName() + " ";
        }
        String origin = "top0 top1 top2 top3 1.jpg 2.jpg 3.jpg 4.jpg folder_in.zip ";
        assertEquals("Check if files list is equal", origin, res);

        manager.openFolder(folders.get(2));
        folders = manager.getFoldersAtPath();
        res = "";
        for (IFolder folder : folders) {
            res += folder.getName() + " ";
        }
        origin = "sub0 sub1 sub2 2.jpg 3.jpg top2.zip ";
        assertEquals("Go one level deeper", origin, res);
    }

    void testFTPErrors() {
        FolderManager manager = new FolderManager();
        FolderManager.OpenFolderStatus status = manager.openPath("ftp://127.0.0.1:2122/folder");
        assertEquals("Return status for closed port", FolderManager.OpenFolderStatus.FTP_CONNECTION_ERROR, status);
        status = manager.openPath("ftp://127.0.0.1:2121/folder123");
        assertEquals("Return status for wrong folder", FolderManager.OpenFolderStatus.SUCCESS, status);
        List<IFolder> folders = manager.getFoldersAtPath();
        assertEquals("Folders list for missing folder should be null", null, folders);
        status = manager.openPath("ftp://127.0.0.1:2121/folder");
        assertEquals("Return status for correct folder", FolderManager.OpenFolderStatus.SUCCESS, status);
        status = manager.openPath("ftp://user:123213@127.0.0.1:2121/folder");
        assertEquals("Return status for wrong credentials", FolderManager.OpenFolderStatus.FTP_CREDENTIALS_NEEDED, status);
    }

    void testFTPNoCredentials() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://127.0.0.1:2121/folder");
        checkOpenFTPPathWithFolder(manager);
    }

    void testOpenFTPPathMultiple() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://anonymous@127.0.0.1:2121/folder");
        checkOpenFTPPathWithFolder(manager);
        manager.openPath("../../testData");
        manager.openPath("ftp://anonymous@127.0.0.1:2121/folder");
        checkOpenFTPPathWithFolder(manager);
        manager.openPath("../../testData/folder");
        manager.openPath("ftp://anonymous@127.0.0.1:2121/folder");
        checkOpenFTPPathWithFolder(manager);
        manager.openPath("../../testData/folder");
        manager.openPath("ftp://anonymous@127.0.0.1:2121/folder");
        manager.openPath("ftp://anonymous@127.0.0.1:2121/folder");
        checkOpenFTPPathWithFolder(manager);
    }

    void testOpenFTPPathWithFolder() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://anonymous@127.0.0.1:2121/folder");
        checkOpenFTPPathWithFolder(manager);
    }
}