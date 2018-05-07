package ui;

import folder.*;
import folder_management.FolderIterator;
import folder_management.FolderManager;
import folder_management.IPathChangedListener;
import ui.file_preview.GenericPreviewPanel;
import ui.folder_button.FolderButton;
import ui.folder_button.FolderButtonSkeleton;
import ui.folder_button.FolderButtonsGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class FolderNavigatorBL implements IPathListener, IOpenFolderListener, IPathChangedListener,
        EditablePathManager.IPathEditListener {

    private enum KeyListenMode {
        NAVIGATION,
        ENTER_ADDRESS,
        NOT_LISTENING
    }

    private KeyListenMode keyListenMode = KeyListenMode.NAVIGATION;

    private JPanel mainPanel;
    private EditablePathManager editablePathManager;
    private JButton levelUpButton;
    private FoldersPanel foldersPanel;
    private JScrollPane foldersScrollPane;
    private GenericPreviewPanel previewPanel;

    private List<FolderButton> folderButtonsFiltered;
    private FolderManager folderManager;
    private List<IPathListener> pathListenerList;
    private FolderButtonsGenerator folderButtonsGenerator;
    private IFoldersPanelSelection foldersPanelSelection;
    private FolderIterator folderIterator;

    final static String FOLDERS_PANEL = "Folders grid panel";
    final static String PREVIEW_PANEL = "Files preview widget";
    private String panelMode;

    public FolderNavigatorBL(JPanel mainPanel, EditablePathManager editablePathManager, JButton levelUpButton) {
        this.mainPanel = mainPanel;
        this.levelUpButton = levelUpButton;
        this.editablePathManager = editablePathManager;

        folderManager = new FolderManager();
        pathListenerList = new LinkedList<>();
        folderButtonsFiltered = new LinkedList<>();
        folderButtonsGenerator = new FolderButtonsGenerator(this);
        folderManager.openPath(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());

        foldersPanel = new FoldersPanel();
        foldersPanelSelection = foldersPanel;
        addPathListener(this);

        editablePathManager.setPath(getCurrentPath());
        editablePathManager.addListener(this);

        this.levelUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelUp();
            }
        });


//        BorderLayout borderLayout = new BorderLayout();
//        mainPanel.setLayout(borderLayout);


        JPanel stretchPanel = new JPanelScrollableFolders(foldersPanel);

        foldersScrollPane = new JScrollPane(stretchPanel);
        foldersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        foldersScrollPane.setPreferredSize(new Dimension(800, 600));

        folderIterator = new FolderIterator(folderManager, this);
        previewPanel = new GenericPreviewPanel(folderIterator);

        mainPanel.setLayout(new CardLayout());
        mainPanel.add(foldersScrollPane, FOLDERS_PANEL);
        mainPanel.add(previewPanel, PREVIEW_PANEL);

//        changeMainPanelContentPane(foldersScrollPane);

        processNewPath();

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {

                    private long lastKeyEventTime = 0;
                    private static final int WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS = 0;
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (keyListenMode != KeyListenMode.NAVIGATION) {
                            return false;
                        }
                        if (e.getID() != KeyEvent.KEY_PRESSED) {
                            return false;
                        }
                        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_BACK_SPACE &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            levelUp();
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            if (panelMode.compareTo(PREVIEW_PANEL) == 0) {
                                folderIterator.next();
                            }
                            else {
                                foldersPanelSelection.next();
                            }
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_LEFT &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            if (panelMode.compareTo(PREVIEW_PANEL) == 0) {
                                folderIterator.prev();
                            }
                            else {
                                foldersPanelSelection.prev();
                            }
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_UP &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            if (panelMode.compareTo(FOLDERS_PANEL) == 0) {
                                foldersPanelSelection.up();
                            }
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            if (panelMode.compareTo(FOLDERS_PANEL) == 0) {
                                foldersPanelSelection.down();
                            }
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            if (panelMode.compareTo(FOLDERS_PANEL) == 0) {
                                foldersPanelSelection.pageDown();
                            }
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_PAGE_UP &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            if (panelMode.compareTo(FOLDERS_PANEL) == 0) {
                                foldersPanelSelection.pageUp();
                            }
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_ENTER &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            if (editablePathManager.getPath().compareTo(getCurrentPath()) == 0) {
                                foldersPanelSelection.getSelection().notifyIOpenFolderListener(FolderNavigatorBL.this);
                            }
                            else {
                                openPath(editablePathManager.getPath());
                            }
                            lastKeyEventTime = System.currentTimeMillis();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown() &&
                                (e.getWhen() - lastKeyEventTime) > WAIT_TIME_AFTER_LAST_PROCESSED_EVENT_MS) {
                            previewPanel.setFullScreen(panelMode.compareTo(FOLDERS_PANEL) == 0 ?
                                    false : !previewPanel.isFullScreen());
                            lastKeyEventTime = System.currentTimeMillis();
                        }

                        return false;
                    }
                });

    }

    private void setNewAddress() {
        openPath(editablePathManager.getPath());
    }

    private void filterFolders() {
        String enteredPath = editablePathManager.getPath();
        String currentPath = getCurrentPath();

        if (enteredPath.compareTo(currentPath) == 0 /*||
                folderButtonsFiltered.size() != folderButtonsGenerator.getFolderButtons().size()*/) {
            filterBySubString("");
            return;
        }

        if (enteredPath.compareTo(currentPath) == 0 || enteredPath.length() == 0) {
            return;
        }

        if (currentPath.startsWith(enteredPath)) {
            if (currentPath.length() == 1 + enteredPath.length()) {
                enteredPath = enteredPath.substring(0, enteredPath.length() - 1);
            }
            IFolder currentFolder = folderManager.getCurrentFolder();
            folderManager.levelUp();
            PathUtils folderManagerPathUtils = new PathUtils(folderManager.getFullPath());
            PathUtils enteredPathPathUtils = new PathUtils(enteredPath);

            if (enteredPathPathUtils.getPath().startsWith(folderManagerPathUtils.getPath())) {
                processNewPath();
                foldersPanelSelection.setSelection(currentFolder.getName());
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
        filterBySubString(filter);
    }

    @Override
    public void setPath(String path) {
        Runnable setPathText = new Runnable() {
            @Override
            public void run() {
                editablePathManager.setPath(path);
            }
        };
        SwingUtilities.invokeLater(setPathText);
    }


    public void openFolder(IFolder folder) {
        folderManager.openFolder(folder);
        processNewPath();
    }

    @Override
    public void selectFolder(FolderButtonSkeleton folderButton) {
        foldersPanelSelection.setSelection(folderButton);
    }

    private void notifyOnPathChange() {
        String path = getCurrentPath();
        for (IPathListener pathListener : pathListenerList) {
            pathListener.setPath(path);
        }
    }

    public void levelUp() {
        IFolder currentFolder = folderManager.getCurrentFolder();
        folderManager.levelUp();
        processNewPath();
        foldersPanelSelection.setSelection(currentFolder.getName());
    }

    public void addPathListener(IPathListener pathListener) {
        pathListenerList.add(pathListener);
    }

    public void removePathListener(IPathListener pathListener) {
        pathListenerList.remove(pathListener);
    }

    public String getCurrentPath() {
        return folderManager.getFullPath();
    }

    public void openPath(String path) {
        openPath(path, false);
    }

    public void openPath(String path, boolean forceOpenByPath) {
        folderManager.getPasswordManager().reset();
        if (!forceOpenByPath && path.startsWith(folderManager.getFullPath()) && folderButtonsFiltered.size() > 0) {
            folderManager.openFolder(foldersPanelSelection.getSelection().getFolder());
        }
        else {
            boolean notSucceed = true;
            FolderManager.OpenFolderStatus folderStatus = FolderManager.OpenFolderStatus.ERROR;
            String prevPath = folderManager.getFullPath();
            while (notSucceed) {
                notSucceed = false;
                folderStatus = folderManager.openPath(path);
                KeyListenMode keyListenModeBackup = keyListenMode;
                keyListenMode = KeyListenMode.NOT_LISTENING;
                switch (folderStatus) {
                    case FTP_CONNECTION_ERROR:
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(mainPanel),
                                "Failed to connect to FTP server",
                                "FTP error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case FTP_CREDENTIALS_NEEDED:
                        PasswordDialog passwordDialog = new PasswordDialog(
                                (JFrame) SwingUtilities.getWindowAncestor(mainPanel), folderManager.getPasswordManager());
                        if (passwordDialog.credentialsAreSet()) {
                            notSucceed = true;
                        }
                        break;
                }
                keyListenMode = keyListenModeBackup;
            }
            if (folderStatus != FolderManager.OpenFolderStatus.SUCCESS &&
                    folderStatus != FolderManager.OpenFolderStatus.HALF_PATH_OPENED) {
                folderManager.openPath(prevPath);
            }
        }
        processNewPath();
    }

    private void changeMainPanelContentPane(String mainPanelItem) {
        CardLayout cardLayout = (CardLayout)(mainPanel.getLayout());
        cardLayout.show(mainPanel, mainPanelItem);
    }

    private void processNewPath() {
        List<IFolder> folders = folderManager.getFoldersAtPath();
        levelUpButton.setEnabled(folderManager.getParent() != null);
        if (folders != null) {
            previewPanel.setFullScreen(false);
            folderButtonsFiltered = folderButtonsGenerator.createFolderButtons(folders);
            setFolderButtons();
            changeMainPanelContentPane(FOLDERS_PANEL);
            panelMode = FOLDERS_PANEL;
        }
        else {
            folderButtonsGenerator.setBackgroundMode(true);
            previewPanel.updatePreviewFile();
            changeMainPanelContentPane(PREVIEW_PANEL);
            panelMode = PREVIEW_PANEL;
        }
        notifyOnPathChange();
    }

    public void filterBySubString(String substring) {
        List<FolderButton> folderButtons = folderButtonsGenerator.getFolderButtons();
        if (substring.isEmpty()) {
            folderButtonsFiltered = folderButtons;
        }
        else {
            folderButtonsFiltered = new LinkedList<>();
            for (FolderButton folderButton : folderButtons) {
                if (folderButton.getFolder().getName().toLowerCase().contains(substring.toLowerCase())) {
                    folderButtonsFiltered.add(folderButton);
                }
            }
        }
        setFolderButtons();
    }

    private void setFolderButtons() {
        List<FolderButtonSkeleton> folderButtons = new LinkedList<>();
        if (folderManager.getParent() != null) {
            folderButtons.add(folderButtonsGenerator.getFolderButtonLevelUp());
        }
        folderButtons.addAll(folderButtonsFiltered);
        foldersPanel.setFolderButtons(folderButtons);
    }

    @Override
    public void folderManagerPathChanged() {
        processNewPath();
    }

    @Override
    public void newPathEntered() {
        openPath(editablePathManager.getPath(), true);
    }

    @Override
    public void pathAppended() {
        filterFolders();
    }

    @Override
    public void setEditableMode(boolean editableMode) {
        keyListenMode = editableMode ? KeyListenMode.ENTER_ADDRESS : KeyListenMode.NAVIGATION;
    }


}
