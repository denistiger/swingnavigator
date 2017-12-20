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
        this.zipEntryData = new ZipEntryData("", name, FolderTypes.ZIP);
        this.factory = new ZipOnFTPFactory(ftpClient, ftpPath);
        initChildren();
    }

    public ZipOnFTPFolder(FTPClient ftpClient, String ftpPath, ZipEntryData zipEntryData, List<ZipEntryData> entries, IFolderFactory factory) throws Exception {
        this.ftpClient = ftpClient;
        this.factory = factory;
        this.zipEntryData = zipEntryData;
        this.ftpPath = ftpPath;
        initChildren(entries);
    }


    @Override
    void resetStream() throws IOException {
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);
            zipStream = new ZipInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
