import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class LocalFolder implements IFolder {

    private File file;


    public class NotALocalFolderException extends Exception {

    }

    public LocalFolder(File file) throws FileNotFoundException, NotALocalFolderException {
        this.file = file;
        if (!this.file.exists()) {
            throw new FileNotFoundException();
        }
        if (!this.file.isDirectory()) {
            throw new NotALocalFolderException();
        }
    }

    public LocalFolder(String path) throws FileNotFoundException, NotALocalFolderException {
        this(new File(path));
    }
    @Override
    public List<IFolder> getItems() {
        File[] files = file.listFiles();
        return FolderFactory.createIFolderList(files);
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.FOLDER;
    }

    public String getName() {
        return file.getName();
    }
}
