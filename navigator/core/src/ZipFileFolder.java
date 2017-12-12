import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipFileFolder implements IFolder {

    ZipFile zipFile = null;
    String name = null;
    String inZipPath = null;
    List<IFolder> children = null;
    FileSystem fileSystem = null;
    FolderTypes type;

    // TODO remove
    static final Character zipPathSeparator = '/';

    private String getLastName(String path) {
        String[] list = splitPath(path);
        if (list.length > 0) {
            return list[list.length - 1];
        }
        return "";
    }

    private String[] splitPath(String path) {
        return path.split(String.valueOf(zipPathSeparator));
    }

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
        type = FolderTypes.ZIP_FILE;
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
        return children;
    }

    @Override
    public String getName() {
        if (name != null)
            return name;
        return getLastName(inZipPath);
    }

    private FolderTypes checkType() {
        ZipEntry entry = zipFile.getEntry(inZipPath);
        if (entry.isDirectory()) {
            return FolderTypes.FOLDER;
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
                return FolderTypes.ZIP_FILE;
            }
        } catch (Exception er) {
        }
        return FolderTypes.FILE;
    }

    @Override
    public FolderTypes getType() {
        return type;
    }
}
