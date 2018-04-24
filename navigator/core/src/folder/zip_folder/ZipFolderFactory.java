package folder.zip_folder;

import folder.IFolder;
import folder.factory.IFolderFactory;
import thirdparty.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;


public class ZipFolderFactory implements IFolderFactory {

    private ZipFile zipFile;

    public ZipFolderFactory(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public IFolder createIFolder(Map<String, Object> params) throws Exception {
        ZipEntryData thisEntry = (ZipEntryData) params.get(THISENTRY);
        List<ZipEntryData> entries = (List<ZipEntryData>) params.get(CHILDENTRIES);
        if (thisEntry.getType() != IFolder.FolderTypes.ZIP) {
            return new ZipFileFolder(zipFile, thisEntry, entries, this);
        }
        else {
            InputStream stream = zipFile.getInputStream(zipFile.getEntry(thisEntry.getInZipPath()));
            byte[] zipData = IOUtils.readFully(stream, -1, true);
            return new ZipInMemoryFolder(zipData, thisEntry);
        }
    }
}
