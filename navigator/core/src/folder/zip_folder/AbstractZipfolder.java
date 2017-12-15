package folder.zip_folder;

import folder.IFolder;

public abstract class AbstractZipfolder implements IFolder {

    // TODO remove
    static final Character zipPathSeparator = '/';

    protected String name = null;

    protected String[] splitPath(String path) {
        return path.split(String.valueOf(zipPathSeparator));
    }

    protected String getLastName(String path) {
        String[] list = splitPath(path);
        if (list.length > 0) {
            return list[list.length - 1];
        }
        return "";
    }

}
