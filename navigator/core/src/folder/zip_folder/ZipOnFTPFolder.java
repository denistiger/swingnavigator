package folder.zip_folder;

import folder.FTPClientWrapper;
import folder.FileTypeGetter;
import folder.IFolder;
import folder.IFolderFactory;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipOnFTPFolder extends AbstractZipFolder {

    FTPClientWrapper ftpClient;
    String ftpPath;
    ZipInputStream zipStream = null;


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

    public void closeStream() {
        try {
            zipStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initChildren() throws Exception {
        resetStream();
        List<ZipEntryData> listNames = new ArrayList<>();
        while (zipStream.available() == 1) {
            ZipEntry entry = zipStream.getNextEntry();
            if (entry == null) {
                break;
            }
            listNames.add(new ZipEntryData(entry.getName(), null, entry.isDirectory() ? FolderTypes.FOLDER : FileTypeGetter.getFileType(entry.getName())));
        }
        listNames.sort(Comparator.naturalOrder());
        initChildren(listNames);
        closeStream();
    }

    @Override
    public IFolder.FolderTypes getType() {
        return zipEntryData.getType();
    }


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

    protected ZipEntry getZipEntry() {
        try {
            resetStream();
            while (zipStream.available() == 1) {
                ZipEntry entry = zipStream.getNextEntry();
                if (entry == null) {
                    return null;
                }
                if (entry.getName().compareTo(zipEntryData.getInZipPath()) == 0) {
                    return entry;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
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
