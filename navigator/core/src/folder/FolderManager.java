package folder;

import folder.factory.IFolderFactory;
import folder.factory.UniversalFolderFactory;
import folder.ftp_folder.FTPClientWrapper;
import folder.ftp_folder.FTPFolder;

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

    private Deque<IFolder> inDepthFolderStack;

    public FolderManager() {
        inDepthFolderStack = new LinkedList<>();
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
            return inDepthFolderStack.peekLast();
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
            inDepthFolderStack.addLast(folder);
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
        while (!inDepthFolderStack.isEmpty()) {
            folder = inDepthFolderStack.pollLast();
        }
        if (folder != null && folder.getClass() == FTPFolder.class) {
            ((FTPFolder)folder).disconnect();
        }
    }

    public void openFolder(IFolder folder) {
        if (inDepthFolderStack.isEmpty() || folder != inDepthFolderStack.getLast()) {
            inDepthFolderStack.addLast(folder);
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

    public IFolder getParent() {
        if (inDepthFolderStack.size() == 0) {
            return null;
        }
        if (inDepthFolderStack.size() > 1) {
            Iterator<IFolder> iterator = inDepthFolderStack.descendingIterator();
            iterator.next();
            return iterator.next();
        }

        IFolder folder = inDepthFolderStack.getFirst();
        if (folder instanceof ILevelUp) {
            IFolder levelUpFolder = ((ILevelUp) folder).levelUp();
            if (levelUpFolder == null) {
                return null;
            }
            // Some optimization - do not find parent multiple times.
            inDepthFolderStack.addFirst(levelUpFolder);
            return levelUpFolder;
        }
        return null;
    }

    public OpenFolderStatus levelUp() {
        if (inDepthFolderStack.size() == 0) {
            return OpenFolderStatus.ERROR;
        }
        if (inDepthFolderStack.size() == 1) {
            IFolder folder = inDepthFolderStack.getLast();
            if (folder instanceof ILevelUp) {
                IFolder levelUpFolder = ((ILevelUp) folder).levelUp();
                if (levelUpFolder == null) {
                    return OpenFolderStatus.ERROR;
                }
                inDepthFolderStack.removeLast();
                inDepthFolderStack.addLast(levelUpFolder);
                return OpenFolderStatus.SUCCESS;
            }
            return OpenFolderStatus.ERROR;
        }
        inDepthFolderStack.removeLast();
        return OpenFolderStatus.SUCCESS;
    }

    public List<IFolder> getFoldersAtPath() {
        if (inDepthFolderStack.isEmpty()) {
            return null;
        }
        return inDepthFolderStack.getLast().getItems();
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
        path = path.replaceAll("\\\\+", "\\\\"); // Fix Windows returns \\ after disk label
        return path;
    }


}
