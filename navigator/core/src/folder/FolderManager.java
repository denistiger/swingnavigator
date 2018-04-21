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

    private Stack<IFolder> inDepthFolderStack;

    public FolderManager() {
        inDepthFolderStack = new Stack<>();
    }

    public OpenFolderStatus openPath(String path) {

        PathUtils curPath = new PathUtils(path);
        LinkedList<String> foldersToOpen = new LinkedList<>();

        OpenFolderStatus status = OpenFolderStatus.ERROR;
        while (curPath.getPath().length() > 0) {
            status = openPathSimple(curPath.getPath());
            if (status == OpenFolderStatus.FTP_CONNECTION_ERROR || status == OpenFolderStatus.FTP_CREDENTIALS_NEEDED) {
                return status;
            }
            if (getFoldersAtPath() != null) {
                for (String folderName : foldersToOpen) {
                    if (!openFolder(folderName)) {
                        // Open as much as we can and consider it as success.
                        return OpenFolderStatus.HALF_PATH_OPENED;
                    }
                }
                return status;
            }
            foldersToOpen.addFirst(curPath.pop());
        }
        return status;
    }

    public IFolder getCurrentFolder() {
        if (inDepthFolderStack != null && !inDepthFolderStack.isEmpty()) {
            return inDepthFolderStack.peek();
        }
        else {
            return null;
        }
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
        if (inDepthFolderStack.empty() || folder != inDepthFolderStack.peek()) {
            inDepthFolderStack.push(folder);
        }
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
            return null;
        }
        return inDepthFolderStack.peek().getItems();
    }

    public String getFullPath() {
        boolean useAbsolutePath = true;
        String path = "";
        for (IFolder folder : inDepthFolderStack) {
            if (useAbsolutePath) {
                path = folder.getAbsolutePath();
            }
            else {
                path += "/" + folder.getName();
            }
            if (!folder.isFileSystemPath()) {
                useAbsolutePath = false;
            }
        }

        if (!path.endsWith("/") && !path.endsWith("\\")) {
            List<IFolder> subFolders = getFoldersAtPath();
            if (subFolders != null /*&& !subFolders.isEmpty()*/) {
                path += "/";
            }
        }
        return path;
    }


}
