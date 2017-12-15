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
    private FolderTypes type;

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
        entriesStr.sort(Comparator.naturalOrder());
        List<String[]> entriesNames = new ArrayList<>();
        for (String st : entriesStr) {
            entriesNames.add(splitPath(st));
        }
        initChildren(entriesNames);
    }


    public ZipFileFolder(ZipFile file, String path, List<String[]> entries, ZipFolderFactory factory ) throws Exception {
        inZipPath = path;
        zipFile = file;
        this.factory = factory;
        initChildren(entries);
        type = checkType();
    }

    private void print(List<String[]> list) {
        for (String[] str : list) {
            for (String st : str) {
                System.out.print(st + " ");
            }
            System.out.println();
        }

    }


    @Override
    public List<IFolder> getItems() {
        if (getType() == IFolder.FolderTypes.FILE) {
            assert children == null || children.size() == 0;
            return null;
        }
        return children;
    }

    @Override
    public String getName() {
        if (name != null)
            return name;
        return getLastName(inZipPath);
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

    @Override
    public IFolder.FolderTypes getType() {
        return type;
    }
}
