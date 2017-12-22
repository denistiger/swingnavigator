package folder

class FolderFactoryTest extends groovy.util.GroovyTestCase {
    void setUp() {
        super.setUp()
    }

    String drawFolderTreeTest(IFolder iFolder) {
        String res = "(" + iFolder.getName() + " : " + iFolder.getType() + ")"
        List<IFolder> list = iFolder.getItems()
        if (list == null || list.size() == 0) {
            return res
        }
        String inner = "{"
        for (IFolder folder : list) {
            inner += " " + drawFolderTreeTest(folder)
        }
        inner += " }"
        return "[ " + res + " has " + inner + " ]"
    }

    String linuxFormat(IFolder folder, String prefix, boolean iterateInZip) {
        String res = "." + prefix + ":\n"
        if (folder == null) {
            return res + "null\n";
        }
        List<IFolder> items = folder.getItems()
        if (items == null) {
            return res
        }
        items.sort(new Comparator<IFolder>(){
            @Override
            int compare(IFolder iFolder, IFolder t1) {
                return iFolder.getName().compareTo(t1.getName())
            }
        })
        boolean first = true
        for (IFolder item : items) {
            if (first) {
                res += item.getName()
                first = false
            }
            else {
                res += "  " + item.getName()
            }
        }
        res += "\n"
        if (items.size() > 0) {
            res += "\n"
        }
        for (IFolder item : items) {
            if (iterateInZip ? FileTypeGetter.isFolderType(item.getType()) : item.getType() == IFolder.FolderTypes.FOLDER) {
                res += linuxFormat(item, prefix + "/" + item.getName(), iterateInZip)
            }
        }
        return res
    }

    IFolder getByName(IFolder folder, String name) {
        List<IFolder> inner = folder.getItems();
        if (inner == null || inner.size() == 0) {
            return null
        }
        for (IFolder fold : inner) {
            if (fold.getName() == name) {
                return fold
            }
        }
        return null
    }

    String getTestFile(String path){
        File base = new File(path)
        Scanner in_data = new Scanner(base)
        String origin = ""
        while (in_data.hasNext()) {
            String tmp = in_data.nextLine()
            origin += tmp + "\n"
        }
        origin += "\n"
        return origin
    }

    boolean checkFileHasNoChildren(IFolder folder) {
        if (!FileTypeGetter.isFolderType(folder.getType())) {
            if (folder.getItems() != null) {
                return false
            }
            return true
        }
        for (IFolder item : folder.getItems()) {
            if (!checkFileHasNoChildren(item)) {
                return false
            }
        }
        return true
    }

    void testFTPNonZip() {
        FTPFolder iFolder = new FTPFolder("127.0.0.1")
        iFolder.setCredentials("anonymous","")
        iFolder.connect()
        String res = linuxFormat(iFolder, "", false)
        String origin = getTestFile("../../testOutput/folder.txt")
        Error e = null
        try {
            assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder))
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
        FTPFolder iFolder = new FTPFolder("127.0.0.1")
        iFolder.setCredentials("anonymous","")
        iFolder.connect()
        String res = linuxFormat(getByName(iFolder, "folder.zip"), "", false)
        String origin = getTestFile("../../testOutput/folderZip.txt")
        Error e = null
        try {
            assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder))
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
        params.put(IFolderFactory.FILESTRING, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        String res = linuxFormat(iFolder, "", false)
        String origin = getTestFile("../../testOutput/folder.txt")
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder))
        assertEquals("Check folder structure no Zip", origin, res)
    }

    IFolder getUnZipFolder() {
        File file1 = new File("../../testOutput/folder_un_zip")
        Map<String, Object> params1 = new HashMap<String, Object>()
        params1.put(IFolderFactory.FILESTRING, file1)
        return new FolderFactory().createIFolder(params1)
    }

    void testFolderNonZipArch() {
        File file = new File("../../testData/folder")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILESTRING, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        String res = linuxFormat(iFolder, "", true)
        IFolder iFolder1 = getUnZipFolder()
        String origin = linuxFormat(iFolder1, "", true)
        assertTrue("There are no files in files for res.", checkFileHasNoChildren(iFolder))
        assertTrue("There are no files in files for origin.", checkFileHasNoChildren(iFolder1))
        assertEquals("Check folder structure with Zip", origin, res)
    }

    void testTele2FTP() {
        FTPFolder iFolder = new FTPFolder("speedtest.tele2.net", 21)
        iFolder.setCredentials("anonymous","")
        iFolder.connect()
        String res = linuxFormat(iFolder, "", true)
        System.out.println(res)
        iFolder.disconnect()
    }

    void testFTPNonZipArch() {
        FTPFolder iFolder = new FTPFolder("127.0.0.1")
        iFolder.setCredentials("anonymous","")
        iFolder.connect()
        String res = linuxFormat(getByName(iFolder, "folder"), "", true)
        IFolder iFolder1 = getUnZipFolder()
        String origin = linuxFormat(iFolder1, "", false)
        Error e = null
        try {
            assertTrue("There are no files in files for res.", checkFileHasNoChildren(iFolder))
            assertTrue("There are no files in files for origin.", checkFileHasNoChildren(iFolder1))
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
        params.put(IFolderFactory.FILESTRING, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        String res = linuxFormat(getByName(iFolder, "folder.zip"), "", false)
        String origin = getTestFile("../../testOutput/folderZip.txt")
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder))
        assertEquals("Check folder structure in folder.zip", origin, res)
    }

    void testCreateIFolderList() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILESTRING, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder))
        IFolder sub0 = getByName(getByName(getByName(getByName(iFolder, "folder.zip"), "folder"), "top0"), "sub0")
        assertEquals("Sub0 contains three folders", 3, sub0.getItems().size())
    }
}
