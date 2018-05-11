package folder;

import folder.factory.FolderFactory;
import folder.factory.TopLevelFolderFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class FileSystemEntity implements IFolder, ILevelUp {
    protected File file;

    public class NotALocalFolderException extends Exception {

    }

    public class NullInitializedFolderException extends Exception {

    }

    @Override
    public IFolder levelUp() {
        try {
            if (file.getParentFile() == null) {
                return new TopLevelFolderFactory().createIFolder(null);
            }
            return new LocalFolder(file.getParentFile());
        } catch (NotALocalFolderException | FileNotFoundException | NullInitializedFolderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public Character getSeparator() {
        return File.separatorChar;
    }

    @Override
    public boolean isFileSystemPath() {
        return true;
    }


    private void initFromFile(File file) throws FileNotFoundException, NullInitializedFolderException {
        if (file == null) {
            throw new NullInitializedFolderException();
        }
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        this.file = file;
    }

    FileSystemEntity(File file) throws FileNotFoundException, NullInitializedFolderException {
        initFromFile(file);
    }

    @Override
    public List<IFolder> getItems() {
        File[] files = file.listFiles();
        // TODO fix FolderFactory usage
        return new FolderFactory().createIFolderList(files);
    }

    @Override
    public IFolder.FolderTypes getType() {
        return IFolder.FolderTypes.FOLDER;
    }

    @Override
    public String getName() {
        if (!file.getName().isEmpty()) {
            return file.getName();
        }
        // For C:\
        return file.getPath();
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }

}
