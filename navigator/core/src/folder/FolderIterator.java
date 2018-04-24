package folder;

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
        if (getFolderManagerFile().getType() == IFolder.FolderTypes.FOLDER
                || getFolderManagerFile().getType() == IFolder.FolderTypes.ZIP) {
            // Do not process folders in iterator
            return;
        }
        if (currentFolder != getFolderManagerFolder()
                || currentFile != getFolderManagerFile()) {
            currentFolder = getFolderManagerFolder();
            currentFile = getFolderManagerFile();
            List<IFolder> folderList = getFolderFilesArray();
            folderFilesArray = new Vector<>();
            for (IFolder folder : folderList) {
                if (folder.getType() != IFolder.FolderTypes.FOLDER && folder.getType() != IFolder.FolderTypes.ZIP) {
                    folderFilesArray.add(folder);
                }
            }
            folderFilesIdx = 0;
            for (IFolder folder : folderFilesArray) {
                if (folder.getName().compareTo(currentFile.getName()) == 0) {
                    break;
                }
                folderFilesIdx++;
            }
            if (folderFilesIdx >= folderFilesArray.size()) {
                try {
                    throw new Exception("Cache is corrupted");
                } catch (Exception e) {
//                    e.printStackTrace();
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
    public IFolder getNext() {
        if (!hasNext()) {
            return null;
        }
        return folderFilesArray.get(folderFilesIdx + 1);
    }

    @Override
    public IFolder getPrev() {
        if (!hasPrev()) {
            return null;
        }
        return folderFilesArray.get(folderFilesIdx - 1);
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
