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

    void testOpenFTPPathWithFolder() {
        FolderManager manager = new FolderManager();
        manager.openPath("ftp://anonymous@127.0.0.1:2121/folder");

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
}