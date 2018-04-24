package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FolderNavigatorUI extends JFrame {

    private JLabel pathLabel;
    private JTextField pathText;
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

        Font font = new Font("ARIAL", Font.BOLD, 20);

        pathLabel = new JLabel("Address");
        pathLabel.setFont(font);

        pathText = new JTextField();
        pathText.setFont(font);

//        KeyListener keyListener = new KeyListener() {
//            public void keyPressed(KeyEvent keyEvent) {
//            }
//
//            public void keyReleased(KeyEvent keyEvent) {
//            }
//
//            public void keyTyped(KeyEvent keyEvent) {
//                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
//                    setNewAddress();
//                }
//                else {
//                    filterFolders();
//                }
//            }
//        };


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

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));
        upPanel.add(pathLabel);
        upPanel.add(pathText);
        upPanel.add(levelUpButton);
        pathLabel.setMaximumSize(new Dimension(150, 50));
        pathText.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        levelUpButton.setMaximumSize(new Dimension(150, 50));

        add(upPanel);

        JPanel mainPanel = new JPanel();
        add(mainPanel);

        folderNavigatorBL = new FolderNavigatorBL(mainPanel, pathText);

//        add(scrollPane);

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