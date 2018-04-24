package ui;

import folder.FolderManager;
import folder.IFolder;

import java.util.List;
import java.util.Vector;

public class FolderIterator implements IFolderIterator{

    private IFolder currentFolder = null;
    private IFolder currentFile = null;
    private Vector<IFolder> folderFilesArray = null;
    private int folderFilesIdx = 0;
    private IPathChangedListener pathChangedListener;
    private FolderManager folderManager;

    public FolderIterator(FolderManager folderManager, IPathChangedListener pathChangedListener) {
        this.folderManager = folderManager;
        this.pathChangedListener = pathChangedListener;
        resetFoldersIfNeeded();
    }

    private IFolder getFolderManagerFolder() {
        return folderManager.getParent();
    }

    private IFolder getFolderManagerFile() {
        return folderManager.getCurrentFolder();
    }

    private List<IFolder> getFolderFilesArray() {
        return folderManager.getParent().getItems();
    }

    private void resetFoldersIfNeeded() {
        if (currentFolder != getFolderManagerFolder()
                || currentFile != getFolderManagerFile()) {
            currentFolder = getFolderManagerFolder();
            currentFile = getFolderManagerFile();
            List<IFolder> folderList = getFolderFilesArray();
            folderFilesArray = new Vector<>(folderList.size());
            folderFilesArray.addAll(folderList);
            folderFilesIdx = 0;
            for (IFolder folder : folderFilesArray) {
                if (folder.getAbsolutePath().compareTo(currentFile.getAbsolutePath()) == 0) {
                    break;
                }
                folderFilesIdx++;
            }
            if (folderFilesIdx >= folderFilesArray.size()) {
                try {
                    throw new Exception("Folder iterator cache is corrupted");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        resetFoldersIfNeeded();
        if (folderFilesIdx < folderFilesArray.size() - 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPrev() {
        resetFoldersIfNeeded();
        if (folderFilesIdx > 0) {
            return true;
        }
        return false;
    }

    private void openAtCurrentIndex() {
        folderManager.levelUp();
        folderManager.openFolder(folderFilesArray.get(folderFilesIdx));
        pathChangedListener.folderManagerPathChanged();
    }

    @Override
    public void next() {
        if (hasNext()) {
            folderFilesIdx++;
            openAtCurrentIndex();
        }
    }

    @Override
    public void prev() {
        if (hasPrev()) {
            folderFilesIdx--;
            openAtCurrentIndex();
        }
    }

    @Override
    public void levelUp() {
        folderManager.levelUp();
        resetFoldersIfNeeded();
        pathChangedListener.folderManagerPathChanged();
    }

    @Override
    public IFolder getIFolder() {
        resetFoldersIfNeeded();
        return folderFilesArray.get(folderFilesIdx);
    }
}
