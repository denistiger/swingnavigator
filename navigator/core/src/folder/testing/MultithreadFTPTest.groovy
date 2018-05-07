package folder.testing

import folder.FolderManager
import folder.IFolder
import folder.file_preview.FilePreviewImage
import folder.file_preview.IFilePreviewListener
import folder.file_preview.LazyIconLoader

import javax.imageio.ImageIO
import javax.swing.ImageIcon
import java.awt.Image
import java.awt.image.BufferedImage

class MultithreadFTPTest extends GroovyTestCase {
    class PreviewListener implements IFilePreviewListener {

        volatile int previewCount = 0;

        @Override
        void setPreviewIcon(ImageIcon icon) {
            previewCount++;
            System.out.println("Got preview for icon dimensions " + icon.getIconWidth() + " " + icon.getIconHeight());
        }
    }

    void testMultipleIcons() {
        FolderManager folderManager = new FolderManager();
        folderManager.openPath("ftp://127.0.0.1:2121/folder.zip/folder");
        PreviewListener previewListener = new PreviewListener();
        LazyIconLoader lazyIconLoader = new LazyIconLoader();

        List<IFolder> folders = folderManager.getFoldersAtPath();
        for (IFolder folder : folders) {
            lazyIconLoader.addListener(previewListener, folder);
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
        Thread.sleep(5000);
        assert(previewListener.previewCount == folders.size() * 2);
    }
}
