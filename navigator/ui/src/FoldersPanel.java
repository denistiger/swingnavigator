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

public class FoldersPanel extends JPanel implements ComponentListener/*, Scrollable*/ {
    private FolderManager folderManager;
    private FilePreviewGenerator previewGenerator;
    private List<PathListener> pathListenerList;
    private Vector<FolderButton> folderButtons;

    private BoxLayout layout;


    public FoldersPanel() {
        folderManager = new FolderManager();
        previewGenerator = new FilePreviewGenerator();
        pathListenerList = new LinkedList<>();
        folderButtons = new Vector<>();

        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        folderManager.openPath(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
        addComponentListener(this);
        processNewPath();

    }

    private void openFolder(IFolder folder) {
        folderManager.openFolder(folder);
        processNewPath();
    }

    private void processNewPath() {
        List<IFolder> folders = folderManager.getFoldersAtPath();
        if (folders != null) {
            folderButtons = new Vector<>();
            for (IFolder folder : folders) {
                FolderButton folderButton = new FolderButton(folder, previewGenerator.getFilePreview(folder));
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
        }
        setUpFolderButtonDimensions();
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
        folderManager.openPath(path);
        processNewPath();
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

    public void updateData(int maxPanelWidth) {
        removeAll();

        if (maxPanelWidth == -1) {
            maxPanelWidth = getWidth();
        }

        if (!folderButtons.isEmpty()) {
            final int rigid_area_width = 5;
            int actualWidth = Math.max(maxPanelWidth, 100);
            int colsCount = actualWidth / ((int)folderButtons.elementAt(0).getMaximumSize().getWidth() + rigid_area_width);

            Iterator<FolderButton> folderButtonIterator = folderButtons.iterator();
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
        revalidate();
        notifyOnPathChange();
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
