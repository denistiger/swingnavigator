package ui.folder_button;

import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;
import ui.IOpenFolderListener;
import ui.LazyIconLoader;

import javax.swing.*;
import java.awt.*;
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

    public List<FolderButton> createFolderButtons(List<IFolder> folders) {
        folderButtons = new LinkedList<>();
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
