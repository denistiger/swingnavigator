import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileFolder implements IFolder {

    ZipFile zipFile = null;
    String inZipPath = null;
    List<IFolder> childs = null;
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

    public ZipFileFolder(File file) throws IOException {
        zipFile = new ZipFile(file, ZipFile.OPEN_READ);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        List<String[]> entriesNames = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            entriesNames.add(splitPath(name));
        }

    }

    public ZipFileFolder(ZipFile file, String pathInZip,  ) throws IOException {
        inZipPath = path;
        zipFile = file;
    }

    @Override
    public List<IFolder> getItems() {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        int curLevel = splitPath(inZipPath).length;
        List<IFolder> list = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            if (splitPath(name).length)
        }


        System.out.println("Zip data for " + getName());
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(entry.isDirectory()){
                System.out.println("dir  : " + entry.getName());
            } else {
                System.out.println("file : " + entry.getName());
            }
        }
        System.out.println("End Of Zip data for " + getName());
        return null;
    }

    @Override
    public String getName() {
        if (inZipPath == null)
            return zipFile.getName();
        return getLastName(inZipPath);
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.ZIP_FILE;
    }
}
