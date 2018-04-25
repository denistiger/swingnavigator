package ui;

import ui.folder_button.FolderButtonSkeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.List;

public class FoldersPanel extends JPanel implements ComponentListener, IFoldersPanelSelection {

    private BoxLayout layout;

    private List<FolderButtonSkeleton> folderButtonsDisplayed;

    private FolderButtonSkeleton folderButtonSelection = null;
    private int selectionIndex;
    private int colsCount;

    public FoldersPanel() {

        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        addComponentListener(this);
    }

    public void setFolderButtons(List<FolderButtonSkeleton> folderButtons) {
        this.folderButtonsDisplayed = folderButtons;

        findSelectionIndex();
        updateData(-1);
    }

    private void setDefaultSelection() {
        if (!folderButtonsDisplayed.isEmpty()) {
            selectionIndex = Math.min(1, folderButtonsDisplayed.size() - 1);
            folderButtonSelection = folderButtonsDisplayed.get(selectionIndex);
            folderButtonSelection.setSelected(true);
        }
        else {
            folderButtonSelection = null;
            selectionIndex = -1;
        }

    }

    private void findSelectionIndex() {
        selectionIndex = -1;
        if (folderButtonSelection != null) {
            selectionIndex = folderButtonsDisplayed.indexOf(folderButtonSelection);
        }
        if (selectionIndex == -1) {
            if (folderButtonSelection != null) {
                folderButtonSelection.setSelected(false);
            }
            setDefaultSelection();
        }
    }

    public void updateData(int maxPanelWidth) {
        removeAll();

        if (maxPanelWidth == -1) {
            maxPanelWidth = getWidth();
        }

        if (!folderButtonsDisplayed.isEmpty()) {
            final int rigid_area_width = 5;
            int actualWidth = Math.max(maxPanelWidth, 100);
            colsCount = actualWidth /
                    ((int) folderButtonsDisplayed.get(0).getMaximumSize().getWidth() + rigid_area_width);

            Iterator<FolderButtonSkeleton> folderButtonIterator = folderButtonsDisplayed.iterator();
            while (folderButtonIterator.hasNext()) {
                JPanel linePanel = new JPanel();
                BoxLayout lineLayout = new BoxLayout(linePanel, BoxLayout.X_AXIS);
                linePanel.setLayout(lineLayout);
                for (int j = 0; j < colsCount && folderButtonIterator.hasNext(); ++j) {
                    FolderButtonSkeleton folderButtonSkeleton = folderButtonIterator.next();
                    linePanel.add(folderButtonSkeleton);
                    linePanel.add(Box.createRigidArea(new Dimension(rigid_area_width, 5)));
                }
                linePanel.add(Box.createHorizontalGlue());
                add(linePanel);
            }
        }
        else {
            colsCount = 1;
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

    @Override
    public void setSelection(FolderButtonSkeleton folderButtonSkeleton) {
        folderButtonSelection = folderButtonSkeleton;
        findSelectionIndex();
        updateSelectionForNewIndex();
    }

    private void updateSelectionForNewIndex() {
        folderButtonSelection.setSelected(false);
        folderButtonSelection = folderButtonsDisplayed.get(selectionIndex);
        folderButtonSelection.setSelected(true);
    }

    @Override
    public void next() {
        if (selectionIndex < folderButtonsDisplayed.size() && selectionIndex != -1) {
            selectionIndex++;
            updateSelectionForNewIndex();
        }
    }

    @Override
    public void prev() {
        if (selectionIndex > 0) {
            selectionIndex--;
            updateSelectionForNewIndex();
        }
    }

    @Override
    public void down() {
        if (selectionIndex < folderButtonsDisplayed.size() - colsCount && selectionIndex != -1) {
            selectionIndex += colsCount;
            updateSelectionForNewIndex();
        }

    }

    @Override
    public void up() {
        if (selectionIndex >= colsCount) {
            selectionIndex -= colsCount;
            updateSelectionForNewIndex();
        }
    }

    @Override
    public FolderButtonSkeleton getSelection() {
        return folderButtonSelection;
    }
}
