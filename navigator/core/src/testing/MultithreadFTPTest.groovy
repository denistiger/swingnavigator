package testing

import file_preview.FilePreviewGenerator
import folder_management.FolderManager
import folder.IFolder
import file_preview.IFilePreviewListener
import file_preview.LazyIconLoader

import javax.imageio.ImageIO
import javax.swing.ImageIcon
import java.awt.image.BufferedImage
import java.util.concurrent.Semaphore

class MultithreadFTPTest extends GroovyTestCase {
    class PreviewListener implements IFilePreviewListener {

        volatile int previewCount = 0;

        @Override
        void setPreviewIcon(ImageIcon icon) {
            ++previewCount;
            System.out.println("Got preview for icon dimensions " + icon.getIconWidth() + " " + icon.getIconHeight());
        }
    }

    void testMultipleIconsFTP() {
        checkMultipleIcons("ftp://127.0.0.1:2121/folder.zip/folder");
        checkMultipleIcons("ftp://127.0.0.1:2121/folder.zip/folder/folder_in.zip/");
        checkMultipleIcons("ftp://127.0.0.1:2121/folder.zip/folder/folder_in.zip/top2/");
        checkMultipleIcons("ftp://127.0.0.1:2121/folder.zip/folder/folder_in.zip/top2/top2.zip/");
        checkMultipleIcons("ftp://127.0.0.1:2121/folder.zip/folder/top2/top2.zip/sub1/");
        checkMultipleIcons("ftp://127.0.0.1:2121/folder.zip/folder/folder_in.zip/top2/top2.zip/sub1/");
    }

    void testMultipleIconsDisk() {
        checkMultipleIcons("../../testData/folder.zip/folder");
        checkMultipleIcons("../../testData/folder.zip/folder/folder_in.zip/");
        checkMultipleIcons("../../testData/folder.zip/folder/folder_in.zip/top2/");
        checkMultipleIcons("../../testData/folder.zip/folder/folder_in.zip/top2/top2.zip/");
        checkMultipleIcons("../../testData/folder.zip/folder/top2/top2.zip/sub1/");
        checkMultipleIcons("../../testData/folder.zip/folder/folder_in.zip/top2/top2.zip/sub1/");
    }

    void checkMultipleIcons(String path) {
        System.out.println("Start test 'checkMultipleIcons' for path " + path);
        FolderManager folderManager = new FolderManager();
        folderManager.openPath(path);
        PreviewListener previewListener = new PreviewListener();
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        LazyIconLoader lazyIconLoader = new LazyIconLoader(previewGenerator);

        List<IFolder> folders = folderManager.getFoldersAtPath();
        int stepsCount = 3;
        for (int i = 0; i < stepsCount; ++i) {
            for (IFolder folder : folders) {
                lazyIconLoader.addListener(previewListener, folder);
            }
        }
        lazyIconLoader.start();
//        for (IFolder folder : folders) {
//            lazyIconLoader.addListener(previewListener, folder);
//        }
        for (IFolder folder : folders) {
            if (folder.getType() == IFolder.FolderTypes.IMAGE) {
                BufferedImage image = ImageIO.read(folder.getInputStream());
                System.out.println("Got full image " + folder.getName() + " dimensions: " + image.getWidth() + " "
                        + image.getHeight());
            }
        }
        Thread.sleep(3000);
        assert(previewListener.previewCount == folders.size() * stepsCount);
    }
}
