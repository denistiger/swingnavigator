package folder;

import java.util.HashMap;
import java.util.Map;

public class FileTypeGetter {

    private static final Map<String, IFolder.FolderTypes> typesMap;
    static {
        typesMap = new HashMap<>();
        typesMap.put("zip", IFolder.FolderTypes.ZIP);
        typesMap.put("jar", IFolder.FolderTypes.ZIP);
        typesMap.put("jpg", IFolder.FolderTypes.IMAGE);
        typesMap.put("png", IFolder.FolderTypes.IMAGE);
        typesMap.put("bmp", IFolder.FolderTypes.IMAGE);
        typesMap.put("jpeg", IFolder.FolderTypes.IMAGE);
        typesMap.put("gif", IFolder.FolderTypes.IMAGE);
        typesMap.put("txt", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("java", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("cpp", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("py", IFolder.FolderTypes.TEXT_FILE);
    }

    public static IFolder.FolderTypes getFileType(String name) {
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == name.length() - 1) {
            return IFolder.FolderTypes.UNKNOWN;
        }
        return typesMap.getOrDefault(name.substring(dotIndex + 1).toLowerCase(), IFolder.FolderTypes.OTHER_FILE);
    }

    public static boolean isFolderType(IFolder.FolderTypes type) {
        if (type == IFolder.FolderTypes.FOLDER || type == IFolder.FolderTypes.ZIP) {
            return true;
        }
        return false;
    }
}
