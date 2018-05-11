package folder;

import java.io.InputStream;
import java.util.List;

public interface IFolder {
    enum FolderTypes {
        FOLDER,
        ZIP,
        TEXT_FILE,
        IMAGE,
        OTHER_FILE,
        UNKNOWN
    }

    List<IFolder> getItems();
    FolderTypes getType();
    String getName();
    String getAbsolutePath();
    Character getSeparator();

    boolean isFileSystemPath();

    InputStream getInputStream();
}
