import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class JPanelScrollableFolders extends JPanel implements Scrollable, ComponentListener{

    private FoldersPanel foldersPanel;
    private JPanel bottomStretchPanel, rightStretchPanel;

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        System.out.println("Set bounds: " + width + " " + height);
        super.setBounds(x, y, width, height);
        foldersPanel.updateData(width - 10);
    }

    public JPanelScrollableFolders(FoldersPanel foldersPanel1 ) {
        this.foldersPanel = foldersPanel1;
        bottomStretchPanel = new JPanel();
        bottomStretchPanel.setBackground(Color.RED);
        rightStretchPanel = new JPanel();
        rightStretchPanel.setBackground(Color.BLUE);
        rightStretchPanel.setMinimumSize(new Dimension(150, 40));

//        setLayout(new F);

//        add(foldersPanel);
//        add(rightStretchPanel);

//        setLayout(new BorderLayout());
//        add(this.foldersPanel, BorderLayout.CENTER);
//        add(bottomStretchPanel, BorderLayout.PAGE_END);
//        add(rightStretchPanel, BorderLayout.LINE_END);

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
        System.out.println("New preffered size: " + super.getPreferredSize());
        return super.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
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
