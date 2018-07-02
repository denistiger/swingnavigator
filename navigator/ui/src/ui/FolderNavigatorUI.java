package ui;

import javax.swing.*;
import java.awt.*;

public class FolderNavigatorUI extends JFrame {


    private FolderNavigatorUI(String[] args) {
        initComponents(args);
    }

    private void initComponents(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        EditablePathManager editablePathManager = new EditablePathManager();

        Font font = new Font("ARIAL", Font.BOLD, 20);

        JButton levelUpButton = new JButton("Level Up");
        levelUpButton.setFont(font);
        levelUpButton.setFocusable(false);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Folder Navigator");

        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));


        JPanel upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));


        upPanel.add(editablePathManager.getPathPanel());
        upPanel.add(levelUpButton);
        levelUpButton.setMaximumSize(new Dimension(150, 50));

        basePanel.add(upPanel);

        JPanel mainPanel = new JPanel();
        basePanel.add(mainPanel);

        add(basePanel);

        FolderNavigatorBL folderNavigatorBL = new FolderNavigatorBL(mainPanel, editablePathManager, levelUpButton);
        if (args.length > 0) {
            folderNavigatorBL.openPath(args[0]);
        }

        pack();
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FolderNavigatorUI(args).setVisible(true));
    }

}