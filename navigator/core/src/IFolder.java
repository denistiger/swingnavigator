import java.io.File;
import java.util.List;

public abstract class IFolder {
    public enum FolderTypes {
        FOLDER,
        ZIP_FILE,
        FILE
    }
    public abstract List<IFolder> getItems();
    public abstract FolderTypes getType();

    public String getName() {
        return file.getName();
    }

    protected File file;
}
