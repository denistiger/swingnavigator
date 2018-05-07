package folder.zip_folder;

import folder.*;

import folder.factory.IFolderFactory;
import folder.factory.ZipOnFTPFactory;
import folder.ftp_folder.FTPClientWrapper;
import folder.ftp_folder.IPrependFTPPath;
import org.testng.internal.collections.Pair;
import thirdparty.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipOnFTPFolder extends AbstractZipFolder implements IPrependFTPPath {

    FTPClientWrapper ftpClient;
    String ftpPath;
//    ZipInputStream zipStream = null;

    public ZipOnFTPFolder(FTPClientWrapper ftpClient, String ftpPath, String name) throws Exception {
        this.ftpClient = ftpClient;
        this.ftpPath = ftpPath;
        this.zipEntryData = new ZipEntryData("", name, FolderTypes.ZIP);
        this.factory = new ZipOnFTPFactory(ftpClient, ftpPath);
    }

    public ZipOnFTPFolder(FTPClientWrapper ftpClient, String ftpPath, ZipEntryData zipEntryData,
                          List<ZipEntryData> entries, IFolderFactory factory) throws Exception {
        this.ftpClient = ftpClient;
        this.factory = factory;
        this.zipEntryData = zipEntryData;
        this.ftpPath = ftpPath;
        initChildren(entries);
        initialized = true;
    }

    private void initChildren() throws Exception {
        ZipInputStream zipStream = resetStream();
        List<ZipEntryData> listNames = new ArrayList<>();
        while (true) {
            ZipEntry entry = zipStream.getNextEntry();
            if (entry == null) {
                break;
            }
            byte[] data = null;
            if (!entry.isDirectory() && FileTypeGetter.getFileType(entry.getName()) == FolderTypes.ZIP) {
                data = IOUtils.readFully(zipStream, -1, true);
            }
            listNames.add(new ZipEntryData(entry.getName(), null,
                    entry.isDirectory() ? FolderTypes.FOLDER : FileTypeGetter.getFileType(entry.getName()), data));
        }
        listNames.sort(Comparator.naturalOrder());
        initChildren(listNames);
        try {
            zipStream.close();
        } catch (IOException er) {
            er.printStackTrace();
        }
    }

    @Override
    protected void init() throws Exception {
        initChildren();
        initialized = true;
    }

    @Override
    public IFolder.FolderTypes getType() {
        return zipEntryData.getType();
    }

    @Override
    public String getAbsolutePath() {
        String path = ftpClient.getFTPPath();
        if (!ftpPath.isEmpty()) {
            path += getSeparator() + ftpPath;
        }
        if (!zipEntryData.getInZipPath().isEmpty()) {
            path += getSeparator() + zipEntryData.getInZipPath();
        }
        return path;
    }

    @Override
    public boolean isFileSystemPath() {
        return false;
    }

    @Override
    public InputStream getInputStream() {
        try {
            if (!initialized) {
                init();
            }
            ZipInputStream zipStream = resetStream();
            ZipEntry entry = zipStream.getNextEntry();
            while (entry != null) {
                if (entry.getName().compareTo(zipEntryData.getInZipPath()) == 0) {
                    return zipStream;
                }
                entry = zipStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    ZipInputStream resetStream() throws IOException {
        try {
            InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);
            return new ZipInputStream(inputStream);
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

    private Pair<ZipEntry, ZipInputStream> getZipEntryAndStream() {
        try {
            ZipInputStream zipStream = resetStream();
            while (true) {
                ZipEntry entry = zipStream.getNextEntry();
                if (entry == null) {
                    return null;
                }
                if (entry.getName().compareTo(zipEntryData.getInZipPath()) == 0) {
                    return new Pair<>(entry, zipStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getEntryData() {
        if (zipEntryData.getData() != null) {
            return zipEntryData.getData();
        }
        Pair<ZipEntry, ZipInputStream> entryAndStream = null;
        try {
            entryAndStream = getZipEntryAndStream();
            if (entryAndStream == null) {
                return null;
            }
            zipEntryData.setData(IOUtils.readFully(entryAndStream.second(), -1, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (entryAndStream != null && entryAndStream.second() != null) {
                    entryAndStream.second().close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return zipEntryData.getData();
    }

    @Override
    public void prependDirectoryToPath(String workingDirectory) {
        ftpPath = workingDirectory + "/" + ftpPath;
    }
}
