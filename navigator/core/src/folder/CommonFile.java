package folder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class CommonFile extends FileSystemEntity {

    public class NotACommonFileException extends Exception {

    }

    public CommonFile(File file) throws NotACommonFileException, FileNotFoundException, NullInitializedFolderException {
        super(file);
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
        return FileTypeGetter.getFileType(file.getName());
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
