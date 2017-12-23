package folder.zip_folder

import folder.IFolder

class ZipEntryDataTest extends GroovyTestCase {
    void testGetInZipSplitPathSimpleSlash() {
        ZipEntryData zipEntryData = new ZipEntryData("folder/folder1/folder2/1.jpg", null, IFolder.FolderTypes.IMAGE)
        String[] split = zipEntryData.getInZipSplitPath();
        assertEquals("Check first param", "folder", split[0])
        assertEquals("Check second param", "folder1", split[1])
        assertEquals("Check third param", "folder2", split[2])
        assertEquals("Check fourth param", "1.jpg", split[3])
    }

    void testGetInZipSplitPathSimpleBackSlash() {
        ZipEntryData zipEntryData = new ZipEntryData("folder\\folder1\\folder2\\1.jpg", null, IFolder.FolderTypes.IMAGE)
        String[] split = zipEntryData.getInZipSplitPath();
        assertEquals("Check first param", "folder", split[0])
        assertEquals("Check second param", "folder1", split[1])
        assertEquals("Check third param", "folder2", split[2])
        assertEquals("Check fourth param", "1.jpg", split[3])
    }

    void checkSplit(String[] split) {
        assertEquals("Check first param", "folder", split[0])
        assertEquals("Check second param", "folder1", split[1])
        assertEquals("Check third param", "folder2", split[2])
        assertEquals("Check third param", "folder3", split[3])
        assertEquals("Check third param", "folder4", split[4])
        assertEquals("Check fourth param", "1.jpg", split[5])
    }

    void testGetInZipSplitPathMixSlashBackSlash1() {
        ZipEntryData zipEntryData = new ZipEntryData("folder\\folder1/folder2/folder3/folder4/1.jpg", null, IFolder.FolderTypes.IMAGE)
        checkSplit(zipEntryData.getInZipSplitPath())
    }

    void testGetInZipSplitPathMixSlashBackSlash2() {
        ZipEntryData zipEntryData = new ZipEntryData("folder\\folder1\\folder2/folder3\\folder4/1.jpg", null, IFolder.FolderTypes.IMAGE)
        checkSplit(zipEntryData.getInZipSplitPath())
    }

    void testGetInZipSplitPathMixSlashBackSlash3() {
        ZipEntryData zipEntryData = new ZipEntryData("folder/folder1\\folder2\\folder3\\folder4/1.jpg", null, IFolder.FolderTypes.IMAGE)
        checkSplit(zipEntryData.getInZipSplitPath())
    }

    void testGetInZipSplitPathMixSlashBackSlash4() {
        ZipEntryData zipEntryData = new ZipEntryData("folder\\folder1/folder2/folder3\\folder4\\1.jpg", null, IFolder.FolderTypes.IMAGE)
        checkSplit(zipEntryData.getInZipSplitPath())
    }

    void testGetName() {
        ZipEntryData zipEntryData = new ZipEntryData("folder/folder1/folder2/1.jpg", null, IFolder.FolderTypes.IMAGE)
        assertEquals("Check name", "1.jpg", zipEntryData.getName())
    }
}
