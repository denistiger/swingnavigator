package folder

class FolderFactoryTest extends groovy.util.GroovyTestCase {
    void setUp() {
        super.setUp()
    }

    String drawFolderTreeTest(IFolder iFolder) {
        String res = "(" + iFolder.getName() + " : " + iFolder.getType() + ")";
        List<IFolder> list = iFolder.getItems();
        if (list == null || list.size() == 0) {
            return res;
        }
        String inner = "{";
        for (IFolder folder : list) {
            inner += " " + drawFolderTreeTest(folder);
        }
        inner += " }";
        return "[ " + res + " has " + inner + " ]";
    }

    String linuxFormat(IFolder folder, String prefix) {
        String res = "." + prefix + ":\n";
        List<IFolder> items = folder.getItems();
        if (items == null) {
            return res;
        }
        items.sort(new Comparator<IFolder>(){
            @Override
            int compare(IFolder iFolder, IFolder t1) {
                return iFolder.getName().compareTo(t1.getName());
            }
        });
        boolean first = true;
        for (IFolder item : items) {
            if (first) {
                res += item.getName();
                first = false;
            }
            else {
                res += "  " + item.getName();
            }
        }
        res += "\n";
        if (items.size() > 0) {
            res += "\n";
        }
        for (IFolder item : items) {
            if (item.getType() == IFolder.FolderTypes.FOLDER) {
                res += linuxFormat(item, prefix + "/" + item.getName());
            }
        }
        return res;
    }

    IFolder getByName(IFolder folder, String name) {
        List<IFolder> inner = folder.getItems();
        if (inner == null || inner.size() == 0) {
            return null;
        }
        for (IFolder fold : inner) {
            if (fold.getName() == name) {
                return fold;
            }
        }
        return null;
    }

    String getTestFile(String path){
        File base = new File(path);
        Scanner in_data = new Scanner(base);
        String origin = "";
        while (in_data.hasNext()) {
            String tmp = in_data.nextLine();
            origin += tmp + "\n";
        }
        origin += "\n";
        return origin;
    }

    boolean checkFileHasNoChildren(IFolder folder) {
        if (!FileTypeGetter.isFolderType(folder.getType())) {
            if (folder.getItems() != null) {
                return false;
            }
            return true;
        }
        for (IFolder item : folder.getItems()) {
            if (!checkFileHasNoChildren(item)) {
                return false;
            }
        }
        return true;
    }

    void testFTPNonZip() {
        FTPFolder iFolder = new FTPFolder("127.0.0.1");
        if (!iFolder.authenticated()) {
            iFolder.login("anonymous","");
        }
        assertTrue("FTP connection and authentication success", iFolder.authenticated());
        String res = linuxFormat(iFolder, "");
        String origin = getTestFile("../../testOutput/folder.txt");
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder));
        assertEquals("Check folder structure no Zip", origin, res);
    }

    void testFTPZip() {
        FTPFolder iFolder = new FTPFolder("127.0.0.1");
        if (!iFolder.authenticated()) {
            iFolder.login("anonymous","");
        }
        assertTrue("FTP connection and authentication success", iFolder.authenticated());
        String res = linuxFormat(getByName(iFolder, "folder.zip"), "");
        String origin = getTestFile("../../testOutput/folderZip.txt");
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder));
        assertEquals("Check folder structure in folder.zip", origin, res);
    }

    void testFolderNonZip() {
        File file = new File("../../testData");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(IFolderFactory.FILESTRING, file);
        IFolder iFolder = new FolderFactory().createIFolder(params);
        String res = linuxFormat(iFolder, "");
        String origin = getTestFile("../../testOutput/folder.txt");
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder));
        assertEquals("Check folder structure no Zip", origin, res);
    }

    void testFolderZip() {
        File file = new File("../../testData");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(IFolderFactory.FILESTRING, file);
        IFolder iFolder = new FolderFactory().createIFolder(params);
        String res = linuxFormat(getByName(iFolder, "folder.zip"), "");
        String origin = getTestFile("../../testOutput/folderZip.txt");
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder));
        assertEquals("Check folder structure in folder.zip", origin, res);
    }

    void testCreateIFolderList() {
        File file = new File("../../testData");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(IFolderFactory.FILESTRING, file);
        IFolder iFolder = new FolderFactory().createIFolder(params);
        assertTrue("There are no files in files.", checkFileHasNoChildren(iFolder));
        IFolder sub0 = getByName(getByName(getByName(getByName(iFolder, "folder.zip"), "folder"), "top0"), "sub0");
        assertEquals("Sub0 contains three folders", 3, sub0.getItems().size());
//        String res = drawFolderTreeTest(iFolder);
//        assertEquals("Check if folder structure ok. No recursive zip", testFolderStr, res);
    }
    String testFolderStr = "[ (testData : FOLDER) has { [ (folder : FOLDER) has { [ (top0 : FOLDER) has { [ (sub0 : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) (3.jpg : FILE) } ] [ (sub1 : FOLDER) has { (4.jpg : FILE) (5.jpg : FILE) (6.jpg : FILE) (7.jpg : FILE) } ] [ (sub2 : FOLDER) has { (1.jpg : FILE) (7.jpg : FILE) } ] (2.jpg : FILE) (4.jpg : FILE) (6.jpg : FILE) } ] [ (top2 : FOLDER) has { [ (sub0 : FOLDER) has { (2.jpg : FILE) (3.jpg : FILE) } ] [ (sub1 : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) (3.jpg : FILE) (4.jpg : FILE) } ] (2.jpg : FILE) (3.jpg : FILE) [ (top2.zip : ZIP_FILE) has { (2.jpg : FILE) (3.jpg : FILE) [ (sub0 : FOLDER) has { (2.jpg : FILE) } ] [ (sub1 : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) (3.jpg : FILE) } ] } ] } ] (1.jpg : FILE) (2.jpg : FILE) (3.jpg : FILE) (4.jpg : FILE) [ (folder_in.zip : ZIP_FILE) has { (2.jpg : FILE) (3.jpg : FILE) [ (top0 : FOLDER) has { (2.jpg : FILE) (4.jpg : FILE) (6.jpg : FILE) [ (sub0 : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) } ] [ (sub1 : FOLDER) has { (4.jpg : FILE) (5.jpg : FILE) (6.jpg : FILE) } ] [ (sub2 : FOLDER) has { (1.jpg : FILE) } ] } ] (top1 : FOLDER) [ (top2 : FOLDER) has { (2.jpg : FILE) (3.jpg : FILE) [ (sub0 : FOLDER) has { (2.jpg : FILE) } ] [ (sub1 : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) (3.jpg : FILE) } ] (sub2 : FOLDER) } ] } ] } ] [ (folder.zip : ZIP_FILE) has { [ (folder : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) (3.jpg : FILE) (4.jpg : FILE) (folder_in.zip : FILE) [ (top0 : FOLDER) has { (2.jpg : FILE) (4.jpg : FILE) (6.jpg : FILE) [ (sub0 : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) } ] [ (sub1 : FOLDER) has { (4.jpg : FILE) (5.jpg : FILE) (6.jpg : FILE) } ] [ (sub2 : FOLDER) has { (1.jpg : FILE) } ] } ] (top1 : FOLDER) [ (top2 : FOLDER) has { (2.jpg : FILE) (3.jpg : FILE) [ (sub0 : FOLDER) has { (2.jpg : FILE) } ] [ (sub1 : FOLDER) has { (1.jpg : FILE) (2.jpg : FILE) (3.jpg : FILE) } ] (sub2 : FOLDER) } ] } ] } ] } ]";

}
