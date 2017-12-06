import java.io.File;
import java.util.List;
import java.util.zip.ZipException;

public class ZipFile extends IFolder {

    public ZipFile(File file) throws ZipException {
        this.file = file;
        if (!ZipManager.isZipFile(this.file)) {
            throw new ZipException();
        }
    }
    @Override
    public List<IFolder> getItems() {
        return null;
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.ZIP_FILE;
    }
}
