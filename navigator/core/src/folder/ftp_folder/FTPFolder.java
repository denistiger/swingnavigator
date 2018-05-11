package folder.ftp_folder;

import folder.*;
import folder.ILevelUp;
import folder_management.PasswordManager;
import org.apache.commons.net.ftp.*;
import folder.zip_folder.ZipOnFTPFolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FTPFolder implements IFolder, ILevelUp, IPrependFTPPath {

    private FTPClientWrapper ftp;
    private String localFTPPath;
    private FolderTypes type;
    private String name = null;
    private List<IFolder> items = null;
    private FTPFolder parent = null;

    public FTPFolder(String ftpPath) {
        localFTPPath = "";
        type = FolderTypes.FOLDER;
        ftp = new FTPClientWrapper(ftpPath);
    }

    public FTPFolder(String ftpPath, int ftpPort) {
        localFTPPath = "";
        type = FolderTypes.FOLDER;
        ftp = new FTPClientWrapper(ftpPath, ftpPort);
    }

    public FTPFolder(String ftpPath, int ftpPort, String localFTPPath) {
        this.localFTPPath = localFTPPath;
        stripLocalFTPPath();
        type = FolderTypes.FOLDER;
        ftp = new FTPClientWrapper(ftpPath, ftpPort);
    }

    private FTPFolder(FTPFolder parent, FTPClientWrapper client, String prefix, FolderTypes type, String name) {
        ftp = client;
        localFTPPath = prefix;
        stripLocalFTPPath();
        this.type = type;
        this.name = name;
        this.parent = parent;
    }

    public void setPasswordManager(PasswordManager passwordManager) {
        ftp.setPasswordManager(passwordManager);
    }

    @Override
    public void prependDirectoryToPath(String workingDirectory) {
        localFTPPath = workingDirectory + getSeparator() + localFTPPath;
        if (items != null) {
            for (IFolder folder : items) {
                if (folder instanceof IPrependFTPPath) {
                    ((IPrependFTPPath) folder).prependDirectoryToPath(workingDirectory);
                }
            }
        }
    }

    private void stripLocalFTPPath() {
        while (localFTPPath.contains("//")) {
            localFTPPath = localFTPPath.replaceAll("//", "/");
        }
        while (localFTPPath.startsWith("/")) {
            localFTPPath = localFTPPath.substring(1);
        }
        while (localFTPPath.endsWith("/")) {
            localFTPPath = localFTPPath.substring(0, localFTPPath.length() - 1);
        }
    }

    public FTPClientWrapper.FTPStatus connect() {
        return ftp.connect();
    }
    public void disconnect() {
        ftp.disconnect();
    }

    @Override
    public List<IFolder> getItems() {
        if (items != null) {
            return items;
        }
        try {
            FTPFile[] files = ftp.listFiles("//" + localFTPPath);
            if (files == null) {
                return null;
            }
            items = new ArrayList<>();
            if (files.length == 0) {
                System.out.println("No FTP files. Reply code is: " + ftp.getReplyCode());
            }

            for (FTPFile file : files) {
                if (file.isDirectory()) {
                    items.add(new FTPFolder(this, ftp, localFTPPath.isEmpty() ? file.getName() : localFTPPath + "/" + file.getName(),
                            FolderTypes.FOLDER, file.getName()));
                }else if (FileTypeGetter.getFileType(file.getName()) == FolderTypes.ZIP) {
                    items.add(new ZipOnFTPFolder(ftp, localFTPPath.isEmpty() ? file.getName() : localFTPPath + "/" + file.getName(),
                            file.getName()));
                }else {
                    items.add(new FTPFolder(this, ftp, localFTPPath.isEmpty() ? file.getName() : localFTPPath + "/" + file.getName(),
                            FileTypeGetter.getFileType(file.getName()), file.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public FolderTypes getType() {
        return type;
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }
        if (!localFTPPath.isEmpty()) {
            name = new PathUtils(localFTPPath).pop();
        }
        else {
            name = "";
        }
        return name;
    }

    @Override
    public String getAbsolutePath() {
        String path = ftp.getFTPPath();
        if (!localFTPPath.isEmpty()) {
            path += getSeparator() + localFTPPath;
        }
        return path;
    }

    @Override
    public Character getSeparator() {
        return '/';
    }

    @Override
    public boolean isFileSystemPath() {
        return false;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return ftp.retrieveFileStream(localFTPPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IFolder levelUp() {

        if (parent != null) {
            return parent;
        }

        if (!localFTPPath.isEmpty()) {
            PathUtils pathUtils = new PathUtils(localFTPPath);
            pathUtils.pop();
            String newLocalFTPPath = pathUtils.getPath();
            parent = new FTPFolder(null, ftp, newLocalFTPPath, FolderTypes.FOLDER, null);
            return parent;
        }
        return null;

    }
}
