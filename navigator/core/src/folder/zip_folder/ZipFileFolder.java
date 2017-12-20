package folder.zip_folder;

import folder.FileTypeGetter;
import folder.IFolder;
import folder.IFolderFactory;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipFileFolder extends AbstractZipFolder {

    private ZipFile zipFile = null;

    public ZipFileFolder(File file) throws Exception {
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


    public ZipFileFolder(ZipFile file, ZipEntryData entry, List<ZipEntryData> zipEntries, ZipFolderFactory factory ) throws Exception {
        this.zipEntryData = entry;
        zipFile = file;
        this.factory = factory;
        initChildren(zipEntries);
    }

}
