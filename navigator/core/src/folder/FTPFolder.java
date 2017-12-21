package folder;

import org.apache.commons.net.ftp.*;
import folder.zip_folder.ZipOnFTPFolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTPFolder implements IFolder {

    FTPClientWrapper ftp = null;
    String localFTPPath;
    FolderTypes type;
    String name;

    public FTPFolder(String ftpPath) {
        localFTPPath = "";
        type = FolderTypes.FOLDER;
        ftp = new FTPClientWrapper(ftpPath);
    }

    public FTPFolder(FTPClientWrapper client, String prefix, FolderTypes type, String name) {
        ftp = client;
        localFTPPath = prefix;
        this.type = type;
        this.name = name;
    }

    public void setCredentials(String login, String pass) {
        ftp.setCredentials(login, pass);
    }


    @Override
    public List<IFolder> getItems() {
        List<IFolder> items = new ArrayList<>();
        try {
            boolean result = ftp.connect();
            System.out.println("Connected status " + result);
            FTPFile[] files = ftp.listFiles("//" + localFTPPath);
            if (files.length == 0) {
                System.out.println("No FTP files. Reply code is: " + ftp.getReplyCode());
            }
            if (files.length == 1){
                if (files[0].getName().compareTo(getName()) == 0) {
                    return null;
                }
            }
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
        ftp.disconnect();
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
}
