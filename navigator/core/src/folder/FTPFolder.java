package folder;

import org.apache.commons.net.ftp.*;
import folder.zip_folder.ZipOnFTPFolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FTPFolder implements IFolder, ILevelUp {

    FTPClientWrapper ftp;
    String localFTPPath;
    FolderTypes type;
    String name;
    List<IFolder> items = null;

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

    public FTPFolder(FTPClientWrapper client, String prefix, FolderTypes type, String name) {
        ftp = client;
        localFTPPath = prefix;
        stripLocalFTPPath();
        this.type = type;
        this.name = name;
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

    public void setCredentials(String login, String pass) {
        ftp.setCredentials(login, pass);
    }

    public FTPClientWrapper.FTPStatus connect() {
        return ftp.connect();
    }
    public void disconnect() {
        ftp.disconnect();
    }

    public void dropCache() {
        items = null;
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
//            if (files.length == 1){
//                if (files[0].getName().compareTo(getName()) == 0) {
//                    return null;
//                }
//            }
            for (FTPFile file : files) {
                if (file.isDirectory()) {
                    items.add(new FTPFolder(ftp, localFTPPath.isEmpty() ? file.getName() : localFTPPath + "/" + file.getName(),
                            FolderTypes.FOLDER, file.getName()));
                }else if (FileTypeGetter.getFileType(file.getName()) == FolderTypes.ZIP) {
                    items.add(new ZipOnFTPFolder(ftp, localFTPPath.isEmpty() ? file.getName() : localFTPPath + "/" + file.getName(),
                            file.getName()));
                }else {
                    items.add(new FTPFolder(ftp, localFTPPath.isEmpty() ? file.getName() : localFTPPath + "/" + file.getName(),
                            FileTypeGetter.getFileType(file.getName()), file.getName()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        return name;
    }

    @Override
    public String getAbsolutePath() {
        String path = ftp.getFTPPath();
        if (!localFTPPath.isEmpty()) {
            path += "/" + localFTPPath;
        }
        return path;
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
    public boolean levelUp() {
        return ftp.levelUp();
    }
}
