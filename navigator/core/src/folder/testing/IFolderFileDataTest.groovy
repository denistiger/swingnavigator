package folder.testing

import folder.FolderFactory
import folder.IFolder
import folder.IFolderFactory
import folder.file_preview.FilePreviewGenerator
import junit.framework.Test

import javax.imageio.ImageIO
import javax.swing.ImageIcon
import java.awt.image.BufferedImage

class IFolderFileDataTest extends GroovyTestCase {

    void printIFolderPreview(IFolder iFolder, String name_prefix) {
        TestUtils.savePreview(iFolder, name_prefix + "__" + iFolder.getName());
        for (IFolder folder : iFolder.getItems()) {
//            if (folder.getType() == IFolder.FolderTypes.ZIP) {
//                continue;
//            }
            printIFolderPreview(folder, name_prefix + "__" + iFolder.getName());
        }
    }

    void testPreview() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILESTRING, file);
        IFolder iFolder = new FolderFactory().createIFolder(params);

        printIFolderPreview(iFolder, "../../Output/");

//        IFolder test_txt = TestUtils.getByName(TestUtils.getByName(TestUtils.getByName(iFolder, "folder"), "top3"), "test.txt");
//        TestUtils.savePreview(test_txt, "../../Output/test_txt.png");
//
//        IFolder test_jpg = TestUtils.getByName(TestUtils.getByName(iFolder, "folder"), "1.jpg");
//        TestUtils.savePreview(test_jpg, "../../Output/1.png")
    }

    void testSimpleFile() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILESTRING, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        IFolder test_txt = TestUtils.getByName(TestUtils.getByName(TestUtils.getByName(iFolder, "folder"), "top3"), "test.txt")
        String file_data = TestUtils.getFromStream(test_txt.getInputStream())
        String file_origin = TestUtils.getTestFile("../../testData/folder/top3/test.txt")
        assertEquals("Check file ", file_origin, file_data)
    }

    void testSimpleFileInZip() {
        File file = new File("../../testData")
        Map<String, Object> params = new HashMap<String, Object>()
        params.put(IFolderFactory.FILESTRING, file)
        IFolder iFolder = new FolderFactory().createIFolder(params)
        IFolder test_txt = TestUtils.getByName(TestUtils.getByName(TestUtils.getByName(TestUtils.getByName(iFolder, "folder.zip"),"folder"), "top3"), "test.txt")
        String file_data = TestUtils.getFromStream(test_txt.getInputStream())
        String file_origin = TestUtils.getTestFile("../../testData/folder/top3/test.txt")
        assertEquals("Check file ", file_origin, file_data)
    }
}
