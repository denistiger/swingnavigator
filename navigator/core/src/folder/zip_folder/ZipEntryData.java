package folder.zip_folder;

import folder.IFolder;

public class ZipEntryData implements Comparable<ZipEntryData>{
    // TODO remove
    private static final Character zipPathSeparator = '/';


    private String inZipPath;
    private String[] inZipSplitPath;
    private String name;
    private IFolder.FolderTypes type;

    private String[] splitPath(String path) {
        if (path.length() == 0) {
            return new String[0];
        }
        return path.split(String.valueOf(zipPathSeparator));
    }

    private String getLastName() {
        if (inZipSplitPath.length > 0) {
            return inZipSplitPath[inZipSplitPath.length - 1];
        }
        return "";
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
