import java.io.File;
import java.util.List;

public class CommonFile extends IFolder {

    public class NotACommonFileException extends Exception {

    }

    public CommonFile(File file) throws NotACommonFileException {
        this.file = file;
        if (!this.file.isFile()) {
            throw new NotACommonFileException();
        }
    }

    @Override
    public List<IFolder> getItems() {
        return null;
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.FILE;
    }
}
