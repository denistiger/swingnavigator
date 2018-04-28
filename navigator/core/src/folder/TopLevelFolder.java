package folder;

import java.io.InputStream;
import java.util.List;

public class TopLevelFolder implements IFolder {

    private List<IFolder> folders;

    public TopLevelFolder(List<IFolder> folders) {
        this.folders = folders;
    }

    @Override
    public List<IFolder> getItems() {
        return folders;
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.FOLDER;
    }

    @Override
    public String getName() {
        return "TopLevelFolder";
    }

    @Override
    public String getAbsolutePath() {
        return "";
    }

    @Override
    public boolean isFileSystemPath() {
        return false;
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }
}
