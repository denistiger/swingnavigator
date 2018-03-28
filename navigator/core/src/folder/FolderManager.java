package folder;

import java.io.File;
import java.util.*;

public class FolderManager {

    public enum OpenFolderStatus {
        SUCCESS,
        HALF_PATH_OPENED,
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

        PathUtils curPath = new PathUtils(path);
        LinkedList<String> foldersToOpen = new LinkedList<>();

        while (curPath.getPath().length() > 0) {
            OpenFolderStatus status = openPathSimple(curPath.getPath());
            if (getFoldersAtPath() != null) {
                for (String folderName : foldersToOpen) {
                    if (!openFolder(folderName)) {
                        // Open as much as we can and consider it as success.
                        return OpenFolderStatus.HALF_PATH_OPENED;
                    }
                }
                return status;
            }
            foldersToOpen.add(curPath.pop());
        }
        return OpenFolderStatus.ERROR;

    }

    private OpenFolderStatus openPathSimple(String path) {
        cleanStack();
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

    public boolean openFolder(String folderName) {
        List<IFolder> folders = getFoldersAtPath();
        if (folders == null) {
            return false;
        }
        for (IFolder folder : folders) {
            if (folder.getName().compareTo(folderName) == 0) {
                openFolder(folder);
                return true;
            }
        }
        return false;
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
