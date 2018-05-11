package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class JPanelScrollableFolders extends JPanel implements Scrollable, ComponentListener{

    private FoldersPanel foldersPanel;

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
        foldersPanel.updateData(width);
    }

    JPanelScrollableFolders(FoldersPanel foldersPanel1 ) {
        this.foldersPanel = foldersPanel1;
        JPanel bottomStretchPanel = new JPanel();
        JPanel rightStretchPanel = new JPanel();

        GroupLayout groupLayout = new GroupLayout(this);
        setLayout(groupLayout);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup().
                        addGroup(groupLayout.createSequentialGroup().
                                addComponent(foldersPanel).
                                addComponent(rightStretchPanel)).
                        addComponent(bottomStretchPanel)
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup().
                        addGroup(groupLayout.createParallelGroup().
                                addComponent(foldersPanel).
                                addComponent(rightStretchPanel)).
                        addComponent(bottomStretchPanel)
        );

    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return super.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 128;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 128;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        System.out.println("New size: " + getSize());
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
