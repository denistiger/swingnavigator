package folder.zip_folder;

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
        type = IFolder.FolderTypes.ZIP_FILE;
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

    // TODO remove
    private void print(List<String[]> list) {
        for (String[] str : list) {
            for (String st : str) {
                System.out.print(st + " ");
            }
            System.out.println();
        }
    }



    private IFolder.FolderTypes checkType() {
        ZipEntry entry = zipFile.getEntry(inZipPath);
        if (entry.isDirectory()) {
            return IFolder.FolderTypes.FOLDER;
        }
        try {
            InputStream stream = zipFile.getInputStream(zipFile.getEntry(inZipPath));
            ZipInputStream zipStream = new ZipInputStream(stream);
//            ZipEntry inZipEntry = zipStream.getNextEntry();
//            while (inZipEntry != null) {
//                System.out.println(inZipEntry.getName());
//                inZipEntry = zipStream.getNextEntry();
//            }
            if (zipStream.getNextEntry() != null) {
                return IFolder.FolderTypes.ZIP_FILE;
            }
        } catch (Exception er) {
        }
        return IFolder.FolderTypes.FILE;
    }

}
