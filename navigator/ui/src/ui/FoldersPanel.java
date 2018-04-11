package ui;

import folder.FolderManager;
import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class FoldersPanel extends JPanel implements ComponentListener {
    private FolderManager folderManager;
    private FilePreviewGenerator previewGenerator;
    private List<PathListener> pathListenerList;
    private Vector<FolderButton> folderButtons, folderButtonsFiltered;
    private ImageIcon imageIcon;

    private BoxLayout layout;

    private LazyIconLoader lazyIconLoader;

    public FoldersPanel() {
        folderManager = new FolderManager();
        previewGenerator = new FilePreviewGenerator();
        pathListenerList = new LinkedList<>();
        folderButtons = new Vector<>();
        folderButtonsFiltered = new Vector<>();
        imageIcon = null;

        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        folderManager.openPath(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
        addComponentListener(this);
        processNewPath();
    }

    public void setFolderButtons(Vector<FolderButton> folderButtons) {
        this.folderButtonsFiltered = folderButtons;
        updateData(-1);
    }

    private void openFolder(IFolder folder) {
        folderManager.openFolder(folder);
        processNewPath();
    }

    private void processNewPath() {
        List<IFolder> folders = folderManager.getFoldersAtPath();
        if (folders != null) {
            imageIcon = null;
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
                            openFolder(folderButton.getFolder());
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
            folderButtonsFiltered = folderButtons;
            lazyIconLoader.start();
            setUpFolderButtonDimensions();
        }
        else {
            IFolder file = folderManager.getCurrentFolder();
            if (file == null) {
                return;
            }
            imageIcon = previewGenerator.getFilePreviewLarge(file);
            if (imageIcon != null) {
                folderButtons.clear();
            }
        }
        updateData(-1);
        notifyOnPathChange();
    }

    public void filterByPrefix(String prefix) {
        if (prefix.isEmpty()) {
            folderButtonsFiltered = folderButtons;
            return;
        }
        folderButtonsFiltered = new Vector<>();
        for (FolderButton folderButton : folderButtons) {
            if (folderButton.getFolder().getName().startsWith(prefix)) {
                folderButtonsFiltered.add(folderButton);
            }
        }
        updateData(-1);
    }

    private void notifyOnPathChange() {
        String path = getCurrentPath();
        for (PathListener pathListener : pathListenerList) {
            pathListener.setPath(path);
        }
    }

    public void levelUp() {
        folderManager.levelUp();
        processNewPath();
    }

    public void addPathListener(PathListener pathListener) {
        pathListenerList.add(pathListener);
    }

    public void removePathListener(PathListener pathListener) {
        pathListenerList.remove(pathListener);
    }

    public String getCurrentPath() {
        return folderManager.getFullPath();
    }

    public void openPath(String path) {
        if (path.startsWith(folderManager.getFullPath()) && folderButtonsFiltered.size() == 1) {
            folderManager.openFolder(folderButtonsFiltered.firstElement().getFolder());
        }
        else {
            folderManager.openPath(path);
        }
        processNewPath();
    }

    private void setUpFolderButtonDimensions() {
        int maxWidth = 10, maxHeight = 10;
        for (FolderButton folderButton : folderButtonsFiltered) {
            if (folderButton.getPreferredSize().getWidth() > maxWidth) {
                maxWidth = (int)folderButton.getPreferredSize().getWidth();
            }
            if (folderButton.getPreferredSize().getHeight() > maxHeight) {
                maxHeight = (int)folderButton.getPreferredSize().getHeight();
            }
        }
        for (FolderButton folderButton : folderButtonsFiltered) {
            folderButton.setMinimumSize(new Dimension(maxWidth, maxHeight));
            folderButton.setPreferredSize(new Dimension(maxWidth, maxHeight));
            folderButton.setMaximumSize(new Dimension(maxWidth, maxHeight));
            folderButton.setAlignmentY(Component.TOP_ALIGNMENT);
            folderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }

    public void updateData(int maxPanelWidth) {
        removeAll();

        if (maxPanelWidth == -1) {
            maxPanelWidth = getWidth();
        }

        if (!folderButtonsFiltered.isEmpty()) {
            final int rigid_area_width = 5;
            int actualWidth = Math.max(maxPanelWidth, 100);
            int colsCount = actualWidth / ((int)folderButtonsFiltered.elementAt(0).getMaximumSize().getWidth() + rigid_area_width);

            Iterator<FolderButton> folderButtonIterator = folderButtonsFiltered.iterator();
            while (folderButtonIterator.hasNext()) {
                JPanel linePanel = new JPanel();
                BoxLayout lineLayout = new BoxLayout(linePanel, BoxLayout.X_AXIS);
                linePanel.setLayout(lineLayout);
                for (int j = 0; j < colsCount && folderButtonIterator.hasNext(); ++j) {
                    FolderButton button = folderButtonIterator.next();
                    linePanel.add(button);
                    linePanel.add(Box.createRigidArea(new Dimension(rigid_area_width, 5)));
                }
                linePanel.add(Box.createHorizontalGlue());
                add(linePanel);
//            add(Box.createRigidArea(new Dimension(5, 5)));
            }
        }
        else {
            if (imageIcon != null) {
                JLabel imageLabel = new JLabel(imageIcon);
                add(imageLabel);
            }
        }
        revalidate();
    }

    @Override
    public void componentResized(ComponentEvent e) {
//        updateData();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
