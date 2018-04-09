package folder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class LocalFolder implements IFolder, ILevelUp {

    private File file;

    @Override
    public boolean levelUp() {
        File prev = file;
        try {
            initFromFile(file.getParentFile());
            return true;
        } catch (NotALocalFolderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullInitializedFolderException e) {
            e.printStackTrace();
        }
        file = prev;
        return false;
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public boolean isFileSystemPath() {
        return true;
    }

    private void initFromFile(File file) throws NotALocalFolderException, FileNotFoundException, NullInitializedFolderException {
        if (file == null) {
            throw new NullInitializedFolderException();
        }
        this.file = file;
        if (!this.file.exists()) {
            throw new FileNotFoundException();
        }
        if (!this.file.isDirectory()) {
            throw new NotALocalFolderException();
        }
    }

    public class NotALocalFolderException extends Exception {

    }

    public class NullInitializedFolderException extends Exception {

    }

    public LocalFolder(File file) throws FileNotFoundException, NotALocalFolderException, NullInitializedFolderException {
        initFromFile(file);
    }

    public LocalFolder(String path) throws FileNotFoundException, NotALocalFolderException, NullInitializedFolderException {
        this(new File(path));
    }
    @Override
    public List<IFolder> getItems() {
        File[] files = file.listFiles();
        // TODO fix FolderFactory usage
        return new FolderFactory().createIFolderList(files);
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.FOLDER;
    }

    public String getName() {
        return file.getName();
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }

}
