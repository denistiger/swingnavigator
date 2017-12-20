package folder.zip_folder;

import folder.IFolder;
import folder.IFolderFactory;
import org.apache.commons.net.ftp.FTPClient;

import java.util.List;
import java.util.Map;

public class ZipOnFTPFactory implements IFolderFactory {

    private FTPClient ftpClient;
    private String ftpPath;

    public ZipOnFTPFactory(FTPClient ftpClient, String ftpPath) {
        this.ftpClient = ftpClient;
        this.ftpPath = ftpPath;
    }

    @Override
    public IFolder createIFolder(Map<String, Object> params) throws Exception {
        ZipEntryData thisEntry = (ZipEntryData) params.get(THISENTRY);
        List<ZipEntryData> entries = (List<ZipEntryData>) params.get(CHILDENTRIES);
        return new ZipOnFTPFolder(ftpClient, ftpPath, thisEntry, entries, this);
    }
}
