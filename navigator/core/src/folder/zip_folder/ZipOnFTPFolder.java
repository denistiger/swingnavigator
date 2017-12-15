package folder.zip_folder;

import folder.IFolderFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipOnFTPFolder extends ZipStreamFolder {

    FTPClient ftpClient;
    String ftpPath;

    public ZipOnFTPFolder(FTPClient ftpClient, String ftpPath, String name) throws Exception {
        this.ftpClient = ftpClient;
        this.ftpPath = ftpPath;
        this.inZipPath = "";
        this.name = name;
        this.factory = new ZipOnFTPFactory(ftpClient, ftpPath);
        this.type = FolderTypes.ZIP_FILE;
        initChildren();
    }

    public ZipOnFTPFolder(FTPClient ftpClient, String ftpPath, String inZipPath, List<String[]> entries, IFolderFactory factory) throws Exception {
        this.ftpClient = ftpClient;
        this.factory = factory;
        this.inZipPath = inZipPath;
        this.ftpPath = ftpPath;
        initChildren(entries);
    }


    @Override
    void resetStream() throws IOException {
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);
        zipStream = new ZipInputStream(inputStream);
    }
}
