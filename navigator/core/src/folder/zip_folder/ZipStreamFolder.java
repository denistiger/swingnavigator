package folder.zip_folder;

import folder.IFolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class ZipStreamFolder extends AbstractZipFolder {
    ZipInputStream zipStream = null;

    abstract void resetStream() throws IOException;

    protected void initChildren() throws Exception {
        resetStream();
        List<String> listNames = new ArrayList<>();
        while (zipStream.available() == 1) {
            ZipEntry entry = zipStream.getNextEntry();
            if (entry == null) {
                break;
            }
            listNames.add(entry.getName());
        }
        initChildren(prepareEntriesList(listNames));
    }

    @Override
    public IFolder.FolderTypes getType() {
        if (type == null) {
            ZipEntry entry = getZipEntry();
            if (entry.isDirectory()) {
                type = FolderTypes.FOLDER;
            }
            // TODO return for zip files.
            type = FolderTypes.FILE;
        }
        return type;
    }


    protected ZipEntry getZipEntry() {
        try {
            resetStream();
            while (zipStream.available() == 1) {
                ZipEntry entry = zipStream.getNextEntry();
                if (entry == null) {
                    return null;
                }
                if (entry.getName() == inZipPath) {
                    return entry;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
