package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Vector;

public class FoldersPanel extends JPanel implements ComponentListener {

    private BoxLayout layout;

    private Vector<FolderButton> folderButtonsDisplayed;

    public FoldersPanel() {

        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        addComponentListener(this);
    }

    public void setFolderButtons(Vector<FolderButton> folderButtons) {
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
            int colsCount = actualWidth / ((int) folderButtonsDisplayed.elementAt(0).getMaximumSize().getWidth() + rigid_area_width);

            Iterator<FolderButton> folderButtonIterator = folderButtonsDisplayed.iterator();
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
//        else {
//            if (imageIcon != null) {
//                JLabel imageLabel = new JLabel(imageIcon);
//                add(imageLabel);
//            }
//        }
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
