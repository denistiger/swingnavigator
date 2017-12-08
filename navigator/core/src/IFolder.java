import java.util.List;

public interface IFolder {
    enum FolderTypes {
        FOLDER,
        ZIP_FILE,
        FILE
    }
    List<IFolder> getItems();
    FolderTypes getType();
    String getName();

}
