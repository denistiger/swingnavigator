import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class ZipStreamFolder implements IFolder{
    ZipInputStream zipStream = null;
    String name = null;

    abstract void resetStream() throws IOException;

    @Override
    public List<IFolder> getItems() {
        try {
            resetStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        List<IFolder> list = new ArrayList<>();
        try {
            while (zipStream.available() == 1) {
                ZipEntry entry = zipStream.getNextEntry();
                list.add(new UnimplementedFolder(entry.getName()));
            }
        } catch (IOException er) {

        }
        return list;
    }

    @Override
    public FolderTypes getType() {
        return FolderTypes.ZIP_FILE;
    }

    @Override
    public String getName() {
        return name;
    }

}
