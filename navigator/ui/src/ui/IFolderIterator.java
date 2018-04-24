package ui;

import folder.IFolder;

public interface IFolderIterator {
    boolean hasNext();
    boolean hasPrev();
    void next();
    void prev();
    void levelUp();
    IFolder getIFolder();
}
