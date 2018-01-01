package folder;

import java.util.*;

public class FolderManager {

    private Stack<IFolder> inDepthFolderStack;
    private String basePath;

    public FolderManager() {
        inDepthFolderStack = new Stack<>();
    }

    public boolean openPath(String path) {
        inDepthFolderStack = new Stack<>();
        IFolderFactory factory = new UniversalFolderFactory();
        Map<String, Object> params = new HashMap<>();
        params.put(IFolderFactory.FILEPATH, path);
        try {
            inDepthFolderStack.push(factory.createIFolder(params));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void openFolder(IFolder folder) {
        inDepthFolderStack.push(folder);
    }

    public boolean levelUp() {
        if (inDepthFolderStack.size() < 2) {
            return false;
        }
        inDepthFolderStack.pop();
        return true;
    }

    public List<IFolder> getFoldersAtPath() {
        if (inDepthFolderStack.empty()) {
            return new ArrayList<>();
        }
        return inDepthFolderStack.peek().getItems();
    }

    private String getRelativePath() {
        String path = "";
        if (inDepthFolderStack.empty()) {
            return path;
        }
        for (IFolder folder : inDepthFolderStack) {
            // TODO fix / separator
            path += folder.getName() + "/";
        }
        return path.substring(0, path.length() - 1);
    }

    public String getFullPath() {
        if (inDepthFolderStack.empty()) {
            return basePath;
        }
        return basePath + "/" + getRelativePath();
    }


}
