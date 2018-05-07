package folder.zip_folder;

import folder.FileTypeGetter;
import folder.factory.IFolderFactory;
import folder.factory.ZipInMemoryFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipInMemoryFolder extends AbstractZipFolder {

    private final byte[] zipData;
    private String zipFileAbsolutePath;

    public ZipInMemoryFolder(byte[] zipData, ZipEntryData zipEntryData,
                             String parentZipFileAbsolutePath, String inZipPath) throws Exception {
        this.zipEntryData = new ZipEntryData("", zipEntryData.getName(), FolderTypes.ZIP, zipData);
        this.zipData = zipData;
        this.zipFileAbsolutePath = parentZipFileAbsolutePath + inZipPath + getSeparator();
        factory = new ZipInMemoryFactory(this.zipData, getAbsolutePath());
    }

    public ZipInMemoryFolder(byte[] zipData, ZipEntryData zipEntryData, String zipFileAbsolutePath) throws Exception {
        this.zipEntryData = new ZipEntryData("", zipEntryData.getName(), FolderTypes.ZIP, zipData);
        this.zipData = zipData;
        this.zipFileAbsolutePath = zipFileAbsolutePath + getSeparator();
        factory = new ZipInMemoryFactory(this.zipData, getAbsolutePath());
    }

    public ZipInMemoryFolder(byte[] zipData, ZipEntryData entry, List<ZipEntryData> zipEntries,
                             IFolderFactory factory, String zipFileAbsolutePath ) throws Exception {
        this.zipEntryData = entry;
        this.zipData = zipData;
        this.factory = factory;
        this.zipFileAbsolutePath = zipFileAbsolutePath + zipEntryData.getInZipPath();
        initChildren(zipEntries);
        initialized = true;
    }

    @Override
    public String getAbsolutePath() {
        return zipFileAbsolutePath;
    }

    @Override
    public boolean isFileSystemPath() {
        return false;
    }

    @Override
    public InputStream getInputStream() {
        if (!initialized) {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (zipData == null) {
            return null;
        }
        InputStream inputStream = new ByteArrayInputStream(zipData);
        if (zipEntryData.getInZipPath().length() == 0) {
            return inputStream;
        }
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        try {
            while (true) {
                ZipEntry entry = zipInputStream.getNextEntry();
                if (entry == null) {
                    return null;
                }
                if (entry.getName().compareTo(zipEntryData.getInZipPath()) == 0){
                    return zipInputStream;
                }
            }
        } catch (IOException er) {
            er.printStackTrace();
            return null;
        }
    }

    @Override
    protected void init() throws Exception {
        InputStream stream = new ByteArrayInputStream(this.zipData);
        ZipInputStream zipInputStream = new ZipInputStream(stream);
        List<ZipEntryData> zipEntries = new ArrayList<>();
        while (true) {
            ZipEntry entry = zipInputStream.getNextEntry();
            if (entry == null) {
                break;
            }
            zipEntries.add(new ZipEntryData(entry.getName(), null, entry.isDirectory() ? FolderTypes.FOLDER : FileTypeGetter.getFileType(entry.getName())));
        }
        zipEntries.sort(Comparator.naturalOrder());
        initChildren(zipEntries);
        initialized = true;
    }
}
