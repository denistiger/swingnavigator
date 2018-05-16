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
        typesMap.put("tiff", IFolder.FolderTypes.IMAGE);
        typesMap.put("tif", IFolder.FolderTypes.IMAGE);
        typesMap.put("txt", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("java", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("cpp", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("py", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("ini", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("xml", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("obj", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("mtl", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("sh", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("bat", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("h", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("c", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("cmake", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("cfg", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("cmd", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("in", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("thrift", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("qsi", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("qss", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("css", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("pro", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("profile", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("json", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("htm", IFolder.FolderTypes.TEXT_FILE);
        typesMap.put("html", IFolder.FolderTypes.TEXT_FILE);
    }

    public static void main(String[] args) {
        System.out.println(typesMap.keySet());
    }


    public static IFolder.FolderTypes getFileType(String name) {
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == name.length() - 1) {
            return IFolder.FolderTypes.UNKNOWN;
        }
        return typesMap.getOrDefault(name.substring(dotIndex + 1).toLowerCase(), IFolder.FolderTypes.OTHER_FILE);
    }

    public static boolean isFolderType(IFolder.FolderTypes type) {
        return  type == IFolder.FolderTypes.FOLDER || type == IFolder.FolderTypes.ZIP;
    }
}
