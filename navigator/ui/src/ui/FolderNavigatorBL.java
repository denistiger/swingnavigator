package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FolderNavigatorBL implements PathListener{
    private JPanel mainPanel;
    private  JTextField pathText;
    private FoldersPanel foldersPanel;

    public FolderNavigatorBL(JPanel mainPanel, JTextField pathText) {
        this.mainPanel = mainPanel;

        foldersPanel = new FoldersPanel();
        foldersPanel.addPathListener(this);

        this.pathText = pathText;
        this.pathText.setText(foldersPanel.getCurrentPath());
        pathText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewAddress();
            }
        });

        DocumentListener documentListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filterFolders();
            }

            public void removeUpdate(DocumentEvent e) {
                filterFolders();
            }

            public void insertUpdate(DocumentEvent e) {
                filterFolders();
            }
        };

        pathText.getDocument().addDocumentListener(documentListener);


        BorderLayout borderLayout = new BorderLayout();
        mainPanel.setLayout(borderLayout);

        JPanel stretchPanel = new JPanelScrollableFolders(foldersPanel);

        JScrollPane scrollPane = new JScrollPane(stretchPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setPreferredSize(new Dimension(800, 600));

        mainPanel.add(scrollPane);

    }

    public void levelUp() {
        foldersPanel.levelUp();
    }

    private void setNewAddress() {
        foldersPanel.openPath(pathText.getText());
    }

    private void filterFolders() {
        String enteredPath = pathText.getText();
        String currentPath = foldersPanel.getCurrentPath();
        if (!enteredPath.startsWith(currentPath) || enteredPath.length() <= currentPath.length()) {
            return;
        }

        String filter = enteredPath.substring(currentPath.length() + 1);
        foldersPanel.filterByPrefix(filter);
    }

    @Override
    public void setPath(String path) {
        pathText.setText(path);
    }

}
