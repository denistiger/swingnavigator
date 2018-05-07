package ui.folder_button;

import folder.IFolder;
import file_preview.FilePreviewGenerator;
import ui.IOpenFolderListener;
import file_preview.LazyIconLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FolderButtonsGenerator {

    private LazyIconLoader lazyIconLoader;
    private List<FolderButton> folderButtons;
    private FilePreviewGenerator previewGenerator;
    private IOpenFolderListener iOpenFolderListener;
    private FolderButtonLevelUp folderButtonLevelUp;

    public FolderButtonsGenerator(IOpenFolderListener iOpenFolderListener) {
        this.iOpenFolderListener = iOpenFolderListener;
        lazyIconLoader = null;
        folderButtons = new LinkedList<>();
        previewGenerator = new FilePreviewGenerator();
        folderButtonLevelUp = new FolderButtonLevelUp();
        folderButtonLevelUp.addOpenFolderListener(iOpenFolderListener);
    }

    public List<FolderButton> getFolderButtons() {
        return folderButtons;
    }

    public FolderButtonLevelUp getFolderButtonLevelUp() {
        return folderButtonLevelUp;
    }

    public void removeFolderButtons() {
        folderButtons = new LinkedList<>();
        if (lazyIconLoader != null) {
            lazyIconLoader.stop();
        }
    }

    public void setBackgroundMode(boolean backgroundMode) {
        lazyIconLoader.setBackgroundMode(backgroundMode);
    }

    private boolean isSameFolder(List<IFolder> folders) {
        if (folders.size() != folderButtons.size()) {
            return false;
        }
        Iterator<FolderButton> folderButtonIterator = folderButtons.iterator();
        Iterator<IFolder> folderIterator = folders.iterator();
        while (folderButtonIterator.hasNext() && folderIterator.hasNext()) {
            if (folderButtonIterator.next().getFolder().getAbsolutePath().compareTo(
                    folderIterator.next().getAbsolutePath()) != 0) {
                return false;
            }
        }
        return true;
    }

    public List<FolderButton> createFolderButtons(List<IFolder> folders) {
        if (isSameFolder(folders)) {
            setBackgroundMode(false);
            return folderButtons;
        }
        folderButtons = new LinkedList<>();
        if (lazyIconLoader != null) {
            lazyIconLoader.stop();
        }
        else {
            lazyIconLoader = new LazyIconLoader();
        }
        for (IFolder folder : folders) {
            FolderButton folderButton;
            if (folder.getType() == IFolder.FolderTypes.IMAGE || folder.getType() == IFolder.FolderTypes.TEXT_FILE
                    || folder.getType() == IFolder.FolderTypes.ZIP || folder.getType() == IFolder.FolderTypes.FOLDER) {
                folderButton = new FolderButton(folder, previewGenerator.getLazyLoadIcon(folder));
                lazyIconLoader.addListener(folderButton, folder);
            }
            else {
                folderButton = new FolderButton(folder, previewGenerator.getFilePreviewSmall(folder));
            }
            folderButton.addOpenFolderListener(iOpenFolderListener);
            folderButtons.add(folderButton);
        }
        lazyIconLoader.start();
        setUpFolderButtonDimensions();
        return folderButtons;
    }

    private void setUpFolderButtonDimension(JLabel folderButton, Dimension maxDimension) {
        folderButton.setMinimumSize(maxDimension);
        folderButton.setPreferredSize(maxDimension);
        folderButton.setMaximumSize(maxDimension);
        folderButton.setAlignmentY(Component.TOP_ALIGNMENT);
        folderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void updateMaxDimension(JLabel folderButton, Dimension maxDimension) {
        maxDimension.width = Math.max( (int)folderButton.getPreferredSize().getWidth(), maxDimension.width);
        maxDimension.height = Math.max( (int)folderButton.getPreferredSize().getHeight(), maxDimension.height);
    }

    private void setUpFolderButtonDimensions() {
        Dimension maxDimension = new Dimension(10, 10);

        for (FolderButton folderButton : folderButtons) {
            updateMaxDimension(folderButton, maxDimension);
        }
        updateMaxDimension(folderButtonLevelUp, maxDimension);

        for (FolderButton folderButton : folderButtons) {
            setUpFolderButtonDimension(folderButton, maxDimension);
        }
        setUpFolderButtonDimension(folderButtonLevelUp, maxDimension);
    }
}
