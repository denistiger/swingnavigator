package folder;

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

    private Stack<IFolder> inDepthFolderStack;
    private String basePath;

    public FolderManager() {
        inDepthFolderStack = new Stack<>();
    }

    public OpenFolderStatus openPath(String path) {
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
