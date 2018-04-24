package ui;

import ui.folder_button.FolderButtonSkeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.List;

public class FoldersPanel extends JPanel implements ComponentListener {

    private BoxLayout layout;

    private List<FolderButtonSkeleton> folderButtonsDisplayed;

    public FoldersPanel() {

        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        addComponentListener(this);
    }

    public void setFolderButtons(List<FolderButtonSkeleton> folderButtons) {
        this.folderButtonsDisplayed = folderButtons;
        updateData(-1);
    }

    public void updateData(int maxPanelWidth) {
        removeAll();

        if (maxPanelWidth == -1) {
            maxPanelWidth = getWidth();
        }

        if (!folderButtonsDisplayed.isEmpty()) {
            final int rigid_area_width = 5;
            int actualWidth = Math.max(maxPanelWidth, 100);
            int colsCount = actualWidth /
                    ((int) folderButtonsDisplayed.get(0).getMaximumSize().getWidth() + rigid_area_width);

            Iterator<FolderButtonSkeleton> folderButtonIterator = folderButtonsDisplayed.iterator();
            while (folderButtonIterator.hasNext()) {
                JPanel linePanel = new JPanel();
                BoxLayout lineLayout = new BoxLayout(linePanel, BoxLayout.X_AXIS);
                linePanel.setLayout(lineLayout);
                for (int j = 0; j < colsCount && folderButtonIterator.hasNext(); ++j) {
                    linePanel.add(folderButtonIterator.next());
                    linePanel.add(Box.createRigidArea(new Dimension(rigid_area_width, 5)));
                }
                linePanel.add(Box.createHorizontalGlue());
                add(linePanel);
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
