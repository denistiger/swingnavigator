package folder.factory;

import folder.IFolder;
import folder.TopLevelFolder;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TopLevelFolderFactory implements IFolderFactory{

    @Override
    public IFolder createIFolder(Map<String, Object> params) throws Exception {
        List<IFolder> folders = new FolderFactory().createIFolderList(File.listRoots());
        if (folders.size() < 2) {
            return null;
        }
        return new TopLevelFolder(folders);
    }
}
