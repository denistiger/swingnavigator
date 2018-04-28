package folder.testing

import folder.IFolder
import folder.factory.IFolderFactory
import folder.factory.UniversalFolderFactory
import folder.ftp_folder.FTPClientWrapper
import folder.ftp_folder.FTPFolder

class FTPFolderTest extends GroovyTestCase {

    FTPFolder createFTP(String path) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(IFolderFactory.FILEPATH, path);
        return new UniversalFolderFactory().createIFolder( params );
    }

    void testLevelUp() {
        FTPFolder ftpFolder = createFTP("ftp://127.0.0.1:2121");
//        assertTrue("FTP Connected", ftpFolder.connect() == FTPClientWrapper.FTPStatus.SUCCESS);
        assertTrue("Raw ftp address does not have parent", ftpFolder.levelUp() == null);
    }

    void testLevelUp1() {
        FTPFolder ftpFolder = createFTP("ftp://127.0.0.1:2121/");
//        assertTrue("FTP Connected", ftpFolder.connect() == FTPClientWrapper.FTPStatus.SUCCESS);
        assertTrue("Raw ftp address does not have parent", ftpFolder.levelUp() == null);
    }

    void testLevelUp2() {
        FTPFolder ftpFolder = createFTP("ftp://127.0.0.1:2121//");
//        assertTrue("FTP Connected", ftpFolder.connect() == FTPClientWrapper.FTPStatus.SUCCESS);
        assertTrue("Raw ftp address does not have parent", ftpFolder.levelUp() == null);
    }

    void testLevelUpFromFolder() {
        FTPFolder ftpFolder = createFTP("ftp://127.0.0.1:2121/folder/top2");
//        assertTrue("FTP Connected", ftpFolder.connect() == FTPClientWrapper.FTPStatus.SUCCESS);
        FTPFolder folder = ftpFolder.levelUp();
        assertEquals("Level up folder is folder", "folder", folder.getName());
        folder = folder.levelUp();
        assertEquals("Level up folder is folder", "", folder.getName());
    }

}
