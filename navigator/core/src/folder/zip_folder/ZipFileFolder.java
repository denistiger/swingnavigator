package folder.zip_folder;

import folder.*;
import folder.factory.IFolderFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileFolder extends AbstractZipFolder implements ILevelUp{

    private ZipFile zipFile;
    private File file;
    private String parentZipPath = null;

    public ZipFileFolder(File file) throws Exception {
        this.file = file;
        zipEntryData = new ZipEntryData("", file.getName(), FolderTypes.ZIP);
    }


    public ZipFileFolder(ZipFile file, ZipEntryData entry, List<ZipEntryData> zipEntries, IFolderFactory factory,
                         String parentZipPath ) throws Exception {
        this.zipEntryData = entry;
        this.parentZipPath = parentZipPath;
        zipFile = file;
        this.factory = factory;
        initChildren(zipEntries);
        initialized = true;
    }

    @Override
    public String getAbsolutePath() {
        if (file != null) {
            return file.getAbsolutePath();
        }
        return parentZipPath + "/" + zipEntryData.getInZipPath();
        //"Get absolute path is not implemented for files inside Zip archive";
    }

    @Override
    public Character getSeparator() {
        return file.separatorChar;
    }

    protected void init() throws Exception {
        zipFile = new ZipFile(file, ZipFile.OPEN_READ);
        factory = new ZipFolderFactory(zipFile, file.getAbsolutePath());
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        List<ZipEntryData> zipEntries = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            zipEntries.add(new ZipEntryData(entry.getName(), null, entry.isDirectory() ? FolderTypes.FOLDER : FileTypeGetter.getFileType(entry.getName())));
        }
        zipEntries.sort(Comparator.naturalOrder());
        initChildren(zipEntries);
        initialized = true;
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
        if (zipEntryData.getInZipPath().compareTo("") == 0) {
            return null;
        }
        try {
            InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(zipEntryData.getInZipPath()));
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IFolder levelUp() {
        try {
            return new LocalFolder(file.getParentFile());
        } catch (FileSystemEntity.NotALocalFolderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileSystemEntity.NullInitializedFolderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
