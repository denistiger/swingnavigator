package folder.zip_folder;

import folder.IFolder;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipFileFolder extends AbstractZipfolder{

    ZipFile zipFile = null;
    private String inZipPath = null;
    List<IFolder> children = null;
    private FileSystem fileSystem = null;
    IFolder.FolderTypes type;

    public ZipFileFolder(File file) throws Exception {
        fileSystem = FileSystems.newFileSystem(Paths.get(file.getPath()), null);
        inZipPath = "";
        name = file.getName();
        zipFile = new ZipFile(file, ZipFile.OPEN_READ);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        List<String> entriesStr = new ArrayList<>();
        List<String[]> entriesNames = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            entriesStr.add(entry.getName());
        }
        entriesStr.sort(Comparator.naturalOrder());
        for (String st : entriesStr) {
            entriesNames.add(splitPath(st));
        }
        initChildren(entriesNames);
        type = IFolder.FolderTypes.ZIP_FILE;
    }


    public ZipFileFolder(ZipFile file, FileSystem fileSystem1, String path, List<String[]> entries ) throws Exception {
        fileSystem = fileSystem1;
        inZipPath = path;
        zipFile = file;
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

    private void initChildren(List<String[]> entries) throws Exception {
        children = new ArrayList<>();
        if (entries.isEmpty()) {
            return;
        }
        Iterator<String[]> iter = entries.iterator();
        String[] itemName = iter.next();
        while (iter.hasNext()) {
            if (itemName.length > 1) {
                throw new Exception("init Children - lead name not a parent!");
            }
            List<String[]> localChildren = new ArrayList<>();
            String[] currentItemName = null;
            while (iter.hasNext()) {
                currentItemName = iter.next();
                if (currentItemName.length == 1) {
                    break;
                }
                String[] cutPath = new String[currentItemName.length - 1];
                for (int i = 1; i < currentItemName.length; ++i) {
                    cutPath[i - 1] = currentItemName[i];
                }
                localChildren.add(cutPath);
            }
            children.add(new ZipFileFolder(zipFile, fileSystem,
                    inZipPath == "" ? itemName[0] : inZipPath + String.valueOf(zipPathSeparator) + itemName[0],
                    localChildren));
            if (!iter.hasNext() && currentItemName.length == 1) {
                children.add(new ZipFileFolder(zipFile, fileSystem,
                        inZipPath == "" ? itemName[0] : inZipPath + String.valueOf(zipPathSeparator) + currentItemName[0],
                        new ArrayList<>()));
            }
            itemName = currentItemName;
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
