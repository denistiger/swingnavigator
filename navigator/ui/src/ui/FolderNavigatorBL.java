package ui;

import folder.FolderManager;
import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class FolderNavigatorBL implements PathListener, IOpenFolderListener {
    private JPanel mainPanel;
    private JTextField pathText;
    private FoldersPanel foldersPanel;

    private Vector<FolderButton> folderButtonsFiltered;
    private ImageIcon imageIcon;
    private FolderManager folderManager;
    private FilePreviewGenerator previewGenerator;
    private List<PathListener> pathListenerList;
    private FolderButtonsGenerator folderButtonsGenerator;


    public FolderNavigatorBL(JPanel mainPanel, JTextField pathText) {
        this.mainPanel = mainPanel;

        folderManager = new FolderManager();
        previewGenerator = new FilePreviewGenerator();
        pathListenerList = new LinkedList<>();
        folderButtonsFiltered = new Vector<>();
        imageIcon = null;
        folderButtonsGenerator = new FolderButtonsGenerator(this);
        folderManager.openPath(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());

        foldersPanel = new FoldersPanel();
        addPathListener(this);

        this.pathText = pathText;
        this.pathText.setText(getCurrentPath());
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

        processNewPath();
    }

    private void setNewAddress() {
        openPath(pathText.getText());
    }

    private void filterFolders() {
        String enteredPath = pathText.getText();
        String currentPath = getCurrentPath();
        if (!enteredPath.startsWith(currentPath) || enteredPath.length() <= currentPath.length()) {
            return;
        }

        String filter = enteredPath.substring(currentPath.length() + 1);
        filterByPrefix(filter);
    }

    @Override
    public void setPath(String path) {
        pathText.setText(path);
    }


    public void openFolder(IFolder folder) {
        folderManager.openFolder(folder);
        processNewPath();
    }

    private void notifyOnPathChange() {
        String path = getCurrentPath();
        for (PathListener pathListener : pathListenerList) {
            pathListener.setPath(path);
        }
    }

    public void levelUp() {
        folderManager.levelUp();
        processNewPath();
    }

    public void addPathListener(PathListener pathListener) {
        pathListenerList.add(pathListener);
    }

    public void removePathListener(PathListener pathListener) {
        pathListenerList.remove(pathListener);
    }

    public String getCurrentPath() {
        return folderManager.getFullPath();
    }

    public void openPath(String path) {
        if (path.startsWith(folderManager.getFullPath()) && folderButtonsFiltered.size() == 1) {
            folderManager.openFolder(folderButtonsFiltered.firstElement().getFolder());
        }
        else {
            folderManager.openPath(path);
        }
        processNewPath();
    }

    private void processNewPath() {
        List<IFolder> folders = folderManager.getFoldersAtPath();
        if (folders != null) {
            imageIcon = null;
            folderButtonsFiltered = folderButtonsGenerator.createFolderButtons(folders);
            foldersPanel.setFolderButtons(folderButtonsFiltered);
            //TODO Switch to foldersPanel mode;
        }
        else {
            IFolder file = folderManager.getCurrentFolder();
            if (file == null) {
                return;
            }
            imageIcon = previewGenerator.getFilePreviewLarge(file);
            //TODO Switch to file preview mode;
        }
        notifyOnPathChange();
    }

    public void filterByPrefix(String prefix) {
        Vector<FolderButton> folderButtons = folderButtonsGenerator.getFolderButtons();
        if (prefix.isEmpty()) {
            folderButtonsFiltered = folderButtons;
            return;
        }
        folderButtonsFiltered = new Vector<>();
        for (FolderButton folderButton : folderButtons) {
            if (folderButton.getFolder().getName().startsWith(prefix)) {
                folderButtonsFiltered.add(folderButton);
            }
        }
        foldersPanel.setFolderButtons(folderButtonsFiltered);
    }

}
