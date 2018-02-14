package folder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class CommonFile implements IFolder {

    private File file;

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

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
        return FileTypeGetter.getFileType(file.getName());
    }

    public String getName() {
        return file.getName();
    }

    @Override
    public InputStream getInputStream() {
        try {
            InputStream inputStream = new FileInputStream(file);
            return inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
