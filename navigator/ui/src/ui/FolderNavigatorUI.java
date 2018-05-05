package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class FolderNavigatorUI extends JFrame {

    private EditablePathManager editablePathManager;
//    private JLabel pathLabel;
//    private JTextField pathText;
    private JButton levelUpButton;
    FolderNavigatorBL folderNavigatorBL;

    public FolderNavigatorUI() {
        initComponents();
    }

    private void initComponents() {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        editablePathManager = new EditablePathManager();

        Font font = new Font("ARIAL", Font.BOLD, 20);

        levelUpButton = new JButton("Level Up");
        levelUpButton.setFont(font);
        levelUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                folderNavigatorBL.levelUp();
            }
        });


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Folder Navigator");

        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));


        JPanel upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));


        upPanel.add(editablePathManager.getPathPanel());
//        upPanel.add(pathLabel);
//        upPanel.add(pathText);
        upPanel.add(levelUpButton);
        levelUpButton.setMaximumSize(new Dimension(150, 50));

        basePanel.add(upPanel);

        JPanel mainPanel = new JPanel();
        basePanel.add(mainPanel);

        add(basePanel);

        folderNavigatorBL = new FolderNavigatorBL(mainPanel, editablePathManager, levelUpButton);

        pack();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            MetalLookAndFeel.setCurrentTheme(new ColorTheme());
        }
        catch (UnsupportedLookAndFeelException e) {
            System.out.println(e.getStackTrace());
            // handle exception
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getStackTrace());
            // handle exception
        }
        catch (InstantiationException e) {
            System.out.println(e.getStackTrace());
            // handle exception
        }
        catch (IllegalAccessException e) {
            System.out.println(e.getStackTrace());
            // handle exception
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FolderNavigatorUI().setVisible(true);
            }
        });
    }

}