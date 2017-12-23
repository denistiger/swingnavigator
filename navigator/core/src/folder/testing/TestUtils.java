package folder.testing;

import folder.FileTypeGetter;
import folder.IFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TestUtils {

    public static String drawFolderTreeTest(IFolder iFolder) {
        String res = "(" + iFolder.getName() + " : " + iFolder.getType() + ")";
        List<IFolder> list = iFolder.getItems();
        if (list == null || list.size() == 0) {
            return res;
        }
        String inner = "{";
        for (IFolder folder : list) {
            inner += " " + drawFolderTreeTest(folder);
        }
        inner += " }";
        return "[ " + res + " has " + inner + " ]";
    }

    public static String linuxFormat(IFolder folder, String prefix, boolean iterateInZip) {
        String res = "." + prefix + ":\n";
        if (folder == null) {
            return res + "null\n";
        }
        List<IFolder> items = folder.getItems();
        if (items == null) {
            return res;
        }
        items.sort(new Comparator<IFolder>(){
            @Override
            public int compare(IFolder iFolder, IFolder t1) {
                return iFolder.getName().compareTo(t1.getName());
            }
        });
        boolean first = true;
        for (IFolder item : items) {
            if (first) {
                res += item.getName();
                first = false;
            }
            else {
                res += "  " + item.getName();
            }
        }
        res += "\n";
        if (items.size() > 0) {
            res += "\n";
        }
        for (IFolder item : items) {
            if (iterateInZip ? FileTypeGetter.isFolderType(item.getType()) : item.getType() == IFolder.FolderTypes.FOLDER) {
                res += linuxFormat(item, prefix + "/" + item.getName(), iterateInZip);
            }
        }
        return res;
    }

    public static IFolder getByName(IFolder folder, String name) {
        List<IFolder> inner = folder.getItems();
        if (inner == null || inner.size() == 0) {
            return null;
        }
        for (IFolder fold : inner) {
            if (fold.getName().compareTo(name)==0) {
                return fold;
            }
        }
        return null;
    }

    public static String getFromScanner(Scanner in_data) {
        String origin = "";
        while (in_data.hasNext()) {
            String tmp = in_data.nextLine();
            origin += tmp + "\n";
        }
        origin += "\n";
        return origin;
    }

    public static String getFromStream(InputStream stream) {
        Scanner in = new Scanner(stream);
        return getFromScanner(in);
    }

    public static String getTestFile(String path){
        File base = new File(path);
        try {
            return getFromScanner(new Scanner(base));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkFileHasNoChildren(IFolder folder) {
        if (!FileTypeGetter.isFolderType(folder.getType())) {
            if (folder.getItems() != null) {
                return false;
            }
            return true;
        }
        for (IFolder item : folder.getItems()) {
            if (!checkFileHasNoChildren(item)) {
                return false;
            }
        }
        return true;
    }
}
