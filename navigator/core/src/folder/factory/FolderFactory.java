package folder.factory;

import folder.CommonFile;
import folder.FileTypeGetter;
import folder.IFolder;
import folder.LocalFolder;
import folder.zip_folder.ZipFileFolder;

import java.io.File;
import java.util.*;

public class FolderFactory implements IFolderFactory {

    public void sortIFoldersList(List<IFolder> iFolders) {
        iFolders.sort((IFolder iFolder, IFolder t1) -> {
            if (iFolder.getType() == IFolder.FolderTypes.FOLDER && t1.getType() != IFolder.FolderTypes.FOLDER) {
                return -1;
            }
            if (iFolder.getType() != IFolder.FolderTypes.FOLDER && t1.getType() == IFolder.FolderTypes.FOLDER) {
                return 1;
            }
            return iFolder.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
        });
    }

    public IFolder createIFolder(Map<String, Object> params) {
        Object fileObj = params.get(FILE);
        File file = (File) fileObj;
        try {
            if (file.isDirectory()) {
                return new LocalFolder(file);
            }
            if (FileTypeGetter.getFileType(file.getName()) == IFolder.FolderTypes.ZIP) {
                return new ZipFileFolder(file);
            }
            return new CommonFile(file);
        } catch (Exception er) {
            er.printStackTrace();
        }
        return null;
    }

    public List<IFolder> createIFolderList(File[] files) {
        List<IFolder> list = new ArrayList<>();
        if (files == null) {
            return list;
        }
        for (File file : files) {
            Map<String, Object> params = new HashMap<>();
            params.put(FILE, file);
            IFolder folder = createIFolder(params);
            if (folder != null) {
                list.add(folder);
            }
        }
        sortIFoldersList(list);
        return list;
    }

}
