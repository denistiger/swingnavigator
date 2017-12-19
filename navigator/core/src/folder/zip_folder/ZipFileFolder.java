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
        inZipPath = "";
        type = IFolder.FolderTypes.ZIP;
        name = file.getName();
        zipFile = new ZipFile(file, ZipFile.OPEN_READ);
        factory = new ZipFolderFactory(zipFile);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        List<String> entriesStr = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            entriesStr.add(entry.getName());
        }
        initChildren(prepareEntriesList(entriesStr));
    }


    public ZipFileFolder(ZipFile file, String path, List<String[]> entries, ZipFolderFactory factory ) throws Exception {
        inZipPath = path;
        zipFile = file;
        this.factory = factory;
        initChildren(entries);
        type = checkType();
    }

    private IFolder.FolderTypes checkType() {
        ZipEntry entry = zipFile.getEntry(inZipPath);
        if (entry.isDirectory()) {
            return IFolder.FolderTypes.FOLDER;
        }
        return FileTypeGetter.getFileType(getName());
    }

}
