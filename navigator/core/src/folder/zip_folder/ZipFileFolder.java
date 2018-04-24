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

    public ZipFileFolder(File file) throws Exception {
        this.file = file;
        zipEntryData = new ZipEntryData("", file.getName(), FolderTypes.ZIP);
        zipFile = new ZipFile(file, ZipFile.OPEN_READ);
        factory = new ZipFolderFactory(zipFile);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        List<ZipEntryData> zipEntries = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            zipEntries.add(new ZipEntryData(entry.getName(), null, entry.isDirectory() ? FolderTypes.FOLDER : FileTypeGetter.getFileType(entry.getName())));
        }
        zipEntries.sort(Comparator.naturalOrder());
        initChildren(zipEntries);
    }


    public ZipFileFolder(ZipFile file, ZipEntryData entry, List<ZipEntryData> zipEntries, IFolderFactory factory ) throws Exception {
        this.zipEntryData = entry;
        zipFile = file;
        this.factory = factory;
        initChildren(zipEntries);
    }

    @Override
    public String getAbsolutePath() {
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "Get absolute path is not implemented for files inside Zip archive";
    }

    @Override
    public boolean isFileSystemPath() {
        return false;
    }

    @Override
    public InputStream getInputStream() {
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
