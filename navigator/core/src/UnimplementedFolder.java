import java.util.List;

public class UnimplementedFolder implements IFolder{

    String name;

    public UnimplementedFolder(String name) {
        this.name = name;
    }

    @Override
    public List<IFolder> getItems() {
        return null;
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.FILE;
    }

    @Override
    public String getName() {
        return name;
    }
}
