import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.util.zip.ZipInputStream;

public class ZipOnFTPFolder extends ZipStreamFolder {

    FTPClient ftpClient;
    String path;

    public ZipOnFTPFolder(FTPClient ftpClient, String path) throws IOException {
        this.ftpClient = ftpClient;
        this.path = path;
        resetStream();
    }
    @Override
    void resetStream() throws IOException {
        zipStream = new ZipInputStream(ftpClient.retrieveFileStream(path));
    }
}
