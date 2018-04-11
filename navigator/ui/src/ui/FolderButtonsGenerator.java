package ui;

import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

public class FolderButtonsGenerator {

    private LazyIconLoader lazyIconLoader;
    private Vector<FolderButton> folderButtons;
    private FilePreviewGenerator previewGenerator;
    private IOpenFolderListener iOpenFolderListener;

    FolderButtonsGenerator(IOpenFolderListener iOpenFolderListener) {
        this.iOpenFolderListener = iOpenFolderListener;
        lazyIconLoader = null;
        folderButtons = new Vector<>();
        previewGenerator = new FilePreviewGenerator();
    }

    Vector<FolderButton> getFolderButtons() {
        return folderButtons;
    }


    Vector<FolderButton> createFolderButtons(List<IFolder> folders) {
        folderButtons = new Vector<>();
        if (lazyIconLoader != null) {
            lazyIconLoader.stop();
        }
        lazyIconLoader = new LazyIconLoader();
        for (IFolder folder : folders) {
            FolderButton folderButton;
            if (folder.getType() == IFolder.FolderTypes.IMAGE || folder.getType() == IFolder.FolderTypes.TEXT_FILE) {
                folderButton = new FolderButton(folder, previewGenerator.getLazyLoadIcon(folder));
                lazyIconLoader.addListener(folderButton, folder);
            }
            else {
                folderButton = new FolderButton(folder, previewGenerator.getFilePreview(folder));
            }
            folderButton.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        iOpenFolderListener.openFolder(folderButton.getFolder());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            folderButtons.add(folderButton);
        }
        lazyIconLoader.start();
        setUpFolderButtonDimensions();
        return folderButtons;
    }

    private void setUpFolderButtonDimensions() {
        int maxWidth = 10, maxHeight = 10;
        for (FolderButton folderButton : folderButtons) {
            if (folderButton.getPreferredSize().getWidth() > maxWidth) {
                maxWidth = (int)folderButton.getPreferredSize().getWidth();
            }
            if (folderButton.getPreferredSize().getHeight() > maxHeight) {
                maxHeight = (int)folderButton.getPreferredSize().getHeight();
            }
        }
        for (FolderButton folderButton : folderButtons) {
            folderButton.setMinimumSize(new Dimension(maxWidth, maxHeight));
            folderButton.setPreferredSize(new Dimension(maxWidth, maxHeight));
            folderButton.setMaximumSize(new Dimension(maxWidth, maxHeight));
            folderButton.setAlignmentY(Component.TOP_ALIGNMENT);
            folderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }
}
