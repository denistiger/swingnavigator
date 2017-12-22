package folder.zip_folder;

import folder.FTPClientWrapper;
import folder.IFolderFactory;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipOnFTPFolder extends ZipStreamFolder {

    FTPClientWrapper ftpClient;
    String ftpPath;

    public ZipOnFTPFolder(FTPClientWrapper ftpClient, String ftpPath, String name) throws Exception {
        this.ftpClient = ftpClient;
        this.ftpPath = ftpPath;
        this.zipEntryData = new ZipEntryData("", name, FolderTypes.ZIP);
        this.factory = new ZipOnFTPFactory(ftpClient, ftpPath);
        initChildren();
    }

    public ZipOnFTPFolder(FTPClientWrapper ftpClient, String ftpPath, ZipEntryData zipEntryData, List<ZipEntryData> entries, IFolderFactory factory) throws Exception {
        this.ftpClient = ftpClient;
        this.factory = factory;
        this.zipEntryData = zipEntryData;
        this.ftpPath = ftpPath;
        initChildren(entries);
    }


    @Override
    void resetStream() throws IOException {
        try {
            InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);
            zipStream = new ZipInputStream(inputStream);
        } catch (IOException e) {
            System.out.println("Fail with path: " + ftpPath);
            e.printStackTrace();
            throw e;
        }
        catch (NullPointerException e) {
            System.out.println("Fail with path: " + ftpPath);
            e.printStackTrace();
            throw e;
        }
    }

    public byte[] getEntryData() {
        byte[] data = null;
        try {
            ZipEntry entry = getZipEntry();
            if (entry == null) {
                return null;
            }
            data = IOUtils.readFully(zipStream, -1, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            closeStream();
        }
        return data;
    }
}
