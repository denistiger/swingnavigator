package folder.testing

import folder.PasswordManager
import folder.ftp_folder.FTPFolder
import folder.factory.FolderFactory
import folder.IFolder
import folder.factory.IFolderFactory

class FolderStructureTest extends groovy.util.GroovyTestCase {
    void setUp() {
        super.setUp()
    }

    void testFTPNonZip() {
        FTPFolder iFolder = new FTPFolder("127.0.0.1", 2121)
        PasswordManager passwordManager = new PasswordManager();
        iFolder.setPasswordManager(passwordManager);
        iFolder.connect()
        String res = TestUtils.linuxFormat(iFolder, "", false)
        String origin = TestUtils.getTestFile("../../testOutput/folder.txt")
        Error e = null
        try {
            assertTrue("There are no files in files.", TestUtils.checkFileHasNoChildren(iFolder))
            assertEquals("Check folder structure no Zip", origin, res)
        }catch (Error er) {
            e = er
        }
        finally {
            iFolder.disconnect()
            if (e != null) {
                throw e
            }
        }
    }

    void testFTPZip() {
        FTPFolder iFolder = new FTPFolder("127.0.0.1", 2121)
        PasswordManager passwordManager = new PasswordManager();
        iFolder.setPasswordManager(passwordManager);
        iFolder.connect()
        String res = TestUtils.linuxFormat(TestUtils.getByName(iFolder, "folder.zip"), "", false)
        String origin = TestUtils.getTestFile("../../testOutput/folderZip.txt")
        Error e = null
        try {
            assertTrue("There are no files in files.", TestUtils.checkFileHasNoChildren(iFolder))
            assertEquals("Check folder structure in folder.zip", origin, res)
        }catch (Error er) {
            e = er
        }
        finally {
            iFolder.disconnect()
            if (e != null) {
                throw e
            }
        }
    }

    void testFolderNonZip() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILE, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        String res = TestUtils.linuxFormat(iFolder, "", false)
        String origin = TestUtils.getTestFile("../../testOutput/folder.txt")
        assertTrue("There are no files in files.", TestUtils.checkFileHasNoChildren(iFolder))
        assertEquals("Check folder structure no Zip", origin, res)
    }

    IFolder getUnZipFolder() {
        File file1 = new File("../../testOutput/folder_un_zip")
        Map<String, Object> params1 = new HashMap<String, Object>()
        params1.put(IFolderFactory.FILE, file1)
        return new FolderFactory().createIFolder(params1)
    }

    void testFolderNonZipArch() {
        File file = new File("../../testData/folder")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILE, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        String res = TestUtils.linuxFormat(iFolder, "", true)
        IFolder iFolder1 = getUnZipFolder()
        String origin = TestUtils.linuxFormat(iFolder1, "", true)
        assertTrue("There are no files in files for res.", TestUtils.checkFileHasNoChildren(iFolder))
        assertTrue("There are no files in files for origin.", TestUtils.checkFileHasNoChildren(iFolder1))
        assertEquals("Check folder structure with Zip", origin, res)
    }

//    void testTele2FTP() {
//        FTPFolder iFolder = new FTPFolder("speedtest.tele2.net", 21)
//        PasswordManager passwordManager = new PasswordManager();
//        iFolder.setPasswordManager(passwordManager);
//        iFolder.connect()
//        String res = linuxFormat(iFolder, "", true)
//        System.out.println(res)
//        iFolder.disconnect()
//    }

    void testFTPNonZipArch() {
        FTPFolder iFolder = new FTPFolder("127.0.0.1", 2121)
        PasswordManager passwordManager = new PasswordManager();
        iFolder.setPasswordManager(passwordManager);
        iFolder.connect()
        String res = TestUtils.linuxFormat(TestUtils.getByName(iFolder, "folder"), "", true)
        IFolder iFolder1 = getUnZipFolder()
        String origin = TestUtils.linuxFormat(iFolder1, "", false)
        Error e = null
        try {
            assertTrue("There are no files in files for res.", TestUtils.checkFileHasNoChildren(iFolder))
            assertTrue("There are no files in files for origin.", TestUtils.checkFileHasNoChildren(iFolder1))
            assertEquals("Check folder structure no Zip", origin, res)
        }catch (Error er) {
            e = er
        }
        finally {
            iFolder.disconnect()
            if (e != null) {
                throw e
            }
        }
    }


    void testFolderZip() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILE, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        String res = TestUtils.linuxFormat(TestUtils.getByName(iFolder, "folder.zip"), "", false)
        String origin = TestUtils.getTestFile("../../testOutput/folderZip.txt")
        assertTrue("There are no files in files.", TestUtils.checkFileHasNoChildren(iFolder))
        assertEquals("Check folder structure in folder.zip", origin, res)
    }

    void testCreateIFolderList() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILE, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        assertTrue("There are no files in files.", TestUtils.checkFileHasNoChildren(iFolder))
        IFolder sub0 = TestUtils.getByName(TestUtils.getByName(TestUtils.getByName(TestUtils.getByName(iFolder, "folder.zip"), "folder"), "top0"), "sub0")
        assertEquals("Sub0 contains three folders", 3, sub0.getItems().size())
    }
}
