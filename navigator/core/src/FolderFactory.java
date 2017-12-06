import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderFactory {

    // TODO may be not so cynical?
    public static IFolder createIFolder(File file) {
        try {
            return new LocalFolder(file);
        } catch (Exception er) {

        }
        try {
            return new ZipFile(file);
        } catch (Exception er) {

        }
        try {
            return new CommonFile(file);
        } catch (Exception er) {

        }
        return null;
    }

    public static List<IFolder> createIFolderList(File[] files) {
        List<IFolder> list = new ArrayList<>();
        // TODO learn lambda
        for (File file : files) {
            IFolder folder = createIFolder(file);
            if (folder != null) {
                list.add(folder);
            }
        }
        return list;
    }
}
