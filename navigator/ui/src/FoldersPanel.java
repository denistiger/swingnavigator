import folder.FolderManager;
import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class FoldersPanel extends JPanel implements ComponentListener/*, Scrollable*/ {
    private FolderManager folderManager;
    private FilePreviewGenerator previewGenerator;
    private List<PathListener> pathListenerList;
    private FlowLayout flowLayout;
    private Vector<FolderButton> folderButtons;

    private GridLayout layout;
    private List<JPanel> emptyItems;


    public FoldersPanel() {
        folderManager = new FolderManager();
        previewGenerator = new FilePreviewGenerator();
        pathListenerList = new LinkedList<>();
        folderButtons = new Vector<>();

        layout = new GridLayout(0, 1, 1, 1);
        setLayout(layout);

//        setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
//        setPreferredSize(new Dimension(800, 700));
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
        if (folders != null && folders.size() > 0) {
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
                System.out.println(folderButton.getPreferredSize());
                folderButtons.add(folderButton);
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
        folderManager.openPath(path);
        processNewPath();
    }

    private void updateData1() {
        removeAll();
        JList list = new JList(folderButtons);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setPrototypeCellValue("DATADATADATA");

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        add(listScroller);

        setPreferredSize(getPreferredSize());
        revalidate();
        notifyOnPathChange();
    }

    public void updateData(int maxPanelWidth) {
        removeAll();

        double maxWidth = 10, maxHeight = 10;
        for (FolderButton folderButton : folderButtons) {
            if (folderButton.getPreferredSize().getWidth() > maxWidth) {
                maxWidth = folderButton.getPreferredSize().getWidth();
            }
            if (folderButton.getPreferredSize().getHeight() > maxHeight) {
                maxHeight = folderButton.getPreferredSize().getHeight();
            }
        }

        if (maxPanelWidth == -1) {
            maxPanelWidth = getWidth();
        }

        System.out.println("Actual width " + getWidth() );

//        Container parent = getParent();
//        if (parent != null) {
//            System.out.println(parent.getClass());
//            System.out.println(parent.getParent().getClass());
//        }

        int actualWidth = Math.max(maxPanelWidth, 100);
        int actualHeight = Math.max(getHeight(), 100);
        int colsCount = (int)(actualWidth / maxWidth);
        int rowsCountByButtonsCount = folderButtons.size() / colsCount + (folderButtons.size() % colsCount == 0 ? 0 : 1);
        int rowsCountByButtonsHeight = (int)(actualHeight / maxHeight + 1);
        int rowsCount = Math.max(rowsCountByButtonsCount, rowsCountByButtonsHeight);
//        JPanel internalPanel = new JPanel();

//        internalPanel.setLayout(layout);


//        BorderLayout boxLayout = new BorderLayout();
//        setLayout(boxLayout);
//        add(internalPanel);

        layout.setColumns(colsCount);
        layout.setRows(rowsCountByButtonsCount);

        Dimension buttonSize = new Dimension((int)maxWidth, (int)maxHeight);

        int count = 0;
        for (FolderButton folderButton : folderButtons) {
            folderButton.setMaximumSize(buttonSize);
            folderButton.setMinimumSize(buttonSize);
            add(folderButton);
            count++;
        }


//        emptyItems = new LinkedList<>();
//
//        while (count < layout.getColumns() * layout.getRows()) {
//            JPanel empty = new JPanel();
//            emptyItems.add(empty);
//            add(empty);
//            count++;
//        }
//        System.out.println("Items count: " + count);
//        System.out.println("Max width: " + maxWidth + " max height " + maxHeight + " cur width " + getWidth());
//        System.out.println("Wish rows: " + rowsCount + " wisth cols: " + colsCount);
//        System.out.println("Layout rows: " + layout.getRows() + " layout cols: " + layout.getColumns());
//
//        System.out.println("Get VGap:" + layout.getVgap());

//        setSize(layout.getColumns() * Math.maxWidth, layout.getRows() * maxHeight);
//        layout.setVgap(0);
//        layout.setHgap(0);

        setPreferredSize(new Dimension(buttonSize.width * layout.getColumns(), buttonSize.height * layout.getRows()));
        setMaximumSize(new Dimension(buttonSize.width * layout.getColumns(), buttonSize.height * layout.getRows()));
        setMinimumSize(new Dimension(buttonSize.width * layout.getColumns(), buttonSize.height * layout.getRows()));
//        setSize(new Dimension(buttonSize.width * layout.getColumns(), buttonSize.height * layout.getRows()));
        System.out.println("Current width: " + getWidth() + " " + getHeight());
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

//    @Override
//    public Dimension getPreferredScrollableViewportSize() {
//        return super.getPreferredSize();
//    }
//
//    @Override
//    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
//        return 16;
//    }
//
//    @Override
//    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
//        return 16;
//    }
//
//    @Override
//    public boolean getScrollableTracksViewportWidth() {
//        return true;
//    }
//
//    @Override
//    public boolean getScrollableTracksViewportHeight() {
//        return false;
//    }
}
