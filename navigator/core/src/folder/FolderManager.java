package folder;

import java.io.File;
import java.util.*;

public class FolderManager {

    public enum OpenFolderStatus {
        SUCCESS,
        ERROR,
        FTP_CONNECTION_ERROR,
        FTP_CREDENTIALS_NEEDED
    }

    private static Map<FTPClientWrapper.FTPStatus, OpenFolderStatus> ftpToFolderStatus;
    static {
        ftpToFolderStatus = new TreeMap<>();
        ftpToFolderStatus.put(FTPClientWrapper.FTPStatus.SUCCESS, OpenFolderStatus.SUCCESS);
        ftpToFolderStatus.put(FTPClientWrapper.FTPStatus.WRONG_CREDENTIALS, OpenFolderStatus.FTP_CREDENTIALS_NEEDED);
        ftpToFolderStatus.put(FTPClientWrapper.FTPStatus.ERROR, OpenFolderStatus.FTP_CONNECTION_ERROR);
    }

    private class PathUtils {
        private String path, pathPrefix;
        private Character separator;
        private int pathBegin;

        public PathUtils(String path) {
            this.path = path;
            separator = '/';
            if (!path.contains("/") && path.contains("\\")) {
                separator = '\\';
            }
            if (path.lastIndexOf(separator) != path.length() - 1) {
                path += separator;
            }
            pathBegin = path.indexOf("//") + 2;
            pathPrefix = path.substring(0, pathBegin);
            if (path.lastIndexOf(separator) == path.length() - 1) {
                path = path.substring(0, path.length() - 1);
            }
        }

        public void push(String name) {
            path += name + separator;
        }

        public String pop() {
            int sepPos = path.lastIndexOf(separator);
            if (sepPos == -1) {
                String out = path;
                path = "";
                return path;
            }
            String res = path.substring(sepPos + 1);
            path = path.substring(0, sepPos);
            return res;
        }

        public String getPath() {
            return path;
        }
    }

    private Stack<IFolder> inDepthFolderStack;

    public FolderManager() {
        inDepthFolderStack = new Stack<>();
    }

    public OpenFolderStatus openPath(String path) {
        cleanStack();
        PathUtils initialPath = new PathUtils(path);
        initialPath.pop();
        IFolderFactory factory = new UniversalFolderFactory();
        Map<String, Object> params = new HashMap<>();
        params.put(IFolderFactory.FILEPATH, path);

        try {
            IFolder folder = factory.createIFolder(params);
            if (folder == null) {
                return OpenFolderStatus.ERROR;
            }
            if (folder.getClass() == FTPFolder.class) {
                FTPClientWrapper.FTPStatus ftpStatus = ((FTPFolder)folder).connect();
                OpenFolderStatus status = ftpToFolderStatus.get(ftpStatus);
                if (status == null) {
                    return OpenFolderStatus.FTP_CONNECTION_ERROR;
                }
                if (status != OpenFolderStatus.SUCCESS) {
                    return status;
                }
            }
            inDepthFolderStack.push(folder);
            return OpenFolderStatus.SUCCESS;
        } catch (CommonFile.NotACommonFileException e) {
            return OpenFolderStatus.FTP_CONNECTION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpenFolderStatus.ERROR;
    }

    private void cleanStack() {
        IFolder folder = null;
        while (!inDepthFolderStack.empty()) {
            folder = inDepthFolderStack.pop();
        }
        if (folder != null && folder.getClass() == FTPFolder.class) {
            ((FTPFolder)folder).disconnect();
        }
    }

    public void openFolder(IFolder folder) {
        inDepthFolderStack.push(folder);
    }

    public OpenFolderStatus levelUp() {
        if (inDepthFolderStack.size() == 0) {
            return OpenFolderStatus.ERROR;
        }
        if (inDepthFolderStack.size() == 1) {
            IFolder folder = inDepthFolderStack.peek();
            if (folder instanceof ILevelUp) {
                return ((ILevelUp) folder).levelUp() ? OpenFolderStatus.SUCCESS : OpenFolderStatus.ERROR;
            }
            String absolutePath = folder.getAbsolutePath();
            File file = new File(absolutePath);
            return openPath(file.getParent());
        }
        inDepthFolderStack.pop();
        return OpenFolderStatus.SUCCESS;
    }

    public List<IFolder> getFoldersAtPath() {
        if (inDepthFolderStack.empty()) {
            return new ArrayList<>();
        }
        return inDepthFolderStack.peek().getItems();
    }

    public String getFullPath() {
        boolean firstFolder = true;
        String path = "";
        for (IFolder folder : inDepthFolderStack) {
            if (firstFolder) {
                path = folder.getAbsolutePath();
                firstFolder = false;
            }
            else {
                path += "/" + folder.getName();
            }
        }
        return path;
    }


}
