package ui;

import javax.swing.*;
import javax.swing.plaf.ListUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FolderNavigatorUI extends JFrame implements PathListener {

    private JLabel pathLabel;
    private JTextField pathText;
    private JButton levelUpButton;
    private FoldersPanel foldersPanel;
    private JPanel stretchPanel;

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

        foldersPanel = new FoldersPanel();
        foldersPanel.addPathListener(this);

        Font font = new Font("ARIAL", Font.BOLD, 20);

        pathLabel = new JLabel("Address");
        pathLabel.setFont(font);

        pathText = new JTextField(foldersPanel.getCurrentPath());
        pathText.setFont(font);
        pathText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewAddress(e);
            }
        });

        levelUpButton = new JButton("Level Up");
        levelUpButton.setFont(font);
        levelUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                foldersPanel.levelUp();
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

        stretchPanel = new JPanelScrollableFolders(foldersPanel);

        JScrollPane scrollPane = new JScrollPane(stretchPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setPreferredSize(new Dimension(800, 600));

        add(scrollPane);

        pack();
    }

    private void setNewAddress(ActionEvent e) {
        foldersPanel.openPath(pathText.getText());
    }

    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            MetalLookAndFeel.setCurrentTheme(new ColorTheme());
//        }
//        catch (UnsupportedLookAndFeelException e) {
//            System.out.println(e.getStackTrace());
//            // handle exception
//        }
//        catch (ClassNotFoundException e) {
//            System.out.println(e.getStackTrace());
//            // handle exception
//        }
//        catch (InstantiationException e) {
//            System.out.println(e.getStackTrace());
//            // handle exception
//        }
//        catch (IllegalAccessException e) {
//            System.out.println(e.getStackTrace());
//            // handle exception
//        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FolderNavigatorUI().setVisible(true);
            }
        });
    }

    @Override
    public void setPath(String path) {
        pathText.setText(path);
        repaint();
    }

}