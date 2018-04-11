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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class FolderNavigatorBL implements PathListener{
    private JPanel mainPanel;
    private  JTextField pathText;
    private FoldersPanel foldersPanel;

    private Vector<FolderButton> folderButtons, folderButtonsFiltered;
    private ImageIcon imageIcon;
    private LazyIconLoader lazyIconLoader;
    private FolderManager folderManager;
    private FilePreviewGenerator previewGenerator;
    private List<PathListener> pathListenerList;


    public FolderNavigatorBL(JPanel mainPanel, JTextField pathText) {
        this.mainPanel = mainPanel;

        folderManager = new FolderManager();
        previewGenerator = new FilePreviewGenerator();
        pathListenerList = new LinkedList<>();
        folderButtons = new Vector<>();
        folderButtonsFiltered = new Vector<>();
        imageIcon = null;
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


    private void openFolder(IFolder folder) {
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
            folderButtons = new Vector<>();
            if (lazyIconLoader != null) {
                lazyIconLoader.stop();
            }
            lazyIconLoader = new LazyIconLoader();
            for (IFolder folder : folders) {
                FolderButton folderButton;
                if (folder.getType() == IFolder.FolderTypes.IMAGE || folder.getType() == IFolder.FolderTypes.TEXT_FILE) {
                    folderButton = new FolderButton(folder, previewGenerator.getLazyLoadIcon(folder));
                    lazyIconLoader.addListener(folderButton, folder);
                }
                else {
                    folderButton = new FolderButton(folder, previewGenerator.getFilePreview(folder));
                }
                folderButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            openFolder(folderButton.getFolder());
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                folderButtons.add(folderButton);
            }
            folderButtonsFiltered = folderButtons;
            lazyIconLoader.start();
            setUpFolderButtonDimensions();
            foldersPanel.setFolderButtons(folderButtonsFiltered);
            //TODO Switch to foldersPanel mode;
        }
        else {
            IFolder file = folderManager.getCurrentFolder();
            if (file == null) {
                return;
            }
            imageIcon = previewGenerator.getFilePreviewLarge(file);
            if (imageIcon != null) {
                folderButtons.clear();
            }
            //TODO Switch to file preview mode;
        }
        notifyOnPathChange();
    }

    public void filterByPrefix(String prefix) {
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

    private void setUpFolderButtonDimensions() {
        int maxWidth = 10, maxHeight = 10;
        for (FolderButton folderButton : folderButtons) {
            if (folderButton.getPreferredSize().getWidth() > maxWidth) {
                maxWidth = (int)folderButton.getPreferredSize().getWidth();
            }
            if (folderButton.getPreferredSize().getHeight() > maxHeight) {
                maxHeight = (int)folderButton.getPreferredSize().getHeight();
            }
        }
        for (FolderButton folderButton : folderButtons) {
            folderButton.setMinimumSize(new Dimension(maxWidth, maxHeight));
            folderButton.setPreferredSize(new Dimension(maxWidth, maxHeight));
            folderButton.setMaximumSize(new Dimension(maxWidth, maxHeight));
            folderButton.setAlignmentY(Component.TOP_ALIGNMENT);
            folderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }


}
