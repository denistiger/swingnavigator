import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class ZipManager {

    // TODO faster implementation
    public static boolean isZipFile(File file) {
        try {
            new ZipFile(file, ZipFile.OPEN_READ);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
