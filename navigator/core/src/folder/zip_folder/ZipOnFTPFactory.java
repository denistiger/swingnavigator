package folder.zip_folder;

import folder.FTPClientWrapper;
import folder.IFolder;
import folder.IFolderFactory;
import org.apache.commons.net.ftp.FTPClient;
import sun.misc.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ZipOnFTPFactory implements IFolderFactory {

    private FTPClientWrapper ftpClient;
    private String ftpPath;

    public ZipOnFTPFactory(FTPClientWrapper ftpClient, String ftpPath) {
        this.ftpClient = ftpClient;
        this.ftpPath = ftpPath;
    }

    @Override
    public IFolder createIFolder(Map<String, Object> params) throws Exception {
        ZipEntryData thisEntry = (ZipEntryData) params.get(THISENTRY);
        List<ZipEntryData> entries = (List<ZipEntryData>) params.get(CHILDENTRIES);
        if (thisEntry.getType() != IFolder.FolderTypes.ZIP || thisEntry.getInZipPath() == "") {
            return new ZipOnFTPFolder(ftpClient, ftpPath, thisEntry, entries, this);
        }
        else {
            ZipOnFTPFolder iFolder = new ZipOnFTPFolder(ftpClient, ftpPath, thisEntry, entries, this);
            byte[] zipData = iFolder.getEntryData();
            return new ZipInMemoryFolder(zipData, thisEntry);
        }

    }
}
