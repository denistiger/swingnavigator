package folder.testing

import folder.FolderFactory
import folder.IFolder
import folder.IFolderFactory

class IFolderFileDataTest extends GroovyTestCase {
    void testGetInputStream() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILESTRING, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        IFolder test_txt = TestUtils.getByName(TestUtils.getByName(TestUtils.getByName(iFolder, "folder"), "top3"), "test.txt")
        String file_data = TestUtils.getFromStream(test_txt.getInputStream())
        String file_origin = TestUtils.getTestFile("../../testData/folder/top3/test.txt")
        assertEquals("Check file ", file_origin, file_data)

    }
}
