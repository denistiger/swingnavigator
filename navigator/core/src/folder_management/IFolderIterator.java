package folder_management;

import folder.IFolder;

public interface IFolderIterator {
    boolean hasNext();
    boolean hasPrev();
    void next();
    void prev();
    IFolder getNext();
    IFolder getPrev();
    void levelUp();
    IFolder getIFolder();
}
