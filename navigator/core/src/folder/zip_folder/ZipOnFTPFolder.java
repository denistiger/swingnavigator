package folder.zip_folder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ZipOnFTPFolder extends ZipStreamFolder {

    FTPClient ftpClient;
    String path;

    public ZipOnFTPFolder(FTPClient ftpClient, String path, String name) throws IOException {
        this.ftpClient = ftpClient;
        this.path = path;
        this.name = name;
    }
    @Override
    void resetStream() throws IOException {
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = ftpClient.retrieveFileStream(path);
        zipStream = new ZipInputStream(inputStream);
    }
}
