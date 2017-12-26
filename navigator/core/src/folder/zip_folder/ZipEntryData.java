package folder.zip_folder;

import folder.IFolder;

import java.util.ArrayList;
import java.util.List;

public class ZipEntryData implements Comparable<ZipEntryData>{
    private static final String[] zipPathSeparator = {"/", "\\\\"};

    private String inZipPath;
    private String[] inZipSplitPath;
    private String name;
    private IFolder.FolderTypes type;
    private byte[] data = null;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    private String[] splitPath(String path) {
        if (path.length() == 0) {
            return new String[0];
        }
        String[] split1 = path.split(String.valueOf(zipPathSeparator[0]));
        List<String> res = new ArrayList<>();
        for (String st : split1) {
            String[] splitSt = st.split(String.valueOf(zipPathSeparator[1]));
            for (String st1 : splitSt) {
                res.add(st1);
            }
        }
        String[] split = new String[res.size()];
        res.toArray(split);
        return split;
    }

    private String getLastName() {
        if (inZipSplitPath.length > 0) {
            return inZipSplitPath[inZipSplitPath.length - 1];
        }
        return "";
    }

    public ZipEntryData(String inZipPath, String name, IFolder.FolderTypes type, byte[] data) {
        this(inZipPath, name, type);
        setData(data);
    }

    public ZipEntryData(String inZipPath, String name, IFolder.FolderTypes type) {
        this.inZipPath = inZipPath;
        inZipSplitPath = splitPath(inZipPath);
        if (name != null) {
            this.name = name;
        } else {
            this.name = getLastName();
        }
        this.type = type;
    }

    public String[] getInZipSplitPath() {
        return inZipSplitPath;
    }

    public String getInZipPath() {
        return inZipPath;
    }

    @Override
    public int compareTo(ZipEntryData o) {
        return inZipPath.compareTo(o.inZipPath);
    }

    public IFolder.FolderTypes getType() {
        return type;
    }

    public String getName() {
        if (name != null)
            return name;
        return getLastName();
    }
}
