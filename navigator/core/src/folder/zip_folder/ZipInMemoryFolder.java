package folder.zip_folder;

import folder.FileTypeGetter;
import folder.IFolderFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipInMemoryFolder extends AbstractZipFolder {

    private final byte[] zipData;

    public ZipInMemoryFolder(byte[] zipData, ZipEntryData zipEntryData) throws Exception {
        this.zipEntryData = new ZipEntryData("", zipEntryData.getName(), FolderTypes.ZIP);
        this.zipData = zipData;
        factory = new ZipInMemoryFactory(this.zipData);
        InputStream stream = new ByteArrayInputStream(this.zipData);
        ZipInputStream zipInputStream = new ZipInputStream(stream);
        List<ZipEntryData> zipEntries = new ArrayList<>();
        while (zipInputStream.available() == 1) {
            ZipEntry entry = zipInputStream.getNextEntry();
            if (entry == null) {
                break;
            }
            zipEntries.add(new ZipEntryData(entry.getName(), null, entry.isDirectory() ? FolderTypes.FOLDER : FileTypeGetter.getFileType(entry.getName())));
        }
        zipEntries.sort(Comparator.naturalOrder());
        initChildren(zipEntries);
    }

    public ZipInMemoryFolder(byte[] zipData, ZipEntryData entry, List<ZipEntryData> zipEntries, IFolderFactory factory ) throws Exception {
        this.zipEntryData = entry;
        this.zipData = zipData;
        this.factory = factory;
        initChildren(zipEntries);
    }

}
