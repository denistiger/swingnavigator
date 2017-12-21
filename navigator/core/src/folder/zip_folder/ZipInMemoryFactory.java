package folder.zip_folder;

import folder.IFolder;
import folder.IFolderFactory;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipInMemoryFactory implements IFolderFactory {

    private final byte[] zipData;

    public ZipInMemoryFactory(byte[] zipData) {
        this.zipData = zipData;
    }

    @Override
    public IFolder createIFolder(Map<String, Object> params) throws Exception {
        ZipEntryData thisEntry = (ZipEntryData) params.get(THISENTRY);
        List<ZipEntryData> entries = (List<ZipEntryData>) params.get(CHILDENTRIES);
        if (thisEntry.getType() != IFolder.FolderTypes.ZIP) {
            return new ZipInMemoryFolder(zipData, thisEntry, entries, this);
        }
        else {
            assert entries == null || entries.isEmpty();
            InputStream stream = new ByteArrayInputStream(zipData);
            ZipInputStream zipInputStream = new ZipInputStream(stream);
            byte[] entryZipData = null;
            while (zipInputStream.available() == 1) {
                ZipEntry entry = zipInputStream.getNextEntry();
                if (entry == null) {
                    return null;
                }
                if (entry.getName().compareTo(thisEntry.getInZipPath()) == 0) {
                    entryZipData = IOUtils.readFully(zipInputStream, -1, false);
                    break;
                }
            }
            if (entryZipData != null) {
                return new ZipInMemoryFolder(entryZipData, thisEntry);
            }
        }
        return null;
    }



}
