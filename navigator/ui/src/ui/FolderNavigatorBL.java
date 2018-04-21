package ui;

import folder.FolderManager;
import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;
import ui.file_preview.FilePreviewPanelFactory;
import ui.folder_button.FolderButton;
import ui.folder_button.FolderButtonsGenerator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class FolderNavigatorBL implements PathListener, IOpenFolderListener {
    private JPanel mainPanel;
    private JTextField pathText;
    private FoldersPanel foldersPanel;
    private JScrollPane foldersScrollPane;
    private JComponent previousPanel;

    private List<FolderButton> folderButtonsFiltered;
    private FolderManager folderManager;
    private FilePreviewGenerator previewGenerator;
    private List<PathListener> pathListenerList;
    private FolderButtonsGenerator folderButtonsGenerator;


    public FolderNavigatorBL(JPanel mainPanel, JTextField pathText) {
        this.mainPanel = mainPanel;

        folderManager = new FolderManager();
        previewGenerator = new FilePreviewGenerator();
        pathListenerList = new LinkedList<>();
        folderButtonsFiltered = new LinkedList<>();
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

        foldersScrollPane = new JScrollPane(stretchPanel);
        foldersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        foldersScrollPane.setPreferredSize(new Dimension(800, 600));

        previousPanel = null;
//        changeMainPanelContentPane(foldersScrollPane);

        processNewPath();
    }

    private void setNewAddress() {
        openPath(pathText.getText());
    }

    private void filterFolders() {
        String enteredPath = pathText.getText();
        String currentPath = getCurrentPath();

        if (enteredPath.compareTo(currentPath) == 0 /*||
                folderButtonsFiltered.size() != folderButtonsGenerator.getFolderButtons().size()*/) {
            filterByPrefix("");
            return;
        }

        if (enteredPath.compareTo(currentPath) == 0 || enteredPath.length() == 0) {
            return;
        }

        if (currentPath.startsWith(enteredPath)) {
            if (currentPath.length() == 1 + enteredPath.length()) {
                enteredPath = enteredPath.substring(0, enteredPath.length() - 1);
            }
            folderManager.levelUp();
            if (enteredPath.startsWith(folderManager.getFullPath())) {
                processNewPath();
                return;
            }
            else {
                folderManager.openPath(currentPath);
                return;
            }
        }

        if (!enteredPath.startsWith(currentPath) || enteredPath.length() <= currentPath.length()) {
            return;
        }

        String filter = enteredPath.substring(currentPath.length());
        filterByPrefix(filter);
    }

    @Override
    public void setPath(String path) {
        Runnable setPathText = new Runnable() {
            @Override
            public void run() {
                pathText.setText(path);
            }
        };
        SwingUtilities.invokeLater(setPathText);
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
            folderManager.openFolder(folderButtonsFiltered.get(0).getFolder());
        }
        else {
            folderManager.openPath(path);
        }
        processNewPath();
    }

    private void changeMainPanelContentPane(JComponent panel) {
        if (previousPanel == panel) {
            return;
        }
        if (previousPanel != null) {
            mainPanel.remove(previousPanel);
        }
        previousPanel = panel;
        mainPanel.add(previousPanel);
    }

    private void processNewPath() {
        List<IFolder> folders = folderManager.getFoldersAtPath();
        if (folders != null) {
            folderButtonsFiltered = folderButtonsGenerator.createFolderButtons(folders);
            setFolderButtons();
            changeMainPanelContentPane(foldersScrollPane);
        }
        else {
            IFolder file = folderManager.getCurrentFolder();
            if (file == null) {
                return;
            }
            changeMainPanelContentPane(FilePreviewPanelFactory.createFilePreviewPanel(file));
//            imageIcon = previewGenerator.getFilePreviewLarge(file);
            //TODO Switch to file preview mode;
        }
        notifyOnPathChange();
    }

    public void filterByPrefix(String prefix) {
        List<FolderButton> folderButtons = folderButtonsGenerator.getFolderButtons();
        if (prefix.isEmpty()) {
            folderButtonsFiltered = folderButtons;
        }
        else {
            folderButtonsFiltered = new LinkedList<>();
            for (FolderButton folderButton : folderButtons) {
                if (folderButton.getFolder().getName().startsWith(prefix)) {
                    folderButtonsFiltered.add(folderButton);
                }
            }
        }
        setFolderButtons();
    }

    private void setFolderButtons() {
        List<JLabel> folderButtons = new LinkedList<>();
        folderButtons.add(folderButtonsGenerator.getFolderButtonLevelUp());
        folderButtons.addAll(folderButtonsFiltered);
        foldersPanel.setFolderButtons(folderButtons);
    }

}
