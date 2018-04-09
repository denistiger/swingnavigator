package folder.testing

import folder.FolderManager
import folder.IFolder

import javax.swing.filechooser.FileSystemView

class FolderManagerTest extends GroovyTestCase {

    void testCorrectRetPath() {
        FolderManager manager = new FolderManager();
        String path = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
        manager.openPath(path);
        assertEquals("Current path equals open path", path, manager.getFullPath());
        manager.levelUp();
        assertEquals("Current path level up equals open path", path.substring(0, path.lastIndexOf("/")),
                manager.getFullPath());
        manager.levelUp();
        assertEquals("Current path level up equals open path", "/", manager.getFullPath());
    }

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


    void testOpenFolderInZipOnFTP() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://127.0.0.1:2121/folder/folder_in.zip/top2");
        assertEquals("Opened folder in zip that is placed on FTP.", "ftp://127.0.0.1:2121/folder/folder_in.zip/top2", manager.getFullPath());
    }


//    void testOpenZipPath() {
//        FolderManager manager = new FolderManager();
//        manager.openPath("../../testData/folder");
//        manager.openFolder("folder_in.zip");
//        manager.openFolder("top2");
//        assertEquals("Absolute path with folder in zip",
//                "/home/denis/IdeaProjects/swingnavigator/testData/folder/folder_in.zip/top2",
//                        manager.getFullPath());
//    }


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

        FolderManager.OpenFolderStatus status;
        status = manager.openPath("ftp://127.0.0.1:2121/folder/top2");
        assertEquals("Return status for correct folder", FolderManager.OpenFolderStatus.SUCCESS, status);
        assertEquals("Folder top2 contains six files", 6, manager.getFoldersAtPath().size());

        status = manager.openPath("ftp://127.0.0.1:2121/folder/top3");
        assertEquals("Return status for correct folder", FolderManager.OpenFolderStatus.SUCCESS, status);
        assertEquals("Folder top3 contains one file", 1, manager.getFoldersAtPath().size());

        status = manager.openPath("ftp://127.0.0.1:2121/folder/folder_in.zip");
        assertEquals("Return status for correct folder", FolderManager.OpenFolderStatus.SUCCESS, status);
        assertEquals("Folder folder_in.zip contains five files", 5, manager.getFoldersAtPath().size());

    }

    void testLocalPathSlash() {
        FolderManager manager = new FolderManager();
        manager.openPath("../../testData/folder///top0///");
        String localPath = manager.getFullPath();
        while (localPath.contains("//")) {
            localPath = localPath.replaceAll("//", "/");
        }
        assertEquals("No additional slash should be in path", localPath, manager.getFullPath());
    }


    void testFTPPathSlash() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://127.0.0.1:2121/");
        assertEquals("No additional slash should be in path", "ftp://127.0.0.1:2121", manager.getFullPath());
        manager.openPath("ftp://127.0.0.1:2121/folder///top1/");
        assertEquals("Middle slash in ftp path should be cut off", "ftp://127.0.0.1:2121/folder/top1", manager.getFullPath());
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
        assertEquals("Return status for wrong folder", FolderManager.OpenFolderStatus.HALF_PATH_OPENED, status);
        assertEquals("Opened only root ftp folder", "ftp://127.0.0.1:2121", manager.getFullPath());
        List<IFolder> folders = manager.getFoldersAtPath();
        assertEquals("Folders list for missing folder should be from the first existing level", 2, folders.size());
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

    void testFTPLevelUp() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://127.0.0.1:2121/folder/top2/sub2");
        assertEquals("Start path for ftp.", "ftp://127.0.0.1:2121/folder/top2/sub2", manager.getFullPath());
        manager.levelUp();
        assertEquals("Level up step one path for ftp.", "ftp://127.0.0.1:2121/folder/top2", manager.getFullPath());
        manager.levelUp();
        assertEquals("Level up step two path for ftp.", "ftp://127.0.0.1:2121/folder", manager.getFullPath());
        manager.openFolder("top2");
        assertEquals("Level down step one path for ftp.", "ftp://127.0.0.1:2121/folder/top2", manager.getFullPath());
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