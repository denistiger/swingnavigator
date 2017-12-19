package folder;

import java.util.HashMap;
import java.util.List;

public class UnimplementedFolder implements IFolder{

    String name;
    FolderTypes type;
    IFolderFactory factory;
    IFolder iFolder;

    public UnimplementedFolder(String name, FolderTypes type, IFolderFactory factory) {
        this.name = name;
        this.type = null;
        iFolder = null;
        this.factory = factory;
    }

    private boolean createIFolder() {
        if (iFolder == null) {
            try {
                iFolder = factory.createIFolder(new HashMap<>());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<IFolder> getItems() {
        if (!createIFolder()) {
            return null;
        }
        return iFolder.getItems();
    }

    @Override
    public FolderTypes getType() {
        if (type != null) {
            return type;
        }
        if (!createIFolder()) {
            return FolderTypes.UNKNOWN;
        }
        return iFolder.getType();
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }
        if (!createIFolder()) {
            return null;
        }
        return iFolder.getName();
    }
}
