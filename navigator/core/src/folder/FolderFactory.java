package folder;

import folder.zip_folder.ZipFileFolder;

import java.io.File;
import java.util.*;

public class FolderFactory implements IFolderFactory {

    public IFolder createIFolder(Map<String, Object> params) {
        Object fileObj = params.get("File");
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
        // TODO learn lambda
        for (File file : files) {
            Map<String, Object> params = new HashMap<>();
            params.put(IFolderFactory.FILESTRING, file);
            IFolder folder = createIFolder(params);
            if (folder != null) {
                list.add(folder);
            }
        }
        list.sort(new Comparator<IFolder>() {
            @Override
            public int compare(IFolder iFolder, IFolder t1) {
                if (iFolder.getType() == IFolder.FolderTypes.FOLDER && t1.getType() != IFolder.FolderTypes.FOLDER) {
                    return -1;
                }
                if (iFolder.getType() != IFolder.FolderTypes.FOLDER && t1.getType() == IFolder.FolderTypes.FOLDER) {
                    return 1;
                }
                return iFolder.getName().compareTo(t1.getName());
            }
        });
        return list;
    }

}
