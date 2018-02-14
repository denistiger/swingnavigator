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
        }
        file = prev;
        return false;
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    private void initFromFile(File file) throws NotALocalFolderException, FileNotFoundException {
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

    public LocalFolder(File file) throws FileNotFoundException, NotALocalFolderException {
        initFromFile(file);
    }

    public LocalFolder(String path) throws FileNotFoundException, NotALocalFolderException {
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
