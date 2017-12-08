import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileFolder implements IFolder {

    ZipFile zipFile = null;
    String name = null;
    String inZipPath = null;
    List<IFolder> children = null;
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
    }


    public ZipFileFolder(ZipFile file, String path, List<String[]> entries ) throws Exception {
        inZipPath = path;
        zipFile = file;
        initChildren(entries);
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
//        System.out.println("init children start for " + inZipPath + (name != null ? name : ""));
//        print(entries);
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
//            System.out.println("Parent: " + itemName[0]);
//            print(localChildren);
            children.add(new ZipFileFolder(zipFile,
                    inZipPath == "" ? itemName[0] : inZipPath + String.valueOf(zipPathSeparator) + itemName[0],
                    localChildren));
            itemName = currentItemName;
        }
//        System.out.println("init children done for " + inZipPath + (name != null ? name : ""));
    }

    @Override
    public List<IFolder> getItems() {
        return children;
//        Enumeration<? extends ZipEntry> entries = zipFile.entries();
//        int curLevel = splitPath(inZipPath).length;
//        List<IFolder> list = new ArrayList<>();
//        while (entries.hasMoreElements()) {
//            ZipEntry entry = entries.nextElement();
//            String name = entry.getName();
//            if (splitPath(name).length)
//        }
//
//
//        System.out.println("Zip data for " + getName());
//        while(entries.hasMoreElements()){
//            ZipEntry entry = entries.nextElement();
//            if(entry.isDirectory()){
//                System.out.println("dir  : " + entry.getName());
//            } else {
//                System.out.println("file : " + entry.getName());
//            }
//        }
//        System.out.println("End Of Zip data for " + getName());
//        return null;
    }

    @Override
    public String getName() {
        if (name != null)
            return name;
        return getLastName(inZipPath);
    }

    @Override
    public FolderTypes getType() {
        if (inZipPath == "") {
            return FolderTypes.ZIP_FILE;
        }
        ZipEntry entry = zipFile.getEntry(inZipPath);
        if (entry.isDirectory()) {
            return FolderTypes.FOLDER;
        }
        // TODO check if zip!
        return FolderTypes.FILE;
    }
}
