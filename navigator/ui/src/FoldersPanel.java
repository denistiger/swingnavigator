import folder.FolderManager;
import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class FoldersPanel extends JPanel {
    private FolderManager folderManager;
    private FilePreviewGenerator previewGenerator;
    private List<PathListener> pathListenerList;
    private FlowLayout flowLayout;


    public FoldersPanel() {
        folderManager = new FolderManager();
        previewGenerator = new FilePreviewGenerator();
        pathListenerList = new LinkedList<>();

        flowLayout = new FlowLayout();
        setLayout(flowLayout);

        folderManager.openPath(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
        processNewPath();
    }

    private void openFolder(IFolder folder) {
        folderManager.openFolder(folder);
        processNewPath();
    }

    private void processNewPath() {
        List<IFolder> folders = folderManager.getFoldersAtPath();
        removeAll();
        if (folders != null) {
            for (IFolder folder : folders) {
                FolderButton folderButton = new FolderButton(folder);
                folderButton.setText(folder.getName());
                folderButton.setIcon(previewGenerator.getFilePreview(folder));
                folderButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openFolder(folderButton.getFolder());
                    }
                });
                add(folderButton);
            }
        }
        setPreferredSize(getPreferredSize());
        notifyOnPathChange();
        revalidate();
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
        folderManager.openPath(path);
        processNewPath();
    }
}
