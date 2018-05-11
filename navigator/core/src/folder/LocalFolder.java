package folder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LocalFolder extends FileSystemEntity {


    public LocalFolder(File file) throws FileNotFoundException, NotALocalFolderException, NullInitializedFolderException {
        super(file);
        if (!this.file.isDirectory()) {
            throw new NotALocalFolderException();
        }
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.FOLDER;
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }
}
