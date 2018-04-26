package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

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


        Action doNothing = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //do nothing
            }
        };
        pathText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), "doNothing");
        pathText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), "doNothing");
        pathText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "doNothing");
        pathText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "doNothing");
        pathText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "doNothing");
        pathText.getActionMap().put("doNothing", doNothing);

        JPanel upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));
        upPanel.add(pathLabel);
        upPanel.add(pathText);
        upPanel.add(levelUpButton);
        pathLabel.setMaximumSize(new Dimension(150, 50));
        pathText.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        levelUpButton.setMaximumSize(new Dimension(150, 50));

        basePanel.add(upPanel);

        JPanel mainPanel = new JPanel();
        basePanel.add(mainPanel);

        add(basePanel);

        folderNavigatorBL = new FolderNavigatorBL(mainPanel, pathText, levelUpButton);

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